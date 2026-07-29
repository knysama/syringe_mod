package cn.autoforged.syringe_mod.network.payload;

import cn.autoforged.syringe_mod.SyringeMod;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record ServerboundUseSyringePayload() implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ServerboundUseSyringePayload> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(SyringeMod.MODID, "use_syringe"));

    public static final StreamCodec<ByteBuf, ServerboundUseSyringePayload> STREAM_CODEC =
            StreamCodec.unit(new ServerboundUseSyringePayload());

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
