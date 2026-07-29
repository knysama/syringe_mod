package cn.autoforged.syringe_mod.integration.jei;

import cn.autoforged.syringe_mod.block.ModBlocks;
import cn.autoforged.syringe_mod.recipe.PotionCraftingRecipe;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class PotionCraftingRecipeCategory implements IRecipeCategory<PotionCraftingRecipe> {
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
    public static final int WIDTH = 100;
    public static final int HEIGHT = 76;

    private final IDrawable icon;
    private final IDrawableStatic arrow;
    private final IDrawableStatic plus;

    public PotionCraftingRecipeCategory(IGuiHelper guiHelper) {
        icon = guiHelper.createDrawableItemStack(new ItemStack(ModBlocks.POTION_CRAFTING_TABLE.get()));
        arrow = guiHelper.createDrawable(tex("brewing_stand_arrow"), 0, 0, ARROW_W, ARROW_H);
        plus = guiHelper.createDrawable(tex("process_plus"), 0, 0, PLUS_SIZE, PLUS_SIZE);
    }

    @Override
    public RecipeType<PotionCraftingRecipe> getRecipeType() {
        return SyringeModJeiPlugin.POTION_CRAFTING_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("recipe_type.syringe_mod.potion_crafting");
    }

    @Override
    public int getWidth() {
        return WIDTH;
    }

    @Override
    public int getHeight() {
        return HEIGHT;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, PotionCraftingRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, SLOT_X_LEFT, SLOT_INPUT_Y).addIngredients(recipe.sideA());
        builder.addSlot(RecipeIngredientRole.INPUT, SLOT_X_CENTER, SLOT_INPUT_Y).addIngredients(recipe.center());
        builder.addSlot(RecipeIngredientRole.INPUT, SLOT_X_RIGHT, SLOT_INPUT_Y).addIngredients(recipe.sideB());
        builder.addSlot(RecipeIngredientRole.OUTPUT, SLOT_X_CENTER, SLOT_OUTPUT_Y).addItemStack(recipe.result());
    }

    @Override
    public void draw(PotionCraftingRecipe recipe, IRecipeSlotsView slots, GuiGraphics graphics, double mouseX, double mouseY) {
        arrow.draw(graphics, ARROW_X, ARROW_Y);
        plus.draw(graphics, PLUS_X_LEFT, PLUS_Y);
        plus.draw(graphics, PLUS_X_RIGHT, PLUS_Y);
    }
}
