package cn.autoforged.syringe_mod.item;

import net.minecraft.core.component.DataComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;

import java.util.List;

public class AmpouleItem extends Item {
    private final MedicineKind medicineKind;
    private final int useCooldown;

    public AmpouleItem(Properties properties, MedicineKind medicineKind, int useCooldown) {
        super(properties.stacksTo(16));
        this.medicineKind = medicineKind;
        this.useCooldown = useCooldown;
    }

    public MedicineKind medicineKind() {
        return medicineKind;
    }

    public int useCooldown() {
        return useCooldown;
    }

    public boolean isVanillaPotionAmpoule() {
        return medicineKind == MedicineKind.VANILLA_POTION;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        return InteractionResultHolder.fail(player.getItemInHand(usedHand));
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {
        return stack;
    }

    @Override
    public ItemStack getDefaultInstance() {
        ItemStack stack = super.getDefaultInstance();
        if (isVanillaPotionAmpoule()) {
            stack.set(DataComponents.POTION_CONTENTS, new PotionContents(Potions.WATER));
        }
        return stack;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        if (isVanillaPotionAmpoule()) {
            stack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY)
                    .addPotionTooltip(tooltipComponents::add, 1.0F, context.tickRate());
        }
        if (medicineKind == MedicineKind.EXPERIMENTAL) {
            tooltipComponents.add(Component.translatable(
                            "tooltip.syringe_mod.experimental_injection.flavor")
                    .withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC));
        }
        if (medicineKind == MedicineKind.VODKA) {
            tooltipComponents.add(Component.translatable(
                            "tooltip.syringe_mod.vodka.flavor")
                    .withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC));
        }
    }
}
