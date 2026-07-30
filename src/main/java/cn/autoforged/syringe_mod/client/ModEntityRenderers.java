package cn.autoforged.syringe_mod.client;

import cn.autoforged.syringe_mod.SyringeMod;
import cn.autoforged.syringe_mod.entity.ModEntities;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(modid = SyringeMod.MODID, value = Dist.CLIENT)
public final class ModEntityRenderers {
    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.AMPOULE_PROJECTILE.get(), InjectionDartRenderer::new);
    }

    private ModEntityRenderers() {
    }
}
