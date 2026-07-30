package cn.autoforged.syringe_mod.entity;

import cn.autoforged.syringe_mod.SyringeMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(Registries.ENTITY_TYPE, SyringeMod.MODID);

    public static final DeferredHolder<EntityType<?>, EntityType<AmpouleProjectile>> AMPOULE_PROJECTILE =
            ENTITY_TYPES.register("ampoule_projectile", () ->
                    EntityType.Builder.<AmpouleProjectile>of(AmpouleProjectile::new, MobCategory.MISC)
                            .sized(0.25F, 0.25F)
                            .clientTrackingRange(4)
                            .updateInterval(1)
                            .build(SyringeMod.MODID + ":ampoule_projectile"));

    private ModEntities() {
    }
}
