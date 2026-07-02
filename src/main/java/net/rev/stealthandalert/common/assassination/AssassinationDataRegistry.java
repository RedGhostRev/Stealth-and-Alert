package net.rev.stealthandalert.common.assassination;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Pose;

import java.util.List;
import java.util.Optional;

public class AssassinationDataRegistry {
    public record AnimationConditions(
            Boolean requireAir,
            Pose requirePose,
            Double minSpeed,
            Double maxSpeed,
            Double minTargetHeight,
            Double maxTargetHeight
    ) {
        public boolean matches(AssassinationContext context) {
            if (requireAir() != null && requireAir() != context.inAir()) return false;
            //if (requirePose() != null && requirePose() != context.pose()) return false;
            if (minSpeed() != null && context.speed() < minSpeed()) return false;
            if (maxSpeed() != null && context.speed() > maxSpeed()) return false;
            if (minTargetHeight() != null && context.targetHeight() < minTargetHeight()) return false;
            if (maxTargetHeight() != null && context.targetHeight() > maxTargetHeight()) return false;

            if (requirePose() != null) {
                return requirePose() == context.pose();
            } else {
                return true;
            }
        }
    }

    public record AnimationEntry(
            ResourceLocation animRL,
            boolean isTwoHanded,
            AnimationConditions conditions
    ) {
    }

    public record DistanceTier(double maxDistance,
                               List<AnimationEntry> animPool) {
        public Optional<AnimationEntry> getRandomValidAnimation(AssassinationContext context, RandomSource random) {
            List<AnimationEntry> validPool = animPool.stream()
                    .filter(entry -> entry.conditions().matches(context))
                    .toList();
            if (validPool.isEmpty()) return Optional.empty();
            return Optional.of(validPool.get(random.nextInt(validPool.size())));
        }
    }

    public record WeaponProfile(
            List<DistanceTier> singleHandedTiers,
            List<DistanceTier> twoHandedTiers
            ) {

        public Optional<DistanceTier> matchTier(double distance, boolean isTwoHanded) {
            List<DistanceTier> targetTiers = isTwoHanded ? twoHandedTiers : singleHandedTiers;
            for (DistanceTier tier : targetTiers) {
                if (distance <= tier.maxDistance()) {
                    if (!tier.animPool().isEmpty()) {
                        return Optional.of(tier);
                    }
                }
            }
            return Optional.empty();
        }
    }
}
