package cn.autoforged.syringe_mod.item;

import cn.autoforged.syringe_mod.SyringeMod;
import cn.autoforged.syringe_mod.entity.AmpouleProjectile;
import cn.autoforged.syringe_mod.network.payload.ClientboundGunAnimationPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@EventBusSubscriber(modid = SyringeMod.MODID)
public final class GunActionController {
    private static final int SELF_INJECT_TICKS = 12;
    private static final int QUICK_INJECT_TICKS = 8;
    private static final int RELOAD_TICKS = 14;
    private static final Map<UUID, PendingAction> PENDING = new HashMap<>();

    public static boolean start(ServerPlayer player, InjectionGunAction action, ItemStack selectedAmpoule) {
        if (!player.isAlive() || PENDING.containsKey(player.getUUID())) {
            return false;
        }

        GunLocation gunLocation = action == InjectionGunAction.QUICK_INJECT
                ? findFirstGun(player)
                : findMainHandGun(player);
        if (gunLocation == null) {
            return false;
        }

        ItemStack gun = gunLocation.resolve(player);
        int duration = switch (action) {
            case SELF_INJECT -> SELF_INJECT_TICKS;
            case QUICK_INJECT -> QUICK_INJECT_TICKS;
            case QUICK_RELOAD, SELECTED_RELOAD -> RELOAD_TICKS;
            case FIRE -> 4;
        };

        if ((action == InjectionGunAction.SELF_INJECT || action == InjectionGunAction.FIRE)
                && !InjectionGunItem.hasLoadedAmpoule(gun)) {
            return false;
        }
        if (action == InjectionGunAction.QUICK_INJECT
                && !InjectionGunItem.hasLoadedAmpoule(gun)
                && !InjectionGunService.quickReload(player, gun)) {
            return false;
        }
        if (action == InjectionGunAction.SELECTED_RELOAD
                && !(selectedAmpoule.getItem() instanceof AmpouleItem)) {
            return false;
        }
        if (action == InjectionGunAction.FIRE) {
            if (player.getCooldowns().isOnCooldown(gun.getItem()) || !fire(player, gun)) {
                return false;
            }
        }

        boolean usesMainHandPose = gunLocation.inventorySlot() == player.getInventory().selected
                && action != InjectionGunAction.FIRE;
        if (usesMainHandPose) {
            player.startUsingItem(InteractionHand.MAIN_HAND);
        }
        PENDING.put(player.getUUID(), new PendingAction(
                action, gunLocation, gun, duration,
                selectedAmpoule.isEmpty() ? ItemStack.EMPTY : selectedAmpoule.copyWithCount(1),
                usesMainHandPose));
        PacketDistributor.sendToPlayersTrackingEntityAndSelf(
                player, new ClientboundGunAnimationPayload(player.getId(), action, duration));
        return true;
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        PendingAction pending = PENDING.get(player.getUUID());
        if (pending == null) {
            return;
        }
        if (!pending.isStillValid(player)) {
            PENDING.remove(player.getUUID());
            if (pending.usesMainHandPose) {
                player.stopUsingItem();
            }
            return;
        }

        if (pending.tick(player)) {
            PENDING.remove(player.getUUID());
            pending.finish(player);
            if (pending.usesMainHandPose || pending.action == InjectionGunAction.FIRE) {
                player.stopUsingItem();
            }
        }
    }

