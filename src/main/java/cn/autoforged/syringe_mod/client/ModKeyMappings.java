package cn.autoforged.syringe_mod.client;

import cn.autoforged.syringe_mod.SyringeMod;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import net.neoforged.neoforge.client.settings.KeyModifier;
import net.neoforged.neoforge.common.util.Lazy;
import org.lwjgl.glfw.GLFW;

@EventBusSubscriber(modid = SyringeMod.MODID, value = Dist.CLIENT)
public class ModKeyMappings {
    public static final String KEY_CATEGORY = "key.categories." + SyringeMod.MODID;

    public static final Lazy<KeyMapping> USE_SYRINGE_KEY = Lazy.of(() -> new KeyMapping(
            "key." + SyringeMod.MODID + ".use_syringe",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_V,
            KEY_CATEGORY
    ));

    public static final Lazy<KeyMapping> OPEN_SYRINGE_BAG_KEY = Lazy.of(() -> new KeyMapping(
            "key." + SyringeMod.MODID + ".open_syringe_bag",
            KeyConflictContext.IN_GAME,
            KeyModifier.CONTROL,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_B,
            KEY_CATEGORY
    ));

    @SubscribeEvent
    public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(USE_SYRINGE_KEY.get());
        event.register(OPEN_SYRINGE_BAG_KEY.get());
    }
}
