package cn.autoforged.syringe_mod.item;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public final class InjectionGunService {
    public static boolean quickReload(ServerPlayer player, ItemStack gun) {
        List<AmmoSourceResolver.SlotRef> ampoules = AmmoSourceResolver.orderedAmpoules(player);
        if (ampoules.isEmpty()) {
            return false;
        }
        return reloadFrom(player, gun, ampoules.getFirst());
    }

    public static boolean reloadSelected(ServerPlayer player, ItemStack gun, ItemStack prototype) {
        if (!(prototype.getItem() instanceof AmpouleItem)) {
            return false;
        }

        for (AmmoSourceResolver.SlotRef source : AmmoSourceResolver.orderedAmpoules(player)) {
            ItemStack candidate = source.stack();
            if (ItemStack.isSameItemSameComponents(candidate, prototype)) {
                return reloadFrom(player, gun, source);
            }
        }
        return false;
    }

    private static boolean reloadFrom(ServerPlayer player, ItemStack gun, AmmoSourceResolver.SlotRef selectedSource) {
        if (!(gun.getItem() instanceof InjectionGunItem)) {
            return false;
        }

        ItemStack selected = selectedSource.stack();
        if (!(selected.getItem() instanceof AmpouleItem)) {
            return false;
        }

        ItemStack loaded = InjectionGunItem.getLoadedAmpoule(gun);
        if (!loaded.isEmpty() && ItemStack.isSameItemSameComponents(loaded, selected)) {
            return true;
        }

        ItemStack extracted = selectedSource.extractOne(false);
        if (extracted.isEmpty()) {
            return false;
        }
        extracted.setCount(1);

        if (!loaded.isEmpty()) {
            AmmoSourceResolver.SlotRef returnSlot = findReturnSlot(player, loaded);
            if (returnSlot == null || !returnSlot.insert(loaded.copyWithCount(1))) {
                if (!selectedSource.insert(extracted)) {
                    throw new IllegalStateException("Failed to roll back ampoule extraction");
                }
                return false;
            }
        }

        if (!InjectionGunItem.setLoadedAmpoule(gun, extracted)) {
            if (!selectedSource.insert(extracted)) {
                throw new IllegalStateException("Failed to roll back invalid gun reload");
            }
            return false;
        }
        return true;
    }

    private static AmmoSourceResolver.SlotRef findReturnSlot(ServerPlayer player, ItemStack ampoule) {
        for (AmmoSourceResolver.SlotRef slot : AmmoSourceResolver.orderedSlots(player)) {
            if (slot.canInsert(ampoule)) {
                return slot;
            }
        }
        return null;
    }

    private InjectionGunService() {
    }
}
