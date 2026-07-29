package cn.autoforged.syringe_mod.item;

import cn.autoforged.syringe_mod.advancement.ModAdvancements;
import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionContents;

public final class MedicineEffectService {
    public static boolean applyDose(Entity source, LivingEntity target, ItemStack ampouleStack) {
        if (target.level().isClientSide()
                || !(ampouleStack.getItem() instanceof AmpouleItem ampoule)) {
            return false;
        }

        if (target instanceof Player targetPlayer
                && targetPlayer.getCooldowns().isOnCooldown(ampoule)) {
            return false;
        }

        if (ampoule.isVanillaPotionAmpoule()) {
            applyPotionContents(source, target,
                    ampouleStack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY));
        } else {
            applyModMedicine(source, target, ampoule.medicineKind());
        }

        target.level().playSound(null, target.getX(), target.getY(), target.getZ(),
                SoundEvents.BOTTLE_EMPTY, SoundSource.PLAYERS, 1.0F, 1.1F);

        if (source instanceof Player sourcePlayer) {
            sourcePlayer.awardStat(Stats.ITEM_USED.get(ampoule));
        }

        boolean sideEffectApplied = false;
        if (target instanceof Player targetPlayer) {
            if (ampoule.useCooldown() > 0) {
                targetPlayer.getCooldowns().addCooldown(ampoule, ampoule.useCooldown());
            }
            if (targetPlayer.getRandom().nextFloat() < 0.5F) {
                targetPlayer.addEffect(new MobEffectInstance(MobEffects.HUNGER, 200, 0));
                sideEffectApplied = true;
            }
            SyringeOveruseTracker.recordUse(targetPlayer);
        }

        if (source instanceof ServerPlayer sourcePlayer) {
            ModAdvancements.recordInjectedMedicine(sourcePlayer, ampoule.medicineKind());
            if (sideEffectApplied) {
                ModAdvancements.awardSideEffect(sourcePlayer);
            }
        }

        return true;
    }

    private static void applyModMedicine(Entity source, LivingEntity target, MedicineKind kind) {
        switch (kind) {
            case CURE -> {
                applyEffect(source, target, new MobEffectInstance(MobEffects.HEAL, 20, 4));
                applyEffect(source, target, new MobEffectInstance(MobEffects.REGENERATION, 600, 1));
            }
            case STEM_CELL ->
                    applyEffect(source, target, new MobEffectInstance(MobEffects.REGENERATION, 1200, 0));
            case CELL_REPAIR ->
                    applyEffect(source, target, new MobEffectInstance(MobEffects.HEAL, 1, 2));
            case SATURATION_METABOLISM ->
                    applyEffect(source, target, new MobEffectInstance(MobEffects.SATURATION, 900, 0));
            case SURGE ->
                    applyEffect(source, target, new MobEffectInstance(MobEffects.CONDUIT_POWER, 1800, 0));
            case RESISTANCE -> {
                applyEffect(source, target, new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 900, 1));
                applyEffect(source, target, new MobEffectInstance(MobEffects.ABSORPTION, 900, 1));
                applyEffect(source, target, new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 900, 0));
            }
            case IMMUNITY_ENHANCEMENT -> {
                target.removeAllEffects();
                applyEffect(source, target, new MobEffectInstance(MobEffects.REGENERATION, 400, 0));
                applyEffect(source, target, new MobEffectInstance(MobEffects.HEALTH_BOOST, 400, 0));
            }
            case EXPERIMENTAL -> {
                if (target.getRandom().nextFloat() < 0.03F) {
                    target.hurt(target.damageSources().genericKill(), Float.MAX_VALUE);
                } else {
                    applyEffect(source, target, new MobEffectInstance(MobEffects.HEAL, 20, 4));
                    applyEffect(source, target, new MobEffectInstance(MobEffects.REGENERATION, 1800, 1));
                    applyEffect(source, target, new MobEffectInstance(MobEffects.CONDUIT_POWER, 1800, 0));
                    applyEffect(source, target, new MobEffectInstance(MobEffects.HEALTH_BOOST, 1800, 1));
                    applyEffect(source, target, new MobEffectInstance(MobEffects.ABSORPTION, 1800, 1));
                    applyEffect(source, target, new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 1800, 1));
                    applyEffect(source, target, new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 1800, 0));
                }
            }
            case VANILLA_POTION -> {
                // Handled through POTION_CONTENTS above.
            }
        }
    }

    private static void applyPotionContents(Entity source, LivingEntity target, PotionContents contents) {
        contents.forEachEffect(effect -> applyEffect(source, target, effect));
    }

    private static void applyEffect(Entity source, LivingEntity target, MobEffectInstance effect) {
        if (effect.getEffect().value().isInstantenous()) {
            effect.getEffect().value().applyInstantenousEffect(
                    source, source, target, effect.getAmplifier(), 1.0D);
        } else {
            target.addEffect(new MobEffectInstance(effect));
        }
    }

    private MedicineEffectService() {
    }
}
