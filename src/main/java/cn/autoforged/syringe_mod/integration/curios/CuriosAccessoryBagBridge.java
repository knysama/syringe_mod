package cn.autoforged.syringe_mod.integration.curios;

import cn.autoforged.syringe_mod.integration.AccessoryBagBridge;
import cn.autoforged.syringe_mod.item.SyringeBagItem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.Optional;

public final class CuriosAccessoryBagBridge implements AccessoryBagBridge {
    @Override
    public Optional<ItemStack> findBeltBag(Player player) {
        return CuriosApi.getCuriosInventory(player)
                .flatMap(handler -> handler.getStacksHandler("belt"))
                .flatMap(stacks -> {
                    for (int slot = 0; slot < stacks.getStacks().getSlots(); slot++) {
                        ItemStack stack = stacks.getStacks().getStackInSlot(slot);
                        if (stack.getItem() instanceof SyringeBagItem) {
                            return Optional.of(stack);
                        }
                    }
                    return Optional.empty();
                });
    }
}
