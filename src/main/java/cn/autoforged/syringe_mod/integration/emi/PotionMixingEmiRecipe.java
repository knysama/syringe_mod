package cn.autoforged.syringe_mod.integration.emi;

import cn.autoforged.syringe_mod.SyringeMod;
import cn.autoforged.syringe_mod.recipe.PotionMixingRecipe;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class PotionMixingEmiRecipe implements EmiRecipe {
    private static final ResourceLocation BACKGROUND_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(
                    SyringeMod.MODID, "textures/gui/recipe/potion_mixing.png");
    private static final int SLOT_X_LEFT = 9;
    private static final int SLOT_LEFT_Y = 13;
    private static final int SLOT_RIGHT_Y = 51;
    private static final int SLOT_OUTPUT_X = 97;
    private static final int SLOT_OUTPUT_Y = 31;
    private static final int WIDTH = 118;
    private static final int HEIGHT = 84;

    private final PotionMixingRecipe recipe;
    private final ResourceLocation id;
    private final List<EmiIngredient> inputs;
    private final List<EmiStack> outputs;

    public PotionMixingEmiRecipe(ResourceLocation recipeId, PotionMixingRecipe recipe) {
        this.recipe = recipe;
        this.id = ResourceLocation.fromNamespaceAndPath(
                SyringeMod.MODID,
                "potion_mixing/" + recipeId.getNamespace() + "/" + recipeId.getPath());
        this.inputs = List.of(
                EmiIngredient.of(recipe.left()),
                EmiIngredient.of(recipe.right()));
        this.outputs = List.of(EmiStack.of(recipe.result()));
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return SyringeModEmiPlugin.POTION_MIXING_CATEGORY;
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
        widgets.addSlot(EmiIngredient.of(recipe.left()), SLOT_X_LEFT, SLOT_LEFT_Y);
        widgets.addSlot(EmiIngredient.of(recipe.right()), SLOT_X_LEFT, SLOT_RIGHT_Y);
        widgets.addSlot(EmiStack.of(recipe.result()), SLOT_OUTPUT_X, SLOT_OUTPUT_Y).recipeContext(this);
    }
}
