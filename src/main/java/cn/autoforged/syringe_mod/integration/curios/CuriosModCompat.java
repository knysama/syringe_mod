package cn.autoforged.syringe_mod.integration.curios;

import cn.autoforged.syringe_mod.item.SyringeBagItem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.Optional;

public class CuriosModCompat {
    public static final String INJECTION_KIT_SLOT = "injection_kit";

    public static Optional<ItemStack> getBagInSlot(Player player) {
        return CuriosApi.getCuriosInventory(player)
                .flatMap(handler -> handler.getStacksHandler(INJECTION_KIT_SLOT))
                .flatMap(stacks -> {
                    ItemStack stack = stacks.getStacks().getStackInSlot(0);
                    if (stack.getItem() instanceof SyringeBagItem) {
                        return Optional.of(stack);
                    }
                    return Optional.empty();
                });
    }
}
