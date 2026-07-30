package cn.autoforged.syringe_mod.ui;

import cn.autoforged.syringe_mod.blockentity.PotionMixingTableBlockEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

public class PotionMixingTableMenu extends AbstractContainerMenu {
    private static final int SLOT_INPUT_X = 20;
    private static final int SLOT_LEFT_INPUT_Y = 40;
    private static final int SLOT_RIGHT_INPUT_Y = 75;
    private static final int SLOT_OUTPUT_X = 160;
    private static final int SLOT_OUTPUT_Y = 57;
    private static final int PLAYER_INV_Y = 142;
    private static final int PLAYER_INV_OFFSET_X = 18;
    private static final int HOTBAR_Y = 200;

    private final PotionMixingTableBlockEntity blockEntity;
    private final ContainerData containerData;
    private final ContainerLevelAccess access;

    public PotionMixingTableMenu(int id, Inventory playerInv, PotionMixingTableBlockEntity be) {
        super(ModMenuTypes.POTION_MIXING_TABLE.get(), id);
        this.blockEntity = be;
        this.containerData = be.getContainerData();
        this.access = ContainerLevelAccess.create(be.getLevel(), be.getBlockPos());
        IItemHandler handler = be.getItemHandler(null);

        addSlot(new SlotItemHandler(handler, 0, SLOT_INPUT_X, SLOT_LEFT_INPUT_Y));
        addSlot(new SlotItemHandler(handler, 1, SLOT_INPUT_X, SLOT_RIGHT_INPUT_Y));
        addSlot(new OutputSlot(handler, 2, SLOT_OUTPUT_X, SLOT_OUTPUT_Y));

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

    public PotionMixingTableMenu(int id, Inventory playerInv, net.minecraft.network.FriendlyByteBuf extraData) {
        this(id, playerInv, (PotionMixingTableBlockEntity) playerInv.player.level().getBlockEntity(extraData.readBlockPos()));
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slotIndex) {
        final int CONTAINER_END = 3;
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
        return stillValid(access, player, blockEntity.getBlockState().getBlock());
    }

    public PotionMixingTableBlockEntity getBlockEntity() { return blockEntity; }
    public ContainerData getContainerData() { return containerData; }
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
}
