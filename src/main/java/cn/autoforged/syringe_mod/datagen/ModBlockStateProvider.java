package cn.autoforged.syringe_mod.datagen;

import cn.autoforged.syringe_mod.SyringeMod;
import cn.autoforged.syringe_mod.block.ModBlocks;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ModBlockStateProvider extends BlockStateProvider {

    public ModBlockStateProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, SyringeMod.MODID, existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlockWithItem(ModBlocks.POTION_CRAFTING_TABLE.get(),
                models().cubeBottomTop("potion_crafting_table",
                        modLoc("block/potion_crafting_table_side"),
                        modLoc("block/potion_crafting_table_bottom"),
                        modLoc("block/potion_crafting_table_top")));

        simpleBlockWithItem(ModBlocks.POTION_MIXING_TABLE.get(),
                models().cubeBottomTop("potion_mixing_table",
                        modLoc("block/potion_mixing_table_side"),
                        modLoc("block/potion_mixing_table_bottom"),
                        modLoc("block/potion_mixing_table_top")));
    }
}
