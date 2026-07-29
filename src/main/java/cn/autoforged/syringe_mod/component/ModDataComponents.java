package cn.autoforged.syringe_mod.component;

import cn.autoforged.syringe_mod.SyringeMod;
import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.world.item.component.ChargedProjectiles;
import net.minecraft.world.item.component.ItemContainerContents;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModDataComponents {
    public static final DeferredRegister.DataComponents DATA_COMPONENTS =
            DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, SyringeMod.MODID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<ChargedProjectiles>> LOADED_AMPOULE =
            DATA_COMPONENTS.registerComponentType("loaded_ampoule", builder -> builder
                    .persistent(ChargedProjectiles.CODEC)
                    .networkSynchronized(ChargedProjectiles.STREAM_CODEC)
                    .cacheEncoding());

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> USED_DOSES =
            DATA_COMPONENTS.registerComponentType("used_doses", builder -> builder
                    .persistent(Codec.intRange(0, Integer.MAX_VALUE))
                    .networkSynchronized(ByteBufCodecs.VAR_INT));

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<ItemContainerContents>> MEDICINE_BAG_CONTENTS =
            DATA_COMPONENTS.registerComponentType("medicine_bag_contents", builder -> builder
                    .persistent(ItemContainerContents.CODEC)
                    .networkSynchronized(ItemContainerContents.STREAM_CODEC)
                    .cacheEncoding());

    private ModDataComponents() {
    }
}
