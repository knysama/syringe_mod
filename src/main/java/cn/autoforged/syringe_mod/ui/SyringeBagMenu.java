package cn.autoforged.syringe_mod.ui;

import cn.autoforged.syringe_mod.tag.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

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

    private SyringeBagMenu(int id, Inventory playerInv, IItemHandler handler,
                           @Nullable ItemStack bagStack, @Nullable HolderLookup.Provider registries) {
        super(ModMenuTypes.SYRINGE_BAG.get(), id);
        this.handler = handler;
        this.bagStack = bagStack;
        this.registries = registries;
        addSlots(playerInv);
    }

    // Server-side constructor
    public SyringeBagMenu(int id, Inventory playerInv, ItemStack bagStack, HolderLookup.Provider registries) {
        this(id, playerInv, createServerHandler(registries, bagStack), bagStack, registries);
    }

    // Client-side constructor
    public SyringeBagMenu(int id, Inventory playerInv, net.minecraft.network.FriendlyByteBuf extraData) {
        this(id, playerInv, new ItemStackHandler(CONTAINER_SIZE) {
            @Override
            public boolean isItemValid(int slot, ItemStack stack) {
                return stack.is(ModTags.Items.SYRINGES);
            }
        }, null, null);
    }

    private static IItemHandler createServerHandler(HolderLookup.Provider registries, ItemStack bagStack) {
        ItemStackHandler handler = new ItemStackHandler(CONTAINER_SIZE) {
            @Override
            public boolean isItemValid(int slot, ItemStack stack) {
                return stack.is(ModTags.Items.SYRINGES);
            }

            @Override
            protected void onContentsChanged(int slot) {
                CompoundTag nbt = serializeNBT(registries);
                CustomData.update(DataComponents.CUSTOM_DATA, bagStack, tag -> {
                    tag.put("SyringeBag", nbt);
                });
            }
        };

        CustomData customData = bagStack.get(DataComponents.CUSTOM_DATA);
        if (customData != null && customData.contains("SyringeBag")) {
            CompoundTag bagTag = customData.copyTag().getCompound("SyringeBag");
            handler.deserializeNBT(registries, bagTag);
        }

        return handler;
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
                addSlot(new SlotItemHandler(handler, row * CONTAINER_COLS + col,
                        BORDER + containerOffsetX + col * SLOT_SIZE, BORDER + row * SLOT_SIZE));
            }
        }

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                addSlot(new Slot(playerInv, 9 + row * 9 + col,
                        invOffsetX + col * SLOT_SIZE, playerInvTop + row * SLOT_SIZE));
            }
        }

        for (int col = 0; col < 9; col++) {
            addSlot(new Slot(playerInv, col,
                    invOffsetX + col * SLOT_SIZE, playerInvTop + 58));
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slotIndex) {
        final int CONTAINER_END = CONTAINER_SIZE;
        final int PLAYER_INV_START = CONTAINER_END;
        final int PLAYER_INV_END = PLAYER_INV_START + 27;
        final int HOTBAR_END = PLAYER_INV_END + 9;

        Slot slot = this.slots.get(slotIndex);
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
    public boolean stillValid(Player player) {
        return true;
    }
}
