package cn.autoforged.syringe_mod.client;

import cn.autoforged.syringe_mod.SyringeMod;
import cn.autoforged.syringe_mod.item.InjectionGunAction;
import cn.autoforged.syringe_mod.item.InjectionGunItem;
import cn.autoforged.syringe_mod.network.payload.ServerboundInjectionGunActionPayload;
import cn.autoforged.syringe_mod.network.payload.ServerboundOpenSyringeBagPayload;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = SyringeMod.MODID, value = Dist.CLIENT)
public class ModClientEvents {
    private static final int WHEEL_HOLD_TICKS = 8;
    private static int reloadHeldTicks;
    private static boolean reloadWasDown;
    private static boolean wheelOpened;
    private static boolean attackWasDown;

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        while (ModKeyMappings.USE_SYRINGE_KEY.get().consumeClick()) {
            PacketDistributor.sendToServer(new ServerboundInjectionGunActionPayload(
                    InjectionGunAction.QUICK_INJECT));
        }

        while (ModKeyMappings.OPEN_SYRINGE_BAG_KEY.get().consumeClick()) {
            PacketDistributor.sendToServer(new ServerboundOpenSyringeBagPayload());
        }

        boolean holdingGun = mc.player.getMainHandItem().getItem() instanceof InjectionGunItem;
        boolean aimingGun = holdingGun
                && mc.player.isUsingItem()
                && mc.player.getUseItem().getItem() instanceof InjectionGunItem;
        boolean attackDown = mc.options.keyAttack.isDown();
        if (mc.screen == null && aimingGun && attackDown && !attackWasDown) {
            PacketDistributor.sendToServer(new ServerboundInjectionGunActionPayload(
                    InjectionGunAction.FIRE));
        }
        attackWasDown = attackDown;

        boolean reloadDown = holdingGun
                && ModKeyMappings.RELOAD_INJECTION_GUN_KEY.get().isDown()
                && (mc.screen == null || mc.screen instanceof AmpouleWheelScreen);

        if (reloadDown) {
            if (!wheelOpened) {
                reloadHeldTicks++;
            }
            if (mc.screen == null && reloadHeldTicks >= WHEEL_HOLD_TICKS && !wheelOpened) {
                wheelOpened = true;
                mc.setScreen(new AmpouleWheelScreen(mc.player));
            }
        } else if (reloadWasDown) {
            if (!wheelOpened && reloadHeldTicks > 0 && holdingGun) {
                PacketDistributor.sendToServer(new ServerboundInjectionGunActionPayload(
                        InjectionGunAction.QUICK_RELOAD));
            }
            reloadHeldTicks = 0;
            wheelOpened = false;
        }
        reloadWasDown = reloadDown;
    }

    @SubscribeEvent
    public static void onInteractionKey(InputEvent.InteractionKeyMappingTriggered event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null
                || mc.screen != null
                || !event.isAttack()
                || !(mc.player.getMainHandItem().getItem() instanceof InjectionGunItem)) {
            return;
        }

        boolean aimingGun = mc.player.isUsingItem()
                && mc.player.getUseItem().getItem() instanceof InjectionGunItem;
        if (!aimingGun) {
            PacketDistributor.sendToServer(new ServerboundInjectionGunActionPayload(
                    InjectionGunAction.SELF_INJECT));
        }
        event.setSwingHand(false);
        event.setCanceled(true);
    }
}
