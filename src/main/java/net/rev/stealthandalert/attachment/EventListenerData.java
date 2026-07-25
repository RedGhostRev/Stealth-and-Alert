package net.rev.stealthandalert.attachment;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public record EventListenerData(Map<String, Map<UUID, Long>> eventStates) {
    public void updateState(String logicId, UUID playerUuid, long gameTime) {
        eventStates.computeIfAbsent(logicId, k -> new HashMap<>())
                .put(playerUuid, gameTime);
    }

    public static EventListenerData createDefault() {
        return new EventListenerData(new HashMap<>());
    }
}
