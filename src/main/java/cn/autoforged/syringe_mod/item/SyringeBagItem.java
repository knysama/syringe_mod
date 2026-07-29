package cn.autoforged.syringe_mod.item;

import cn.autoforged.syringe_mod.SyringeMod;
import cn.autoforged.syringe_mod.ui.SyringeBagMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class SyringeBagItem extends Item {
    public SyringeBagItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide() && player instanceof ServerPlayer sp) {
            sp.openMenu(new SimpleMenuProvider(
                    (id, inv, p) -> new SyringeBagMenu(id, inv, stack, level.registryAccess()),
                    Component.translatable("container." + SyringeMod.MODID + ".syringe_bag")
            ));
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }
}
