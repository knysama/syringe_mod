package cn.autoforged.syringe_mod.item;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionContents;

/**
 * Shared medicine palette used by item tinting, projectiles and particles.
 */
public final class MedicineColors {
    public static int color(ItemStack ampoule) {
        if (!(ampoule.getItem() instanceof AmpouleItem ampouleItem)) {
            return 0xFFFFFF;
        }

        return switch (ampouleItem.medicineKind()) {
            case CURE -> 0xA9796C;
            case STEM_CELL -> 0x347761;
            case CELL_REPAIR -> 0x79698A;
            case SATURATION_METABOLISM -> 0x807544;
            case SURGE -> 0x4F9A83;
            case RESISTANCE -> 0x607487;
            case IMMUNITY_ENHANCEMENT -> 0x667B58;
            case EXPERIMENTAL -> 0x171D2B;
            case VANILLA_POTION -> ampoule.getOrDefault(
                    DataComponents.POTION_CONTENTS,
                    PotionContents.EMPTY).getColor();
        };
    }

    private MedicineColors() {
    }
}
