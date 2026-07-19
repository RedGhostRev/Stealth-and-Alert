package net.rev.stealthandalert.common.alert.condition;

import net.minecraft.resources.ResourceLocation;
import net.rev.stealthandalert.StealthAndAlert;

public class ModAlertConditions {
    private static final String MOD_ID = StealthAndAlert.MOD_ID;

    public static void registerAll() {
        AlertConditionRegistry.register(
                ResourceLocation.fromNamespaceAndPath(MOD_ID, "fight_back"),
                new FightBackCondition()
        );
        AlertConditionRegistry.register(
                ResourceLocation.fromNamespaceAndPath(MOD_ID, "protect_others"),
                new ProtectOthersCondition()
        );
        AlertConditionRegistry.register(
                ResourceLocation.fromNamespaceAndPath(MOD_ID, "light_sensitive"),
                new LightSensitiveCondition()
        );
        AlertConditionRegistry.register(
                ResourceLocation.fromNamespaceAndPath(MOD_ID, "eye_contact"),
                new EyeContactCondition()
        );
        AlertConditionRegistry.register(
                ResourceLocation.fromNamespaceAndPath(MOD_ID, "close_to_child"),
                new CloseToChildCondition()
        );
        AlertConditionRegistry.register(
                ResourceLocation.fromNamespaceAndPath(MOD_ID, "item_wearing"),
                new ItemWearingCondition()
        );
        AlertConditionRegistry.register(
                ResourceLocation.fromNamespaceAndPath(MOD_ID, "action_on_container"),
                new ActionOnContainerCondition()
        );
        AlertConditionRegistry.register(
                ResourceLocation.fromNamespaceAndPath(MOD_ID, "village_reputation"),
                new VillageReputationCondition()
        );

        StealthAndAlert.LOGGER.info("成功注册了所有条件！");
    }
}
