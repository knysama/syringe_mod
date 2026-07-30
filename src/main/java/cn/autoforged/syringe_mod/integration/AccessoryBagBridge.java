package cn.autoforged.syringe_mod.integration;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public interface AccessoryBagBridge {
    Optional<ItemStack> findBeltBag(Player player);
}
