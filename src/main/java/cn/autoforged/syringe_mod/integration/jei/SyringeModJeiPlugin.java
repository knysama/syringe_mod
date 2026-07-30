package cn.autoforged.syringe_mod.integration.jei;

import cn.autoforged.syringe_mod.SyringeMod;
import cn.autoforged.syringe_mod.block.ModBlocks;
import cn.autoforged.syringe_mod.recipe.ModRecipeTypes;
import cn.autoforged.syringe_mod.recipe.PotionCraftingRecipe;
import cn.autoforged.syringe_mod.recipe.PotionMixingRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;

import java.util.List;

@JeiPlugin
public class SyringeModJeiPlugin implements IModPlugin {
    public static final RecipeType<PotionCraftingRecipe> POTION_CRAFTING_TYPE =
            RecipeType.create(SyringeMod.MODID, "potion_crafting", PotionCraftingRecipe.class);

    public static final RecipeType<PotionMixingRecipe> POTION_MIXING_TYPE =
            RecipeType.create(SyringeMod.MODID, "potion_mixing", PotionMixingRecipe.class);

    @Override
    public ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(SyringeMod.MODID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(
                new PotionCraftingRecipeCategory(registration.getJeiHelpers().getGuiHelper()),
                new PotionMixingRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(
                ModBlocks.POTION_CRAFTING_TABLE.toStack(), POTION_CRAFTING_TYPE);
        registration.addRecipeCatalyst(
                ModBlocks.POTION_MIXING_TABLE.toStack(), POTION_MIXING_TYPE);
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        var level = Minecraft.getInstance().level;
        if (level == null) return;
        RecipeManager recipeManager = level.getRecipeManager();
        List<RecipeHolder<PotionCraftingRecipe>> holders = recipeManager.getAllRecipesFor(ModRecipeTypes.POTION_CRAFTING);
        List<PotionCraftingRecipe> recipes = holders.stream().map(RecipeHolder::value).toList();
        registration.addRecipes(POTION_CRAFTING_TYPE, recipes);

        List<RecipeHolder<PotionMixingRecipe>> mixingHolders = recipeManager.getAllRecipesFor(ModRecipeTypes.POTION_MIXING);
        List<PotionMixingRecipe> mixingRecipes = mixingHolders.stream().map(RecipeHolder::value).toList();
        registration.addRecipes(POTION_MIXING_TYPE, mixingRecipes);
    }
}
