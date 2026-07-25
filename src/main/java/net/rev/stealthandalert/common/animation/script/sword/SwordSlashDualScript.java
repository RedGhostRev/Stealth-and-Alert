package net.rev.stealthandalert.common.animation.script.sword;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.rev.stealthandalert.client.camera.CameraShakeManager;
import net.rev.stealthandalert.common.animation.AssassinationScript;
import net.rev.stealthandalert.common.animation.AssassinationStateHandler;
import net.rev.stealthandalert.damagetype.AssassinationDamageSource;
import net.rev.stealthandalert.datagen.LangKeys;
import net.rev.stealthandalert.util.AssassinationHandler;
import org.jetbrains.annotations.NotNull;

public class SwordSlashDualScript extends AssassinationScript {

    public SwordSlashDualScript(AssassinationHandler.AssassinateHand hand) {
        super(hand);
    }

    @Override
    public long getTotalTicks() {
        return 31;
    }

    @Override
    public void onStart(@NotNull Player player, @NotNull LivingEntity target) {
        AssassinationHandler.start(player, target);
    }

    @Override
    public void onServerTick(@NotNull ServerPlayer player, @NotNull LivingEntity target, long currentTick) {
        if (target.isAlive()) {
            if (currentTick == 0L) {
                AssassinationHandler.move(player, target, 0.1);
            } else if (currentTick == 3L) {
                AssassinationHandler.move(player, target, 0.1);
            } else if (currentTick == 6L) {
                AssassinationDamageSource source = AssassinationDamageSource.getSource(player, target, LangKeys.SWORD_SLASH, getHand());
                AssassinationHandler.executeByPercentage(player, target, source, 0.1F);
                player.level().playSound(null, target.getX(), target.getY(), target.getZ(), SoundEvents.PLAYER_ATTACK_SWEEP, SoundSource.PLAYERS);
            } else if (currentTick == 9L) {
                AssassinationDamageSource source = AssassinationDamageSource.getSource(player, target, LangKeys.SWORD_SLASH, getHand());
                AssassinationHandler.executeByPercentage(player, target, source, 0.1F);
                player.level().playSound(null, target.getX(), target.getY(), target.getZ(), SoundEvents.PLAYER_ATTACK_SWEEP, SoundSource.PLAYERS);
            } else if (currentTick == 12L) {
                AssassinationHandler.move(player, target, 0.1);
            } else if (currentTick == 15L) {
                AssassinationHandler.move(player, target, 0.1);
            } else if (currentTick == 20L) {
                AssassinationDamageSource source = AssassinationDamageSource.getSource(player, target, LangKeys.SWORD_SLASH, getHand());
                AssassinationHandler.executeByPercentage(player, target, source, 0.1F);
                player.level().playSound(null, target.getX(), target.getY(), target.getZ(), SoundEvents.PLAYER_ATTACK_SWEEP, SoundSource.PLAYERS);
            } else if (currentTick == 23L) {
                AssassinationDamageSource source = AssassinationDamageSource.getSource(player, target, LangKeys.SWORD_SLASH, getHand());
                AssassinationHandler.execute(player, target, source);
                player.level().playSound(null, target.getX(), target.getY(), target.getZ(), SoundEvents.PLAYER_ATTACK_SWEEP, SoundSource.PLAYERS);
            }
        }
    }

    @Override
    public void onClientTick(@NotNull Player player, @NotNull LivingEntity target, long currentTick) {
        if (player instanceof LocalPlayer localPlayer) {
            AssassinationStateHandler.disableInput(localPlayer);
            if (currentTick == 6L) {
                CameraShakeManager.triggerShake(2, 1.5F);
            } else if (currentTick == 9L) {
                CameraShakeManager.triggerShake(2, 1.5F);
            } else if (currentTick == 20L) {
                CameraShakeManager.triggerShake(2, 1.5F);
            } else if (currentTick == 23L) {
                CameraShakeManager.triggerShake(5, 2F);
            }
        }
    }

    @Override
    public void onEnd(@NotNull Player player, @NotNull LivingEntity target) {
        AssassinationHandler.end(player, target);
    }
}
