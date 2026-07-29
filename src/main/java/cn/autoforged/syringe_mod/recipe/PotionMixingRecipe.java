package cn.autoforged.syringe_mod.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public record PotionMixingRecipe(Ingredient left, Ingredient right, ItemStack result) implements Recipe<PotionMixingInput> {

    @Override
    public boolean matches(PotionMixingInput input, Level level) {
        return (left.test(input.left()) && right.test(input.right()))
                || (left.test(input.right()) && right.test(input.left()));
    }

    @Override
    public ItemStack assemble(PotionMixingInput input, HolderLookup.Provider registries) {
        return result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return result;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return NonNullList.of(Ingredient.EMPTY, left, right);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.POTION_MIXING.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipeTypes.POTION_MIXING;
    }

    public static class Serializer implements RecipeSerializer<PotionMixingRecipe> {
        public static final MapCodec<PotionMixingRecipe> CODEC = RecordCodecBuilder.mapCodec(instance ->
                instance.group(
                        Ingredient.CODEC_NONEMPTY.fieldOf("left").forGetter(r -> r.left),
                        Ingredient.CODEC_NONEMPTY.fieldOf("right").forGetter(r -> r.right),
                        ItemStack.STRICT_CODEC.fieldOf("result").forGetter(r -> r.result)
                ).apply(instance, PotionMixingRecipe::new)
        );

        public static final StreamCodec<RegistryFriendlyByteBuf, PotionMixingRecipe> STREAM_CODEC =
                StreamCodec.composite(
                        Ingredient.CONTENTS_STREAM_CODEC, r -> r.left,
                        Ingredient.CONTENTS_STREAM_CODEC, r -> r.right,
                        ItemStack.STREAM_CODEC, r -> r.result,
                        PotionMixingRecipe::new
                );

        @Override
        public MapCodec<PotionMixingRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, PotionMixingRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
