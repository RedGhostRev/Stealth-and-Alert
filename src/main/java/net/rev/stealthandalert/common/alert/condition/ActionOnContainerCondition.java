package net.rev.stealthandalert.common.alert.condition;

import com.google.gson.JsonElement;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import net.rev.stealthandalert.StealthAndAlert;
import net.rev.stealthandalert.attachment.EventListenerData;
import net.rev.stealthandalert.attachment.ModAttachments;
import net.rev.stealthandalert.config.EntityAlertConditionConfigLoader;

import java.util.Map;
import java.util.UUID;

public class ActionOnContainerCondition implements IGameEventCondition {
    public static final String ID = StealthAndAlert.MOD_ID + ":action_on_container";

    @Override
    public boolean test(Mob mob, Player player, Map<String, JsonElement> params) {
        EventListenerData data = mob.getData(ModAttachments.EVENT_LISTENER_DATA);
        Map<UUID, Long> playerStates = data.eventStates().get(ID);
        if (playerStates == null) return false;
        Long lastTriggerTime = playerStates.get(player.getUUID());
        if (lastTriggerTime == null) return false;
        long durationTicks = 20L;
        long currentTime = mob.level().getGameTime();

        return lastTriggerTime >= 0 && (currentTime - lastTriggerTime) <= durationTicks;
    }

    @Override
    public boolean shouldTrigger(Mob mob, GameEvent event, GameEvent.Context context, Map<String, JsonElement> params) {
        boolean isOpen = (event == GameEvent.CONTAINER_OPEN.value());
        boolean isDestroy = (event == GameEvent.BLOCK_DESTROY.value());
        if (!isOpen && !isDestroy) return false;

        if (isDestroy) {
            BlockState state = context.affectedState();
            if (state == null) return false;
            boolean isContainer = state.is(BlockTags.GUARDED_BY_PIGLINS);
            if (!isContainer) return false;
        }
        double range = getDouble(params, "range", EntityAlertConditionConfigLoader.get(mob.getType()).getViewRange());
        Vec3 eventPos = context.sourceEntity() != null ? context.sourceEntity().position() : null;
        if (eventPos == null) return false;

        return mob.distanceToSqr(eventPos) <= range * range;
    }
}
