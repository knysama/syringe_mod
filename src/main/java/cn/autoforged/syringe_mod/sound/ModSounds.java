package cn.autoforged.syringe_mod.sound;

import cn.autoforged.syringe_mod.SyringeMod;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, SyringeMod.MODID);

    public static final DeferredHolder<SoundEvent, SoundEvent> INJECTION = SOUND_EVENTS.register(
            "injection",
            () -> SoundEvent.createVariableRangeEvent(
                    ResourceLocation.fromNamespaceAndPath(SyringeMod.MODID, "injection"))
    );

    private ModSounds() {}
}
