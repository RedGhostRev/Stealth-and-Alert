package net.rev.stealthandalert.common.alert.condition;

import com.google.gson.JsonElement;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;

import java.util.Map;

public class LightSensitiveCondition implements IAlertCondition {
    @Override
    public boolean test(Mob mob, Player player, Map<String, JsonElement> params) {
        int threshold = getInt(params, "threshold", 11);
        boolean invert = getBool(params, "invert", false);

        int currentLight = mob.level().getMaxLocalRawBrightness(mob.blockPosition());
        return invert ? (currentLight >= threshold) : (currentLight <= threshold);
    }
}
