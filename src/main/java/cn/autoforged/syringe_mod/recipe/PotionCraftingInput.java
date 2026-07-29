package cn.autoforged.syringe_mod.recipe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

public record PotionCraftingInput(ItemStack left, ItemStack center, ItemStack right) implements RecipeInput {
    @Override
    public ItemStack getItem(int index) {
        return switch (index) {
            case 0 -> left;
            case 1 -> center;
            case 2 -> right;
            default -> ItemStack.EMPTY;
        };
    }

    @Override
    public int size() {
        return 3;
    }

    @Override
    public boolean isEmpty() {
        return left.isEmpty() && center.isEmpty() && right.isEmpty();
    }
}
