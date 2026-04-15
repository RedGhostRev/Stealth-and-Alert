package net.rev.stealthandalert.attachment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.phys.Vec3;
import net.rev.stealthandalert.config.CommonConfigs;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public record AlertData(
        int state,
        Map<UUID, Float> targetProgress,
        Map<UUID, Integer> targetStates,
        Map<UUID, Integer> targetReactions,
        Optional<Vec3> lastSeenPos,
        Optional<UUID> primaryTarget,
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
                    Codec.unboundedMap(UUIDUtil.STRING_CODEC, Codec.INT).fieldOf("target_reactions").forGetter(AlertData::targetReactions),
                    Vec3.CODEC.optionalFieldOf("last_pos").forGetter(AlertData::lastSeenPos),
                    UUIDUtil.STRING_CODEC.optionalFieldOf("primary_target").forGetter(AlertData::primaryTarget),
                    Codec.INT.fieldOf("state_ticks").forGetter(AlertData::stateTicks),
                    Codec.INT.fieldOf("patience_ticks").forGetter(AlertData::patienceTicks)
            ).apply(instance, AlertData::new)
    );

    public static final StreamCodec<ByteBuf, Vec3> VEC3_STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.DOUBLE, Vec3::x,
            ByteBufCodecs.DOUBLE, Vec3::y,
            ByteBufCodecs.DOUBLE, Vec3::z,
            Vec3::new
    );

    public static final StreamCodec<ByteBuf, AlertData> STREAM_CODEC =
            ByteBufCodecs.fromCodec(CODEC);


//    public static final StreamCodec<ByteBuf, AlertData> STREAM_CODEC = StreamCodec.composite(
//            ByteBufCodecs.VAR_INT, AlertData::state,
//            ByteBufCodecs.map(HashMap::new, UUIDUtil.STREAM_CODEC, ByteBufCodecs.FLOAT), AlertData::targetProgress,
//            ByteBufCodecs.map(HashMap::new, UUIDUtil.STREAM_CODEC, ByteBufCodecs.VAR_INT), AlertData::targetStates,
//            ByteBufCodecs.map(HashMap::new, UUIDUtil.STREAM_CODEC, ByteBufCodecs.VAR_INT), AlertData::targetReactions,
//            ByteBufCodecs.optional(VEC3_STREAM_CODEC), AlertData::lastSeenPos,
//            ByteBufCodecs.optional(UUIDUtil.STREAM_CODEC), AlertData::primaryTarget,
//            ByteBufCodecs.VAR_INT, AlertData::stateTicks,
//            ByteBufCodecs.VAR_INT, AlertData::patienceTicks,
//            AlertData::new);

    public static final AlertData DEFAULT = new AlertData(IDLE, Map.of(), Map.of(), Map.of(), Optional.empty(), Optional.empty(), 0, CommonConfigs.PATIENCE_TICKS.getAsInt());
}

