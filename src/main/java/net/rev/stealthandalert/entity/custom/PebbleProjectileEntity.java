package net.rev.stealthandalert.entity.custom;

import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.neoforged.neoforge.common.NeoForge;
import net.rev.stealthandalert.attachment.AlertSoundData;
import net.rev.stealthandalert.entity.ModEntities;
import net.rev.stealthandalert.event.StealthSoundEvent;
import net.rev.stealthandalert.item.ModItems;
import net.rev.stealthandalert.sound.ModSounds;
import org.jetbrains.annotations.Nullable;

public class PebbleProjectileEntity extends ThrowableItemProjectile {

    public PebbleProjectileEntity(EntityType<? extends ThrowableItemProjectile> entityType, Level level) {
        super(entityType, level);
    }

    public PebbleProjectileEntity(Level level, LivingEntity owner) {
        super(ModEntities.PEBBLE.get(), owner, level);
    }

    @Override
    public @Nullable ItemStack getPickResult() {
        return new ItemStack(ModItems.PEBBLE.get());
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        if (!this.level().isClientSide) {
            Entity target = result.getEntity();
            DamageSource damageSource = this.damageSources().thrown(this, this.getOwner());
            target.hurt(damageSource, 0.5F);
            this.level().playSound(null, this.blockPosition(), ModSounds.PEBBLE_LAND.get(), SoundSource.NEUTRAL, 0.5F, 2F);
            NeoForge.EVENT_BUS.post(new StealthSoundEvent(StealthSoundEvent.Type.ENVIRONMENT, this.position(), this.getOwner(), 50.0, 9.0, AlertSoundData.LOW));
            this.discard();
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        if (!this.level().isClientSide) {
            this.level().playSound(null, this.blockPosition(), ModSounds.PEBBLE_LAND.get(), SoundSource.NEUTRAL, 0.5F, 2F);
            NeoForge.EVENT_BUS.post(new StealthSoundEvent(StealthSoundEvent.Type.ENVIRONMENT, this.position(), this.getOwner(), 50.0, 9.0, AlertSoundData.LOW));
            this.discard();
        }
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.PEBBLE.get();
    }
}
