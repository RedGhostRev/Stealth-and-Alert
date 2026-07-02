package net.rev.stealthandalert.common.animation.script.mace;

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

public class MaceSmashDualScript extends AssassinationScript {
    public MaceSmashDualScript(AssassinationHandler.AssassinateHand hand) {
        super(hand);
    }

    @Override
    public long getTotalTicks() {
        return 60;
    }

    @Override
    public void onStart(@NotNull Player player, @NotNull LivingEntity target) {
        AssassinationHandler.start(player, target);
    }

    @Override
    public void onServerTick(@NotNull ServerPlayer player, @NotNull LivingEntity target, long currentTick) {
        if (currentTick == 0) {
            AssassinationHandler.move(player, target, 0.1);
        } else if (currentTick == 16) {
            AssassinationDamageSource source = AssassinationDamageSource.getSource(player, target, LangKeys.MACE_SMASH, getHand());
            AssassinationHandler.executeByPercentage(player, target, source, 0.3F);
            player.level().playSound(null, target.getX(), target.getY(), target.getZ(), SoundEvents.MACE_SMASH_AIR, SoundSource.PLAYERS, 1F, 1F);
        } else if (currentTick == 33) {
            AssassinationDamageSource source = AssassinationDamageSource.getSource(player, target, LangKeys.MACE_SMASH, getHand());
            AssassinationHandler.executeByPercentage(player, target, source, 0.3F);
            player.level().playSound(null, target.getX(), target.getY(), target.getZ(), SoundEvents.MACE_SMASH_AIR, SoundSource.PLAYERS, 1F, 1F);
        } else if (currentTick == 51) {
            AssassinationDamageSource source = AssassinationDamageSource.getSource(player, target, LangKeys.MACE_SMASH, getHand());
            AssassinationHandler.execute(player, target, source);
            player.level().playSound(null, target.getX(), target.getY(), target.getZ(), SoundEvents.MACE_SMASH_GROUND, SoundSource.PLAYERS, 1F, 1F);
        }
    }

    @Override
    public void onClientTick(@NotNull Player player, @NotNull LivingEntity target, long currentTick) {
        if (player instanceof LocalPlayer localPlayer) {
            AssassinationStateHandler.disableInput(localPlayer);
            if (currentTick == 16) {
                CameraShakeManager.triggerShake(6, 2.5F);
            } else if (currentTick == 33) {
                CameraShakeManager.triggerShake(6, 2.5F);
            } else if (currentTick == 51) {
                CameraShakeManager.triggerShake(8, 3F);
            }
        }
    }

    @Override
    public void onEnd(@NotNull Player player, @NotNull LivingEntity target) {
        AssassinationHandler.end(player, target);
    }
}
