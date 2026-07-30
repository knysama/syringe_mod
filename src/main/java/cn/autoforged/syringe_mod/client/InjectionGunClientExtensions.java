package cn.autoforged.syringe_mod.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.Nullable;

public final class InjectionGunClientExtensions implements IClientItemExtensions {
    @Override
    public boolean applyForgeHandTransform(
            PoseStack poseStack, LocalPlayer player, HumanoidArm arm,
            ItemStack itemInHand, float partialTick,
            float equipProcess, float swingProcess) {
        return ClientGunAnimations.applyFirstPersonTransform(
                poseStack, player, arm, itemInHand, partialTick, equipProcess);
    }

    @Override
    @Nullable
    public HumanoidModel.ArmPose getArmPose(
            LivingEntity living, InteractionHand hand, ItemStack stack) {
        if (hand != InteractionHand.MAIN_HAND) {
            return null;
        }

        var action = ClientGunAnimations.actionFor(living);
        if (action != null
                || (living.isUsingItem() && living.getUsedItemHand() == hand)) {
            return InjectionGunArmPose.ARM_POSE.getValue();
        }
        return null;
    }
}
