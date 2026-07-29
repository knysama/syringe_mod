package cn.autoforged.syringe_mod.item;

import net.minecraft.world.entity.player.Player;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SyringeOveruseTracker {
    private static final Map<UUID, ArrayDeque<Long>> playerUseTimes = new HashMap<>();
    private static final long WINDOW_TICKS = 24000L;
    private static final int MAX_USES = 9;

    public static void recordUse(Player player) {
        long gameTime = player.level().getGameTime();
        playerUseTimes.computeIfAbsent(player.getUUID(), k -> new ArrayDeque<>())
                .addLast(gameTime);
    }

    public static boolean isOverused(Player player) {
        ArrayDeque<Long> times = playerUseTimes.get(player.getUUID());
        if (times == null) return false;

        long gameTime = player.level().getGameTime();
        while (!times.isEmpty() && gameTime - times.peekFirst() >= WINDOW_TICKS) {
            times.pollFirst();
        }
        return times.size() > MAX_USES;
    }
}
