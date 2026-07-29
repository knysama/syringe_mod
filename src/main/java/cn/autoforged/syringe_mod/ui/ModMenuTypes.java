package cn.autoforged.syringe_mod.ui;

import cn.autoforged.syringe_mod.SyringeMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(Registries.MENU, SyringeMod.MODID);

    public static final DeferredHolder<MenuType<?>, MenuType<SyringeBagMenu>> SYRINGE_BAG =
            MENUS.register("syringe_bag",
                    () -> IMenuTypeExtension.create(SyringeBagMenu::new));

    public static final DeferredHolder<MenuType<?>, MenuType<PotionCraftingTableMenu>> POTION_CRAFTING_TABLE =
            MENUS.register("potion_crafting_table",
                    () -> IMenuTypeExtension.create(PotionCraftingTableMenu::new));

    public static final DeferredHolder<MenuType<?>, MenuType<PotionMixingTableMenu>> POTION_MIXING_TABLE =
            MENUS.register("potion_mixing_table",
                    () -> IMenuTypeExtension.create(PotionMixingTableMenu::new));

    private ModMenuTypes() {}
}
