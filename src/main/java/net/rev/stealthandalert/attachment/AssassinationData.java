package net.rev.stealthandalert.attachment;

import net.minecraft.resources.ResourceLocation;
import net.rev.stealthandalert.common.animation.ModAnimations;
import net.rev.stealthandalert.util.AssassinationHandler;

import java.util.Optional;
import java.util.UUID;

public record AssassinationData(Optional<UUID> playerUUID,
                                int targetId,
                                boolean isAssassinating,
                                long startTick,
                                ResourceLocation animRL,
                                AssassinationHandler.AssassinateHand hand) {

    public static final AssassinationData DEFAULT = new AssassinationData(Optional.empty(),
            -1,
            false,
            -1L,
            ModAnimations.EMPTY,
            AssassinationHandler.AssassinateHand.RIGHT_HAND);

    public static AssassinationData getDefaultExceptHand(AssassinationData data, UUID playerUUID) {
        return new AssassinationData(
                Optional.of(playerUUID),
                -1,
                false,
                -1L,
                ModAnimations.EMPTY,
                data.hand()
        );
    }

    public static AssassinationData getDefaultExceptHand(AssassinationData data) {
        return new AssassinationData(
                Optional.empty(),
                -1,
                false,
                -1L,
                ModAnimations.EMPTY,
                data.hand()
        );
    }
}
