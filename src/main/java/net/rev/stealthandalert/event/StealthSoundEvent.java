package net.rev.stealthandalert.event;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.Event;


public class StealthSoundEvent extends Event {
    public final Type type;
    public final Vec3 soundPos;
    public final Entity soundSource;
    public final double volume;
    public final double radius;
    public final int threatLevel;

    public StealthSoundEvent(Type type, Vec3 soundPos, Entity soundSource, double volume, double radius, int threatLevel) {
        this.type = type;
        this.soundPos = soundPos;
        this.soundSource = soundSource;
        this.volume = volume;
        this.radius = radius;
        this.threatLevel = threatLevel;
    }

    public enum Type {
        PLAYER_SELF,
        ENVIRONMENT
    }
}
