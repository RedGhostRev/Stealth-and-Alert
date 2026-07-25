package net.rev.stealthandalert.config;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.Map;

public record EntityAlertConditionSettings(
        DetectionSettings detection,
        AssassinationSettings assassination,
        Map<String, Map<String, JsonElement>> alertConditions
) {
    public record DetectionSettings(
            boolean ignoreBaby,
            double detectionRange,
            double horizontalFov,
            double verticalUpFov,
            double verticalDownFov,
            int reactionTicks,
            int trackingTicks,
            int patienceTicks,
            int memoryTicks
    ) {
        public static final Codec<DetectionSettings> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        Codec.BOOL.optionalFieldOf("ignore_baby", false).forGetter(DetectionSettings::ignoreBaby),
                        Codec.DOUBLE.optionalFieldOf("detection_range", -1.0).forGetter(DetectionSettings::detectionRange),
                        Codec.DOUBLE.optionalFieldOf("horizontal_fov",-1.0).forGetter(DetectionSettings::horizontalFov),
                        Codec.DOUBLE.optionalFieldOf("vertical_up_fov", -1.0).forGetter(DetectionSettings::verticalUpFov),
                        Codec.DOUBLE.optionalFieldOf("vertical_down_fov", -1.0).forGetter(DetectionSettings::verticalDownFov),
                        Codec.INT.optionalFieldOf("reaction_ticks", -1).forGetter(DetectionSettings::reactionTicks),
                        Codec.INT.optionalFieldOf("tracking_ticks", -1).forGetter(DetectionSettings::trackingTicks),
                        Codec.INT.optionalFieldOf("patience_ticks", -1).forGetter(DetectionSettings::patienceTicks),
                        Codec.INT.optionalFieldOf("memory_ticks", -1).forGetter(DetectionSettings::memoryTicks)
                ).apply(instance, DetectionSettings::new));
        public static final DetectionSettings DEFAULT = new DetectionSettings(
                false, -1.0, -1.0, -1.0, -1.0,
                -1, -1, -1, -1);
    }

    public record AssassinationSettings(
            double successChance
    ) {
        public static final Codec<AssassinationSettings> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        Codec.DOUBLE.optionalFieldOf("success_chance", -1.0).forGetter(AssassinationSettings::successChance)
                ).apply(instance, AssassinationSettings::new));
        public static final AssassinationSettings DEFAULT = new AssassinationSettings(
                -1.0
        );
    }

    public static final Codec<JsonElement> JSON_ELEMENT_CODEC = Codec.PASSTHROUGH.xmap(
            dynamic -> dynamic.convert(JsonOps.INSTANCE).getValue(), // 从数据转为 JsonElement
            element -> new Dynamic<>(JsonOps.INSTANCE, element)      // 从 JsonElement 转为数据
    );

    public static final Codec<EntityAlertConditionSettings> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    DetectionSettings.CODEC.optionalFieldOf("detection", DetectionSettings.DEFAULT)
                            .forGetter(EntityAlertConditionSettings::detection),
                    AssassinationSettings.CODEC.optionalFieldOf("assassination", AssassinationSettings.DEFAULT)
                            .forGetter(EntityAlertConditionSettings::assassination),
                    Codec.unboundedMap(Codec.STRING, Codec.unboundedMap(Codec.STRING, JSON_ELEMENT_CODEC))
                            .optionalFieldOf("alert_conditions", Map.of())
                            .forGetter(EntityAlertConditionSettings::alertConditions)
            ).apply(instance, EntityAlertConditionSettings::new)
    );

    public double getViewRange() {
        double val = detection.detectionRange;
        return val >= 0 ? val : CommonConfigs.DETECTION.maxDetectionRange.get();
    }

    public double getHorizontalFov() {
        double val = detection.horizontalFov;
        return val >= 0 ? val : CommonConfigs.DETECTION.horizontalFov.get();
    }

    public double getMaxUpPitch() {
        double val = detection.verticalUpFov;
        return val >= 0 ? val : CommonConfigs.DETECTION.verticalUpFov.get();
    }

    public double getMaxDownPitch() {
        double val = detection.verticalDownFov;
        return val >= 0 ? val : CommonConfigs.DETECTION.verticalDownFov.get();
    }

    public int getReactionTicks() {
        int val = detection.reactionTicks;
        return val >= 0 ? val : CommonConfigs.DETECTION.reactionTicks.get();
    }

    public int getTrackingTicks() {
        int val = detection.trackingTicks;
        return val >= 0 ? val : CommonConfigs.DETECTION.trackingTicks.get();
    }

    public int getPatienceTicks() {
        int val = detection.patienceTicks;
        return val >= 0 ? val : CommonConfigs.DETECTION.patienceTicks.get();
    }

    public int getMemoryTicks() {
        int val = detection.memoryTicks;
        return val >= 0 ? val : CommonConfigs.DETECTION.memoryTicks.get();
    }

    public double getSuccessChance() {
        double val = assassination.successChance;
        return val >= 0 ? val : CommonConfigs.ASSASSINATION.successChance.get();
    }
}
