package net.rev.stealthandalert.common.assassination;

import net.minecraft.world.entity.Pose;

public record AssassinationContext(
        double distance,
        boolean inAir,
        Pose pose,
        double speed,
        double targetHeight
) {
}
