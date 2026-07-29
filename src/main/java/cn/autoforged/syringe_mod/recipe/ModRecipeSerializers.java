package cn.autoforged.syringe_mod.recipe;

import cn.autoforged.syringe_mod.SyringeMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModRecipeSerializers {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, SyringeMod.MODID);

    public static final DeferredHolder<RecipeSerializer<?>, InjectionCraftingRecipe.Serializer> INJECTION_CRAFTING =
            SERIALIZERS.register("crafting_injection", InjectionCraftingRecipe.Serializer::new);

    public static final DeferredHolder<RecipeSerializer<?>, PotionCraftingRecipe.Serializer> POTION_CRAFTING =
            SERIALIZERS.register("potion_crafting", PotionCraftingRecipe.Serializer::new);

    public static final DeferredHolder<RecipeSerializer<?>, PotionMixingRecipe.Serializer> POTION_MIXING =
            SERIALIZERS.register("potion_mixing", PotionMixingRecipe.Serializer::new);

    private ModRecipeSerializers() {}
}
