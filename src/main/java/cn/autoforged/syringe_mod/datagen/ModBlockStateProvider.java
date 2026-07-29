package cn.autoforged.syringe_mod.datagen;

import cn.autoforged.syringe_mod.SyringeMod;
import cn.autoforged.syringe_mod.block.ModBlocks;
import cn.autoforged.syringe_mod.block.PotionCraftingTableBlock;
import cn.autoforged.syringe_mod.block.PotionMixingTableBlock;
import net.minecraft.data.PackOutput;
import net.minecraft.core.Direction;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ModBlockStateProvider extends BlockStateProvider {

    public ModBlockStateProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, SyringeMod.MODID, existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        ModelFile craftingTable = models().getExistingFile(modLoc("block/potion_crafting_table"));
        ModelFile activeCraftingTable = models().getExistingFile(modLoc("block/potion_crafting_table_active"));
        var craftingVariants = getVariantBuilder(ModBlocks.POTION_CRAFTING_TABLE.get());
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            // Both Blockbench models are authored with their control panel on
            // the north (-Z) face, so north is the unrotated variant.
            int yRotation = (((int) direction.toYRot()) + 180) % 360;
            craftingVariants
                    .partialState()
                    .with(PotionCraftingTableBlock.LIT, false)
                    .with(PotionCraftingTableBlock.FACING, direction)
                    .modelForState()
                    .modelFile(craftingTable)
                    .rotationY(yRotation)
                    .addModel()
                    .partialState()
                    .with(PotionCraftingTableBlock.LIT, true)
                    .with(PotionCraftingTableBlock.FACING, direction)
                    .modelForState()
                    .modelFile(activeCraftingTable)
                    .rotationY(yRotation)
                    .addModel();
        }
        simpleBlockItem(ModBlocks.POTION_CRAFTING_TABLE.get(), craftingTable);

        ModelFile mixingTable = models().getExistingFile(modLoc("block/potion_mixing_table"));
        ModelFile activeMixingTable =
                models().getExistingFile(modLoc("block/potion_mixing_table_active"));
        var mixingVariants = getVariantBuilder(ModBlocks.POTION_MIXING_TABLE.get());
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            int yRotation = (((int) direction.toYRot()) + 180) % 360;
            mixingVariants
                    .partialState()
                    .with(PotionMixingTableBlock.PROCESSING, false)
                    .with(PotionMixingTableBlock.FACING, direction)
                    .modelForState()
                    .modelFile(mixingTable)
                    .rotationY(yRotation)
                    .addModel()
                    .partialState()
                    .with(PotionMixingTableBlock.PROCESSING, true)
                    .with(PotionMixingTableBlock.FACING, direction)
                    .modelForState()
                    .modelFile(activeMixingTable)
                    .rotationY(yRotation)
                    .addModel();
        }
        simpleBlockItem(ModBlocks.POTION_MIXING_TABLE.get(), mixingTable);
    }
}
