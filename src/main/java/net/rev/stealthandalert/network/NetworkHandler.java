package net.rev.stealthandalert.network;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.rev.stealthandalert.StealthAndAlert;

@EventBusSubscriber(modid = StealthAndAlert.MOD_ID)
public class NetworkHandler {
    @SubscribeEvent
    public static void register(RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(StealthAndAlert.MOD_ID);
        registrar.playToClient(
                S2CAlertDataPacket.TYPE,
                S2CAlertDataPacket.STREAM_CODEC,
                S2CAlertDataPacket::handle
        );
    }
}
