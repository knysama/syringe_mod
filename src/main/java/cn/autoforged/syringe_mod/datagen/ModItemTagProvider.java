package cn.autoforged.syringe_mod.datagen;

import cn.autoforged.syringe_mod.SyringeMod;
import cn.autoforged.syringe_mod.item.ModItems;
import cn.autoforged.syringe_mod.tag.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends ItemTagsProvider {
    public ModItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
                              CompletableFuture<TagLookup<Block>> blockTags,
                              @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags, SyringeMod.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(ModTags.Items.SYRINGES)
                .add(ModItems.CURE_INJECTION.get())
                .add(ModItems.STEM_CELL_INJECTION.get())
                .add(ModItems.CELL_REPAIR_INJECTION.get())
                .add(ModItems.SATURATION_METABOLISM_INJECTION.get())
                .add(ModItems.SURGE_INJECTION.get())
                .add(ModItems.RESISTANCE_INJECTION.get())
                .add(ModItems.EXPERIMENTAL_INJECTION.get())
                .add(ModItems.IMMUNITY_ENHANCEMENT_INJECTION.get());

        tag(ModTags.Items.CURIOS_INJECTION_KIT)
                .add(ModItems.SYRINGE_BAG.get());

        tag(ModTags.Items.MEDICINE_MIXTURES)
                .add(ModItems.MIXED_MEDICAL_MEDICINE_MIXTURE.get())
                .add(ModItems.STEM_CELL_MEDICINE_MIXTURE.get())
                .add(ModItems.CELL_REPAIR_MEDICINE_MIXTURE.get())
                .add(ModItems.SATURATION_METABOLISM_MEDICINE_MIXTURE.get())
                .add(ModItems.SURGE_MEDICINE_MIXTURE.get())
                .add(ModItems.RESISTANCE_MEDICINE_MIXTURE.get())
                .add(ModItems.EXPERIMENTAL_MEDICINE_MIXTURE.get())
                .add(ModItems.IMMUNITY_ENHANCEMENT_MEDICINE_MIXTURE.get());
    }
}
