package cn.autoforged.syringe_mod.network.payload;

import cn.autoforged.syringe_mod.SyringeMod;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record ServerboundOpenSyringeBagPayload() implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ServerboundOpenSyringeBagPayload> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(SyringeMod.MODID, "open_syringe_bag"));

    public static final StreamCodec<ByteBuf, ServerboundOpenSyringeBagPayload> STREAM_CODEC =
            StreamCodec.unit(new ServerboundOpenSyringeBagPayload());

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
