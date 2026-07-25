package net.rev.stealthandalert.common.alert.condition;

import com.google.gson.JsonElement;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;

import java.util.List;
import java.util.Map;

public class VillageReputationCondition implements IAlertCondition {
    @Override
    public boolean test(Mob mob, Player player, Map<String, JsonElement> params) {
        int threshold = getInt(params, "threshold", -15);
        double range = getDouble(params, "range", 16.0);
        List<Villager> nearbyVillagers = mob.level().getEntitiesOfClass(Villager.class,
                mob.getBoundingBox().inflate(range));

        if (nearbyVillagers.isEmpty()) return false;

        for (Villager villager : nearbyVillagers) {
            if (villager.getPlayerReputation(player) <= threshold) {
                return true;
            }
        }
        return false;
    }
}
