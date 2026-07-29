package net.rev.stealthandalert.common.alert.condition;

import com.google.gson.JsonElement;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.rev.stealthandalert.common.alert.util.ReputationUtil;
import net.rev.stealthandalert.compat.SupportedMods;
import net.rev.stealthandalert.compat.guardvillagers.GuardVillagersCompat;
import net.rev.stealthandalert.config.CommonConfigs;

import java.util.List;
import java.util.Map;

public class VillageReputationCondition implements IAlertCondition {
    @Override
    public boolean test(Mob mob, Player player, Map<String, JsonElement> params) {
        int threshold = getInt(params, "threshold", -100);
        if (SupportedMods.GUARDVILLAGERS.isLoaded()) {
            if (GuardVillagersCompat.isGuard(mob) && CommonConfigs.COMPAT.GUARDVILLAGERS.applyGuardVillagerReputationConfig.get()) {
                threshold = GuardVillagersCompat.getReputationThreshold();
            }
        }
        double range = getDouble(params, "range", 16.0);
        List<Mob> nearbyVillagers = mob.level().getEntitiesOfClass(Mob.class,
                mob.getBoundingBox().inflate(range),
                ReputationUtil::hasReputation);

        if (nearbyVillagers.isEmpty()) return false;

        for (Mob mob1 : nearbyVillagers) {
            if (ReputationUtil.getReputation(mob1, player) <= threshold) {
                return true;
            }
        }
        return false;
    }
}
