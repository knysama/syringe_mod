package cn.autoforged.syringe_mod.datagen;

import cn.autoforged.syringe_mod.SyringeMod;
import cn.autoforged.syringe_mod.item.ModItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
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
        withExistingParent(ModItems.VODKA.getId().getPath(), mcLoc("item/generated"))
                .texture("layer0", modLoc("item/potion_ampoule_liquid"))
                .texture("layer1", modLoc("item/potion_ampoule_glass"));
        withExistingParent(ModItems.POTION_AMPOULE.getId().getPath(), mcLoc("item/generated"))
                .texture("layer0", modLoc("item/potion_ampoule_liquid"))
                .texture("layer1", modLoc("item/potion_ampoule_glass"));

        getBuilder(ModItems.SYRINGE_BAG.getId().getPath())
                .parent(new ModelFile.UncheckedModelFile(mcLoc("builtin/entity")))
                .texture("particle", modLoc("item/syringe_bag"));
        withExistingParent("syringe_bag_open", mcLoc("item/generated"))
                .texture("layer0", modLoc("item/syringe_bag_open"));

        withExistingParent(ModItems.JOJA_COLA.getId().getPath(), mcLoc("item/generated"))
                .texture("layer0", modLoc("item/joja_cola"));
    }
}
