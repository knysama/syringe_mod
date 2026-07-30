package cn.autoforged.syringe_mod.item;

import cn.autoforged.syringe_mod.component.ModDataComponents;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.neoforged.neoforge.items.ComponentItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;

public class MedicineBagItemHandler extends ComponentItemHandler {
    public static final int SLOT_COUNT = 9;
    public static final int INTERNAL_STACK_LIMIT = 16;
    private static final String LEGACY_ROOT = "SyringeBag";

    public MedicineBagItemHandler(ItemStack bagStack, HolderLookup.Provider registries) {
        super(bagStack, ModDataComponents.MEDICINE_BAG_CONTENTS.get(), SLOT_COUNT);
        migrateLegacyData(bagStack, registries);
    }

    @Override
    public void setStackInSlot(int slot, ItemStack stack) {
        if (!stack.isEmpty() && (!isItemValid(slot, stack) || stack.getCount() > INTERNAL_STACK_LIMIT)) {
            throw new IllegalArgumentException("Invalid medicine bag stack for slot " + slot + ": " + stack);
        }
        super.setStackInSlot(slot, stack);
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack toInsert, boolean simulate) {
        validateSlot(slot);
        if (toInsert.isEmpty()) {
            return ItemStack.EMPTY;
        }
        if (!isItemValid(slot, toInsert)) {
            return toInsert;
        }

        var contents = getContents();
        ItemStack existing = getStackFromContents(contents, slot);
        if (!existing.isEmpty() && !ItemStack.isSameItemSameComponents(existing, toInsert)) {
            return toInsert;
        }

        int stackLimit = Math.min(INTERNAL_STACK_LIMIT, toInsert.getMaxStackSize());
        int room = stackLimit - existing.getCount();
        if (room <= 0) {
            return toInsert;
        }

        int inserted = Math.min(room, toInsert.getCount());
        if (!simulate) {
            updateContents(contents, toInsert.copyWithCount(existing.getCount() + inserted), slot);
        }
        return inserted == toInsert.getCount()
                ? ItemStack.EMPTY
                : toInsert.copyWithCount(toInsert.getCount() - inserted);
    }

    @Override
    public int getSlotLimit(int slot) {
        validateSlot(slot);
        return INTERNAL_STACK_LIMIT;
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        return stack.getItem() instanceof AmpouleItem;
    }

    private void validateSlot(int slot) {
        if (slot < 0 || slot >= SLOT_COUNT) {
            throw new IndexOutOfBoundsException("Medicine bag slot " + slot + " outside [0," + SLOT_COUNT + ")");
        }
    }

    private void migrateLegacyData(ItemStack bagStack, HolderLookup.Provider registries) {
        if (!getContents().nonEmptyStream().findAny().isEmpty()) {
            return;
        }

        CustomData legacyData = bagStack.get(DataComponents.CUSTOM_DATA);
        if (legacyData == null || !legacyData.contains(LEGACY_ROOT)) {
            return;
        }

        CompoundTag legacyRoot = legacyData.copyTag().getCompound(LEGACY_ROOT);
        ItemStackHandler oldHandler = new ItemStackHandler(SLOT_COUNT);
        oldHandler.deserializeNBT(registries, legacyRoot);

        for (int slot = 0; slot < SLOT_COUNT; slot++) {
            ItemStack oldStack = oldHandler.getStackInSlot(slot);
            if (oldStack.getItem() instanceof AmpouleItem) {
                ItemStack remainder = insertItem(slot, oldStack.copy(), false);
                if (!remainder.isEmpty()) {
                    throw new IllegalStateException("Legacy medicine bag slot exceeds migration capacity");
                }
            }
        }

        CustomData.update(DataComponents.CUSTOM_DATA, bagStack, tag -> tag.remove(LEGACY_ROOT));
    }
}
