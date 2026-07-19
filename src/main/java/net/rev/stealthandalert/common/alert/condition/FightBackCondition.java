package net.rev.stealthandalert.common.alert.condition;

import com.google.gson.JsonElement;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.rev.stealthandalert.StealthAndAlert;
import net.rev.stealthandalert.attachment.AlertData;
import net.rev.stealthandalert.attachment.ModAttachments;

import java.util.Map;
import java.util.UUID;

public class FightBackCondition implements IAlertCondition {
    public static final String ID = StealthAndAlert.MOD_ID + ":fight_back";

    @Override
    public boolean test(Mob mob, Player player, Map<String, JsonElement> params) {
        return isPhysicallyProvoked(mob, player);
    }

    private static boolean isPhysicallyProvoked(Mob mob, Player player) {
        AlertData data = mob.getData(ModAttachments.ALERT_DATA);
        if (mob.getTarget() == player || data.targetStates().getOrDefault(player.getUUID(), AlertData.UNTRACKED) > AlertData.UNTRACKED) {
            return true;
        }
        Map<UUID, Long> map = mob.getData(ModAttachments.EVENT_LISTENER_DATA).eventStates().get(ID);
        if (map != null) {
            Long lastTime = map.getOrDefault(player.getUUID(), -1L);
            if (lastTime > -1 && mob.level().getGameTime() - lastTime < 100L) return true;
        }
        return data.targetMemoryTicks().getOrDefault(player.getUUID(), 0) > 0;
    }
}
