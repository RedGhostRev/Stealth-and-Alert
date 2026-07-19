package net.rev.stealthandalert.common.alert.condition;

import com.google.gson.JsonElement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.rev.stealthandalert.StealthAndAlert;
import net.rev.stealthandalert.config.EntityAlertConditionSettings;

import java.util.Map;

public class AlertConditionHandler {
    public static boolean checkAllConditions(Mob mob, Player player, EntityAlertConditionSettings settings) {
        for (Map.Entry<String, Map<String, JsonElement>> entry : settings.alertConditions().entrySet()) {
            ResourceLocation logicId = ResourceLocation.tryParse(entry.getKey());
            if (logicId == null) continue;

            IAlertCondition logic = AlertConditionRegistry.get(logicId);
            if (logic != null) {
                if (logic.test(mob, player, entry.getValue())) {
                    return true;
                }
            } else {
                StealthAndAlert.LOGGER.debug("注册表中未找到条件：{}", logicId);
            }
        }
        return false;
    }
}
