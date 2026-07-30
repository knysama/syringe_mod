package cn.autoforged.syringe_mod.network.payload;

import cn.autoforged.syringe_mod.SyringeMod;
import cn.autoforged.syringe_mod.item.InjectionGunAction;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record ClientboundGunAnimationPayload(
        int entityId, InjectionGunAction action, int durationTicks) implements CustomPacketPayload {
    public static final Type<ClientboundGunAnimationPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(SyringeMod.MODID, "gun_animation"));

    private static final StreamCodec<ByteBuf, InjectionGunAction> ACTION_CODEC =
            ByteBufCodecs.VAR_INT.map(
                    id -> InjectionGunAction.values()[Math.clamp(id, 0, InjectionGunAction.values().length - 1)],
                    InjectionGunAction::ordinal
            );

    public static final StreamCodec<ByteBuf, ClientboundGunAnimationPayload> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.VAR_INT, ClientboundGunAnimationPayload::entityId,
                    ACTION_CODEC, ClientboundGunAnimationPayload::action,
                    ByteBufCodecs.VAR_INT, ClientboundGunAnimationPayload::durationTicks,
                    ClientboundGunAnimationPayload::new
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
