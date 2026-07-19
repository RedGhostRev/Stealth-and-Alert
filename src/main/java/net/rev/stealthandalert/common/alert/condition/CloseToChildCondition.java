package net.rev.stealthandalert.common.alert.condition;

import com.google.gson.JsonElement;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;

import java.util.List;
import java.util.Map;

public class CloseToChildCondition implements IAlertCondition {
    @Override
    public boolean test(Mob mob, Player player, Map<String, JsonElement> params) {
        if (mob.isBaby()) return false;
        double horizontalRange = getDouble(params, "horizontal_range", 8.0);
        double verticalRange = getDouble(params, "vertical_range", 4.0);

        AABB searchBox = mob.getBoundingBox().inflate(horizontalRange, verticalRange, horizontalRange);
        List<? extends Mob> children = mob.level().getEntitiesOfClass(mob.getClass(), searchBox, Mob::isBaby);

        for (Mob child : children) {
            if (child.distanceToSqr(player) < horizontalRange * horizontalRange) {
                return true;
            }
        }
        return false;
    }
}
