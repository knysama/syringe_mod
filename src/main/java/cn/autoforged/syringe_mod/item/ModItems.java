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

    public static final DeferredItem<SyringeItem> CURE_INJECTION = registerItem("cure_injection",
            () -> new SyringeItem(new Item.Properties()
                    .stacksTo(16)
                    .rarity(Rarity.RARE)
                    .food(ModFoods.HEALING_SYRINGE),
                    300));

    public static final DeferredItem<Item> MIXED_MEDICAL_MEDICINE_MIXTURE = registerItem("mixed_medical_medicine_mixture",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<SyringeItem> STEM_CELL_INJECTION = registerItem("stem_cell_injection",
            () -> new SyringeItem(new Item.Properties()
                    .stacksTo(16)
                    .rarity(Rarity.RARE)
                    .food(ModFoods.STEM_CELL_INJECTION),
                    400));

    public static final DeferredItem<SyringeItem> CELL_REPAIR_INJECTION = registerItem("cell_repair_injection",
            () -> new SyringeItem(new Item.Properties()
                    .stacksTo(16)
                    .rarity(Rarity.RARE)
                    .food(ModFoods.CELL_REPAIR_INJECTION),
                    40));

    public static final DeferredItem<Item> STEM_CELL_MEDICINE_MIXTURE = registerItem("stem_cell_medicine_mixture",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> CELL_REPAIR_MEDICINE_MIXTURE = registerItem("cell_repair_medicine_mixture",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<SyringeItem> SATURATION_METABOLISM_INJECTION = registerItem("saturation_metabolism_injection",
            () -> new SyringeItem(new Item.Properties()
                    .stacksTo(16)
                    .rarity(Rarity.RARE)
                    .food(ModFoods.SATURATION_METABOLISM_INJECTION),
                    400));

    public static final DeferredItem<Item> SATURATION_METABOLISM_MEDICINE_MIXTURE = registerItem("saturation_metabolism_medicine_mixture",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<SyringeItem> SURGE_INJECTION = registerItem("surge_injection",
            () -> new SyringeItem(new Item.Properties()
                    .stacksTo(16)
                    .rarity(Rarity.RARE)
                    .food(ModFoods.SURGE_INJECTION),
                    400));

    public static final DeferredItem<Item> SURGE_MEDICINE_MIXTURE = registerItem("surge_medicine_mixture",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<SyringeItem> RESISTANCE_INJECTION = registerItem("resistance_injection",
            () -> new SyringeItem(new Item.Properties()
                    .stacksTo(16)
                    .rarity(Rarity.RARE)
                    .food(ModFoods.RESISTANCE_INJECTION),
                    400));

    public static final DeferredItem<Item> RESISTANCE_MEDICINE_MIXTURE = registerItem("resistance_medicine_mixture",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<SyringeBagItem> SYRINGE_BAG = registerItem("syringe_bag",
            () -> new SyringeBagItem(new Item.Properties()
                    .stacksTo(1)));

    public static final DeferredItem<Item> X_REAGENT = registerItem("x_reagent",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> EXPERIMENTAL_MEDICINE_MIXTURE = registerItem("experimental_medicine_mixture",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<SyringeItem> IMMUNITY_ENHANCEMENT_INJECTION = registerItem("immunity_enhancement_injection",
            () -> new ImmunitySyringeItem(new Item.Properties()
                    .stacksTo(16)
                    .rarity(Rarity.RARE)
                    .food(ModFoods.IMMUNITY_ENHANCEMENT_INJECTION),
                    400));

    public static final DeferredItem<Item> IMMUNITY_ENHANCEMENT_MEDICINE_MIXTURE = registerItem("immunity_enhancement_medicine_mixture",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<SyringeItem> EXPERIMENTAL_INJECTION = registerItem("experimental_injection",
            () -> new ExperimentalSyringeItem(new Item.Properties()
                    .stacksTo(16)
                    .rarity(Rarity.RARE)
                    .food(ModFoods.EXPERIMENTAL_INJECTION),
                    600));

    public static final DeferredItem<BlockItem> POTION_CRAFTING_TABLE = registerItem("potion_crafting_table",
            () -> new BlockItem(ModBlocks.POTION_CRAFTING_TABLE.get(), new Item.Properties()));

    public static final DeferredItem<BlockItem> POTION_MIXING_TABLE = registerItem("potion_mixing_table",
            () -> new BlockItem(ModBlocks.POTION_MIXING_TABLE.get(), new Item.Properties()));

    public static <T extends Item> DeferredItem<T> registerItem(String name, Supplier<T> item) {
        return ITEMS.register(name, item);
    }

    private ModItems() {}
}
