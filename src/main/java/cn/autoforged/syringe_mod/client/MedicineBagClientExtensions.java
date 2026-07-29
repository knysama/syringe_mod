package cn.autoforged.syringe_mod.client;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;

public final class MedicineBagClientExtensions implements IClientItemExtensions {
    private final MedicineBagItemRenderer renderer = new MedicineBagItemRenderer();

    @Override
    public BlockEntityWithoutLevelRenderer getCustomRenderer() {
        return renderer;
    }
}
