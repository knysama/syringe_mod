package cn.autoforged.syringe_mod.integration.emi;

import cn.autoforged.syringe_mod.SyringeMod;
import cn.autoforged.syringe_mod.block.ModBlocks;
import cn.autoforged.syringe_mod.recipe.ModRecipeTypes;
import cn.autoforged.syringe_mod.recipe.PotionCraftingRecipe;
import cn.autoforged.syringe_mod.recipe.PotionMixingRecipe;
import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;

@EmiEntrypoint
public class SyringeModEmiPlugin implements EmiPlugin {
    public static final EmiRecipeCategory POTION_CRAFTING_CATEGORY = new EmiRecipeCategory(
            ResourceLocation.fromNamespaceAndPath(SyringeMod.MODID, "potion_crafting"),
            EmiStack.of(ModBlocks.POTION_CRAFTING_TABLE.get()));

    public static final EmiRecipeCategory POTION_MIXING_CATEGORY = new EmiRecipeCategory(
            ResourceLocation.fromNamespaceAndPath(SyringeMod.MODID, "potion_mixing"),
            EmiStack.of(ModBlocks.POTION_MIXING_TABLE.get()));

    @Override
    public void register(EmiRegistry registry) {
        registry.addCategory(POTION_CRAFTING_CATEGORY);
        registry.addWorkstation(POTION_CRAFTING_CATEGORY, EmiStack.of(ModBlocks.POTION_CRAFTING_TABLE.get()));

        registry.addCategory(POTION_MIXING_CATEGORY);
        registry.addWorkstation(POTION_MIXING_CATEGORY, EmiStack.of(ModBlocks.POTION_MIXING_TABLE.get()));

        RecipeManager recipeManager = registry.getRecipeManager();
        for (RecipeHolder<PotionCraftingRecipe> holder : recipeManager.getAllRecipesFor(ModRecipeTypes.POTION_CRAFTING)) {
            registry.addRecipe(new PotionCraftingEmiRecipe(holder.value()));
        }
        for (RecipeHolder<PotionMixingRecipe> holder : recipeManager.getAllRecipesFor(ModRecipeTypes.POTION_MIXING)) {
            registry.addRecipe(new PotionMixingEmiRecipe(holder.value()));
        }
    }
}
