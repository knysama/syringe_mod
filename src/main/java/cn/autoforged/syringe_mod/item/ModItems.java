package cn.autoforged.syringe_mod.item;

import cn.autoforged.syringe_mod.SyringeMod;
import cn.autoforged.syringe_mod.block.ModBlocks;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(SyringeMod.MODID);

    public static final DeferredItem<AmpouleItem> CURE_INJECTION = registerItem("cure_injection",
            () -> new AmpouleItem(new Item.Properties().rarity(Rarity.RARE), MedicineKind.CURE, 300));

    public static final DeferredItem<AmpouleItem> STEM_CELL_INJECTION = registerItem("stem_cell_injection",
            () -> new AmpouleItem(new Item.Properties().rarity(Rarity.RARE), MedicineKind.STEM_CELL, 400));

    public static final DeferredItem<AmpouleItem> CELL_REPAIR_INJECTION = registerItem("cell_repair_injection",
            () -> new AmpouleItem(new Item.Properties().rarity(Rarity.RARE), MedicineKind.CELL_REPAIR, 40));

    public static final DeferredItem<AmpouleItem> SATURATION_METABOLISM_INJECTION = registerItem("saturation_metabolism_injection",
            () -> new AmpouleItem(new Item.Properties().rarity(Rarity.RARE), MedicineKind.SATURATION_METABOLISM, 400));

    public static final DeferredItem<AmpouleItem> SURGE_INJECTION = registerItem("surge_injection",
            () -> new AmpouleItem(new Item.Properties().rarity(Rarity.RARE), MedicineKind.SURGE, 400));

    public static final DeferredItem<AmpouleItem> RESISTANCE_INJECTION = registerItem("resistance_injection",
            () -> new AmpouleItem(new Item.Properties().rarity(Rarity.RARE), MedicineKind.RESISTANCE, 400));

    public static final DeferredItem<SyringeBagItem> SYRINGE_BAG = registerItem("syringe_bag",
            () -> new SyringeBagItem(new Item.Properties()
                    .stacksTo(1)));

    public static final DeferredItem<Item> JOJA_COLA = registerItem("joja_cola",
            () -> new Item(new Item.Properties().stacksTo(16).rarity(Rarity.UNCOMMON)));

    public static final DeferredItem<AmpouleItem> IMMUNITY_ENHANCEMENT_INJECTION = registerItem("immunity_enhancement_injection",
            () -> new AmpouleItem(new Item.Properties().rarity(Rarity.RARE), MedicineKind.IMMUNITY_ENHANCEMENT, 400));

    public static final DeferredItem<AmpouleItem> EXPERIMENTAL_INJECTION = registerItem("experimental_injection",
            () -> new AmpouleItem(new Item.Properties().rarity(Rarity.RARE), MedicineKind.EXPERIMENTAL, 600));

    public static final DeferredItem<AmpouleItem> VODKA = registerItem("vodka",
            () -> new AmpouleItem(new Item.Properties().rarity(Rarity.UNCOMMON), MedicineKind.VODKA, 0));

    public static final DeferredItem<AmpouleItem> POTION_AMPOULE = registerItem("potion_ampoule",
            () -> new AmpouleItem(new Item.Properties().rarity(Rarity.UNCOMMON), MedicineKind.VANILLA_POTION, 0));

    public static final DeferredItem<InjectionGunItem> INJECTION_GUN = registerItem("injection_gun",
            () -> new InjectionGunItem(new Item.Properties().rarity(Rarity.UNCOMMON)));

    /**
     * Client-rendering carrier for the fired needle. It is intentionally not
     * exposed in the creative tab or recipes.
     */
    public static final DeferredItem<Item> INJECTION_DART = registerItem("injection_dart",
            () -> new Item(new Item.Properties().stacksTo(1)));

    public static final DeferredItem<BlockItem> POTION_CRAFTING_TABLE = registerItem("potion_crafting_table",
            () -> new BlockItem(ModBlocks.POTION_CRAFTING_TABLE.get(), new Item.Properties()));

    public static final DeferredItem<BlockItem> POTION_MIXING_TABLE = registerItem("potion_mixing_table",
            () -> new BlockItem(ModBlocks.POTION_MIXING_TABLE.get(), new Item.Properties()));

    public static <T extends Item> DeferredItem<T> registerItem(String name, Supplier<T> item) {
        return ITEMS.register(name, item);
    }

    private ModItems() {}
}
