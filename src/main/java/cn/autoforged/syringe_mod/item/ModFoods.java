package cn.autoforged.syringe_mod.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;

public class ModFoods {
    public static final FoodProperties HEALING_SYRINGE = new FoodProperties.Builder()
            .nutrition(0)
            .saturationModifier(0)
            .alwaysEdible()
            .fast()
            .effect(() -> new MobEffectInstance(MobEffects.HEAL, 20, 4), 1.0F)
            .effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 600, 1), 1.0F)
            .build();

    public static final FoodProperties STEM_CELL_INJECTION = new FoodProperties.Builder()
            .nutrition(0)
            .saturationModifier(0)
            .alwaysEdible()
            .fast()
            .effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 1200, 0), 1.0F)
            .build();

    public static final FoodProperties CELL_REPAIR_INJECTION = new FoodProperties.Builder()
            .nutrition(0)
            .saturationModifier(0)
            .alwaysEdible()
            .fast()
            .effect(() -> new MobEffectInstance(MobEffects.HEAL, 1, 2), 1.0F)
            .build();

    public static final FoodProperties SATURATION_METABOLISM_INJECTION = new FoodProperties.Builder()
            .nutrition(0)
            .saturationModifier(0)
            .alwaysEdible()
            .fast()
            .effect(() -> new MobEffectInstance(MobEffects.SATURATION, 900, 0), 1.0F)
            .build();

    public static final FoodProperties SURGE_INJECTION = new FoodProperties.Builder()
            .nutrition(0)
            .saturationModifier(0)
            .alwaysEdible()
            .fast()
            .effect(() -> new MobEffectInstance(MobEffects.CONDUIT_POWER, 1800, 0), 1.0F)
            .build();

    public static final FoodProperties RESISTANCE_INJECTION = new FoodProperties.Builder()
            .nutrition(0)
            .saturationModifier(0)
            .alwaysEdible()
            .fast()
            .effect(() -> new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 900, 1), 1.0F)
            .effect(() -> new MobEffectInstance(MobEffects.ABSORPTION, 900, 1), 1.0F)
            .effect(() -> new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 900, 0), 1.0F)
            .build();

    public static final FoodProperties IMMUNITY_ENHANCEMENT_INJECTION = new FoodProperties.Builder()
            .nutrition(0)
            .saturationModifier(0)
            .alwaysEdible()
            .fast()
            .effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 400, 0), 1.0F)
            .effect(() -> new MobEffectInstance(MobEffects.HEALTH_BOOST, 400, 0), 1.0F)
            .build();

    public static final FoodProperties EXPERIMENTAL_INJECTION = new FoodProperties.Builder()
            .nutrition(0)
            .saturationModifier(0)
            .alwaysEdible()
            .fast()
            .effect(() -> new MobEffectInstance(MobEffects.HEAL, 20, 4), 1.0F)
            .effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 1800, 1), 1.0F)
            .effect(() -> new MobEffectInstance(MobEffects.CONDUIT_POWER, 1800, 0), 1.0F)
            .effect(() -> new MobEffectInstance(MobEffects.HEALTH_BOOST, 1800, 1), 1.0F)
            .effect(() -> new MobEffectInstance(MobEffects.ABSORPTION, 1800, 1), 1.0F)
            .effect(() -> new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 1800, 1), 1.0F)
            .effect(() -> new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 1800, 0), 1.0F)
            .build();

    private ModFoods() {}
}
