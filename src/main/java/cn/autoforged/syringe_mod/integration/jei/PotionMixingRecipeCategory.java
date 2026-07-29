package cn.autoforged.syringe_mod.integration.jei;

import cn.autoforged.syringe_mod.block.ModBlocks;
import cn.autoforged.syringe_mod.recipe.PotionMixingRecipe;
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

public class PotionMixingRecipeCategory implements IRecipeCategory<PotionMixingRecipe> {
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
    public static final int WIDTH = 80;
    public static final int HEIGHT = 76;

    private final IDrawable icon;
    private final IDrawableStatic arrow;

    public PotionMixingRecipeCategory(IGuiHelper guiHelper) {
        icon = guiHelper.createDrawableItemStack(new ItemStack(ModBlocks.POTION_MIXING_TABLE.get()));
        arrow = guiHelper.createDrawable(tex("brewing_stand_arrow"), 0, 0, ARROW_W, ARROW_H);
    }

    @Override
    public RecipeType<PotionMixingRecipe> getRecipeType() {
        return SyringeModJeiPlugin.POTION_MIXING_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("recipe_type.syringe_mod.potion_mixing");
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
    public void setRecipe(IRecipeLayoutBuilder builder, PotionMixingRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, SLOT_X_LEFT, SLOT_INPUT_Y).addIngredients(recipe.left());
        builder.addSlot(RecipeIngredientRole.INPUT, SLOT_X_RIGHT, SLOT_INPUT_Y).addIngredients(recipe.right());
        builder.addSlot(RecipeIngredientRole.OUTPUT, SLOT_OUTPUT_X, SLOT_OUTPUT_Y).addItemStack(recipe.result());
    }

    @Override
    public void draw(PotionMixingRecipe recipe, IRecipeSlotsView slots, GuiGraphics graphics, double mouseX, double mouseY) {
        arrow.draw(graphics, ARROW_X, ARROW_Y);
    }
}
