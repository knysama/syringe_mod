package cn.autoforged.syringe_mod.recipe;

import cn.autoforged.syringe_mod.SyringeMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeType;

public class ModRecipeTypes {
    public static final RecipeType<PotionCraftingRecipe> POTION_CRAFTING = RecipeType.simple(
            ResourceLocation.fromNamespaceAndPath(SyringeMod.MODID, "potion_crafting"));

    public static final RecipeType<PotionMixingRecipe> POTION_MIXING = RecipeType.simple(
            ResourceLocation.fromNamespaceAndPath(SyringeMod.MODID, "potion_mixing"));

    private ModRecipeTypes() {}
}
