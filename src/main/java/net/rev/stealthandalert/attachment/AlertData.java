package net.rev.stealthandalert.attachment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.UUIDUtil;
import net.minecraft.world.phys.Vec3;
import net.rev.stealthandalert.config.CommonConfigs;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public record AlertData(
        int state,
        Map<UUID, Float> targetProgress,
        Map<UUID, Integer> targetStates,
        Optional<Vec3> lastSeenPos,
        int reactionTicks,
        int stateTicks,
        int patienceTicks) {

    // 生物全局警戒状态
    public static final int IDLE = 0;
    public static final int SUSPICIOUS = 1;
    public static final int SEARCHING = 2;
    public static final int FIGHTING = 3;

    // 针对目标的观测状态
    public static final int UNTRACKED = 0;
    public static final int AWARE = 1;
    public static final int TRACKING = 2;

    public static final Codec<AlertData> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.INT.fieldOf("state").forGetter(AlertData::state),
                    Codec.unboundedMap(UUIDUtil.STRING_CODEC, Codec.FLOAT).fieldOf("target_progress").forGetter(AlertData::targetProgress),
                    Codec.unboundedMap(UUIDUtil.STRING_CODEC, Codec.INT).fieldOf("target_states").forGetter(AlertData::targetStates),
                    Vec3.CODEC.optionalFieldOf("last_pos").forGetter(AlertData::lastSeenPos),
                    Codec.INT.fieldOf("reaction_ticks").forGetter(AlertData::reactionTicks),
                    Codec.INT.fieldOf("state_ticks").forGetter(AlertData::stateTicks),
                    Codec.INT.fieldOf("patience_ticks").forGetter(AlertData::patienceTicks)
            ).apply(instance, AlertData::new)
    );

    public static final AlertData DEFAULT = new AlertData(IDLE, Map.of(), Map.of(), Optional.empty(), 0, 0, CommonConfigs.PATIENCE_TICKS.getAsInt());

    public AlertData withState(int newState, int ticks) {
        return new AlertData(newState, this.targetProgress, this.targetStates, this.lastSeenPos, this.reactionTicks, ticks, CommonConfigs.PATIENCE_TICKS.getAsInt());
    }

    public AlertData updateTarget(UUID uuid, float level, int pState) {
        return this;
    }
}

