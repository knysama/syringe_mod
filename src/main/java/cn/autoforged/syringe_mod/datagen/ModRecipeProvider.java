package cn.autoforged.syringe_mod.datagen;

import cn.autoforged.syringe_mod.SyringeMod;
import cn.autoforged.syringe_mod.item.ModItems;
import cn.autoforged.syringe_mod.recipe.InjectionCraftingRecipe;
import cn.autoforged.syringe_mod.recipe.PotionCraftingRecipe;
import cn.autoforged.syringe_mod.recipe.PotionMixingRecipe;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {

    public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput output) {
        ResourceLocation cureId = ResourceLocation.fromNamespaceAndPath(SyringeMod.MODID, "cure_injection");
        output.accept(
                cureId,
                new InjectionCraftingRecipe(
                        Ingredient.of(ModItems.MIXED_MEDICAL_MEDICINE_MIXTURE.get()),
                        new ItemStack(ModItems.CURE_INJECTION.get(), 4),
                        CraftingBookCategory.MISC),
                output.advancement()
                        .addCriterion("has_mixture", has(ModItems.MIXED_MEDICAL_MEDICINE_MIXTURE.get()))
                        .addCriterion("has_water_bottle", has(Items.POTION))
                        .addCriterion("has_iron_nugget", has(Items.IRON_NUGGET))
                        .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(cureId))
                        .rewards(AdvancementRewards.Builder.recipe(cureId))
                        .requirements(AdvancementRequirements.Strategy.OR)
                        .build(cureId.withPrefix("recipes/"))
        );

        ResourceLocation stemCellId = ResourceLocation.fromNamespaceAndPath(SyringeMod.MODID, "stem_cell_injection");
        output.accept(
                stemCellId,
                new InjectionCraftingRecipe(
                        Ingredient.of(ModItems.STEM_CELL_MEDICINE_MIXTURE.get()),
                        new ItemStack(ModItems.STEM_CELL_INJECTION.get(), 4),
                        CraftingBookCategory.MISC),
                output.advancement()
                        .addCriterion("has_mixture", has(ModItems.STEM_CELL_MEDICINE_MIXTURE.get()))
                        .addCriterion("has_water_bottle", has(Items.POTION))
                        .addCriterion("has_iron_nugget", has(Items.IRON_NUGGET))
                        .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(stemCellId))
                        .rewards(AdvancementRewards.Builder.recipe(stemCellId))
                        .requirements(AdvancementRequirements.Strategy.OR)
                        .build(stemCellId.withPrefix("recipes/"))
        );

        ResourceLocation cellRepairId = ResourceLocation.fromNamespaceAndPath(SyringeMod.MODID, "cell_repair_injection");
        output.accept(
                cellRepairId,
                new InjectionCraftingRecipe(
                        Ingredient.of(ModItems.CELL_REPAIR_MEDICINE_MIXTURE.get()),
                        new ItemStack(ModItems.CELL_REPAIR_INJECTION.get(), 4),
                        CraftingBookCategory.MISC),
                output.advancement()
                        .addCriterion("has_mixture", has(ModItems.CELL_REPAIR_MEDICINE_MIXTURE.get()))
                        .addCriterion("has_water_bottle", has(Items.POTION))
                        .addCriterion("has_iron_nugget", has(Items.IRON_NUGGET))
                        .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(cellRepairId))
                        .rewards(AdvancementRewards.Builder.recipe(cellRepairId))
                        .requirements(AdvancementRequirements.Strategy.OR)
                        .build(cellRepairId.withPrefix("recipes/"))
        );

        ResourceLocation mixtureId = ResourceLocation.fromNamespaceAndPath(SyringeMod.MODID, "mixed_medical_medicine_mixture");
        output.accept(
                mixtureId,
                new PotionMixingRecipe(
                        Ingredient.of(ModItems.STEM_CELL_MEDICINE_MIXTURE.get()),
                        Ingredient.of(ModItems.CELL_REPAIR_MEDICINE_MIXTURE.get()),
                        new ItemStack(ModItems.MIXED_MEDICAL_MEDICINE_MIXTURE.get())),
                output.advancement()
                        .addCriterion("has_stem_cell", has(ModItems.STEM_CELL_MEDICINE_MIXTURE.get()))
                        .addCriterion("has_cell_repair", has(ModItems.CELL_REPAIR_MEDICINE_MIXTURE.get()))
                        .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(mixtureId))
                        .rewards(AdvancementRewards.Builder.recipe(mixtureId))
                        .requirements(AdvancementRequirements.Strategy.OR)
                        .build(mixtureId.withPrefix("recipes/"))
        );

        ResourceLocation stemCellMixtureId = ResourceLocation.fromNamespaceAndPath(SyringeMod.MODID, "stem_cell_medicine_mixture");
        output.accept(
                stemCellMixtureId,
                new PotionCraftingRecipe(
                        Ingredient.of(Items.GLASS_BOTTLE),
                        Ingredient.of(Items.GLISTERING_MELON_SLICE),
                        Ingredient.EMPTY,
                        new ItemStack(ModItems.STEM_CELL_MEDICINE_MIXTURE.get())),
                output.advancement()
                        .addCriterion("has_glass_bottle", has(Items.GLASS_BOTTLE))
                        .addCriterion("has_melon", has(Items.GLISTERING_MELON_SLICE))
                        .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(stemCellMixtureId))
                        .rewards(AdvancementRewards.Builder.recipe(stemCellMixtureId))
                        .requirements(AdvancementRequirements.Strategy.OR)
                        .build(stemCellMixtureId.withPrefix("recipes/"))
        );

        ResourceLocation cellRepairMixtureId = ResourceLocation.fromNamespaceAndPath(SyringeMod.MODID, "cell_repair_medicine_mixture");
        output.accept(
                cellRepairMixtureId,
                new PotionCraftingRecipe(
                        Ingredient.of(Items.GLASS_BOTTLE),
                        Ingredient.of(Items.GHAST_TEAR),
                        Ingredient.EMPTY,
                        new ItemStack(ModItems.CELL_REPAIR_MEDICINE_MIXTURE.get())),
                output.advancement()
                        .addCriterion("has_glass_bottle", has(Items.GLASS_BOTTLE))
                        .addCriterion("has_ghast_tear", has(Items.GHAST_TEAR))
                        .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(cellRepairMixtureId))
                        .rewards(AdvancementRewards.Builder.recipe(cellRepairMixtureId))
                        .requirements(AdvancementRequirements.Strategy.OR)
                        .build(cellRepairMixtureId.withPrefix("recipes/"))
        );

        ResourceLocation saturationInjectionId = ResourceLocation.fromNamespaceAndPath(SyringeMod.MODID, "saturation_metabolism_injection");
        output.accept(
                saturationInjectionId,
                new InjectionCraftingRecipe(
                        Ingredient.of(ModItems.SATURATION_METABOLISM_MEDICINE_MIXTURE.get()),
                        new ItemStack(ModItems.SATURATION_METABOLISM_INJECTION.get(), 4),
                        CraftingBookCategory.MISC),
                output.advancement()
                        .addCriterion("has_mixture", has(ModItems.SATURATION_METABOLISM_MEDICINE_MIXTURE.get()))
                        .addCriterion("has_water_bottle", has(Items.POTION))
                        .addCriterion("has_iron_nugget", has(Items.IRON_NUGGET))
                        .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(saturationInjectionId))
                        .rewards(AdvancementRewards.Builder.recipe(saturationInjectionId))
                        .requirements(AdvancementRequirements.Strategy.OR)
                        .build(saturationInjectionId.withPrefix("recipes/"))
        );

        ResourceLocation surgeInjectionId = ResourceLocation.fromNamespaceAndPath(SyringeMod.MODID, "surge_injection");
        output.accept(
                surgeInjectionId,
                new InjectionCraftingRecipe(
                        Ingredient.of(ModItems.SURGE_MEDICINE_MIXTURE.get()),
                        new ItemStack(ModItems.SURGE_INJECTION.get(), 4),
                        CraftingBookCategory.MISC),
                output.advancement()
                        .addCriterion("has_mixture", has(ModItems.SURGE_MEDICINE_MIXTURE.get()))
                        .addCriterion("has_water_bottle", has(Items.POTION))
                        .addCriterion("has_iron_nugget", has(Items.IRON_NUGGET))
                        .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(surgeInjectionId))
                        .rewards(AdvancementRewards.Builder.recipe(surgeInjectionId))
                        .requirements(AdvancementRequirements.Strategy.OR)
                        .build(surgeInjectionId.withPrefix("recipes/"))
        );

        ResourceLocation resistanceInjectionId = ResourceLocation.fromNamespaceAndPath(SyringeMod.MODID, "resistance_injection");
        output.accept(
                resistanceInjectionId,
                new InjectionCraftingRecipe(
                        Ingredient.of(ModItems.RESISTANCE_MEDICINE_MIXTURE.get()),
                        new ItemStack(ModItems.RESISTANCE_INJECTION.get(), 4),
                        CraftingBookCategory.MISC),
                output.advancement()
                        .addCriterion("has_mixture", has(ModItems.RESISTANCE_MEDICINE_MIXTURE.get()))
                        .addCriterion("has_water_bottle", has(Items.POTION))
                        .addCriterion("has_iron_nugget", has(Items.IRON_NUGGET))
                        .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(resistanceInjectionId))
                        .rewards(AdvancementRewards.Builder.recipe(resistanceInjectionId))
                        .requirements(AdvancementRequirements.Strategy.OR)
                        .build(resistanceInjectionId.withPrefix("recipes/"))
        );

        ResourceLocation saturationMixtureId = ResourceLocation.fromNamespaceAndPath(SyringeMod.MODID, "saturation_metabolism_medicine_mixture");
        output.accept(
                saturationMixtureId,
                new PotionCraftingRecipe(
                        Ingredient.of(Items.GLASS_BOTTLE),
                        Ingredient.of(Items.SUGAR),
                        Ingredient.of(Items.GOLDEN_CARROT),
                        new ItemStack(ModItems.SATURATION_METABOLISM_MEDICINE_MIXTURE.get())),
                output.advancement()
                        .addCriterion("has_glass_bottle", has(Items.GLASS_BOTTLE))
                        .addCriterion("has_sugar", has(Items.SUGAR))
                        .addCriterion("has_golden_carrot", has(Items.GOLDEN_CARROT))
                        .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(saturationMixtureId))
                        .rewards(AdvancementRewards.Builder.recipe(saturationMixtureId))
                        .requirements(AdvancementRequirements.Strategy.OR)
                        .build(saturationMixtureId.withPrefix("recipes/"))
        );

        ResourceLocation surgeMixtureId = ResourceLocation.fromNamespaceAndPath(SyringeMod.MODID, "surge_medicine_mixture");
        output.accept(
                surgeMixtureId,
                new PotionCraftingRecipe(
                        Ingredient.of(Items.GLASS_BOTTLE),
                        Ingredient.of(Items.PRISMARINE_SHARD),
                        Ingredient.of(Items.PRISMARINE_CRYSTALS),
                        new ItemStack(ModItems.SURGE_MEDICINE_MIXTURE.get())),
                output.advancement()
                        .addCriterion("has_glass_bottle", has(Items.GLASS_BOTTLE))
                        .addCriterion("has_prismarine_shard", has(Items.PRISMARINE_SHARD))
                        .addCriterion("has_prismarine_crystals", has(Items.PRISMARINE_CRYSTALS))
                        .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(surgeMixtureId))
                        .rewards(AdvancementRewards.Builder.recipe(surgeMixtureId))
                        .requirements(AdvancementRequirements.Strategy.OR)
                        .build(surgeMixtureId.withPrefix("recipes/"))
        );

        ResourceLocation resistanceMixtureId = ResourceLocation.fromNamespaceAndPath(SyringeMod.MODID, "resistance_medicine_mixture");
        output.accept(
                resistanceMixtureId,
                new PotionCraftingRecipe(
                        Ingredient.of(Items.GLASS_BOTTLE),
                        Ingredient.of(Items.IRON_INGOT),
                        Ingredient.EMPTY,
                        new ItemStack(ModItems.RESISTANCE_MEDICINE_MIXTURE.get())),
                output.advancement()
                        .addCriterion("has_glass_bottle", has(Items.GLASS_BOTTLE))
                        .addCriterion("has_iron_ingot", has(Items.IRON_INGOT))
                        .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(resistanceMixtureId))
                        .rewards(AdvancementRewards.Builder.recipe(resistanceMixtureId))
                        .requirements(AdvancementRequirements.Strategy.OR)
                        .build(resistanceMixtureId.withPrefix("recipes/"))
        );

        // --- X Reagent (Potion Mixing: Resistance Mixture + Surge Mixture) ---
        ResourceLocation xReagentId = ResourceLocation.fromNamespaceAndPath(SyringeMod.MODID, "x_reagent");
        output.accept(
                xReagentId,
                new PotionMixingRecipe(
                        Ingredient.of(ModItems.RESISTANCE_MEDICINE_MIXTURE.get()),
                        Ingredient.of(ModItems.SURGE_MEDICINE_MIXTURE.get()),
                        new ItemStack(ModItems.X_REAGENT.get())),
                output.advancement()
                        .addCriterion("has_resistance", has(ModItems.RESISTANCE_MEDICINE_MIXTURE.get()))
                        .addCriterion("has_surge", has(ModItems.SURGE_MEDICINE_MIXTURE.get()))
                        .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(xReagentId))
                        .rewards(AdvancementRewards.Builder.recipe(xReagentId))
                        .requirements(AdvancementRequirements.Strategy.OR)
                        .build(xReagentId.withPrefix("recipes/"))
        );

        // --- Experimental Medicine Mixture (Potion Crafting: Mixed Medical + X Reagent + Glass Bottle) ---
        ResourceLocation experimentalMixtureId = ResourceLocation.fromNamespaceAndPath(SyringeMod.MODID, "experimental_medicine_mixture");
        output.accept(
                experimentalMixtureId,
                new PotionCraftingRecipe(
                        Ingredient.of(Items.GLASS_BOTTLE),
                        Ingredient.of(ModItems.MIXED_MEDICAL_MEDICINE_MIXTURE.get()),
                        Ingredient.of(ModItems.X_REAGENT.get()),
                        new ItemStack(ModItems.EXPERIMENTAL_MEDICINE_MIXTURE.get())),
                output.advancement()
                        .addCriterion("has_mixed", has(ModItems.MIXED_MEDICAL_MEDICINE_MIXTURE.get()))
                        .addCriterion("has_x_reagent", has(ModItems.X_REAGENT.get()))
                        .addCriterion("has_glass_bottle", has(Items.GLASS_BOTTLE))
                        .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(experimentalMixtureId))
                        .rewards(AdvancementRewards.Builder.recipe(experimentalMixtureId))
                        .requirements(AdvancementRequirements.Strategy.OR)
                        .build(experimentalMixtureId.withPrefix("recipes/"))
        );

        // --- Experimental Injection (Shapeless: Experimental Mixture + Iron Nugget → 2) ---
        ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, ModItems.EXPERIMENTAL_INJECTION.get(), 2)
                .requires(ModItems.EXPERIMENTAL_MEDICINE_MIXTURE.get())
                .requires(Items.IRON_NUGGET)
                .unlockedBy("has_mixture", has(ModItems.EXPERIMENTAL_MEDICINE_MIXTURE.get()))
                .unlockedBy("has_iron_nugget", has(Items.IRON_NUGGET))
                .save(output);

        // --- Immunity Enhancement Medicine Mixture (Potion Crafting: Milk Bucket + Glistening Melon + Glass Bottle) ---
        ResourceLocation immunityMixtureId = ResourceLocation.fromNamespaceAndPath(SyringeMod.MODID, "immunity_enhancement_medicine_mixture");
        output.accept(
                immunityMixtureId,
                new PotionCraftingRecipe(
                        Ingredient.of(Items.GLASS_BOTTLE),
                        Ingredient.of(Items.MILK_BUCKET),
                        Ingredient.of(Items.GLISTERING_MELON_SLICE),
                        new ItemStack(ModItems.IMMUNITY_ENHANCEMENT_MEDICINE_MIXTURE.get())),
                output.advancement()
                        .addCriterion("has_milk_bucket", has(Items.MILK_BUCKET))
                        .addCriterion("has_glistening_melon", has(Items.GLISTERING_MELON_SLICE))
                        .addCriterion("has_glass_bottle", has(Items.GLASS_BOTTLE))
                        .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(immunityMixtureId))
                        .rewards(AdvancementRewards.Builder.recipe(immunityMixtureId))
                        .requirements(AdvancementRequirements.Strategy.OR)
                        .build(immunityMixtureId.withPrefix("recipes/"))
        );

        // --- Immunity Enhancement Injection (Injection Crafting: Mixture + Water Bottle + Iron Nugget) ---
        ResourceLocation immunityInjectionId = ResourceLocation.fromNamespaceAndPath(SyringeMod.MODID, "immunity_enhancement_injection");
        output.accept(
                immunityInjectionId,
                new InjectionCraftingRecipe(
                        Ingredient.of(ModItems.IMMUNITY_ENHANCEMENT_MEDICINE_MIXTURE.get()),
                        new ItemStack(ModItems.IMMUNITY_ENHANCEMENT_INJECTION.get(), 1),
                        CraftingBookCategory.MISC),
                output.advancement()
                        .addCriterion("has_mixture", has(ModItems.IMMUNITY_ENHANCEMENT_MEDICINE_MIXTURE.get()))
                        .addCriterion("has_water_bottle", has(Items.POTION))
                        .addCriterion("has_iron_nugget", has(Items.IRON_NUGGET))
                        .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(immunityInjectionId))
                        .rewards(AdvancementRewards.Builder.recipe(immunityInjectionId))
                        .requirements(AdvancementRequirements.Strategy.OR)
                        .build(immunityInjectionId.withPrefix("recipes/"))
        );

        // --- Syringe Bag ---
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.SYRINGE_BAG.get())
                .define('#', Items.IRON_NUGGET)
                .define('L', Items.LEATHER)
                .pattern("###")
                .pattern("LLL")
                .pattern("L L")
                .unlockedBy("has_leather", has(Items.LEATHER))
                .unlockedBy("has_iron_nugget", has(Items.IRON_NUGGET))
                .save(output);

        // --- Potion Crafting Table ---
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModItems.POTION_CRAFTING_TABLE.get())
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

        // --- Potion Mixing Table ---
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModItems.POTION_MIXING_TABLE.get())
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
}
