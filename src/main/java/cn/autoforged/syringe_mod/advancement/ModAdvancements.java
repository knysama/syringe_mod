package cn.autoforged.syringe_mod.advancement;

import cn.autoforged.syringe_mod.SyringeMod;
import cn.autoforged.syringe_mod.item.MedicineKind;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public final class ModAdvancements {
    private static final ResourceLocation SIDE_EFFECT =
            ResourceLocation.fromNamespaceAndPath(SyringeMod.MODID, "side_effect");
    private static final ResourceLocation ALL_MEDICINES =
            ResourceLocation.fromNamespaceAndPath(SyringeMod.MODID, "all_medicines");

    public static void awardSideEffect(ServerPlayer player) {
        award(player, SIDE_EFFECT, "side_effect");
    }

    public static void recordInjectedMedicine(ServerPlayer player, MedicineKind kind) {
        if (kind == MedicineKind.VANILLA_POTION) {
            return;
        }
        award(player, ALL_MEDICINES, kind.name().toLowerCase(java.util.Locale.ROOT));
    }

    private static void award(ServerPlayer player, ResourceLocation id, String criterion) {
        MinecraftServer server = player.getServer();
        if (server == null) {
            return;
        }
        AdvancementHolder advancement = server.getAdvancements().get(id);
        if (advancement != null) {
            player.getAdvancements().award(advancement, criterion);
        }
    }

    private ModAdvancements() {
    }
}
