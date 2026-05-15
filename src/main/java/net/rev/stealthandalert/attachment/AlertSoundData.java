package net.rev.stealthandalert.attachment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.UUIDUtil;
import net.minecraft.world.phys.Vec3;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public record AlertSoundData(Optional<UUID> source, Optional<Vec3> pos, double volume, double distance, int threatLevel,
                             double score) {
    public static final int LOW = 0;
    public static final int MEDIUM = 1;
    public static final int HIGH = 2;

    public static final Codec<AlertSoundData> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    UUIDUtil.CODEC.optionalFieldOf("sound_source").forGetter(AlertSoundData::source),
                    Vec3.CODEC.optionalFieldOf("sound_pos").forGetter(AlertSoundData::pos),
                    Codec.DOUBLE.optionalFieldOf("volume", 0D).forGetter(AlertSoundData::volume),
                    Codec.DOUBLE.optionalFieldOf("distance", 0D).forGetter(AlertSoundData::distance),
                    Codec.INT.optionalFieldOf("threat_level", 0).forGetter(AlertSoundData::threatLevel),
                    Codec.DOUBLE.optionalFieldOf("score", 0D).forGetter(AlertSoundData::score)
            ).apply(instance, AlertSoundData::new)
    );

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AlertSoundData that = (AlertSoundData) o;
        return Double.compare(score, that.score) == 0 && Double.compare(volume, that.volume) == 0 && Double.compare(distance, that.distance) == 0 && threatLevel == that.threatLevel && Objects.equals(pos, that.pos);
    }

    public static final AlertSoundData DEFAULT = new AlertSoundData(Optional.empty(), Optional.empty(), 0D, 0D, LOW, 0D);
}
