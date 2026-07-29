package cn.autoforged.syringe_mod.client;

import cn.autoforged.syringe_mod.SyringeMod;
import cn.autoforged.syringe_mod.network.payload.ServerboundOpenSyringeBagPayload;
import cn.autoforged.syringe_mod.network.payload.ServerboundUseSyringePayload;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = SyringeMod.MODID, value = Dist.CLIENT)
public class ModClientEvents {

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        while (ModKeyMappings.USE_SYRINGE_KEY.get().consumeClick()) {
            PacketDistributor.sendToServer(new ServerboundUseSyringePayload());
        }

        while (ModKeyMappings.OPEN_SYRINGE_BAG_KEY.get().consumeClick()) {
            PacketDistributor.sendToServer(new ServerboundOpenSyringeBagPayload());
        }
    }
}
