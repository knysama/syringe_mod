package cn.autoforged.syringe_mod.client;

import cn.autoforged.syringe_mod.item.InjectionGunAction;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.fml.common.asm.enumextension.EnumProxy;
import net.neoforged.neoforge.client.IArmPoseTransformer;

/**
 * Client-only parameters for the extended injection-gun arm pose.
 *
 * <p>The enum proxy is referenced by META-INF/enumextensions.json and must
 * remain in a separate client class because HumanoidModel is not available on
 * a dedicated server.</p>
 */
public final class InjectionGunArmPose {
    public static final EnumProxy<HumanoidModel.ArmPose> ARM_POSE =
            new EnumProxy<>(
                    HumanoidModel.ArmPose.class,
                    false,
                    (IArmPoseTransformer) InjectionGunArmPose::apply);

    private static void apply(
            HumanoidModel<?> model, LivingEntity living, HumanoidArm mainArm) {
        ModelPart main = mainArm == HumanoidArm.RIGHT ? model.rightArm : model.leftArm;
        ModelPart support = mainArm == HumanoidArm.RIGHT ? model.leftArm : model.rightArm;
        int side = mainArm == HumanoidArm.RIGHT ? 1 : -1;

        InjectionGunAction action = ClientGunAnimations.actionFor(living);
        float ticks = ClientGunAnimations.actionProgressTicks(living);
        if (action == null) {
            applyAim(model, main, support, side, 0.0F);
            return;
        }

        switch (action) {
            case FIRE -> applyAim(model, main, support, side, recoilAmount(ticks));
            case QUICK_RELOAD, SELECTED_RELOAD ->
                    applyReload(model, main, support, side, reloadAmount(ticks));
            case SELF_INJECT ->
                    applyInjection(model, main, support, side, selfInjectionAmount(ticks));
            case QUICK_INJECT ->
                    applyInjection(model, main, support, side, quickInjectionAmount(ticks));
        }
    }

    private static void applyAim(
            HumanoidModel<?> model, ModelPart main, ModelPart support,
            int side, float recoil) {
        // Both hands follow the head so the modeled front/rear sight line and
        // the server-side projectile direction share the player's look vector.
        main.xRot = model.head.xRot - Mth.HALF_PI - 0.13F * recoil;
        main.yRot = model.head.yRot - 0.08F * side;
        main.zRot = -0.03F * side;

        support.xRot = model.head.xRot - 1.42F - 0.07F * recoil;
        support.yRot = model.head.yRot + 0.43F * side;
        support.zRot = 0.12F * side;
    }

    private static void applyReload(
            HumanoidModel<?> model, ModelPart main, ModelPart support,
            int side, float amount) {
        float aimMainX = model.head.xRot - Mth.HALF_PI;
        float aimMainY = model.head.yRot - 0.08F * side;
        float aimSupportX = model.head.xRot - 1.42F;
        float aimSupportY = model.head.yRot + 0.43F * side;

        main.xRot = Mth.lerp(amount, aimMainX, -0.82F);
        main.yRot = Mth.lerp(amount, aimMainY, -0.38F * side);
        main.zRot = Mth.lerp(amount, -0.03F * side, 0.35F * side);

        support.xRot = Mth.lerp(amount, aimSupportX, -1.10F);
        support.yRot = Mth.lerp(amount, aimSupportY, 0.48F * side);
        support.zRot = Mth.lerp(amount, 0.12F * side, -0.22F * side);
    }

    private static void applyInjection(
            HumanoidModel<?> model, ModelPart main, ModelPart support,
            int side, float amount) {
        float heldMainX = -0.32F;
        float heldMainY = 0.0F;
        float heldSupportX = -0.24F;

        // Raise the gun across the torso. The first-person item animation turns
        // the gun end-for-end; here the hand/arm arc supplies the visible
        // third-person motion while the item remains rigidly attached.
        main.xRot = Mth.lerp(amount, heldMainX, -1.18F);
        main.yRot = Mth.lerp(amount, heldMainY, -0.78F * side);
        main.zRot = Mth.lerp(amount, 0.0F, -0.42F * side);

        support.xRot = Mth.lerp(amount, heldSupportX, -0.92F);
        support.yRot = Mth.lerp(amount, 0.0F, 0.54F * side);
        support.zRot = Mth.lerp(amount, 0.0F, 0.30F * side);
    }

    private static float recoilAmount(float ticks) {
        if (ticks <= 1.0F) {
            return easeOut(Mth.clamp(ticks, 0.0F, 1.0F));
        }
        return 1.0F - easeInOut(Mth.clamp((ticks - 1.0F) / 3.0F, 0.0F, 1.0F));
    }

    private static float reloadAmount(float ticks) {
        if (ticks < 3.0F) {
            return easeOut(Mth.clamp(ticks / 3.0F, 0.0F, 1.0F));
        }
        if (ticks < 10.0F) {
            return 1.0F;
        }
        return 1.0F - easeInOut(Mth.clamp((ticks - 10.0F) / 4.0F, 0.0F, 1.0F));
    }

    private static float selfInjectionAmount(float ticks) {
        if (ticks < 3.0F) {
            return easeOut(Mth.clamp(ticks / 3.0F, 0.0F, 1.0F));
        }
        if (ticks < 10.0F) {
            return 1.0F;
        }
        return 1.0F - easeInOut(Mth.clamp((ticks - 10.0F) / 2.0F, 0.0F, 1.0F));
    }

    private static float quickInjectionAmount(float ticks) {
        if (ticks < 2.0F) {
            return easeOut(Mth.clamp(ticks / 2.0F, 0.0F, 1.0F));
        }
        if (ticks < 5.0F) {
            return 1.0F;
        }
        return 1.0F - easeInOut(Mth.clamp((ticks - 5.0F) / 3.0F, 0.0F, 1.0F));
    }

    private static float easeOut(float value) {
        float inverse = 1.0F - value;
        return 1.0F - inverse * inverse * inverse;
    }

    private static float easeInOut(float value) {
        float clamped = Mth.clamp(value, 0.0F, 1.0F);
        return clamped * clamped * (3.0F - 2.0F * clamped);
    }

    private InjectionGunArmPose() {
    }
}
