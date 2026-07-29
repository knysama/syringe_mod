package cn.autoforged.syringe_mod.blockentity;

import cn.autoforged.syringe_mod.recipe.ModRecipeTypes;
import cn.autoforged.syringe_mod.recipe.PotionCraftingInput;
import cn.autoforged.syringe_mod.recipe.PotionCraftingRecipe;
import cn.autoforged.syringe_mod.ui.PotionCraftingTableMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

public class PotionCraftingTableBlockEntity extends BlockEntity implements MenuProvider {
    public static final int SLOT_LEFT_INPUT = 0;
    public static final int SLOT_CENTER_INPUT = 1;
    public static final int SLOT_RIGHT_INPUT = 2;
    public static final int SLOT_OUTPUT = 3;
    public static final int TOTAL_SLOTS = 4;

    private final ItemStackHandler itemHandler = new ItemStackHandler(TOTAL_SLOTS) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if (level != null && !level.isClientSide) {
                level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
            }
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            if (slot == SLOT_OUTPUT) return false;
            return super.isItemValid(slot, stack);
        }
    };

    private final ContainerData data = new SimpleContainerData(2);
    private int progress = 0;
    private int maxProgress = 80;
    private final RecipeManager.CachedCheck<PotionCraftingInput, PotionCraftingRecipe> quickCheck =
            RecipeManager.createCheck(ModRecipeTypes.POTION_CRAFTING);

    public PotionCraftingTableBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.POTION_CRAFTING_TABLE.get(), pos, state);
    }

    public IItemHandler getItemHandler(@Nullable Direction side) {
        if (side == null) return itemHandler;
        return switch (side) {
            case UP -> new SidedHandler(itemHandler, new int[]{SLOT_CENTER_INPUT}, new int[]{SLOT_OUTPUT});
            case WEST -> new SidedHandler(itemHandler, new int[]{SLOT_LEFT_INPUT}, new int[]{SLOT_OUTPUT});
            case EAST -> new SidedHandler(itemHandler, new int[]{SLOT_RIGHT_INPUT}, new int[]{SLOT_OUTPUT});
            default -> new SidedHandler(itemHandler, new int[]{}, new int[]{SLOT_OUTPUT});
        };
    }

    public void tick() {
        if (level == null || level.isClientSide) return;

        PotionCraftingInput input = new PotionCraftingInput(
                itemHandler.getStackInSlot(SLOT_LEFT_INPUT),
                itemHandler.getStackInSlot(SLOT_CENTER_INPUT),
                itemHandler.getStackInSlot(SLOT_RIGHT_INPUT));

        var recipeHolder = quickCheck.getRecipeFor(input, level);
        if (recipeHolder.isEmpty()) {
            if (progress != 0) {
                progress = 0;
                setChanged();
            }
            data.set(0, 0);
            data.set(1, maxProgress);
            return;
        }

        PotionCraftingRecipe recipe = recipeHolder.get().value();
        ItemStack result = recipe.assemble(input, level.registryAccess());
        ItemStack output = itemHandler.getStackInSlot(SLOT_OUTPUT);

        if (!output.isEmpty() && (!ItemStack.isSameItemSameComponents(output, result)
                || output.getCount() + result.getCount() > output.getMaxStackSize())) {
            if (progress != 0) {
                progress = 0;
                setChanged();
            }
            data.set(0, 0);
            data.set(1, maxProgress);
            return;
        }

        progress++;
        data.set(0, progress);
        data.set(1, maxProgress);

        if (progress >= maxProgress) {
            for (int slot : new int[]{SLOT_LEFT_INPUT, SLOT_CENTER_INPUT, SLOT_RIGHT_INPUT}) {
                ItemStack stack = itemHandler.getStackInSlot(slot);
                if (!stack.isEmpty()) {
                    ItemStack remainder = stack.getCraftingRemainingItem();
                    stack.shrink(1);
                    if (stack.isEmpty() && !remainder.isEmpty()) {
                        itemHandler.setStackInSlot(slot, remainder);
                    }
                }
            }
            if (output.isEmpty()) {
                itemHandler.setStackInSlot(SLOT_OUTPUT, result.copy());
            } else {
                output.grow(result.getCount());
            }
            progress = 0;
        }
        setChanged();
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("container.syringe_mod.potion_crafting_table");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInv, Player player) {
        return new PotionCraftingTableMenu(id, playerInv, this);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("inventory", itemHandler.serializeNBT(registries));
        tag.putInt("progress", progress);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        itemHandler.deserializeNBT(registries, tag.getCompound("inventory"));
        progress = tag.getInt("progress");
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag, registries);
        return tag;
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt, HolderLookup.Provider registries) {
        loadAdditional(pkt.getTag(), registries);
    }

    public void dropContents() {
        if (level != null) {
            for (int i = 0; i < itemHandler.getSlots(); i++) {
                Containers.dropItemStack(level,
                        worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(),
                        itemHandler.getStackInSlot(i));
            }
        }
    }

    public ItemStackHandler getItemHandler() { return itemHandler; }
    public ContainerData getContainerData() { return data; }

    private record SidedHandler(IItemHandler wrapped, int[] insertSlots, int[] extractSlots) implements IItemHandler {
        @Override
        public int getSlots() { return wrapped.getSlots(); }

        @Override
        public ItemStack getStackInSlot(int slot) { return wrapped.getStackInSlot(slot); }

        @Override
        public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
            for (int s : insertSlots) {
                if (s == slot) return wrapped.insertItem(slot, stack, simulate);
            }
            return stack;
        }

        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            for (int s : extractSlots) {
                if (s == slot) return wrapped.extractItem(slot, amount, simulate);
            }
            return ItemStack.EMPTY;
        }

        @Override
        public int getSlotLimit(int slot) { return wrapped.getSlotLimit(slot); }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            if (!wrapped.isItemValid(slot, stack)) return false;
            for (int s : insertSlots) {
                if (s == slot) return true;
            }
            return false;
        }
    }
}
