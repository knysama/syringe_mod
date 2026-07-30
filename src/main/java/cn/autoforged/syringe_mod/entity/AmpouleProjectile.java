package cn.autoforged.syringe_mod.entity;

import cn.autoforged.syringe_mod.item.MedicineEffectService;
import cn.autoforged.syringe_mod.item.MedicineColors;
import cn.autoforged.syringe_mod.item.ModItems;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public final class AmpouleProjectile extends ThrowableItemProjectile {
    private static final int MAX_LIFETIME_TICKS = 24;
    private static final int TRAIL_PARTICLES_PER_TICK = 4;

    public AmpouleProjectile(EntityType<? extends AmpouleProjectile> entityType, Level level) {
        super(entityType, level);
    }

    public AmpouleProjectile(Level level, LivingEntity owner) {
        super(ModEntities.AMPOULE_PROJECTILE.get(), owner, level);
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.POTION_AMPOULE.get();
    }

    @Override
    protected double getDefaultGravity() {
        return 0.015D;
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        if (!level().isClientSide() && result.getEntity() instanceof LivingEntity target) {
            MedicineEffectService.applyDose(getOwner(), target, getItem());
            discard();
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        if (!level().isClientSide()) {
            discard();
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (level().isClientSide()) {
            spawnMedicineTrail();
        }
        if (!level().isClientSide() && tickCount >= MAX_LIFETIME_TICKS) {
            discard();
        }
    }

    private void spawnMedicineTrail() {
        Vec3 velocity = getDeltaMovement();
        if (velocity.lengthSqr() <= 1.0E-7D) {
            return;
        }
        int color = MedicineColors.color(getItem());
        Vector3f rgb = new Vector3f(
                ((color >> 16) & 0xFF) / 255.0F,
                ((color >> 8) & 0xFF) / 255.0F,
                (color & 0xFF) / 255.0F);
        Vec3 direction = velocity.normalize();
        DustParticleOptions particle = new DustParticleOptions(rgb, 0.62F);
        for (int index = 0; index < TRAIL_PARTICLES_PER_TICK; index++) {
            double distance = 0.18D + index * 0.22D;
            Vec3 trailPosition = position().subtract(direction.scale(distance));
            double jitter = 0.018D;
            level().addParticle(
                    particle,
                    trailPosition.x + (random.nextDouble() - 0.5D) * jitter,
                    trailPosition.y + (random.nextDouble() - 0.5D) * jitter,
                    trailPosition.z + (random.nextDouble() - 0.5D) * jitter,
                    -velocity.x * 0.018D,
                    -velocity.y * 0.018D,
                    -velocity.z * 0.018D);
        }
    }
}
