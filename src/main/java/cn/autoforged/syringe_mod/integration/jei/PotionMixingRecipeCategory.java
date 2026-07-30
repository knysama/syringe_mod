package cn.autoforged.syringe_mod.integration.jei;

import cn.autoforged.syringe_mod.block.ModBlocks;
import cn.autoforged.syringe_mod.recipe.PotionMixingRecipe;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
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
    private static final ResourceLocation BACKGROUND_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(
                    "syringe_mod", "textures/gui/recipe/potion_mixing.png");
    private static final int SLOT_X_LEFT = 9;
    private static final int SLOT_LEFT_Y = 13;
    private static final int SLOT_RIGHT_Y = 51;
    private static final int SLOT_OUTPUT_X = 97;
    private static final int SLOT_OUTPUT_Y = 31;
    public static final int WIDTH = 118;
    public static final int HEIGHT = 84;

    private final IDrawable icon;
    private final IDrawable background;

    public PotionMixingRecipeCategory(IGuiHelper guiHelper) {
        icon = guiHelper.createDrawableItemStack(new ItemStack(ModBlocks.POTION_MIXING_TABLE.get()));
        background = guiHelper.createDrawable(BACKGROUND_TEXTURE, 0, 0, WIDTH, HEIGHT);
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
        builder.addSlot(RecipeIngredientRole.INPUT, SLOT_X_LEFT, SLOT_LEFT_Y).addIngredients(recipe.left());
        builder.addSlot(RecipeIngredientRole.INPUT, SLOT_X_LEFT, SLOT_RIGHT_Y).addIngredients(recipe.right());
        builder.addSlot(RecipeIngredientRole.OUTPUT, SLOT_OUTPUT_X, SLOT_OUTPUT_Y).addItemStack(recipe.result());
    }

    @Override
    public void draw(PotionMixingRecipe recipe, IRecipeSlotsView slots, GuiGraphics graphics, double mouseX, double mouseY) {
        background.draw(graphics);
    }
}
