package cn.autoforged.syringe_mod.block;

import cn.autoforged.syringe_mod.SyringeMod;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(SyringeMod.MODID);

    public static final DeferredBlock<Block> POTION_CRAFTING_TABLE = registerBlock("potion_crafting_table",
            () -> new PotionCraftingTableBlock(BlockBehaviour.Properties.of()
                    .strength(2.0f)
                    .sound(SoundType.STONE)
                    .noOcclusion()));

    public static final DeferredBlock<Block> POTION_MIXING_TABLE = registerBlock("potion_mixing_table",
            () -> new PotionMixingTableBlock(BlockBehaviour.Properties.of()
                    .strength(2.0f)
                    .sound(SoundType.STONE)
                    .noOcclusion()));

    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block) {
        return BLOCKS.register(name, block);
    }

    private ModBlocks() {}
}
