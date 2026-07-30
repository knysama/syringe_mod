package cn.autoforged.syringe_mod.network.payload;

import cn.autoforged.syringe_mod.SyringeMod;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public record ServerboundSelectAmpoulePayload(ItemStack ampoule) implements CustomPacketPayload {
    public static final Type<ServerboundSelectAmpoulePayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(SyringeMod.MODID, "select_ampoule"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ServerboundSelectAmpoulePayload> STREAM_CODEC =
            ItemStack.STREAM_CODEC.map(ServerboundSelectAmpoulePayload::new, ServerboundSelectAmpoulePayload::ampoule);

    public ServerboundSelectAmpoulePayload {
        ampoule = ampoule.copyWithCount(1);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
