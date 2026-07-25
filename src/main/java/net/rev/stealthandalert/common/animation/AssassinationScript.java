package net.rev.stealthandalert.common.animation;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.rev.stealthandalert.util.AssassinationHandler;
import org.jetbrains.annotations.NotNull;

public abstract class AssassinationScript {
    private final AssassinationHandler.AssassinateHand hand;

    public AssassinationScript(AssassinationHandler.AssassinateHand hand) {
        this.hand = hand;
    }

    public abstract long getTotalTicks();

    public abstract void onStart(@NotNull Player player,@NotNull LivingEntity target);

    public abstract void onServerTick(@NotNull ServerPlayer player,@NotNull LivingEntity target, long currentTick);

    public abstract void onClientTick(@NotNull Player player,@NotNull LivingEntity target, long currentTick);

    public abstract void onEnd(@NotNull Player player,@NotNull LivingEntity target);

    public AssassinationHandler.AssassinateHand getHand() {
        return this.hand;
    }
}
