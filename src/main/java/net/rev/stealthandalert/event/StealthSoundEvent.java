package net.rev.stealthandalert.event;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.Event;
import net.rev.stealthandalert.attribute.ModAttributes;


public class StealthSoundEvent extends Event {
    public final Type type;
    public final Vec3 soundPos;
    public final Entity soundSource;
    public double volume;
    public double radius;
    public final int threatLevel;

    public StealthSoundEvent(Type type, Vec3 soundPos, Entity soundSource, double volume, double radius, int threatLevel) {
        if (type == Type.PLAYER_SELF && soundSource instanceof Player player) {
            double multiplier = player.getAttributeValue(ModAttributes.SOUND_MULTIPLIER);
            volume = volume * multiplier;
            radius = radius * multiplier;
        }
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

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }
}
