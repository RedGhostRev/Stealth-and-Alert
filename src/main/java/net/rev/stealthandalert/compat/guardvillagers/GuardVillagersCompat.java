package net.rev.stealthandalert.compat.guardvillagers;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.rev.stealthandalert.compat.SupportedMods;
import net.rev.stealthandalert.datagen.ModEntityTypeTagsProvider;
import net.rev.stealthandalert.util.ModTags;
import tallestegg.guardvillagers.common.entities.Guard;
import tallestegg.guardvillagers.configuration.GuardConfig;

public class GuardVillagersCompat {
    private static final SupportedMods mod = SupportedMods.GUARDVILLAGERS;

    public static void addEntityTags(ModEntityTypeTagsProvider provider) {
        provider.tag(ModTags.Entities.CONDITIONAL_SEEKERS)
                .addOptional(mod.rl("guard"));
    }

    public static boolean isGuard(Entity entity) {
        return entity instanceof Guard;
    }

    public static int getReputation(Entity entity, Player player) {
        return ((Guard)entity).getPlayerReputation(player);
    }

    public static int getReputationThreshold() {
        return GuardConfig.COMMON.reputationRequirementToBeAttacked.get();
    }
}
