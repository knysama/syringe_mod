package cn.autoforged.syringe_mod.integration.curios.client;

import cn.autoforged.syringe_mod.client.MedicineBagItemRenderer;
import cn.autoforged.syringe_mod.item.ModItems;
import cn.autoforged.syringe_mod.ui.SyringeBagScreen;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;
import top.theillusivec4.curios.api.client.ICurioRenderer;

public final class CuriosMedicineBagRenderer implements ICurioRenderer {
    private static final MedicineBagItemRenderer BAG_RENDERER = new MedicineBagItemRenderer();

    public static void register() {
        CuriosRendererRegistry.register(
                ModItems.SYRINGE_BAG.get(),
                CuriosMedicineBagRenderer::new);
    }

    @Override
    public <T extends LivingEntity, M extends EntityModel<T>> void render(
            ItemStack stack,
            SlotContext slotContext,
            PoseStack poseStack,
            RenderLayerParent<T, M> renderLayerParent,
            MultiBufferSource bufferSource,
            int packedLight,
            float limbSwing,
            float limbSwingAmount,
            float partialTicks,
            float ageInTicks,
            float netHeadYaw,
            float headPitch) {
        poseStack.pushPose();
        if (renderLayerParent.getModel() instanceof HumanoidModel<?> humanoidModel) {
            humanoidModel.body.translateAndRotate(poseStack);
        }
        BAG_RENDERER.renderEquipped(
                stack,
                slotContext.entity(),
                poseStack,
                bufferSource,
                packedLight,
                isOpenFor(slotContext.entity()));
        poseStack.popPose();
    }

    private static boolean isOpenFor(LivingEntity wearer) {
        Minecraft minecraft = Minecraft.getInstance();
        return wearer == minecraft.player
                && minecraft.screen instanceof SyringeBagScreen;
    }
}
