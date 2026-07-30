package cn.autoforged.syringe_mod.ui;

import cn.autoforged.syringe_mod.blockentity.PotionCraftingTableBlockEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

public class PotionCraftingTableMenu extends AbstractContainerMenu {
    private static final int SLOT_LEFT_X = 20;
    private static final int SLOT_CENTER_X = 46;
    private static final int SLOT_RIGHT_X = 72;
    private static final int SLOT_OUTPUT_X = 145;
    private static final int ROW_INPUT_Y = 43;
    private static final int ROW_OUTPUT_Y = 43;
    private static final int PLAYER_INV_Y = 110;
    private static final int PLAYER_INV_OFFSET_X = 7;
    private static final int HOTBAR_Y = 168;

    private final PotionCraftingTableBlockEntity blockEntity;
    private final ContainerData containerData;
    private final ContainerLevelAccess access;

    public PotionCraftingTableMenu(int id, Inventory playerInv, PotionCraftingTableBlockEntity be) {
        super(ModMenuTypes.POTION_CRAFTING_TABLE.get(), id);
        this.blockEntity = be;
        this.containerData = be.getContainerData();
        this.access = ContainerLevelAccess.create(be.getLevel(), be.getBlockPos());
        IItemHandler handler = be.getItemHandler(null);

        addSlot(new SlotItemHandler(handler, 0, SLOT_LEFT_X, ROW_INPUT_Y));
        addSlot(new CenterContainerSlot(handler, 1, SLOT_CENTER_X, ROW_INPUT_Y));
        addSlot(new SlotItemHandler(handler, 2, SLOT_RIGHT_X, ROW_INPUT_Y));
        addSlot(new OutputSlot(handler, 3, SLOT_OUTPUT_X, ROW_OUTPUT_Y));

        addDataSlots(this.containerData);

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                addSlot(new Slot(playerInv, 9 + row * 9 + col,
                        PLAYER_INV_OFFSET_X + col * 18, PLAYER_INV_Y + row * 18));
            }
        }
        for (int col = 0; col < 9; col++) {
            addSlot(new Slot(playerInv, col,
                    PLAYER_INV_OFFSET_X + col * 18, HOTBAR_Y));
        }
    }

    public PotionCraftingTableMenu(int id, Inventory playerInv, net.minecraft.network.FriendlyByteBuf extraData) {
        this(id, playerInv, (PotionCraftingTableBlockEntity) playerInv.player.level().getBlockEntity(extraData.readBlockPos()));
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slotIndex) {
        final int CONTAINER_END = 4;
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
            boolean moved = stack.is(Items.GLASS_BOTTLE) || stack.is(Items.POTION)
                    ? moveItemStackTo(stack, 1, 2, false)
                    : moveItemStackTo(stack, 0, 1, false)
                            || moveItemStackTo(stack, 2, 3, false);
            if (!moved) {
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
        return stillValid(access, player, blockEntity.getBlockState().getBlock());
    }

    public PotionCraftingTableBlockEntity getBlockEntity() { return blockEntity; }
    public int getProgress() { return containerData.get(0); }
    public int getMaxProgress() { return containerData.get(1); }
    public boolean isOutputBlocked() { return containerData.get(2) != 0; }

    private static class OutputSlot extends SlotItemHandler {
        public OutputSlot(IItemHandler handler, int index, int x, int y) {
            super(handler, index, x, y);
        }
        @Override
        public boolean mayPlace(ItemStack stack) { return false; }
    }

    private static class CenterContainerSlot extends SlotItemHandler {
        public CenterContainerSlot(IItemHandler handler, int index, int x, int y) {
            super(handler, index, x, y);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return stack.is(Items.GLASS_BOTTLE) || stack.is(Items.POTION);
        }
    }
}
