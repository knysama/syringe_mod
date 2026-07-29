package cn.autoforged.syringe_mod.tag;

import cn.autoforged.syringe_mod.SyringeMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class ModTags {
    public static class Items {
        public static final TagKey<Item> SYRINGES = TagKey.create(
                Registries.ITEM,
                ResourceLocation.fromNamespaceAndPath(SyringeMod.MODID, "syringes"));

        public static final TagKey<Item> CURIOS_BELT = TagKey.create(
                Registries.ITEM,
                ResourceLocation.fromNamespaceAndPath("curios", "belt"));

    }
}
