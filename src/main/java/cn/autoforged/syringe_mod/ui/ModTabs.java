package cn.autoforged.syringe_mod.ui;

import cn.autoforged.syringe_mod.SyringeMod;
import cn.autoforged.syringe_mod.item.ModItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, SyringeMod.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> SYRINGE_TAB =
            CREATIVE_TABS.register("syringe_tab", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup." + SyringeMod.MODID))
                    .withTabsBefore(CreativeModeTabs.FOOD_AND_DRINKS)
                    .icon(() -> ModItems.CURE_INJECTION.get().getDefaultInstance())
                    .displayItems((params, output) -> {
                        output.accept(ModItems.CURE_INJECTION.get());
                        output.accept(ModItems.STEM_CELL_INJECTION.get());
                        output.accept(ModItems.CELL_REPAIR_INJECTION.get());
                        output.accept(ModItems.MIXED_MEDICAL_MEDICINE_MIXTURE.get());
                        output.accept(ModItems.STEM_CELL_MEDICINE_MIXTURE.get());
                        output.accept(ModItems.CELL_REPAIR_MEDICINE_MIXTURE.get());
                        output.accept(ModItems.SATURATION_METABOLISM_INJECTION.get());
                        output.accept(ModItems.SATURATION_METABOLISM_MEDICINE_MIXTURE.get());
                        output.accept(ModItems.SURGE_INJECTION.get());
                        output.accept(ModItems.SURGE_MEDICINE_MIXTURE.get());
                        output.accept(ModItems.RESISTANCE_INJECTION.get());
                        output.accept(ModItems.RESISTANCE_MEDICINE_MIXTURE.get());
                        output.accept(ModItems.X_REAGENT.get());
                        output.accept(ModItems.EXPERIMENTAL_MEDICINE_MIXTURE.get());
                        output.accept(ModItems.EXPERIMENTAL_INJECTION.get());
                        output.accept(ModItems.IMMUNITY_ENHANCEMENT_INJECTION.get());
                        output.accept(ModItems.IMMUNITY_ENHANCEMENT_MEDICINE_MIXTURE.get());
                        output.accept(ModItems.SYRINGE_BAG.get());
                        output.accept(ModItems.POTION_CRAFTING_TABLE.get());
                        output.accept(ModItems.POTION_MIXING_TABLE.get());
                    })
                    .build());

    private ModTabs() {}
}
