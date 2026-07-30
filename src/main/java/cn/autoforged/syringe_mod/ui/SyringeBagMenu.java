package cn.autoforged.syringe_mod.ui;

import cn.autoforged.syringe_mod.tag.ModTags;
import cn.autoforged.syringe_mod.item.AmpouleItem;
import cn.autoforged.syringe_mod.item.MedicineBagItemHandler;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemHandlerCopySlot;
import net.neoforged.neoforge.items.ItemStackHandler;

import javax.annotation.Nullable;

public class SyringeBagMenu extends AbstractContainerMenu {
    private static final int CONTAINER_SIZE = 9;
    private static final int CONTAINER_COLS = 3;
    private static final int CONTAINER_ROWS = 3;
    private static final int SLOT_SIZE = 18;
    private static final int BORDER = 5;

    private final IItemHandler handler;
    @Nullable
    private final ItemStack bagStack;
    @Nullable
    private final HolderLookup.Provider registries;
    private final int backingInventoryIndex;

    private SyringeBagMenu(int id, Inventory playerInv, IItemHandler handler,
                           @Nullable ItemStack bagStack, @Nullable HolderLookup.Provider registries,
                           int backingInventoryIndex) {
        super(ModMenuTypes.SYRINGE_BAG.get(), id);
        this.handler = handler;
        this.bagStack = bagStack;
        this.registries = registries;
        this.backingInventoryIndex = backingInventoryIndex;
        addSlots(playerInv);
    }

    // Server-side constructor
    public SyringeBagMenu(int id, Inventory playerInv, ItemStack bagStack, HolderLookup.Provider registries) {
        this(id, playerInv, createServerHandler(registries, bagStack), bagStack, registries,
                findBackingInventoryIndex(playerInv, bagStack));
    }

    // Client-side constructor
    public SyringeBagMenu(int id, Inventory playerInv, net.minecraft.network.FriendlyByteBuf extraData) {
        this(id, playerInv, new ItemStackHandler(CONTAINER_SIZE) {
            @Override
            public boolean isItemValid(int slot, ItemStack stack) {
                return stack.getItem() instanceof AmpouleItem;
            }

            @Override
            public int getSlotLimit(int slot) {
                return MedicineBagItemHandler.INTERNAL_STACK_LIMIT;
            }

            @Override
            protected int getStackLimit(int slot, ItemStack stack) {
                return MedicineBagItemHandler.INTERNAL_STACK_LIMIT;
            }
        }, null, null, -1);
    }

    private static IItemHandler createServerHandler(HolderLookup.Provider registries, ItemStack bagStack) {
        return new MedicineBagItemHandler(bagStack, registries);
    }

    private static int findBackingInventoryIndex(Inventory inventory, ItemStack bagStack) {
        for (int inventoryIndex = 0; inventoryIndex < Inventory.INVENTORY_SIZE; inventoryIndex++) {
            if (inventory.getItem(inventoryIndex) == bagStack) {
                return inventoryIndex;
            }
        }
        return -1;
    }

    private void addSlots(Inventory playerInv) {
        int contentWidth = Math.max(CONTAINER_COLS * SLOT_SIZE, 162);
        int imageWidth = BORDER * 2 + contentWidth;
        int containerGridWidth = CONTAINER_COLS * SLOT_SIZE;
        int containerOffsetX = (contentWidth - containerGridWidth) / 2;
        int invOffsetX = (imageWidth - 162) / 2;
        int playerInvTop = BORDER + CONTAINER_ROWS * SLOT_SIZE + 14;

        for (int row = 0; row < CONTAINER_ROWS; row++) {
            for (int col = 0; col < CONTAINER_COLS; col++) {
                addSlot(new MedicineBagSlot(handler, row * CONTAINER_COLS + col,
                        BORDER + containerOffsetX + col * SLOT_SIZE, BORDER + row * SLOT_SIZE));
            }
        }

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                int inventoryIndex = 9 + row * 9 + col;
                addSlot(new BackingBagSafeSlot(playerInv, inventoryIndex,
                        invOffsetX + col * SLOT_SIZE, playerInvTop + row * SLOT_SIZE,
                        inventoryIndex == backingInventoryIndex));
            }
        }

        for (int col = 0; col < 9; col++) {
            addSlot(new BackingBagSafeSlot(playerInv, col,
                    invOffsetX + col * SLOT_SIZE, playerInvTop + 58,
                    col == backingInventoryIndex));
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slotIndex) {
        final int CONTAINER_END = CONTAINER_SIZE;
        final int PLAYER_INV_START = CONTAINER_END;
        final int PLAYER_INV_END = PLAYER_INV_START + 27;
        final int HOTBAR_END = PLAYER_INV_END + 9;

        if (slotIndex < 0 || slotIndex >= this.slots.size()) {
            return ItemStack.EMPTY;
        }

        Slot slot = this.slots.get(slotIndex);
        if (slot instanceof BackingBagSafeSlot safeSlot && safeSlot.isBackingBagSlot()) {
            return ItemStack.EMPTY;
        }
        if (!slot.hasItem()) return ItemStack.EMPTY;

        ItemStack stack = slot.getItem();
        ItemStack original = stack.copy();

        if (slotIndex < CONTAINER_END) {
            if (!moveItemStackTo(stack, PLAYER_INV_START, HOTBAR_END, true)) {
                return ItemStack.EMPTY;
            }
        } else {
            if (!moveItemStackTo(stack, 0, CONTAINER_END, false)) {
                return ItemStack.EMPTY;
            }
        }

        if (stack.isEmpty()) {
            slot.setByPlayer(ItemStack.EMPTY);
        }
        slot.setChanged();

        return original;
    }

    @Override
    public void clicked(int slotId, int button, ClickType clickType, Player player) {
        if (isBackingBagMenuSlot(slotId)
                || clickType == ClickType.SWAP && button == backingInventoryIndex) {
            return;
        }
        super.clicked(slotId, button, clickType, player);
    }

    private boolean isBackingBagMenuSlot(int slotId) {
        return slotId >= 0
                && slotId < this.slots.size()
                && this.slots.get(slotId) instanceof BackingBagSafeSlot safeSlot
                && safeSlot.isBackingBagSlot();
    }

    @Override
    public boolean stillValid(Player player) {
        return bagStack == null || !bagStack.isEmpty();
    }

    private static final class MedicineBagSlot extends ItemHandlerCopySlot {
        private MedicineBagSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
            super(itemHandler, index, xPosition, yPosition);
        }

        @Override
        public int getMaxStackSize(ItemStack stack) {
            return Math.min(MedicineBagItemHandler.INTERNAL_STACK_LIMIT, stack.getMaxStackSize());
        }
    }

    private static final class BackingBagSafeSlot extends Slot {
        private final boolean backingBagSlot;

        private BackingBagSafeSlot(Inventory inventory, int index, int xPosition, int yPosition,
                                   boolean backingBagSlot) {
            super(inventory, index, xPosition, yPosition);
            this.backingBagSlot = backingBagSlot;
        }

        private boolean isBackingBagSlot() {
            return backingBagSlot;
        }

        @Override
        public boolean mayPickup(Player player) {
            return !backingBagSlot && super.mayPickup(player);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return !backingBagSlot && super.mayPlace(stack);
        }
    }
}
