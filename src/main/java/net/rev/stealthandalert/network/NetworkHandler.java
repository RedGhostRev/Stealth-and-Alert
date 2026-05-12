package net.rev.stealthandalert.network;

import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.rev.stealthandalert.StealthAndAlert;
import net.rev.stealthandalert.attachment.ModAttachments;
import net.rev.stealthandalert.attachment.VisibilityData;

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

        registrar.playToClient(
                S2CVisibilityDataPacket.TYPE,
                S2CVisibilityDataPacket.STREAM_CODEC,
                (payload, context) -> context.enqueueWork(() -> {
                    Player player = context.player();
                    player.setData(ModAttachments.VISIBILITY_DATA, new VisibilityData(payload.visibility(), payload.isVisible()));
                })
        );

//        registrar.playToClient(
//                S2CCrawlPacket.TYPE,
//                S2CCrawlPacket.STREAM_CODEC,
//                (payload, context) -> context.enqueueWork(() -> {
//                    Player player = context.player();
//                    player.setData(ModAttachments.CRAWL_DATA, new CrawlData(payload.isCrawling()));
//                })
//        );
//
//        registrar.playToServer(
//                C2SCrawlPacket.TYPE,
//                C2SCrawlPacket.STREAM_CODEC,
//                ((payload, context) -> {
//                    Player player = context.player();
//                    player.setData(ModAttachments.CRAWL_DATA, new CrawlData(payload.isCrawling()));
//                })
//        );
    }
}
