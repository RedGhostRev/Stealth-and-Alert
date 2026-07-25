package net.rev.stealthandalert.common.alert.condition;

import net.minecraft.resources.ResourceLocation;
import net.rev.stealthandalert.StealthAndAlert;

import java.util.HashMap;
import java.util.Map;

public class AlertConditionRegistry {
    private static final Map<ResourceLocation, IAlertCondition> REGISTRY = new HashMap<>();

    public static void register(ResourceLocation id, IAlertCondition logic) {
        REGISTRY.put(id, logic);
        StealthAndAlert.LOGGER.info("已注册条件：{}", id);
    }

    public static IAlertCondition get(ResourceLocation id) {
        return REGISTRY.get(id);
    }
}
