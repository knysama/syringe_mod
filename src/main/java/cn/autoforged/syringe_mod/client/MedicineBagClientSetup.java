package cn.autoforged.syringe_mod.client;

import cn.autoforged.syringe_mod.SyringeMod;
import cn.autoforged.syringe_mod.item.ModItems;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;

@EventBusSubscriber(modid = SyringeMod.MODID, value = Dist.CLIENT)
public final class MedicineBagClientSetup {
    @SubscribeEvent
    public static void registerClientExtensions(RegisterClientExtensionsEvent event) {
        event.registerItem(new MedicineBagClientExtensions(), ModItems.SYRINGE_BAG.get());
    }

    @SubscribeEvent
    public static void registerAdditionalModels(ModelEvent.RegisterAdditional event) {
        event.register(MedicineBagItemRenderer.ICON_MODEL);
        event.register(MedicineBagItemRenderer.OPEN_ICON_MODEL);
        event.register(MedicineBagItemRenderer.BODY_MODEL);
        event.register(MedicineBagItemRenderer.LID_MODEL);
        event.register(MedicineBagItemRenderer.AMPOULES_MODEL);
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        if (!ModList.get().isLoaded("curios")) {
            return;
        }
        event.enqueueWork(MedicineBagClientSetup::registerOptionalCuriosRenderer);
    }

    private static void registerOptionalCuriosRenderer() {
        try {
            Class<?> registration = Class.forName(
                    "cn.autoforged.syringe_mod.integration.curios.client.CuriosMedicineBagRenderer");
            registration.getMethod("register").invoke(null);
        } catch (ReflectiveOperationException exception) {
            throw new IllegalStateException("Failed to register the optional Curios medicine bag renderer", exception);
        }
    }

    private MedicineBagClientSetup() {
    }
}
