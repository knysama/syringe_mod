package cn.autoforged.syringe_mod.recipe;

import cn.autoforged.syringe_mod.SyringeMod;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public record PotionCraftingRecipe(Ingredient center, Ingredient sideA, Ingredient sideB, ItemStack result) implements Recipe<PotionCraftingInput> {

    @Override
    public boolean matches(PotionCraftingInput input, Level level) {
        if (!center.test(input.center())) return false;
        ItemStack left = input.left();
        ItemStack right = input.right();
        return (sideA.test(left) && sideB.test(right)) || (sideA.test(right) && sideB.test(left));
    }

    @Override
    public ItemStack assemble(PotionCraftingInput input, HolderLookup.Provider registries) {
        return result.copy();
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
    public NonNullList<Ingredient> getIngredients() {
        return NonNullList.of(Ingredient.EMPTY, center, sideA, sideB);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.POTION_CRAFTING.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipeTypes.POTION_CRAFTING;
    }

    public static class Serializer implements RecipeSerializer<PotionCraftingRecipe> {
        public static final MapCodec<PotionCraftingRecipe> CODEC = RecordCodecBuilder.mapCodec(instance ->
                instance.group(
                        Ingredient.CODEC_NONEMPTY.fieldOf("center").forGetter(r -> r.center),
                        Ingredient.CODEC.optionalFieldOf("side_a", Ingredient.EMPTY).forGetter(r -> r.sideA),
                        Ingredient.CODEC.optionalFieldOf("side_b", Ingredient.EMPTY).forGetter(r -> r.sideB),
                        ItemStack.STRICT_CODEC.fieldOf("result").forGetter(r -> r.result)
                ).apply(instance, PotionCraftingRecipe::new)
        );

        public static final StreamCodec<RegistryFriendlyByteBuf, PotionCraftingRecipe> STREAM_CODEC =
                StreamCodec.composite(
                        Ingredient.CONTENTS_STREAM_CODEC, r -> r.center,
                        Ingredient.CONTENTS_STREAM_CODEC, r -> r.sideA,
                        Ingredient.CONTENTS_STREAM_CODEC, r -> r.sideB,
                        ItemStack.STREAM_CODEC, r -> r.result,
                        PotionCraftingRecipe::new
                );

        @Override
        public MapCodec<PotionCraftingRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, PotionCraftingRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
