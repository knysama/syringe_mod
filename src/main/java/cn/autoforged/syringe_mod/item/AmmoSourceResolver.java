package cn.autoforged.syringe_mod.item;

import cn.autoforged.syringe_mod.integration.OptionalAccessoryBag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.wrapper.PlayerMainInvWrapper;

import java.util.ArrayList;
import java.util.List;

public final class AmmoSourceResolver {
    public record SlotRef(IItemHandler handler, int slot) {
        public ItemStack stack() {
            return handler.getStackInSlot(slot);
        }

        public ItemStack extractOne(boolean simulate) {
            return handler.extractItem(slot, 1, simulate);
        }

        public boolean canInsert(ItemStack stack) {
            return handler.insertItem(slot, stack, true).isEmpty();
        }

        public boolean insert(ItemStack stack) {
            return handler.insertItem(slot, stack, false).isEmpty();
        }
    }

    public static List<SlotRef> orderedSlots(ServerPlayer player) {
        List<SlotRef> result = new ArrayList<>();

        OptionalAccessoryBag.findBeltBag(player).ifPresent(bagStack -> {
            MedicineBagItemHandler bag = new MedicineBagItemHandler(bagStack, player.registryAccess());
            for (int bagSlot = 0; bagSlot < bag.getSlots(); bagSlot++) {
                result.add(new SlotRef(bag, bagSlot));
            }
        });

        for (int inventorySlot = 0; inventorySlot < player.getInventory().getContainerSize(); inventorySlot++) {
            ItemStack stack = player.getInventory().getItem(inventorySlot);
            if (stack.getItem() instanceof SyringeBagItem) {
                MedicineBagItemHandler bag = new MedicineBagItemHandler(stack, player.registryAccess());
                for (int bagSlot = 0; bagSlot < bag.getSlots(); bagSlot++) {
                    result.add(new SlotRef(bag, bagSlot));
                }
            }
        }

        PlayerMainInvWrapper mainInventory = new PlayerMainInvWrapper(player.getInventory());
        for (int slot = 0; slot < mainInventory.getSlots(); slot++) {
            ItemStack stack = mainInventory.getStackInSlot(slot);
            if (!(stack.getItem() instanceof SyringeBagItem)) {
                result.add(new SlotRef(mainInventory, slot));
            }
        }

        return result;
    }

    public static List<SlotRef> orderedAmpoules(ServerPlayer player) {
        return orderedSlots(player).stream()
                .filter(slot -> slot.stack().getItem() instanceof AmpouleItem)
                .toList();
    }

    private AmmoSourceResolver() {
    }
}
