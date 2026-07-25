package net.rev.stealthandalert.util;

import com.google.gson.JsonElement;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.gameevent.EntityPositionSource;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEventListener;
import net.minecraft.world.level.gameevent.PositionSource;
import net.minecraft.world.phys.Vec3;
import net.rev.stealthandalert.attachment.EventListenerData;
import net.rev.stealthandalert.attachment.ModAttachments;
import net.rev.stealthandalert.common.alert.condition.AlertConditionRegistry;
import net.rev.stealthandalert.common.alert.condition.IAlertCondition;
import net.rev.stealthandalert.common.alert.condition.IGameEventCondition;
import net.rev.stealthandalert.config.EntityAlertConditionConfigLoader;
import net.rev.stealthandalert.config.EntityAlertConditionSettings;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class StealthEventListener implements GameEventListener {
    private final Mob mob;
    private final PositionSource positionSource;

    public StealthEventListener(Mob mob) {
        this.mob = mob;
        this.positionSource = new EntityPositionSource(mob, mob.getEyeHeight());
    }

    @Override
    public @NotNull PositionSource getListenerSource() {
        return this.positionSource;
    }

    @Override
    public int getListenerRadius() {
        EntityAlertConditionSettings settings = EntityAlertConditionConfigLoader.get(mob.getType());
        return (int) settings.getViewRange();
    }

    @Override
    public boolean handleGameEvent(@NotNull ServerLevel level, @NotNull Holder<GameEvent> gameEvent, GameEvent.@NotNull Context context, @NotNull Vec3 pos) {
        EntityAlertConditionSettings settings = EntityAlertConditionConfigLoader.get(mob.getType());
        EventListenerData data = mob.getData(ModAttachments.EVENT_LISTENER_DATA);

        for (Map.Entry<String, Map<String, JsonElement>> entry : settings.alertConditions().entrySet()) {
            String logicId = entry.getKey();
            ResourceLocation rl = ResourceLocation.tryParse(logicId);
            if (rl != null) {
                IAlertCondition logic = AlertConditionRegistry.get(rl);
                if (logic instanceof IGameEventCondition eventLogic) {
                    // 判断事件是否触发逻辑
                    Map<String, JsonElement> params = entry.getValue();
                    if (eventLogic.shouldTrigger(mob, gameEvent.value(), context, params)) {
                        if (context.sourceEntity() == null) continue;
                        data.updateState(logicId, context.sourceEntity().getUUID(), level.getGameTime());
                    }
                }
            }
        }
        return false;
    }
}
