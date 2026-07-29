package cn.autoforged.syringe_mod.item;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ImmunitySyringeItem extends SyringeItem {

    public ImmunitySyringeItem(Properties properties, int useCooldown) {
        super(properties, useCooldown);
    }

    @Override
    protected void applyEffects(Player player, ItemStack stack) {
        Level level = player.level();
        if (!level.isClientSide()) {
            player.removeAllEffects();
        }
        super.applyEffects(player, stack);
    }
}
