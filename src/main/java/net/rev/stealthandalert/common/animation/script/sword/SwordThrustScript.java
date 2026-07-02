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

public class SwordThrustScript extends AssassinationScript {

    public SwordThrustScript(AssassinationHandler.AssassinateHand hand) {
        super(hand);
    }

    @Override
    public long getTotalTicks() {
        return 24;
    }

    @Override
    public void onStart(@NotNull Player player, @NotNull LivingEntity target) {
        AssassinationHandler.start(player, target);
    }

    @Override
    public void onServerTick(@NotNull ServerPlayer player, @NotNull LivingEntity target, long currentTick) {
        if (target.isAlive()) {
            if (currentTick == 0L) {
                AssassinationHandler.move(player, target, 0.3);
            } else if (currentTick == 5L) {
                AssassinationHandler.move(player, target, 0.3);
            } else if (currentTick == 11L) {
                AssassinationDamageSource source = AssassinationDamageSource.getSource(player, target, LangKeys.SWORD_THRUST, getHand());
                AssassinationHandler.executeByPercentage(player, target, source, 0.5F);
                player.level().playSound(null, target.getX(), target.getY(), target.getZ(), SoundEvents.PLAYER_ATTACK_STRONG, SoundSource.PLAYERS);
            } else if (currentTick == 18L) {
                AssassinationDamageSource source = AssassinationDamageSource.getSource(player, target, LangKeys.SWORD_THRUST, getHand());
                AssassinationHandler.execute(player, target, source);
                player.level().playSound(null, target.getX(), target.getY(), target.getZ(), SoundEvents.HONEY_BLOCK_BREAK, SoundSource.PLAYERS, 1F, 0.7F);
            }
        }
    }

    @Override
    public void onClientTick(@NotNull Player player, @NotNull LivingEntity target, long currentTick) {
        if (player instanceof LocalPlayer localPlayer) {
            AssassinationStateHandler.disableInput(localPlayer);
            if (currentTick == 11L) {
                CameraShakeManager.triggerShake(2, 1.5F);
            } else if (currentTick == 18L) {
                CameraShakeManager.triggerShake(5, 2F);
            }
        }
    }

    @Override
    public void onEnd(@NotNull Player player, @NotNull LivingEntity target) {
        AssassinationHandler.end(player, target);
    }
}
