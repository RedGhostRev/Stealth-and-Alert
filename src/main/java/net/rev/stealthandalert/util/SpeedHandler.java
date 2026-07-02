package net.rev.stealthandalert.util;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SpeedHandler {
    private static final Map<UUID, Double> playerSpeeds = new ConcurrentHashMap<>();

    public static void updateSpeed(UUID uuid, double speed) {
        playerSpeeds.put(uuid, speed);
    }

    public static double getSpeed(UUID uuid) {
        return playerSpeeds.getOrDefault(uuid, 0.0);
    }

    public static void clear(UUID uuid) {
        playerSpeeds.remove(uuid);
    }
}
