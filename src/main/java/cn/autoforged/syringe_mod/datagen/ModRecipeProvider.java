package cn.autoforged.syringe_mod.datagen;

import cn.autoforged.syringe_mod.SyringeMod;
import cn.autoforged.syringe_mod.item.ModItems;
import cn.autoforged.syringe_mod.recipe.PotionCraftingRecipe;
import cn.autoforged.syringe_mod.recipe.PotionMixingRecipe;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;
import net.neoforged.neoforge.common.crafting.DataComponentIngredient;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
    public ModRecipeProvider(
            PackOutput output,
            CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput output) {
        savePotionAmpouleRecipe(output);
        saveVodkaRecipe(output);
        saveInjectionGunRecipe(output);

        // The crafting table now fills ampoules directly. There is no
        // intermediate "medicine mixture" item layer.
        saveCraftingAmpoule(output, "stem_cell_injection",
                Ingredient.of(Items.GLISTERING_MELON_SLICE),
                Ingredient.of(Items.IRON_NUGGET),
                ModItems.STEM_CELL_INJECTION.get(), 4,
                Items.GLISTERING_MELON_SLICE);
        saveCraftingAmpoule(output, "cell_repair_injection",
                Ingredient.of(Items.GHAST_TEAR),
                Ingredient.of(Items.IRON_NUGGET),
                ModItems.CELL_REPAIR_INJECTION.get(), 4,
                Items.GHAST_TEAR);
        saveCraftingAmpoule(output, "saturation_metabolism_injection",
                Ingredient.of(Items.SUGAR),
                Ingredient.of(Items.GOLDEN_CARROT),
                ModItems.SATURATION_METABOLISM_INJECTION.get(), 4,
                Items.GOLDEN_CARROT);
        saveCraftingAmpoule(output, "surge_injection",
                Ingredient.of(Items.PRISMARINE_SHARD),
                Ingredient.of(Items.PRISMARINE_CRYSTALS),
                ModItems.SURGE_INJECTION.get(), 4,
                Items.PRISMARINE_CRYSTALS);
        saveCraftingAmpoule(output, "resistance_injection",
                Ingredient.of(Items.IRON_INGOT),
                Ingredient.EMPTY,
                ModItems.RESISTANCE_INJECTION.get(), 4,
                Items.IRON_INGOT);
        saveCraftingAmpoule(output, "immunity_enhancement_injection",
                Ingredient.of(Items.MILK_BUCKET),
                Ingredient.of(Items.GLISTERING_MELON_SLICE),
                ModItems.IMMUNITY_ENHANCEMENT_INJECTION.get(), 1,
                Items.MILK_BUCKET);

        // The mixing table combines completed ampoules directly.
        saveMixingAmpoule(output, "cure_injection",
                ModItems.STEM_CELL_INJECTION.get(),
                ModItems.CELL_REPAIR_INJECTION.get(),
                ModItems.CURE_INJECTION.get(), 4);
        saveMixingAmpoule(output, "experimental_injection",
                ModItems.RESISTANCE_INJECTION.get(),
                ModItems.SURGE_INJECTION.get(),
                ModItems.EXPERIMENTAL_INJECTION.get(), 2);

        saveJojaRecipe(output);
        saveEquipmentRecipes(output);
    }

    private void savePotionAmpouleRecipe(RecipeOutput output) {
        ResourceLocation id = id("potion_ampoule");
        output.accept(
                id,
                new PotionCraftingRecipe(
                        Ingredient.of(Items.GLASS_BOTTLE),
                        Ingredient.of(Items.POTION),
                        Ingredient.EMPTY,
                        new ItemStack(ModItems.POTION_AMPOULE.get(), 4)),
                output.advancement()
                        .addCriterion("has_potion", has(Items.POTION))
                        .addCriterion("has_glass_bottle", has(Items.GLASS_BOTTLE))
                        .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
                        .rewards(AdvancementRewards.Builder.recipe(id))
                        .requirements(AdvancementRequirements.Strategy.OR)
                        .build(id.withPrefix("recipes/")));
    }

    private void saveVodkaRecipe(RecipeOutput output) {
        ResourceLocation id = id("vodka");
        output.accept(
                id,
                new PotionCraftingRecipe(
                        DataComponentIngredient.of(
                                false,
                                DataComponents.POTION_CONTENTS,
                                new PotionContents(Potions.WATER),
                                Items.POTION),
                        Ingredient.of(Items.POTATO, Items.POISONOUS_POTATO),
                        Ingredient.EMPTY,
                        new ItemStack(ModItems.VODKA.get())),
                output.advancement()
                        .addCriterion("has_potato", has(Items.POTATO))
                        .addCriterion("has_poisonous_potato", has(Items.POISONOUS_POTATO))
                        .addCriterion("has_water_bottle", has(Items.POTION))
                        .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
                        .rewards(AdvancementRewards.Builder.recipe(id))
                        .requirements(AdvancementRequirements.Strategy.OR)
                        .build(id.withPrefix("recipes/")));
    }

    private void saveCraftingAmpoule(
            RecipeOutput output,
            String name,
            Ingredient sideA,
            Ingredient sideB,
            Item result,
            int count,
            ItemLike unlockItem) {
        ResourceLocation id = id(name);
        output.accept(
                id,
                new PotionCraftingRecipe(
                        Ingredient.of(Items.GLASS_BOTTLE),
                        sideA,
                        sideB,
                        new ItemStack(result, count)),
                output.advancement()
                        .addCriterion("has_glass_bottle", has(Items.GLASS_BOTTLE))
                        .addCriterion("has_ingredient", has(unlockItem))
                        .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
                        .rewards(AdvancementRewards.Builder.recipe(id))
                        .requirements(AdvancementRequirements.Strategy.OR)
                        .build(id.withPrefix("recipes/")));
    }

    private void saveMixingAmpoule(
            RecipeOutput output,
            String name,
            ItemLike left,
            ItemLike right,
            Item result,
            int count) {
        ResourceLocation id = id(name);
        output.accept(
                id,
                new PotionMixingRecipe(
                        Ingredient.of(left),
                        Ingredient.of(right),
                        new ItemStack(result, count)),
                output.advancement()
                        .addCriterion("has_left_ampoule", has(left))
                        .addCriterion("has_right_ampoule", has(right))
                        .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
                        .rewards(AdvancementRewards.Builder.recipe(id))
                        .requirements(AdvancementRequirements.Strategy.OR)
                        .build(id.withPrefix("recipes/")));
    }

    private void saveJojaRecipe(RecipeOutput output) {
        ResourceLocation id = id("joja_cola");
        output.accept(
                id,
                new PotionMixingRecipe(
                        DataComponentIngredient.of(
                                false,
                                DataComponents.POTION_CONTENTS,
                                new PotionContents(Potions.POISON),
                                Items.POTION),
                        DataComponentIngredient.of(
                                false,
                                DataComponents.POTION_CONTENTS,
                                new PotionContents(Potions.WEAKNESS),
                                Items.POTION),
                        new ItemStack(ModItems.JOJA_COLA.get())),
                output.advancement()
                        .addCriterion("has_poison", has(Items.POTION))
                        .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
                        .rewards(AdvancementRewards.Builder.recipe(id))
                        .requirements(AdvancementRequirements.Strategy.OR)
                        .build(id.withPrefix("recipes/")));
    }

    private void saveInjectionGunRecipe(RecipeOutput output) {
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.INJECTION_GUN.get())
                .define('I', Items.IRON_INGOT)
                .define('C', Items.COPPER_INGOT)
                .define('L', Items.LEVER)
                .pattern(" II")
                .pattern("ICI")
                .pattern("L  ")
                .unlockedBy("has_iron_ingot", has(Items.IRON_INGOT))
                .unlockedBy("has_copper_ingot", has(Items.COPPER_INGOT))
                .save(output);
    }

    private void saveEquipmentRecipes(RecipeOutput output) {
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.SYRINGE_BAG.get())
                .define('#', Items.IRON_NUGGET)
                .define('L', Items.LEATHER)
                .pattern("###")
                .pattern("LLL")
                .pattern("L L")
                .unlockedBy("has_leather", has(Items.LEATHER))
                .unlockedBy("has_iron_nugget", has(Items.IRON_NUGGET))
                .save(output);

        ShapedRecipeBuilder.shaped(
                        RecipeCategory.DECORATIONS,
                        ModItems.POTION_CRAFTING_TABLE.get())
                .define('C', Items.COPPER_BLOCK)
                .define('G', Items.GLASS_BOTTLE)
                .define('I', Items.IRON_BLOCK)
                .define('H', Items.CHEST)
                .pattern("CGC")
                .pattern("IGI")
                .pattern("IHI")
                .unlockedBy("has_iron_block", has(Items.IRON_BLOCK))
                .unlockedBy("has_copper_block", has(Items.COPPER_BLOCK))
                .unlockedBy("has_glass_bottle", has(Items.GLASS_BOTTLE))
                .unlockedBy("has_chest", has(Items.CHEST))
                .save(output);

        ShapedRecipeBuilder.shaped(
                        RecipeCategory.DECORATIONS,
                        ModItems.POTION_MIXING_TABLE.get())
                .define('G', Items.GLASS_BOTTLE)
                .define('I', Items.IRON_BLOCK)
                .define('H', Items.CHEST)
                .define('C', Items.COPPER_BLOCK)
                .pattern("GGG")
                .pattern("IHI")
                .pattern("ICI")
                .unlockedBy("has_iron_block", has(Items.IRON_BLOCK))
                .unlockedBy("has_copper_block", has(Items.COPPER_BLOCK))
                .unlockedBy("has_glass_bottle", has(Items.GLASS_BOTTLE))
                .unlockedBy("has_chest", has(Items.CHEST))
                .save(output);
    }

    private static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(SyringeMod.MODID, path);
    }
}
