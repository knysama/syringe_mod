package cn.autoforged.syringe_mod.event;

import cn.autoforged.syringe_mod.SyringeMod;
import cn.autoforged.syringe_mod.item.SyringeOveruseTracker;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.ServerChatEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@EventBusSubscriber(modid = SyringeMod.MODID)
public class ModChatEvents {

    @SubscribeEvent
    public static void onServerChat(ServerChatEvent event) {
        ServerPlayer player = event.getPlayer();
        if (SyringeOveruseTracker.isOverused(player)) {
            String scrambled = scramble(event.getRawText());
            event.setMessage(Component.literal(scrambled));
        }
    }

    private static String scramble(String text) {
        List<Character> chars = new ArrayList<>();
        for (char c : text.toCharArray()) {
            chars.add(c);
        }
        Collections.shuffle(chars);
        StringBuilder sb = new StringBuilder();
        for (char c : chars) {
            sb.append(c);
        }
        return sb.toString();
    }
}
