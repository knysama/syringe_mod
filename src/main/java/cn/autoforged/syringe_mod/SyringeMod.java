package cn.autoforged.syringe_mod;

import cn.autoforged.syringe_mod.block.ModBlocks;
import cn.autoforged.syringe_mod.blockentity.ModBlockEntities;
import cn.autoforged.syringe_mod.blockentity.PotionCraftingTableBlockEntity;
import cn.autoforged.syringe_mod.blockentity.PotionMixingTableBlockEntity;
import cn.autoforged.syringe_mod.item.ModItems;
import cn.autoforged.syringe_mod.recipe.ModRecipeSerializers;
import cn.autoforged.syringe_mod.sound.ModSounds;
import cn.autoforged.syringe_mod.ui.ModMenuTypes;
import cn.autoforged.syringe_mod.ui.ModTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

@Mod(SyringeMod.MODID)
public class SyringeMod {
    public static final String MODID = "syringe_mod";

    public SyringeMod(IEventBus modEventBus, ModContainer modContainer) {
        ModBlocks.BLOCKS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModBlockEntities.BLOCK_ENTITIES.register(modEventBus);
        ModSounds.SOUND_EVENTS.register(modEventBus);
        ModTabs.CREATIVE_TABS.register(modEventBus);
        ModMenuTypes.MENUS.register(modEventBus);
        ModRecipeSerializers.SERIALIZERS.register(modEventBus);

        modEventBus.addListener(this::registerCapabilities);
    }

    private void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                ModBlockEntities.POTION_CRAFTING_TABLE.get(),
                PotionCraftingTableBlockEntity::getItemHandler);
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                ModBlockEntities.POTION_MIXING_TABLE.get(),
                PotionMixingTableBlockEntity::getItemHandler);
    }
}
