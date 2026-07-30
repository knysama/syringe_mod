package cn.autoforged.syringe_mod.client;

import cn.autoforged.syringe_mod.SyringeMod;
import cn.autoforged.syringe_mod.item.InjectionGunAction;
import cn.autoforged.syringe_mod.item.InjectionGunItem;
import cn.autoforged.syringe_mod.item.ModItems;
import cn.autoforged.syringe_mod.network.payload.ClientboundGunAnimationPayload;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderHandEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@EventBusSubscriber(modid = SyringeMod.MODID, value = Dist.CLIENT)
public final class ClientGunAnimations {
    private static final Map<Integer, AnimationState> ACTIVE = new HashMap<>();

    @SubscribeEvent
    public static void registerPayload(RegisterPayloadHandlersEvent event) {
        event.registrar("2").playToClient(
                ClientboundGunAnimationPayload.TYPE,
                ClientboundGunAnimationPayload.STREAM_CODEC,
                ClientGunAnimations::handle);
    }

    private static void handle(ClientboundGunAnimationPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            Minecraft minecraft = Minecraft.getInstance();
            if (minecraft.level == null) {
                return;
            }
            if (minecraft.level.getEntity(payload.entityId()) instanceof LivingEntity living) {
                ACTIVE.put(payload.entityId(),
                        new AnimationState(payload.action(), Math.max(1, payload.durationTicks())));
                if (payload.action() == InjectionGunAction.FIRE) {
                    living.swing(InteractionHand.MAIN_HAND);
                } else if (living.getMainHandItem().getItem() instanceof InjectionGunItem) {
                    living.startUsingItem(InteractionHand.MAIN_HAND);
                }
            }
        });
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level == null) {
            ACTIVE.clear();
            return;
        }

        Iterator<Map.Entry<Integer, AnimationState>> iterator = ACTIVE.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, AnimationState> entry = iterator.next();
            if (!entry.getValue().tick()) {
                continue;
            }
            if (minecraft.level.getEntity(entry.getKey()) instanceof LivingEntity living
                    && living.getMainHandItem().getItem() instanceof InjectionGunItem) {
                living.stopUsingItem();
            }
            iterator.remove();
        }
    }

    @SubscribeEvent
    public static void onRenderHand(RenderHandEvent event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null
                || event.getHand() != InteractionHand.MAIN_HAND
                || event.getItemStack().getItem() instanceof InjectionGunItem) {
            return;
        }

        AnimationState state = ACTIVE.get(minecraft.player.getId());
        if (state == null || state.action != InjectionGunAction.QUICK_INJECT) {
            return;
        }

        event.setCanceled(true);
        event.getPoseStack().pushPose();
        HumanoidArm arm = minecraft.player.getMainArm();
        applyQuickInjectionPose(
                event.getPoseStack(), arm,
                state.progressTicks(event.getPartialTick()), false);
        minecraft.getItemRenderer().renderStatic(
                minecraft.player,
                findVisibleGun(minecraft.player),
                arm == HumanoidArm.RIGHT
                        ? ItemDisplayContext.FIRST_PERSON_RIGHT_HAND
                        : ItemDisplayContext.FIRST_PERSON_LEFT_HAND,
                arm == HumanoidArm.LEFT,
                event.getPoseStack(),
                event.getMultiBufferSource(),
                minecraft.level,
                event.getPackedLight(),
                OverlayTexture.NO_OVERLAY,
                minecraft.player.getId());
        event.getPoseStack().popPose();
    }

    public static boolean applyFirstPersonTransform(
            com.mojang.blaze3d.vertex.PoseStack poseStack,
            LocalPlayer player, HumanoidArm arm, ItemStack stack,
            float partialTick, float equipProgress) {
        AnimationState state = ACTIVE.get(player.getId());
        if (state != null) {
            float ticks = state.progressTicks(partialTick);
            switch (state.action) {
                case SELF_INJECT -> {
                    applyHeldBase(poseStack, arm, equipProgress);
                    applySelfInjectionPose(poseStack, arm, ticks);
                    return true;
                }
                case QUICK_INJECT -> {
                    applyHeldBase(poseStack, arm, equipProgress);
                    applyQuickInjectionPose(poseStack, arm, ticks, true);
                    return true;
                }
                case FIRE -> {
                    applyHeldBase(poseStack, arm, equipProgress);
                    applyRecoilPose(poseStack, arm, ticks);
                    return true;
                }
                case QUICK_RELOAD, SELECTED_RELOAD -> {
                    applyHeldBase(poseStack, arm, equipProgress);
                    applyReloadPose(poseStack, arm, ticks);
                    return true;
                }
            }
        }

        if (player.isUsingItem() && player.getUseItem() == stack) {
            applyHeldBase(poseStack, arm, equipProgress);
            int side = arm == HumanoidArm.RIGHT ? 1 : -1;
            float aim = easeOut(Mth.clamp(
                    (player.getTicksUsingItem() + partialTick) / 5.0F,
                    0.0F, 1.0F));
            poseStack.translate(
                    -0.33D * side * aim,
                    -0.075D * aim,
                    -0.24D * aim);
            // Keep the receiver level while aiming. The previous negative
            // X rotation raised the muzzle above the crosshair and made the
            // first-person view feel like the player was lifting their head.
            poseStack.mulPose(Axis.XP.rotationDegrees(0.75F * aim));
            poseStack.mulPose(Axis.YP.rotationDegrees(-2.5F * side * aim));
            poseStack.mulPose(Axis.ZP.rotationDegrees(0.5F * side * aim));
            return true;
        }
        return false;
    }

    public static InjectionGunAction actionFor(LivingEntity living) {
        AnimationState state = ACTIVE.get(living.getId());
        return state == null ? null : state.action;
    }

    public static float reloadModelPull(LivingEntity living) {
        if (living == null) {
            return 0.0F;
        }
        AnimationState state = ACTIVE.get(living.getId());
        if (state == null
                || (state.action != InjectionGunAction.QUICK_RELOAD
                && state.action != InjectionGunAction.SELECTED_RELOAD)) {
            return 0.0F;
        }
        return reloadPlungerAmount(state.progressTicks(0.0F));
    }

    private static void applyHeldBase(
            com.mojang.blaze3d.vertex.PoseStack poseStack,
            HumanoidArm arm, float equipProgress) {
        int side = arm == HumanoidArm.RIGHT ? 1 : -1;
        poseStack.translate(
                side * 0.56D,
                -0.52D - equipProgress * 0.6D,
                -0.72D);
    }

    private static void applySelfInjectionPose(
            com.mojang.blaze3d.vertex.PoseStack poseStack,
            HumanoidArm arm, float ticks) {
        applyInjectionPose(poseStack, arm, selfPoseAmount(ticks), selfContactAmount(ticks));
    }

    private static void applyQuickInjectionPose(
            com.mojang.blaze3d.vertex.PoseStack poseStack, HumanoidArm arm,
            float ticks, boolean held) {
        float pose = quickPoseAmount(ticks);
        float contact = quickContactAmount(ticks);
        if (!held) {
            int side = arm == HumanoidArm.RIGHT ? 1 : -1;
            poseStack.translate(
                    side * (0.74D - 0.19D * pose),
                    -1.12D + 0.67D * pose,
                    -0.82D + 0.07D * pose);
        }
        applyInjectionPose(poseStack, arm, pose, contact);
    }

    private static void applyRecoilPose(
            com.mojang.blaze3d.vertex.PoseStack poseStack,
            HumanoidArm arm, float ticks) {
        int side = arm == HumanoidArm.RIGHT ? 1 : -1;
        float recoil = ticks <= 1.0F
                ? easeOut(Mth.clamp(ticks, 0.0F, 1.0F))
                : 1.0F - easeInOut(Mth.clamp((ticks - 1.0F) / 3.0F, 0.0F, 1.0F));
        poseStack.translate(0.04D * side * recoil, 0.035D * recoil, 0.12D * recoil);
        poseStack.mulPose(Axis.XP.rotationDegrees(-7.0F * recoil));
        poseStack.mulPose(Axis.ZP.rotationDegrees(-3.0F * side * recoil));
    }

    private static void applyReloadPose(
            com.mojang.blaze3d.vertex.PoseStack poseStack,
            HumanoidArm arm, float ticks) {
        int side = arm == HumanoidArm.RIGHT ? 1 : -1;
        float lowered = reloadPoseAmount(ticks);
        float plungerPull = reloadPlungerAmount(ticks);
        float ampouleSeat = reloadAmpouleSeatAmount(ticks);

        poseStack.translate(
                -0.20D * side * lowered + 0.08D * side * plungerPull,
                0.24D * lowered + 0.035D * ampouleSeat,
                0.12D * lowered + 0.055D * plungerPull);
        poseStack.mulPose(Axis.XP.rotationDegrees(27.0F * lowered - 3.0F * ampouleSeat));
        poseStack.mulPose(Axis.YP.rotationDegrees(-25.0F * side * lowered));
        poseStack.mulPose(Axis.ZP.rotationDegrees(-19.0F * side * lowered
                + 5.0F * side * plungerPull));
    }

    private static void applyInjectionPose(
            com.mojang.blaze3d.vertex.PoseStack poseStack, HumanoidArm arm,
            float pose, float contact) {
        int side = arm == HumanoidArm.RIGHT ? 1 : -1;
        poseStack.translate(
                -0.70D * side * pose - 0.08D * side * contact,
                0.13D * pose + 0.015D * contact,
                0.14D * pose - 0.035D * contact);
        poseStack.mulPose(Axis.XP.rotationDegrees(-18.0F * pose));
        poseStack.mulPose(Axis.YP.rotationDegrees(-24.0F * side * pose));
        poseStack.mulPose(Axis.ZP.rotationDegrees(-66.0F * side * pose));
    }

    private static float selfPoseAmount(float ticks) {
        if (ticks < 3.0F) {
            return easeOut(Mth.clamp(ticks / 3.0F, 0.0F, 1.0F));
        }
        if (ticks < 10.0F) {
            return 1.0F;
        }
        return 1.0F - easeInOut(Mth.clamp((ticks - 10.0F) / 2.0F, 0.0F, 1.0F));
    }

    private static float selfContactAmount(float ticks) {
        if (ticks < 3.0F) {
            return 0.0F;
        }
        if (ticks < 5.0F) {
            return easeInOut((ticks - 3.0F) / 2.0F);
        }
        if (ticks < 10.0F) {
            return 1.0F;
        }
        return 1.0F - easeInOut(Mth.clamp((ticks - 10.0F) / 2.0F, 0.0F, 1.0F));
    }

    private static float quickPoseAmount(float ticks) {
        if (ticks < 2.0F) {
            return easeOut(Mth.clamp(ticks / 2.0F, 0.0F, 1.0F));
        }
        if (ticks < 5.0F) {
            return 1.0F;
        }
        return 1.0F - easeInOut(Mth.clamp((ticks - 5.0F) / 3.0F, 0.0F, 1.0F));
    }

    private static float quickContactAmount(float ticks) {
        if (ticks < 2.0F) {
            return 0.0F;
        }
        if (ticks < 5.0F) {
            return easeInOut((ticks - 2.0F) / 3.0F);
        }
        return 1.0F - easeInOut(Mth.clamp((ticks - 5.0F) / 3.0F, 0.0F, 1.0F));
    }

    private static float reloadPoseAmount(float ticks) {
        if (ticks < 3.0F) {
            return easeOut(Mth.clamp(ticks / 3.0F, 0.0F, 1.0F));
        }
        if (ticks < 10.0F) {
            return 1.0F;
        }
        return 1.0F - easeInOut(Mth.clamp((ticks - 10.0F) / 4.0F, 0.0F, 1.0F));
    }

    private static float reloadPlungerAmount(float ticks) {
        if (ticks < 3.0F) {
            return 0.0F;
        }
        if (ticks < 5.0F) {
            return easeInOut((ticks - 3.0F) / 2.0F);
        }
        if (ticks < 10.0F) {
            return 1.0F;
        }
        return 1.0F - easeInOut(Mth.clamp((ticks - 10.0F) / 4.0F, 0.0F, 1.0F));
    }

    private static float reloadAmpouleSeatAmount(float ticks) {
        if (ticks < 6.0F) {
            return 0.0F;
        }
        if (ticks < 8.0F) {
            return easeInOut((ticks - 6.0F) / 2.0F);
        }
        if (ticks < 10.0F) {
            return 1.0F - easeInOut((ticks - 8.0F) / 2.0F);
        }
        return 0.0F;
    }

    private static float easeOut(float value) {
        float inverse = 1.0F - value;
        return 1.0F - inverse * inverse * inverse;
    }

    private static float easeInOut(float value) {
        float clamped = Mth.clamp(value, 0.0F, 1.0F);
        return clamped * clamped * (3.0F - 2.0F * clamped);
    }

    private static ItemStack findVisibleGun(LocalPlayer player) {
        for (int slot = 0; slot < player.getInventory().getContainerSize(); slot++) {
            ItemStack stack = player.getInventory().getItem(slot);
            if (stack.getItem() instanceof InjectionGunItem) {
                return stack;
            }
        }
        return new ItemStack(ModItems.INJECTION_GUN.get());
    }

    private static final class AnimationState {
        private final InjectionGunAction action;
        private final int durationTicks;
        private int remainingTicks;

        private AnimationState(InjectionGunAction action, int durationTicks) {
            this.action = action;
            this.durationTicks = durationTicks;
            this.remainingTicks = durationTicks;
        }

        private boolean tick() {
            return --remainingTicks <= 0;
        }

        private float progressTicks(float partialTick) {
            return Mth.clamp(
                    durationTicks - remainingTicks + partialTick,
                    0.0F, durationTicks);
        }
    }

    private ClientGunAnimations() {
    }
}
