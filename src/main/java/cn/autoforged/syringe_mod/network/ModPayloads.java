package cn.autoforged.syringe_mod.network;

import cn.autoforged.syringe_mod.SyringeMod;
import cn.autoforged.syringe_mod.integration.curios.CuriosModCompat;
import cn.autoforged.syringe_mod.item.SyringeBagItem;
import cn.autoforged.syringe_mod.item.SyringeItem;
import cn.autoforged.syringe_mod.network.payload.ServerboundOpenSyringeBagPayload;
import cn.autoforged.syringe_mod.network.payload.ServerboundUseSyringePayload;
import cn.autoforged.syringe_mod.ui.SyringeBagMenu;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EventBusSubscriber(modid = SyringeMod.MODID)
public class ModPayloads {

    @SubscribeEvent
    public static void register(RegisterPayloadHandlersEvent event) {
        var registrar = event.registrar("1");
        registrar.playToServer(
                ServerboundUseSyringePayload.TYPE,
                ServerboundUseSyringePayload.STREAM_CODEC,
                ModPayloads::handleUseSyringe
        );
        registrar.playToServer(
                ServerboundOpenSyringeBagPayload.TYPE,
                ServerboundOpenSyringeBagPayload.STREAM_CODEC,
                ModPayloads::handleOpenSyringeBag
        );
    }

    private static void countBagContents(Map<Item, Integer> totalCounts, ItemStack bagStack, Player player) {
        CustomData customData = bagStack.get(DataComponents.CUSTOM_DATA);
        if (customData != null && customData.contains("SyringeBag")) {
            CompoundTag bagTag = customData.copyTag().getCompound("SyringeBag");
            ItemStackHandler bagHandler = new ItemStackHandler(9);
            bagHandler.deserializeNBT(player.level().registryAccess(), bagTag);
            for (int j = 0; j < bagHandler.getSlots(); j++) {
                ItemStack syringeStack = bagHandler.getStackInSlot(j);
                if (syringeStack.getItem() instanceof SyringeItem) {
                    totalCounts.merge(syringeStack.getItem(), syringeStack.getCount(), Integer::sum);
                }
            }
        }
    }

    private static void handleUseSyringe(ServerboundUseSyringePayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();
            if (!(player instanceof ServerPlayer serverPlayer)) return;

            Map<Item, Integer> totalCounts = new HashMap<>();

            for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                ItemStack stack = player.getInventory().getItem(i);
                if (stack.getItem() instanceof SyringeItem) {
                    totalCounts.merge(stack.getItem(), stack.getCount(), Integer::sum);
                }
            }

            List<ItemStack> bags = new ArrayList<>();
            for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                ItemStack stack = player.getInventory().getItem(i);
                if (stack.getItem() instanceof SyringeBagItem) {
                    bags.add(stack);
                    countBagContents(totalCounts, stack, player);
                }
            }

            CuriosModCompat.getBagInSlot(player).ifPresent(bagStack -> {
                bags.add(bagStack);
                countBagContents(totalCounts, bagStack, player);
            });

            if (totalCounts.isEmpty()) return;

            Item bestItem = null;
            int bestTotal = 0;
            for (Map.Entry<Item, Integer> entry : totalCounts.entrySet()) {
                if (player.getCooldowns().isOnCooldown(entry.getKey())) continue;
                if (entry.getValue() > bestTotal) {
                    bestTotal = entry.getValue();
                    bestItem = entry.getKey();
                }
            }

            if (bestItem == null) return;

            for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                ItemStack stack = player.getInventory().getItem(i);
                if (stack.getItem() == bestItem) {
                    SyringeItem.applySyringeEffects(serverPlayer, stack);
                    stack.consume(1, serverPlayer);
                    return;
                }
            }

            for (ItemStack bagStack : bags) {
                CustomData customData = bagStack.get(DataComponents.CUSTOM_DATA);
                if (customData != null && customData.contains("SyringeBag")) {
                    CompoundTag bagTag = customData.copyTag().getCompound("SyringeBag");
                    ItemStackHandler bagHandler = new ItemStackHandler(9);
                    bagHandler.deserializeNBT(player.level().registryAccess(), bagTag);
                    for (int slot = 0; slot < bagHandler.getSlots(); slot++) {
                        ItemStack syringeStack = bagHandler.getStackInSlot(slot);
                        if (syringeStack.getItem() == bestItem) {
                            SyringeItem.applySyringeEffects(serverPlayer, syringeStack);
                            syringeStack.shrink(1);
                            bagHandler.setStackInSlot(slot, syringeStack);
                            CompoundTag newNbt = bagHandler.serializeNBT(player.level().registryAccess());
                            CustomData.update(DataComponents.CUSTOM_DATA, bagStack, tag -> {
                                tag.put("SyringeBag", newNbt);
                            });
                            return;
                        }
                    }
                }
            }
        });
    }

    private static void handleOpenSyringeBag(ServerboundOpenSyringeBagPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();
            if (!(player instanceof ServerPlayer serverPlayer)) return;

            CuriosModCompat.getBagInSlot(player).ifPresent(bagStack -> {
                serverPlayer.openMenu(new SimpleMenuProvider(
                        (id, inv, p) -> new SyringeBagMenu(id, inv, bagStack, serverPlayer.level().registryAccess()),
                        Component.translatable("container." + SyringeMod.MODID + ".syringe_bag")
                ));
            });
        });
    }
}
