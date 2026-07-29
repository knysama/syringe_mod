package cn.autoforged.syringe_mod.item;

import cn.autoforged.syringe_mod.sound.ModSounds;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class SyringeItem extends Item {
    protected final int useCooldown;

    public SyringeItem(Properties properties, int useCooldown) {
        super(properties);
        this.useCooldown = useCooldown;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide()) {
            applySyringeEffects(player, stack);
            stack.consume(1, player);
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if (entity instanceof Player player) {
            applySyringeEffects(player, stack);
        }
        return stack;
    }

    protected void applyEffects(Player player, ItemStack stack) {
        Level level = player.level();

        FoodProperties food = stack.getFoodProperties(player);
        if (food != null && !level.isClientSide()) {
            for (FoodProperties.PossibleEffect effect : food.effects()) {
                if (player.getRandom().nextFloat() < effect.probability()) {
                    player.addEffect(effect.effect());
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

    public static void applySyringeEffects(Player player, ItemStack stack) {
        if (stack.getItem() instanceof SyringeItem syringe) {
            syringe.applyEffects(player, stack);
        }
        if (!player.level().isClientSide()) {
            cn.autoforged.syringe_mod.item.SyringeOveruseTracker.recordUse(player);
        }
    }
}
