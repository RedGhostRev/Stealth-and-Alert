package net.rev.stealthandalert.attachment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.phys.Vec3;
import net.rev.stealthandalert.config.CommonConfigs;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public record AlertData(
        int state,
        Map<UUID, Float> targetAwareness,
        Map<UUID, Integer> targetStates,
        Map<UUID, Integer> targetReactionTicks,
        Map<UUID, Integer> targetMemoryTicks,
        Optional<Vec3> lastKnownPos,
        Optional<UUID> primaryTarget,
        int stateChangeTicks,
        int patienceTicks,
        boolean canSeeAnyone,
        boolean willFighting) {

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
                    Codec.unboundedMap(UUIDUtil.STRING_CODEC, Codec.FLOAT).optionalFieldOf("target_awareness", Map.of()).forGetter(AlertData::targetAwareness),
                    Codec.unboundedMap(UUIDUtil.STRING_CODEC, Codec.INT).optionalFieldOf("target_states", Map.of()).forGetter(AlertData::targetStates),
                    Codec.unboundedMap(UUIDUtil.STRING_CODEC, Codec.INT).optionalFieldOf("target_reaction_ticks", Map.of()).forGetter(AlertData::targetReactionTicks),
                    Codec.unboundedMap(UUIDUtil.STRING_CODEC, Codec.INT).optionalFieldOf("target_memory_ticks", Map.of()).forGetter(AlertData::targetMemoryTicks),
                    Vec3.CODEC.optionalFieldOf("last_known_pos").forGetter(AlertData::lastKnownPos),
                    UUIDUtil.STRING_CODEC.optionalFieldOf("primary_target").forGetter(AlertData::primaryTarget),

                    Codec.INT.optionalFieldOf("state_change_ticks", 0).forGetter(AlertData::stateChangeTicks),
                    Codec.INT.optionalFieldOf("patience_ticks", 600).forGetter(AlertData::patienceTicks),
                    Codec.BOOL.optionalFieldOf("can_see_anyone", false).forGetter(AlertData::canSeeAnyone),
                    Codec.BOOL.optionalFieldOf("will_fighting", false).forGetter(AlertData::willFighting)
            ).apply(instance, AlertData::new)
    );

    public static final StreamCodec<ByteBuf, Vec3> VEC3_STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.DOUBLE, Vec3::x,
            ByteBufCodecs.DOUBLE, Vec3::y,
            ByteBufCodecs.DOUBLE, Vec3::z,
            Vec3::new
    );

    private static final StreamCodec<ByteBuf, Map<UUID, Float>> PROGRESS_MAP_CODEC =
            ByteBufCodecs.map(HashMap::new, UUIDUtil.STREAM_CODEC, ByteBufCodecs.FLOAT);

    private static final StreamCodec<ByteBuf, Map<UUID, Integer>> INT_MAP_CODEC =
            ByteBufCodecs.map(HashMap::new, UUIDUtil.STREAM_CODEC, ByteBufCodecs.VAR_INT);

    public static final StreamCodec<ByteBuf, AlertData> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public @NotNull AlertData decode(@NotNull ByteBuf buf) {
            return new AlertData(
                    ByteBufCodecs.VAR_INT.decode(buf),                           // state
                    PROGRESS_MAP_CODEC.decode(buf),                              // targetAwareness
                    INT_MAP_CODEC.decode(buf),                                   // targetStates
                    INT_MAP_CODEC.decode(buf),                                   // targetReactionTicks
                    INT_MAP_CODEC.decode(buf),                                   // targetMemoryTicks
                    ByteBufCodecs.optional(VEC3_STREAM_CODEC).decode(buf),       // lastKnownPos
                    ByteBufCodecs.optional(UUIDUtil.STREAM_CODEC).decode(buf),   // primaryTarget
                    ByteBufCodecs.VAR_INT.decode(buf),                           // stateChangeTicks
                    ByteBufCodecs.VAR_INT.decode(buf),                           // patienceTicks
                    ByteBufCodecs.BOOL.decode(buf),                              // canSeeAnyone
                    ByteBufCodecs.BOOL.decode(buf)                               // willFighting
            );
        }

        @Override
        public void encode(@NotNull ByteBuf buf, AlertData data) {
            ByteBufCodecs.VAR_INT.encode(buf, data.state());
            PROGRESS_MAP_CODEC.encode(buf, data.targetAwareness());
            INT_MAP_CODEC.encode(buf, data.targetStates());
            INT_MAP_CODEC.encode(buf, data.targetReactionTicks());
            INT_MAP_CODEC.encode(buf, data.targetMemoryTicks());
            ByteBufCodecs.optional(VEC3_STREAM_CODEC).encode(buf, data.lastKnownPos());
            ByteBufCodecs.optional(UUIDUtil.STREAM_CODEC).encode(buf, data.primaryTarget());
            ByteBufCodecs.VAR_INT.encode(buf, data.stateChangeTicks());
            ByteBufCodecs.VAR_INT.encode(buf, data.patienceTicks());
            ByteBufCodecs.BOOL.encode(buf, data.canSeeAnyone());
            ByteBufCodecs.BOOL.encode(buf, data.willFighting());
        }
    };

    public AlertData withSound(int newState, Optional<Vec3> newLkp, int newStateChangeTicks, int newPatienceTicks) {
        return new AlertData(newState, this.targetAwareness, this.targetStates, this.targetReactionTicks, this.targetMemoryTicks, newLkp, this.primaryTarget, newStateChangeTicks, newPatienceTicks, this.canSeeAnyone, this.willFighting);
    }

    public static AlertData createDefault() {
        return new AlertData(IDLE, Map.of(), Map.of(), Map.of(), Map.of(), Optional.empty(), Optional.empty(), 0, CommonConfigs.DETECTION.patienceTicks.getAsInt(), false, false);
    }
}

