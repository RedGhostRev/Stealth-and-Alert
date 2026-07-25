package net.rev.stealthandalert.common.alert.condition;

import com.google.gson.JsonElement;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.gameevent.GameEvent;

import java.util.Map;

public interface IGameEventCondition extends IAlertCondition {
    boolean shouldTrigger(Mob mob, GameEvent event, GameEvent.Context context, Map<String, JsonElement> params);
}
