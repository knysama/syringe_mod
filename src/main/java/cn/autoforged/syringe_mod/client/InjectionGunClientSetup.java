package cn.autoforged.syringe_mod.client;

import cn.autoforged.syringe_mod.SyringeMod;
import cn.autoforged.syringe_mod.item.InjectionGunItem;
import cn.autoforged.syringe_mod.item.ModItems;
import net.minecraft.core.component.DataComponents;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.component.DyedItemColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;

@EventBusSubscriber(
        modid = SyringeMod.MODID,
        value = Dist.CLIENT)
public final class InjectionGunClientSetup {
    @SubscribeEvent
    public static void registerClientExtensions(RegisterClientExtensionsEvent event) {
        event.registerItem(new InjectionGunClientExtensions(), ModItems.INJECTION_GUN.get());
    }

    @SubscribeEvent
    public static void registerItemColors(RegisterColorHandlersEvent.Item event) {
        event.register(
                (stack, tintIndex) -> tintIndex == 0
                        ? FastColor.ARGB32.opaque(stack.getOrDefault(
                                DataComponents.POTION_CONTENTS,
                                PotionContents.EMPTY).getColor())
                        : -1,
                ModItems.POTION_AMPOULE.get());
        event.register(
                (stack, tintIndex) -> tintIndex == 0
                        ? FastColor.ARGB32.opaque(AmpouleColors.color(
                                InjectionGunItem.getLoadedAmpoule(stack)))
                        : -1,
                ModItems.INJECTION_GUN.get());
        event.register(
                (stack, tintIndex) -> tintIndex == 1
                        ? FastColor.ARGB32.opaque(
                                DyedItemColor.getOrDefault(stack, 0xFFFFFF))
                        : -1,
                ModItems.INJECTION_DART.get());
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ItemProperties.register(
                    ModItems.INJECTION_GUN.get(),
                    ResourceLocation.fromNamespaceAndPath(SyringeMod.MODID, "loaded"),
                    (stack, level, living, seed) ->
                            InjectionGunItem.hasLoadedAmpoule(stack) ? 1.0F : 0.0F);
            ItemProperties.register(
                    ModItems.INJECTION_GUN.get(),
                    ResourceLocation.fromNamespaceAndPath(SyringeMod.MODID, "reload_pull"),
                    (stack, level, living, seed) ->
                            ClientGunAnimations.reloadModelPull(living));
        });
    }

    private InjectionGunClientSetup() {
    }
}
