package cn.autoforged.syringe_mod.integration.jei;

import cn.autoforged.syringe_mod.block.ModBlocks;
import cn.autoforged.syringe_mod.recipe.PotionCraftingRecipe;
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

public class PotionCraftingRecipeCategory implements IRecipeCategory<PotionCraftingRecipe> {
    private static final ResourceLocation BACKGROUND_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(
                    "syringe_mod", "textures/gui/recipe/potion_crafting.png");
    private static final int SLOT_X_LEFT = 7;
    private static final int SLOT_X_CENTER = 34;
    private static final int SLOT_X_RIGHT = 61;
    private static final int SLOT_INPUT_Y = 13;
    private static final int SLOT_OUTPUT_X = 97;
    private static final int SLOT_OUTPUT_Y = 13;
    public static final int WIDTH = 118;
    public static final int HEIGHT = 84;

    private final IDrawable icon;
    private final IDrawable background;

    public PotionCraftingRecipeCategory(IGuiHelper guiHelper) {
        icon = guiHelper.createDrawableItemStack(new ItemStack(ModBlocks.POTION_CRAFTING_TABLE.get()));
        background = guiHelper.createDrawable(BACKGROUND_TEXTURE, 0, 0, WIDTH, HEIGHT);
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
        builder.addSlot(RecipeIngredientRole.OUTPUT, SLOT_OUTPUT_X, SLOT_OUTPUT_Y).addItemStack(recipe.result());
    }

    @Override
    public void draw(PotionCraftingRecipe recipe, IRecipeSlotsView slots, GuiGraphics graphics, double mouseX, double mouseY) {
        background.draw(graphics);
    }
}
