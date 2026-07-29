package cn.autoforged.syringe_mod.blockentity;

import cn.autoforged.syringe_mod.SyringeMod;
import cn.autoforged.syringe_mod.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, SyringeMod.MODID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<PotionCraftingTableBlockEntity>> POTION_CRAFTING_TABLE =
            BLOCK_ENTITIES.register("potion_crafting_table",
                    () -> BlockEntityType.Builder.of(PotionCraftingTableBlockEntity::new,
                            ModBlocks.POTION_CRAFTING_TABLE.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<PotionMixingTableBlockEntity>> POTION_MIXING_TABLE =
            BLOCK_ENTITIES.register("potion_mixing_table",
                    () -> BlockEntityType.Builder.of(PotionMixingTableBlockEntity::new,
                            ModBlocks.POTION_MIXING_TABLE.get()).build(null));

    private ModBlockEntities() {}
}
