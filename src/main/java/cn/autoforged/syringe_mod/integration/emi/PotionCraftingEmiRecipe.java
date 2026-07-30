package cn.autoforged.syringe_mod.integration.emi;

import cn.autoforged.syringe_mod.SyringeMod;
import cn.autoforged.syringe_mod.recipe.PotionCraftingRecipe;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class PotionCraftingEmiRecipe implements EmiRecipe {
    private static final ResourceLocation BACKGROUND_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(
                    SyringeMod.MODID, "textures/gui/recipe/potion_crafting.png");
    private static final int SLOT_X_LEFT = 7;
    private static final int SLOT_X_CENTER = 34;
    private static final int SLOT_X_RIGHT = 61;
    private static final int SLOT_INPUT_Y = 13;
    private static final int SLOT_OUTPUT_X = 97;
    private static final int SLOT_OUTPUT_Y = 13;
    private static final int WIDTH = 118;
    private static final int HEIGHT = 84;

    private final PotionCraftingRecipe recipe;
    private final ResourceLocation id;
    private final List<EmiIngredient> inputs;
    private final List<EmiStack> outputs;

    public PotionCraftingEmiRecipe(ResourceLocation recipeId, PotionCraftingRecipe recipe) {
        this.recipe = recipe;
        this.id = ResourceLocation.fromNamespaceAndPath(
                SyringeMod.MODID,
                "potion_crafting/" + recipeId.getNamespace() + "/" + recipeId.getPath());
        this.inputs = List.of(
                EmiIngredient.of(recipe.sideA()),
                EmiIngredient.of(recipe.center()),
                EmiIngredient.of(recipe.sideB()));
        this.outputs = List.of(EmiStack.of(recipe.result()));
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return SyringeModEmiPlugin.POTION_CRAFTING_CATEGORY;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return inputs;
    }

    @Override
    public List<EmiStack> getOutputs() {
        return outputs;
    }

    @Override
    public int getDisplayWidth() {
        return WIDTH;
    }

    @Override
    public int getDisplayHeight() {
        return HEIGHT;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(
                BACKGROUND_TEXTURE, 0, 0, WIDTH, HEIGHT,
                0, 0, WIDTH, HEIGHT, WIDTH, HEIGHT);
        widgets.addSlot(EmiIngredient.of(recipe.sideA()), SLOT_X_LEFT, SLOT_INPUT_Y);
        widgets.addSlot(EmiIngredient.of(recipe.center()), SLOT_X_CENTER, SLOT_INPUT_Y);
        widgets.addSlot(EmiIngredient.of(recipe.sideB()), SLOT_X_RIGHT, SLOT_INPUT_Y);
        widgets.addSlot(EmiStack.of(recipe.result()), SLOT_OUTPUT_X, SLOT_OUTPUT_Y).recipeContext(this);
    }
}
