package cn.autoforged.syringe_mod.network.payload;

import cn.autoforged.syringe_mod.SyringeMod;
import cn.autoforged.syringe_mod.item.InjectionGunAction;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record ServerboundInjectionGunActionPayload(InjectionGunAction action) implements CustomPacketPayload {
    public static final Type<ServerboundInjectionGunActionPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(SyringeMod.MODID, "injection_gun_action"));

    private static final StreamCodec<ByteBuf, InjectionGunAction> ACTION_CODEC =
            ByteBufCodecs.VAR_INT.map(
                    id -> InjectionGunAction.values()[Math.clamp(id, 0, InjectionGunAction.values().length - 1)],
                    InjectionGunAction::ordinal
            );

    public static final StreamCodec<ByteBuf, ServerboundInjectionGunActionPayload> STREAM_CODEC =
            ACTION_CODEC.map(ServerboundInjectionGunActionPayload::new, ServerboundInjectionGunActionPayload::action);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
