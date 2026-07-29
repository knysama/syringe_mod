package cn.autoforged.syringe_mod.integration.emi;

import cn.autoforged.syringe_mod.SyringeMod;
import cn.autoforged.syringe_mod.recipe.PotionCraftingRecipe;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class PotionCraftingEmiRecipe implements EmiRecipe {
    private static final String TEX_NS = "autoforge_bricks";

    private static ResourceLocation tex(String name) {
        return ResourceLocation.fromNamespaceAndPath(TEX_NS, "textures/gui/" + name + ".png");
    }

    private static final int SLOT_X_LEFT = 5;
    private static final int SLOT_X_CENTER = 40;
    private static final int SLOT_X_RIGHT = 75;
    private static final int SLOT_INPUT_Y = 5;
    private static final int SLOT_OUTPUT_Y = 50;
    private static final int ARROW_X = 34;
    private static final int ARROW_Y = 23;
    private static final int ARROW_W = 7;
    private static final int ARROW_H = 26;
    private static final int PLUS_X_LEFT = 22;
    private static final int PLUS_X_RIGHT = 57;
    private static final int PLUS_Y = 8;
    private static final int PLUS_SIZE = 13;
    private static final int WIDTH = 100;
    private static final int HEIGHT = 76;

    private final PotionCraftingRecipe recipe;
    private final ResourceLocation id;
    private final List<EmiIngredient> inputs;
    private final List<EmiStack> outputs;

    public PotionCraftingEmiRecipe(PotionCraftingRecipe recipe) {
        this.recipe = recipe;
        var resultPath = BuiltInRegistries.ITEM.getKey(recipe.result().getItem()).getPath();
        this.id = ResourceLocation.fromNamespaceAndPath(SyringeMod.MODID, "potion_crafting/" + resultPath);
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
        widgets.addSlot(EmiIngredient.of(recipe.sideA()), SLOT_X_LEFT, SLOT_INPUT_Y);
        widgets.addSlot(EmiIngredient.of(recipe.center()), SLOT_X_CENTER, SLOT_INPUT_Y);
        widgets.addSlot(EmiIngredient.of(recipe.sideB()), SLOT_X_RIGHT, SLOT_INPUT_Y);
        widgets.addSlot(EmiStack.of(recipe.result()), SLOT_X_CENTER, SLOT_OUTPUT_Y).recipeContext(this);

        widgets.addTexture(tex("brewing_stand_arrow"), ARROW_X, ARROW_Y, ARROW_W, ARROW_H, 0, 0, ARROW_W, ARROW_H, ARROW_W, ARROW_H);
        widgets.addTexture(tex("process_plus"), PLUS_X_LEFT, PLUS_Y, PLUS_SIZE, PLUS_SIZE, 0, 0, PLUS_SIZE, PLUS_SIZE, PLUS_SIZE, PLUS_SIZE);
        widgets.addTexture(tex("process_plus"), PLUS_X_RIGHT, PLUS_Y, PLUS_SIZE, PLUS_SIZE, 0, 0, PLUS_SIZE, PLUS_SIZE, PLUS_SIZE, PLUS_SIZE);
    }
}
