package cn.autoforged.syringe_mod.client;

import cn.autoforged.syringe_mod.SyringeMod;
import cn.autoforged.syringe_mod.ui.SyringeBagScreen;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public final class MedicineBagItemRenderer extends BlockEntityWithoutLevelRenderer {
    public static final ModelResourceLocation ICON_MODEL = standalone("syringe_bag_icon");
    public static final ModelResourceLocation OPEN_ICON_MODEL = standalone("syringe_bag_open");
    public static final ModelResourceLocation BODY_MODEL = standalone("syringe_bag_3d_body");
    public static final ModelResourceLocation LID_MODEL = standalone("syringe_bag_3d_lid");
    public static final ModelResourceLocation AMPOULES_MODEL = standalone("syringe_bag_3d_ampoules");

    private static final float LID_PIVOT_X = 8.0F / 16.0F;
    private static final float LID_PIVOT_Y = 10.75F / 16.0F;
    private static final float LID_PIVOT_Z = 6.0F / 16.0F;
    public MedicineBagItemRenderer() {
        super(
                Minecraft.getInstance().getBlockEntityRenderDispatcher(),
                Minecraft.getInstance().getEntityModels());
    }

    @Override
    public void renderByItem(
            ItemStack stack,
            ItemDisplayContext context,
            PoseStack poseStack,
            MultiBufferSource bufferSource,
            int packedLight,
            int packedOverlay) {
        if (context == ItemDisplayContext.GUI) {
            poseStack.pushPose();
            poseStack.translate(0.5F, 0.5F, 0.5F);
            poseStack.scale(0.88F, 0.88F, 0.88F);
            poseStack.translate(-0.5F, -0.5F, -0.5F);
            ModelResourceLocation icon = Minecraft.getInstance().screen instanceof SyringeBagScreen
                    ? OPEN_ICON_MODEL
                    : ICON_MODEL;
            renderModel(
                    icon,
                    stack,
                    poseStack,
                    bufferSource,
                    LightTexture.FULL_BRIGHT,
                    packedOverlay);
            poseStack.popPose();
            return;
        }

        poseStack.pushPose();
        applyHandheldTransform(context, poseStack);
        renderBag(
                stack,
                poseStack,
                bufferSource,
                packedLight,
                packedOverlay,
                heldAnimationPose(stack, context));
        poseStack.popPose();
    }

    public void renderEquipped(
            ItemStack stack,
            net.minecraft.world.entity.LivingEntity wearer,
            PoseStack poseStack,
            MultiBufferSource bufferSource,
            int packedLight,
            boolean open) {
        poseStack.pushPose();
        // The Curios pose is body-local. Center the 0..1 item model on the
        // torso before scaling; transformAroundCenter would leave its center
        // at (+0.5,+0.5,+0.5), which is why it previously floated beside the
        // player's upper body.
        poseStack.translate(0.0F, 0.58F, -0.27F);
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(180.0F));
        poseStack.scale(0.5F, 0.5F, 0.5F);
        poseStack.translate(-0.5F, -0.5F, -0.5F);
        renderBag(
                stack,
                poseStack,
                bufferSource,
                packedLight,
                OverlayTexture.NO_OVERLAY,
                MedicineBagAnimationController.sample(wearer, open));
        poseStack.popPose();
    }

    private void renderBag(
            ItemStack stack,
            PoseStack poseStack,
            MultiBufferSource bufferSource,
            int packedLight,
            int packedOverlay,
            MedicineBagAnimationController.Pose animationPose) {
        renderModel(BODY_MODEL, stack, poseStack, bufferSource, packedLight, packedOverlay);

        poseStack.pushPose();
        poseStack.translate(0.0F, animationPose.ampouleOffsetY() / 16.0F, 0.0F);
        renderModel(AMPOULES_MODEL, stack, poseStack, bufferSource, packedLight, packedOverlay);
        poseStack.popPose();

        poseStack.pushPose();
        poseStack.translate(LID_PIVOT_X, LID_PIVOT_Y, LID_PIVOT_Z);
        poseStack.mulPose(Axis.XP.rotationDegrees(animationPose.lidAngleDegrees()));
        poseStack.translate(-LID_PIVOT_X, -LID_PIVOT_Y, -LID_PIVOT_Z);
        renderModel(LID_MODEL, stack, poseStack, bufferSource, packedLight, packedOverlay);
        poseStack.popPose();
    }

    private static void renderModel(
            ModelResourceLocation modelLocation,
            ItemStack stack,
            PoseStack poseStack,
            MultiBufferSource bufferSource,
            int packedLight,
            int packedOverlay) {
        Minecraft minecraft = Minecraft.getInstance();
        ItemRenderer itemRenderer = minecraft.getItemRenderer();
        BakedModel model = minecraft.getModelManager().getModel(modelLocation);
        VertexConsumer consumer = ItemRenderer.getFoilBufferDirect(
                bufferSource,
                Sheets.cutoutBlockSheet(),
                true,
                stack.hasFoil());
        itemRenderer.renderModelLists(
                model,
                stack,
                packedLight,
                packedOverlay,
                poseStack,
                consumer);
    }

    private static void applyHandheldTransform(ItemDisplayContext context, PoseStack poseStack) {
        switch (context) {
            case FIRST_PERSON_RIGHT_HAND -> {
                poseStack.translate(0.20F, -0.12F, -0.10F);
                transformAroundCenter(poseStack, 12.0F, 180.0F, -7.0F, 0.62F);
            }
            case FIRST_PERSON_LEFT_HAND -> {
                poseStack.translate(-0.20F, -0.12F, -0.10F);
                transformAroundCenter(poseStack, 12.0F, 180.0F, 7.0F, 0.62F);
            }
            case THIRD_PERSON_RIGHT_HAND -> {
                poseStack.translate(0.0F, 0.02F, 0.0F);
                transformAroundCenter(poseStack, 0.0F, 180.0F, -8.0F, 0.46F);
            }
            case THIRD_PERSON_LEFT_HAND -> {
                poseStack.translate(0.0F, 0.02F, 0.0F);
                transformAroundCenter(poseStack, 0.0F, 180.0F, 8.0F, 0.46F);
            }
            case GROUND -> {
                poseStack.translate(0.0F, -0.10F, 0.0F);
                transformAroundCenter(poseStack, 0.0F, 180.0F, 0.0F, 0.50F);
            }
            case FIXED -> transformAroundCenter(
                    poseStack, 0.0F, 180.0F, 0.0F, 0.68F);
            default -> transformAroundCenter(
                    poseStack, 0.0F, 180.0F, 0.0F, 0.52F);
        }
    }

    private static void transformAroundCenter(
            PoseStack poseStack,
            float xRotation,
            float yRotation,
            float zRotation,
            float scale) {
        poseStack.translate(0.5F, 0.5F, 0.5F);
        poseStack.mulPose(Axis.XP.rotationDegrees(xRotation));
        poseStack.mulPose(Axis.YP.rotationDegrees(yRotation));
        poseStack.mulPose(Axis.ZP.rotationDegrees(zRotation));
        poseStack.scale(scale, scale, scale);
        poseStack.translate(-0.5F, -0.5F, -0.5F);
    }

    private static MedicineBagAnimationController.Pose heldAnimationPose(
            ItemStack renderedStack,
            ItemDisplayContext context) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null || !isHandContext(context)) {
            return MedicineBagAnimationController.closedPose();
        }
        boolean heldByPlayer = ItemStack.isSameItemSameComponents(
                        minecraft.player.getMainHandItem(), renderedStack)
                || ItemStack.isSameItemSameComponents(
                        minecraft.player.getOffhandItem(), renderedStack);
        if (!heldByPlayer) {
            return MedicineBagAnimationController.closedPose();
        }
        return MedicineBagAnimationController.sample(
                minecraft.player,
                minecraft.screen instanceof SyringeBagScreen);
    }

    private static boolean isHandContext(ItemDisplayContext context) {
        return context == ItemDisplayContext.FIRST_PERSON_RIGHT_HAND
                || context == ItemDisplayContext.FIRST_PERSON_LEFT_HAND
                || context == ItemDisplayContext.THIRD_PERSON_RIGHT_HAND
                || context == ItemDisplayContext.THIRD_PERSON_LEFT_HAND;
    }

    private static ModelResourceLocation standalone(String path) {
        return ModelResourceLocation.standalone(
                ResourceLocation.fromNamespaceAndPath(SyringeMod.MODID, "item/" + path));
    }
}
