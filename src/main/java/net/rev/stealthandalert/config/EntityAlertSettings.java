package net.rev.stealthandalert.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.rev.stealthandalert.StealthAndAlert;

import java.util.List;
import java.util.Map;

public record EntityAlertSettings(
        double viewRange,
        double horizontalFov,
        double maxUpPitch,
        double maxDownPitch,
        boolean ignoreBaby,
        List<String> logicList,
        Map<String, String> params
) {

    public static EntityAlertSettings fromConfig() {
        return new EntityAlertSettings(
                CommonConfigs.MAX_DETECTION_RANGE.get(),
                CommonConfigs.DETECTION_HORIZONTAL_FOV.get(),
                CommonConfigs.DETECTION_VERTICAL_UP_FOV.get(),
                CommonConfigs.DETECTION_VERTICAL_DOWN_FOV.get(),
                false,
                List.of(),
                Map.of()
        );
    }

    public int getIntParam(String key, int defaultValue) {
        String val = params.get(key);
        if (val == null) return defaultValue;
        try {
            return Integer.parseInt(val);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public int getLogicInt(String logicName, String paramKey, int defaultValue) {
        String fullKey = StealthAndAlert.MOD_ID + ":" + logicName + "." + paramKey;
        String val = params.get(fullKey);
        if (val == null) return defaultValue;
        try {
            return Integer.parseInt(val);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public double getLogicDouble(String logicName, String paramKey, double defaultValue) {
        String fullKey = StealthAndAlert.MOD_ID + ":" + logicName + "." + paramKey;
        String val = params.get(fullKey);
        if (val == null) return defaultValue;
        try {
            return Double.parseDouble(val);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public boolean getLogicBool(String logicName, String paramKey, boolean defaultValue) {
        String fullKey = StealthAndAlert.MOD_ID + ":" + logicName + "." + paramKey;
        String val = params.get(fullKey);
        if (val == null) return defaultValue;
        try {
            return Boolean.parseBoolean(val);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public String getLogicString(String logicName, String paramKey, String defaultValue) {
        String fullKey = StealthAndAlert.MOD_ID + ":" + logicName + "." + paramKey;
        String val = params.get(fullKey);
        if (val == null) return defaultValue;
        return val;
    }

    public static final Codec<EntityAlertSettings> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.DOUBLE.optionalFieldOf("view_range", -1.0).forGetter(EntityAlertSettings::viewRange),
                    Codec.DOUBLE.optionalFieldOf("horizontal_fov", -1.0).forGetter(EntityAlertSettings::horizontalFov),
                    Codec.DOUBLE.optionalFieldOf("max_up_pitch", -1.0).forGetter(EntityAlertSettings::maxUpPitch),
                    Codec.DOUBLE.optionalFieldOf("max_down_pitch", -1.0).forGetter(EntityAlertSettings::maxDownPitch),
                    Codec.BOOL.optionalFieldOf("ignore_baby", false).forGetter(EntityAlertSettings::ignoreBaby),
                    Codec.STRING.listOf().optionalFieldOf("logic_list", List.of()).forGetter(EntityAlertSettings::logicList),
                    Codec.unboundedMap(Codec.STRING, Codec.STRING).optionalFieldOf("params", Map.of()).forGetter(EntityAlertSettings::params)
            ).apply(instance, EntityAlertSettings::new));

}
