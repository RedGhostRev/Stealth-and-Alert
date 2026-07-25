package net.rev.stealthandalert.common.alert.condition;

import com.google.gson.JsonElement;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;

import java.util.List;
import java.util.Map;

public interface IAlertCondition {
    /*
    * @param params 该条件对应的参数 Map（例如 {"threshold": 11, "invert": false}）
    * */
    boolean test(Mob mob, Player player, Map<String, JsonElement> params);

    default int getInt(Map<String, JsonElement> params, String key, int defaultValue) {
        JsonElement el = params.get(key);
        return (el != null && el.isJsonPrimitive() ? el.getAsInt() : defaultValue);
    }

    default double getDouble(Map<String, JsonElement> params, String key, double defaultValue) {
        JsonElement el = params.get(key);
        return (el != null && el.isJsonPrimitive()) ? el.getAsDouble() : defaultValue;
    }

    default boolean getBool(Map<String, JsonElement> params, String key, boolean defaultValue) {
        JsonElement el = params.get(key);
        return (el != null && el.isJsonPrimitive()) ? el.getAsBoolean() : defaultValue;
    }

    default String getString(Map<String, JsonElement> params, String key, String defaultValue) {
        JsonElement el = params.get(key);
        return (el != null && el.isJsonPrimitive()) ? el.getAsString() : defaultValue;
    }

    default List<String> getStringList(Map<String, JsonElement> params, String key) {
        JsonElement el = params.get(key);
        if (el != null && el.isJsonArray()) {
            return el.getAsJsonArray().asList().stream()
                    .map(JsonElement::getAsString)
                    .toList();
        }
        return List.of();
    }
}
