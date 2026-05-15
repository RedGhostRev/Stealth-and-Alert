package net.rev.stealthandalert.event;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.rev.stealthandalert.StealthAndAlert;
import net.rev.stealthandalert.util.StealthUtils;

@EventBusSubscriber(modid = StealthAndAlert.MOD_ID)
public class StealthSoundEventHandler {
    @SubscribeEvent
    public static void onStealthSound(StealthSoundEvent event) {
        if (event.soundSource instanceof ServerPlayer player && !player.isCreative() && !player.isSpectator()) {
            StealthUtils.reactToSound(event);
        }
    }
}
