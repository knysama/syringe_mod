package cn.autoforged.syringe_mod.network;

import cn.autoforged.syringe_mod.SyringeMod;
import cn.autoforged.syringe_mod.client.ClientGunAnimations;
import cn.autoforged.syringe_mod.integration.OptionalAccessoryBag;
import cn.autoforged.syringe_mod.item.GunActionController;
import cn.autoforged.syringe_mod.item.InjectionGunAction;
import cn.autoforged.syringe_mod.network.payload.ClientboundGunAnimationPayload;
import cn.autoforged.syringe_mod.network.payload.ServerboundOpenSyringeBagPayload;
import cn.autoforged.syringe_mod.network.payload.ServerboundInjectionGunActionPayload;
import cn.autoforged.syringe_mod.network.payload.ServerboundSelectAmpoulePayload;
import cn.autoforged.syringe_mod.ui.SyringeBagMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;

@EventBusSubscriber(modid = SyringeMod.MODID)
public class ModPayloads {

    @SubscribeEvent
    public static void register(RegisterPayloadHandlersEvent event) {
        var registrar = event.registrar("2");
        registrar.playToServer(
                ServerboundInjectionGunActionPayload.TYPE,
                ServerboundInjectionGunActionPayload.STREAM_CODEC,
                ModPayloads::handleGunAction
        );
        registrar.playToServer(
                ServerboundSelectAmpoulePayload.TYPE,
                ServerboundSelectAmpoulePayload.STREAM_CODEC,
                ModPayloads::handleSelectAmpoule
        );
        registrar.playToServer(
                ServerboundOpenSyringeBagPayload.TYPE,
                ServerboundOpenSyringeBagPayload.STREAM_CODEC,
                ModPayloads::handleOpenSyringeBag
        );
        registrar.playToClient(
                ClientboundGunAnimationPayload.TYPE,
                ClientboundGunAnimationPayload.STREAM_CODEC,
                ModPayloads::handleGunAnimation
        );
    }

    /**
     * Keep the registered handler itself in common code so a dedicated server
     * can build the same payload registry without resolving Minecraft client
     * classes. This payload is play-to-client only, so the body is invoked only
     * on the receiving client.
     */
    private static void handleGunAnimation(
            ClientboundGunAnimationPayload payload, IPayloadContext context) {
        ClientGunAnimations.handle(payload, context);
    }

    private static void handleGunAction(ServerboundInjectionGunActionPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer player
                    && payload.action() != InjectionGunAction.SELECTED_RELOAD) {
                GunActionController.start(player, payload.action(), net.minecraft.world.item.ItemStack.EMPTY);
            }
        });
    }

    private static void handleSelectAmpoule(ServerboundSelectAmpoulePayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer player) {
                GunActionController.start(
                        player, InjectionGunAction.SELECTED_RELOAD, payload.ampoule());
            }
        });
    }

    private static void handleOpenSyringeBag(ServerboundOpenSyringeBagPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();
            if (!(player instanceof ServerPlayer serverPlayer)) return;

            OptionalAccessoryBag.findBeltBag(player).ifPresent(bagStack -> {
                serverPlayer.openMenu(new SimpleMenuProvider(
                        (id, inv, p) -> new SyringeBagMenu(id, inv, bagStack, serverPlayer.level().registryAccess()),
                        Component.translatable("container." + SyringeMod.MODID + ".syringe_bag")
                ));
            });
        });
    }
}
