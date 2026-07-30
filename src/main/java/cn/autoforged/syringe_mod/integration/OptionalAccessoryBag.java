package cn.autoforged.syringe_mod.integration;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.ModList;

import java.util.Optional;

public final class OptionalAccessoryBag {
    private static final AccessoryBagBridge BRIDGE = createBridge();

    public static Optional<ItemStack> findBeltBag(Player player) {
        return BRIDGE.findBeltBag(player);
    }

    private static AccessoryBagBridge createBridge() {
        if (!ModList.get().isLoaded("curios")) {
            return player -> Optional.empty();
        }

        try {
            Class<?> implementation = Class.forName(
                    "cn.autoforged.syringe_mod.integration.curios.CuriosAccessoryBagBridge");
            return (AccessoryBagBridge) implementation.getDeclaredConstructor().newInstance();
        } catch (ReflectiveOperationException | LinkageError error) {
            return player -> Optional.empty();
        }
    }

    private OptionalAccessoryBag() {
    }
}
