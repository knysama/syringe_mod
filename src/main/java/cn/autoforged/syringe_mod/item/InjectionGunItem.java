package cn.autoforged.syringe_mod.item;

import cn.autoforged.syringe_mod.component.ModDataComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.component.ChargedProjectiles;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;

import java.util.List;

public class InjectionGunItem extends Item {
    public static final int BASE_CAPACITY = 64;
    public static final int CAPACITY_PER_DURABILITY_LEVEL = 16;

    public InjectionGunItem(Properties properties) {
        super(properties
                .stacksTo(1)
                .durability(BASE_CAPACITY)
                .component(ModDataComponents.USED_DOSES, 0));
    }

    public static ItemStack getLoadedAmpoule(ItemStack gun) {
        ChargedProjectiles loaded = gun.getOrDefault(
                ModDataComponents.LOADED_AMPOULE.get(), ChargedProjectiles.EMPTY);
        List<ItemStack> items = loaded.getItems();
        if (items.size() != 1) {
            return ItemStack.EMPTY;
        }

        ItemStack ampoule = items.getFirst();
        if (!(ampoule.getItem() instanceof AmpouleItem)) {
            return ItemStack.EMPTY;
        }

        return ampoule.copyWithCount(1);
    }

    public static boolean hasLoadedAmpoule(ItemStack gun) {
        return !getLoadedAmpoule(gun).isEmpty();
    }

    public static boolean setLoadedAmpoule(ItemStack gun, ItemStack ampoule) {
        if (!(gun.getItem() instanceof InjectionGunItem)
                || !(ampoule.getItem() instanceof AmpouleItem)
                || ampoule.isEmpty()) {
            return false;
        }

        ItemStack oneAmpoule = ampoule.copyWithCount(1);
        gun.set(ModDataComponents.LOADED_AMPOULE.get(), ChargedProjectiles.of(oneAmpoule));
        return true;
    }

    public static ItemStack removeLoadedAmpoule(ItemStack gun) {
        ItemStack loaded = getLoadedAmpoule(gun);
        gun.remove(ModDataComponents.LOADED_AMPOULE.get());
        return loaded;
    }

    public static int getDurabilityEnchantmentLevel(ItemStack gun) {
        for (var entry : gun.getTagEnchantments().entrySet()) {
            if (entry.getKey().is(Enchantments.UNBREAKING)) {
                return Mth.clamp(entry.getIntValue(), 0, 3);
            }
        }
        return 0;
    }

    public static int getCapacity(ItemStack gun) {
        return BASE_CAPACITY + CAPACITY_PER_DURABILITY_LEVEL * getDurabilityEnchantmentLevel(gun);
    }

    public static int getUsedDoses(ItemStack gun) {
        return Mth.clamp(gun.getOrDefault(ModDataComponents.USED_DOSES.get(), 0), 0, getCapacity(gun));
    }

    public static int getRemainingDoses(ItemStack gun) {
        return Math.max(0, getCapacity(gun) - getUsedDoses(gun));
    }

    public static boolean consumeCapacity(ItemStack gun, Player owner) {
        if (!(gun.getItem() instanceof InjectionGunItem) || gun.isEmpty()) {
            return false;
        }

        int next = getUsedDoses(gun) + 1;
        if (next >= getCapacity(gun)) {
            gun.shrink(1);
            return true;
        }

        gun.set(ModDataComponents.USED_DOSES.get(), next);
        return true;
    }

    @Override
    public int getDamage(ItemStack stack) {
        return getUsedDoses(stack);
    }

    @Override
    public void setDamage(ItemStack stack, int damage) {
        stack.set(ModDataComponents.USED_DOSES.get(), Mth.clamp(damage, 0, getCapacity(stack)));
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        return getCapacity(stack);
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return getUsedDoses(stack) > 0;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        return Math.round(13.0F * getRemainingDoses(stack) / getCapacity(stack));
    }

    @Override
    public int getBarColor(ItemStack stack) {
        float remaining = (float) getRemainingDoses(stack) / (float) getCapacity(stack);
        return Mth.hsvToRgb(remaining / 3.0F, 1.0F, 1.0F);
    }

    @Override
    public boolean supportsEnchantment(ItemStack stack, Holder<Enchantment> enchantment) {
        return enchantment.is(Enchantments.UNBREAKING);
    }

    @Override
    public boolean isPrimaryItemFor(ItemStack stack, Holder<Enchantment> enchantment) {
        return enchantment.is(Enchantments.UNBREAKING);
    }

    @Override
    public int getEnchantmentValue(ItemStack stack) {
        return 10;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        player.startUsingItem(usedHand);
        return InteractionResultHolder.consume(player.getItemInHand(usedHand));
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.NONE;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 72_000;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        ItemStack loaded = getLoadedAmpoule(stack);
        if (loaded.isEmpty()) {
            tooltipComponents.add(Component.translatable("tooltip.syringe_mod.injection_gun.empty")
                    .withStyle(ChatFormatting.GRAY));
        } else {
            tooltipComponents.add(Component.translatable(
                            "tooltip.syringe_mod.injection_gun.loaded", loaded.getHoverName())
                    .withStyle(ChatFormatting.AQUA));
        }

        tooltipComponents.add(Component.translatable(
                        "tooltip.syringe_mod.injection_gun.capacity",
                        getRemainingDoses(stack),
                        getCapacity(stack))
                .withStyle(ChatFormatting.DARK_GRAY));
    }
}
