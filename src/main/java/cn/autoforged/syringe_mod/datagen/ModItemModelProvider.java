package cn.autoforged.syringe_mod.datagen;

import cn.autoforged.syringe_mod.SyringeMod;
import cn.autoforged.syringe_mod.item.ModItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ModItemModelProvider extends ItemModelProvider {

    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, SyringeMod.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicItem(ModItems.CURE_INJECTION.get());
        basicItem(ModItems.STEM_CELL_INJECTION.get());
        basicItem(ModItems.CELL_REPAIR_INJECTION.get());
        basicItem(ModItems.SATURATION_METABOLISM_INJECTION.get());
        basicItem(ModItems.SURGE_INJECTION.get());
        basicItem(ModItems.RESISTANCE_INJECTION.get());
        basicItem(ModItems.EXPERIMENTAL_INJECTION.get());
        basicItem(ModItems.IMMUNITY_ENHANCEMENT_INJECTION.get());

        basicItem(ModItems.SYRINGE_BAG.get());

        basicItem(ModItems.MIXED_MEDICAL_MEDICINE_MIXTURE.get());
        basicItem(ModItems.STEM_CELL_MEDICINE_MIXTURE.get());
        basicItem(ModItems.CELL_REPAIR_MEDICINE_MIXTURE.get());
        basicItem(ModItems.SATURATION_METABOLISM_MEDICINE_MIXTURE.get());
        basicItem(ModItems.SURGE_MEDICINE_MIXTURE.get());
        basicItem(ModItems.RESISTANCE_MEDICINE_MIXTURE.get());
        basicItem(ModItems.EXPERIMENTAL_MEDICINE_MIXTURE.get());
        basicItem(ModItems.IMMUNITY_ENHANCEMENT_MEDICINE_MIXTURE.get());
        basicItem(ModItems.X_REAGENT.get());
    }
}
