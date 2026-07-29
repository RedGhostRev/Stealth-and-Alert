package net.rev.stealthandalert.common.alert.util;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.rev.stealthandalert.compat.SupportedMods;
import net.rev.stealthandalert.compat.guardvillagers.GuardVillagersCompat;

public class ReputationUtil {
    public static boolean hasReputation(Entity entity) {
        if (entity instanceof Villager) return true;
        if (SupportedMods.GUARDVILLAGERS.isLoaded() && GuardVillagersCompat.isGuard(entity)) return true;
        return false;
    }

    public static int getReputation(Entity entity, Player player) {
        if (entity instanceof Villager villager) {
            return villager.getPlayerReputation(player);
        }
        if (SupportedMods.GUARDVILLAGERS.isLoaded() && GuardVillagersCompat.isGuard(entity)) {
            return GuardVillagersCompat.getReputation(entity, player);
        }
        return 0;
    }
}
