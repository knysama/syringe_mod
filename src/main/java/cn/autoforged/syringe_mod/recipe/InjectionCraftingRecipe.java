package cn.autoforged.syringe_mod.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public record InjectionCraftingRecipe(Ingredient mixture, ItemStack result, CraftingBookCategory category) implements CraftingRecipe {

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return NonNullList.of(Ingredient.EMPTY, mixture, Ingredient.of(Items.POTION), Ingredient.of(Items.IRON_NUGGET));
    }

    @Override
    public boolean matches(CraftingInput input, Level level) {
        boolean hasMixture = false;
        boolean hasWaterBottle = false;
        boolean hasIronNugget = false;

        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (stack.isEmpty()) continue;

            if (mixture.test(stack)) {
                if (hasMixture) return false;
                hasMixture = true;
            } else if (stack.is(Items.POTION)) {
                if (hasWaterBottle) return false;
                hasWaterBottle = true;
            } else if (stack.is(Items.IRON_NUGGET)) {
                if (hasIronNugget) return false;
                hasIronNugget = true;
            } else {
                return false;
            }
        }

        return hasMixture && hasWaterBottle && hasIronNugget;
    }

    @Override
    public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
        return result.copy();
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingInput input) {
        NonNullList<ItemStack> remaining = NonNullList.withSize(input.size(), ItemStack.EMPTY);
        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (stack.is(Items.POTION)) {
                remaining.set(i, new ItemStack(Items.GLASS_BOTTLE));
            } else if (stack.hasCraftingRemainingItem()) {
                remaining.set(i, stack.getCraftingRemainingItem());
            }
        }
        return remaining;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 3;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return result;
    }

    @Override
    public CraftingBookCategory category() {
        return category;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.INJECTION_CRAFTING.get();
    }

    public static class Serializer implements RecipeSerializer<InjectionCraftingRecipe> {
        public static final MapCodec<InjectionCraftingRecipe> CODEC = RecordCodecBuilder.mapCodec(instance ->
                instance.group(
                        Ingredient.CODEC_NONEMPTY.fieldOf("mixture").forGetter(r -> r.mixture),
                        ItemStack.STRICT_CODEC.fieldOf("result").forGetter(r -> r.result),
                        CraftingBookCategory.CODEC.fieldOf("category")
                                .orElse(CraftingBookCategory.MISC)
                                .forGetter(r -> r.category)
                ).apply(instance, InjectionCraftingRecipe::new)
        );

        public static final StreamCodec<RegistryFriendlyByteBuf, InjectionCraftingRecipe> STREAM_CODEC =
                StreamCodec.composite(
                        Ingredient.CONTENTS_STREAM_CODEC, r -> r.mixture,
                        ItemStack.STREAM_CODEC, r -> r.result,
                        CraftingBookCategory.STREAM_CODEC, r -> r.category,
                        InjectionCraftingRecipe::new
                );

        @Override
        public MapCodec<InjectionCraftingRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, InjectionCraftingRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
