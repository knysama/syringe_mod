package cn.autoforged.syringe_mod.integration.emi;

import cn.autoforged.syringe_mod.SyringeMod;
import cn.autoforged.syringe_mod.recipe.PotionMixingRecipe;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class PotionMixingEmiRecipe implements EmiRecipe {
    private static final String TEX_NS = "autoforge_bricks";

    private static ResourceLocation tex(String name) {
        return ResourceLocation.fromNamespaceAndPath(TEX_NS, "textures/gui/" + name + ".png");
    }

    private static final int SLOT_X_LEFT = 20;
    private static final int SLOT_X_RIGHT = 48;
    private static final int SLOT_INPUT_Y = 5;
    private static final int SLOT_OUTPUT_X = 34;
    private static final int SLOT_OUTPUT_Y = 50;
    private static final int ARROW_X = 31;
    private static final int ARROW_Y = 23;
    private static final int ARROW_W = 7;
    private static final int ARROW_H = 26;
    private static final int WIDTH = 80;
    private static final int HEIGHT = 76;

    private final PotionMixingRecipe recipe;
    private final ResourceLocation id;
    private final List<EmiIngredient> inputs;
    private final List<EmiStack> outputs;

    public PotionMixingEmiRecipe(PotionMixingRecipe recipe) {
        this.recipe = recipe;
        var resultPath = BuiltInRegistries.ITEM.getKey(recipe.result().getItem()).getPath();
        this.id = ResourceLocation.fromNamespaceAndPath(SyringeMod.MODID, "potion_mixing/" + resultPath);
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
        widgets.addSlot(EmiIngredient.of(recipe.left()), SLOT_X_LEFT, SLOT_INPUT_Y);
        widgets.addSlot(EmiIngredient.of(recipe.right()), SLOT_X_RIGHT, SLOT_INPUT_Y);
        widgets.addSlot(EmiStack.of(recipe.result()), SLOT_OUTPUT_X, SLOT_OUTPUT_Y).recipeContext(this);

        widgets.addTexture(tex("brewing_stand_arrow"), ARROW_X, ARROW_Y, ARROW_W, ARROW_H, 0, 0, ARROW_W, ARROW_H, ARROW_W, ARROW_H);
    }
}
