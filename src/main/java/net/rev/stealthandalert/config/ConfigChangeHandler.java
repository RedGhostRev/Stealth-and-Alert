package net.rev.stealthandalert.config;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.rev.stealthandalert.StealthAndAlert;
import net.rev.stealthandalert.client.renderer.ClientMarkManager;

@EventBusSubscriber(modid = StealthAndAlert.MOD_ID)
public class ConfigChangeHandler {
    private static boolean wasMarkEnabled = true;

    @SubscribeEvent
    public static void onConfigLoad(ModConfigEvent.Loading event) {
        if (event.getConfig().getSpec() == ClientConfigs.SPEC) {
            wasMarkEnabled = ClientConfigs.SPYGLASS_MARK.enable.get();
        }
    }

    @SubscribeEvent
    public static void onConfigReload(ModConfigEvent.Reloading event) {
        if (event.getConfig().getSpec() == ClientConfigs.SPEC) {
            boolean isNowEnabled = ClientConfigs.SPYGLASS_MARK.enable.get();
            if (wasMarkEnabled && !isNowEnabled) {
                ClientMarkManager.clear();
            }
            wasMarkEnabled = isNowEnabled;
        }
    }
}
