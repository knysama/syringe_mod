package cn.autoforged.syringe_mod.client;

import cn.autoforged.syringe_mod.item.MedicineColors;
import net.minecraft.world.item.ItemStack;

public final class AmpouleColors {
    public static int color(ItemStack ampoule) {
        return MedicineColors.color(ampoule);
    }

    private AmpouleColors() {
    }
}