    @SubscribeEvent
    public static void onLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        PENDING.remove(event.getEntity().getUUID());
    }

    private static GunLocation findMainHandGun(ServerPlayer player) {
        ItemStack stack = player.getMainHandItem();
        return stack.getItem() instanceof InjectionGunItem
                ? new GunLocation(player.getInventory().selected)
                : null;
    }

    private static GunLocation findFirstGun(ServerPlayer player) {
        GunLocation mainHand = findMainHandGun(player);
        if (mainHand != null) {
            return mainHand;
        }

        for (int slot = 0; slot < player.getInventory().getContainerSize(); slot++) {
            if (player.getInventory().getItem(slot).getItem() instanceof InjectionGunItem) {
                return new GunLocation(slot);
            }
        }
        return null;
    }

    private record GunLocation(int inventorySlot) {
        ItemStack resolve(ServerPlayer player) {
            if (inventorySlot < 0 || inventorySlot >= player.getInventory().getContainerSize()) {
                return ItemStack.EMPTY;
            }
            return player.getInventory().getItem(inventorySlot);
        }
    }

    private static final class PendingAction {
        private final InjectionGunAction action;
        private final GunLocation gunLocation;
        private final ItemStack originalGun;
        private final ItemStack selectedAmpoule;
        private final boolean usesMainHandPose;
        private final int durationTicks;
        private int elapsedTicks;
        private boolean injectionCommitted;
        private boolean contactCuePlayed;

        private PendingAction(InjectionGunAction action, GunLocation gunLocation, ItemStack originalGun,
                              int durationTicks, ItemStack selectedAmpoule, boolean usesMainHandPose) {
            this.action = action;
            this.gunLocation = gunLocation;
            this.originalGun = originalGun;
            this.durationTicks = durationTicks;
            this.selectedAmpoule = selectedAmpoule;
            this.usesMainHandPose = usesMainHandPose;
        }

        boolean isStillValid(ServerPlayer player) {
            return player.isAlive()
                    && player.containerMenu == player.inventoryMenu
                    && gunLocation.resolve(player) == originalGun
                    && originalGun.getItem() instanceof InjectionGunItem;
        }

        boolean tick(ServerPlayer player) {
            elapsedTicks++;

            int contactTick = action == InjectionGunAction.QUICK_INJECT ? 2 : 4;
            if (isInjection() && !contactCuePlayed && elapsedTicks >= contactTick) {
                contactCuePlayed = true;
                playCue(player, SoundEvents.TRIPWIRE_CLICK_ON, 0.28F, 1.55F);
            }

            int commitTick = action == InjectionGunAction.QUICK_INJECT ? 5 : 8;
            if (isInjection() && !injectionCommitted && elapsedTicks >= commitTick) {
                injectionCommitted = true;
                if (!injectSelf(player, originalGun)) {
                    showFailure(player);
                } else {
                    playCue(player, SoundEvents.PISTON_CONTRACT, 0.22F, 1.65F);
                }
            }

            return elapsedTicks >= durationTicks;
        }

        void finish(ServerPlayer player) {
            switch (action) {
                case SELF_INJECT, QUICK_INJECT -> {
                    // The dose is committed at the plunger keyframe; the remaining
                    // ticks only hold the needle in place and return the hand.
                }
                case QUICK_RELOAD -> {
                    if (!InjectionGunService.quickReload(player, originalGun)) {
                        showFailure(player);
                    }
                }
                case SELECTED_RELOAD -> {
                    if (!InjectionGunService.reloadSelected(player, originalGun, selectedAmpoule)) {
                        showFailure(player);
                    }
                }
                case FIRE -> {
                    // The projectile is committed immediately; these four ticks are recoil lock.
                }
            }
        }

        private boolean isInjection() {
            return action == InjectionGunAction.SELF_INJECT
                    || action == InjectionGunAction.QUICK_INJECT;
        }
    }

    private static boolean injectSelf(ServerPlayer player, ItemStack gun) {
        ItemStack loaded = InjectionGunItem.getLoadedAmpoule(gun);
        if (loaded.isEmpty() || !MedicineEffectService.applyDose(player, player, loaded)) {
            return false;
        }

        InjectionGunItem.removeLoadedAmpoule(gun);
        InjectionGunItem.consumeCapacity(gun, player);
        return true;
    }

    private static boolean fire(ServerPlayer player, ItemStack gun) {
        ItemStack loaded = InjectionGunItem.getLoadedAmpoule(gun);
        if (loaded.isEmpty()) {
            return false;
        }

        AmpouleProjectile projectile = new AmpouleProjectile(player.level(), player);
        projectile.setItem(loaded);
        projectile.shootFromRotation(player, player.getXRot(), player.getYRot(),
                0.0F, 3.10F, 0.08F);
        if (!player.level().addFreshEntity(projectile)) {
            return false;
        }

        InjectionGunItem.removeLoadedAmpoule(gun);
        InjectionGunItem.consumeCapacity(gun, player);
        player.getCooldowns().addCooldown(gun.getItem(), 10);
        player.swing(InteractionHand.MAIN_HAND, true);
        return true;
    }

    private static void showFailure(ServerPlayer player) {
        player.displayClientMessage(
                Component.translatable("message.syringe_mod.injection_gun.action_failed"), true);
    }

    private static void playCue(ServerPlayer player, net.minecraft.sounds.SoundEvent sound,
                                float volume, float pitch) {
        player.level().playSound(
                null, player.getX(), player.getY(), player.getZ(),
                sound, SoundSource.PLAYERS, volume, pitch);
    }

    private GunActionController() {
    }
}
