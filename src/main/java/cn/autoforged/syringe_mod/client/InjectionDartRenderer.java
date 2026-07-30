package cn.autoforged.syringe_mod.client;

import cn.autoforged.syringe_mod.entity.AmpouleProjectile;
import cn.autoforged.syringe_mod.item.MedicineColors;
import cn.autoforged.syringe_mod.item.ModItems;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;

/**
 * Renders a fired dose as a compact needle-shaped dart while the projectile
 * continues to carry the real ampoule stack for authoritative hit effects.
 */
public final class InjectionDartRenderer extends EntityRenderer<AmpouleProjectile> {
    private static final float SCALE = 0.72F;
    private final ItemRenderer itemRenderer;

    public InjectionDartRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
        this.shadowRadius = 0.0F;
    }

    @Override
    public void render(
            AmpouleProjectile projectile,
            float entityYaw,
            float partialTick,
            PoseStack poseStack,
            MultiBufferSource bufferSource,
            int packedLight) {
        poseStack.pushPose();
        poseStack.scale(SCALE, SCALE, SCALE);
        Vec3 velocity = projectile.getDeltaMovement();
        if (velocity.lengthSqr() > 1.0E-7D) {
            Vec3 direction = velocity.normalize();
            // The texture's needle tip points along local -X.
            poseStack.mulPose(new Quaternionf().rotationTo(
                    -1.0F,
                    0.0F,
                    0.0F,
                    (float) direction.x,
                    (float) direction.y,
                    (float) direction.z));
        }

        ItemStack dart = new ItemStack(ModItems.INJECTION_DART.get());
        dart.set(
                DataComponents.DYED_COLOR,
                new DyedItemColor(MedicineColors.color(projectile.getItem()), false));
        renderPlane(projectile, dart, poseStack, bufferSource, packedLight, 0);
        // A second plane makes the thin dart readable from above and from the
        // side without returning to camera-facing billboard behavior.
        poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
        renderPlane(projectile, dart, poseStack, bufferSource, packedLight, 1);
        poseStack.popPose();
        super.render(
                projectile,
                entityYaw,
                partialTick,
                poseStack,
                bufferSource,
                packedLight);
    }

    private void renderPlane(
            AmpouleProjectile projectile,
            ItemStack dart,
            PoseStack poseStack,
            MultiBufferSource bufferSource,
            int packedLight,
            int seedOffset) {
        itemRenderer.renderStatic(
                dart,
                ItemDisplayContext.FIXED,
                packedLight,
                OverlayTexture.NO_OVERLAY,
                poseStack,
                bufferSource,
                projectile.level(),
                projectile.getId() + seedOffset);
    }

    @Override
    public ResourceLocation getTextureLocation(AmpouleProjectile projectile) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}
