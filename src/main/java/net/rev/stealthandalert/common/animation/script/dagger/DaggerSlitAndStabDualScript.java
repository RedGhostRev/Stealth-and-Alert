package net.rev.stealthandalert.common.animation.script.dagger;

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

public class DaggerSlitAndStabDualScript extends AssassinationScript {

    public DaggerSlitAndStabDualScript(AssassinationHandler.AssassinateHand hand) {
        super(hand);
    }

    @Override
    public long getTotalTicks() {
        return 58;
    }

    @Override
    public void onStart(@NotNull Player player, @NotNull LivingEntity target) {
        AssassinationHandler.start(player, target);
    }

    @Override
    public void onServerTick(@NotNull ServerPlayer player, @NotNull LivingEntity target, long currentTick) {
        if (currentTick == 0) {
            AssassinationHandler.move(player, target, 0.1);
        } else if (currentTick == 10) {
            AssassinationHandler.move(player, target, 0.4);
        } else if (currentTick == 16) {
            AssassinationDamageSource source = AssassinationDamageSource.getSource(player, target, LangKeys.DAGGER_THROAT_SLIT, getHand());
            AssassinationHandler.executeByPercentage(player, target, source, 0.15F);
            player.level().playSound(null, target.getX(), target.getY(), target.getZ(), SoundEvents.PLAYER_ATTACK_CRIT, SoundSource.PLAYERS);
        } else if (currentTick == 29) {
            AssassinationHandler.move(player, target, 0.1);
        } else if (currentTick == 35) {
            AssassinationDamageSource source = AssassinationDamageSource.getSource(player, target, LangKeys.DAGGER_THROAT_SLIT, getHand());
            AssassinationHandler.executeByPercentage(player, target, source, 0.15F);
            player.level().playSound(null, target.getX(), target.getY(), target.getZ(), SoundEvents.PLAYER_ATTACK_STRONG, SoundSource.PLAYERS);
            player.level().playSound(null, target.getX(), target.getY(), target.getZ(), SoundEvents.HONEY_BLOCK_BREAK, SoundSource.PLAYERS, 1F, 0.7F);
        } else if (currentTick == 39) {
            AssassinationDamageSource source = AssassinationDamageSource.getSource(player, target, LangKeys.DAGGER_THROAT_SLIT, getHand());
            AssassinationHandler.executeByPercentage(player, target, source, 0.15F);
            player.level().playSound(null, target.getX(), target.getY(), target.getZ(), SoundEvents.PLAYER_ATTACK_STRONG, SoundSource.PLAYERS);
            player.level().playSound(null, target.getX(), target.getY(), target.getZ(), SoundEvents.HONEY_BLOCK_BREAK, SoundSource.PLAYERS, 1F, 0.7F);
        } else if (currentTick == 43) {
            AssassinationDamageSource source = AssassinationDamageSource.getSource(player, target, LangKeys.DAGGER_THROAT_SLIT, getHand());
            AssassinationHandler.executeByPercentage(player, target, source, 0.15F);
            player.level().playSound(null, target.getX(), target.getY(), target.getZ(), SoundEvents.PLAYER_ATTACK_STRONG, SoundSource.PLAYERS);
            player.level().playSound(null, target.getX(), target.getY(), target.getZ(), SoundEvents.HONEY_BLOCK_BREAK, SoundSource.PLAYERS, 1F, 0.7F);
        } else if (currentTick == 48) {
            AssassinationDamageSource source = AssassinationDamageSource.getSource(player, target, LangKeys.DAGGER_THROAT_SLIT, getHand());
            AssassinationHandler.execute(player, target, source);
            player.level().playSound(null, target.getX(), target.getY(), target.getZ(), SoundEvents.PLAYER_ATTACK_CRIT, SoundSource.PLAYERS);
        }
    }

    @Override
    public void onClientTick(@NotNull Player player, @NotNull LivingEntity target, long currentTick) {
        if (player instanceof LocalPlayer localPlayer) {
            AssassinationStateHandler.disableInput(localPlayer);
            if (currentTick == 16) {
                CameraShakeManager.triggerShake(5, 1.5F);
            } else if (currentTick == 35) {
                CameraShakeManager.triggerShake(2, 1.5F);
            } else if (currentTick == 39) {
                CameraShakeManager.triggerShake(2, 1.5F);
            } else if (currentTick == 43) {
                CameraShakeManager.triggerShake(2, 1.5F);
            } else if (currentTick == 48) {
                CameraShakeManager.triggerShake(5, 2F);
            }
        }
    }

    @Override
    public void onEnd(@NotNull Player player, @NotNull LivingEntity target) {
        AssassinationHandler.end(player, target);
    }
}
