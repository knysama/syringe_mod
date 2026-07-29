package cn.autoforged.syringe_mod.item;

import cn.autoforged.syringe_mod.sound.ModSounds;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ExperimentalSyringeItem extends SyringeItem {

    public ExperimentalSyringeItem(Properties properties, int useCooldown) {
        super(properties, useCooldown);
    }

    @Override
    protected void applyEffects(Player player, ItemStack stack) {
        Level level = player.level();
        if (!level.isClientSide()) {
            if (player.getRandom().nextFloat() < 0.03F) {
                player.hurt(player.damageSources().genericKill(), Float.MAX_VALUE);
            } else {
                FoodProperties food = stack.getFoodProperties(player);
                if (food != null) {
                    for (FoodProperties.PossibleEffect effect : food.effects()) {
                        if (player.getRandom().nextFloat() < effect.probability()) {
                            player.addEffect(effect.effect());
                        }
                    }
                }
            }
        }

        if (!level.isClientSide()) {
            level.playSound(null, player.getX(), player.getY(), player.getZ(),
                    ModSounds.INJECTION.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
            player.getCooldowns().addCooldown(stack.getItem(), this.useCooldown);
            player.awardStat(Stats.ITEM_USED.get(stack.getItem()));

            if (player.getRandom().nextFloat() < 0.5F) {
                player.addEffect(new MobEffectInstance(MobEffects.HUNGER, 200, 0));
            }
        }
    }
}
