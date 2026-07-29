package cn.autoforged.syringe_mod.client;

import cn.autoforged.syringe_mod.SyringeMod;
import cn.autoforged.syringe_mod.ui.ModMenuTypes;
import cn.autoforged.syringe_mod.ui.PotionCraftingTableScreen;
import cn.autoforged.syringe_mod.ui.PotionMixingTableScreen;
import cn.autoforged.syringe_mod.ui.SyringeBagScreen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

@EventBusSubscriber(modid = SyringeMod.MODID, value = Dist.CLIENT)
public class ModScreenRegistrar {

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(ModMenuTypes.SYRINGE_BAG.get(), SyringeBagScreen::new);
        event.register(ModMenuTypes.POTION_CRAFTING_TABLE.get(), PotionCraftingTableScreen::new);
        event.register(ModMenuTypes.POTION_MIXING_TABLE.get(), PotionMixingTableScreen::new);
    }
}
