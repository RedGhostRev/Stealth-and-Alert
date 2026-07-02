package net.rev.stealthandalert.network;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.rev.stealthandalert.StealthAndAlert;
import net.rev.stealthandalert.attachment.AlertSoundData;
import net.rev.stealthandalert.attachment.CrawlData;
import net.rev.stealthandalert.attachment.ModAttachments;
import net.rev.stealthandalert.client.network.S2CAlertDataPacketClientHandler;
import net.rev.stealthandalert.client.network.S2CAssassinationPacketClientHandler;
import net.rev.stealthandalert.event.StealthSoundEvent;
import net.rev.stealthandalert.util.SpeedHandler;

@EventBusSubscriber(modid = StealthAndAlert.MOD_ID)
public class NetworkHandler {
    public static IEventBus bus = NeoForge.EVENT_BUS;

    @SubscribeEvent
    public static void register(RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(StealthAndAlert.MOD_ID);

        registrar.playToClient(
                S2CAlertDataPacket.TYPE,
                S2CAlertDataPacket.STREAM_CODEC,
                ((payload, context) -> {
                    S2CAlertDataPacketClientHandler.handle(payload, context);
                })
        );
        registrar.playToClient(
                S2CVisibilityDataPacket.TYPE,
                S2CVisibilityDataPacket.STREAM_CODEC,
                S2CVisibilityDataPacket::handle
        );

        registrar.playToClient(
                S2CCrawlPacket.TYPE,
                S2CCrawlPacket.STREAM_CODEC,
                S2CCrawlPacket::handle
        );

        registrar.playToClient(
                S2CSoundPacket.TYPE,
                S2CSoundPacket.STREAM_CODEC,
                S2CSoundPacket::handle
        );

        registrar.playToClient(
                S2CAssassinationPacket.TYPE,
                S2CAssassinationPacket.STREAM_CODEC,
                (payload, context) ->
                        S2CAssassinationPacketClientHandler.handle(payload, context)
        );

        registrar.playToServer(
                C2SCrawlPacket.TYPE,
                C2SCrawlPacket.STREAM_CODEC,
                ((payload, context) -> {
                    Player player = context.player();
                    player.setData(ModAttachments.CRAWL_DATA, new CrawlData(payload.isCrawling()));
                })
        );

        registrar.playToServer(
                C2SSpeedPacket.TYPE,
                C2SSpeedPacket.STREAM_CODEC,
                (((payload, context) -> {
                    double speedPerSecond = payload.speed();
                    SpeedHandler.updateSpeed(context.player().getUUID(), speedPerSecond);
                    if (context.player() instanceof ServerPlayer player) {
                        if (player.isInWater()) {
                            if (speedPerSecond >= 0.5) {
                                if (speedPerSecond <= 1.5) {
                                    sendAndPostSound(StealthSoundEvent.Type.PLAYER_SELF, player, 30.0, 1.6, AlertSoundData.LOW);
                                } else if (speedPerSecond <= 2.5) {
                                    sendAndPostSound(StealthSoundEvent.Type.PLAYER_SELF, player, 35.0, 3.0, AlertSoundData.LOW);
                                } else if (speedPerSecond <= 3.5) {
                                    sendAndPostSound(StealthSoundEvent.Type.PLAYER_SELF, player, 40.0, 4.5, AlertSoundData.LOW);
                                } else if (speedPerSecond <= 4.2) {
                                    sendAndPostSound(StealthSoundEvent.Type.PLAYER_SELF, player, 47.0, 5.5, AlertSoundData.MEDIUM);
                                } else {
                                    sendAndPostSound(StealthSoundEvent.Type.PLAYER_SELF, player, 55.0, 8.0, AlertSoundData.MEDIUM);
                                }
                            }
                        } else if (player.isFallFlying()) {
                            if (speedPerSecond >= 8.0) {
                                if (speedPerSecond <= 25.0) {
                                    sendAndPostSound(StealthSoundEvent.Type.PLAYER_SELF, player, 47.0, 7.0, AlertSoundData.LOW);
                                } else if (speedPerSecond <= 30.0) {
                                    sendAndPostSound(StealthSoundEvent.Type.PLAYER_SELF, player, 58.0, 12.0, AlertSoundData.MEDIUM);
                                } else if (speedPerSecond <= 33.0) {
                                    sendAndPostSound(StealthSoundEvent.Type.PLAYER_SELF, player, 64.0, 15.0, AlertSoundData.MEDIUM);
                                } else {
                                    sendAndPostSound(StealthSoundEvent.Type.PLAYER_SELF, player, 70.0, 20.0, AlertSoundData.HIGH);
                                }
                            }
                        } else if (player.onGround()) {
                            if (speedPerSecond >= 0.5) {
                                if (speedPerSecond <= 1.5) {
                                    sendAndPostSound(StealthSoundEvent.Type.PLAYER_SELF, player, 24.0, 0.8, AlertSoundData.LOW);
                                } else if (speedPerSecond <= 2.5) {
                                    sendAndPostSound(StealthSoundEvent.Type.PLAYER_SELF, player, 27.0, 1.5, AlertSoundData.LOW);
                                } else if (speedPerSecond <= 5.0) {
                                    sendAndPostSound(StealthSoundEvent.Type.PLAYER_SELF, player, 35.0, 5.5, AlertSoundData.LOW);
                                } else if (speedPerSecond <= 6.0) {
                                    sendAndPostSound(StealthSoundEvent.Type.PLAYER_SELF, player, 42.0, 7.5, AlertSoundData.MEDIUM);
                                } else {
                                    sendAndPostSound(StealthSoundEvent.Type.PLAYER_SELF, player, 50.0, 8.5, AlertSoundData.MEDIUM);
                                }
                            }
                        }
                    }
                }))
        );

        registrar.playToServer(
                C2SBreakPacket.TYPE,
                C2SBreakPacket.STREAM_CODEC,
                ((payload, context) -> {
                    if (context.player() instanceof ServerPlayer player) {
                        PacketDistributor.sendToPlayer(player, new S2CSoundPacket(36.0));
                        if (player.level().getGameTime() % 8 == 0) {
                            bus.post(new StealthSoundEvent(StealthSoundEvent.Type.PLAYER_SELF, payload.pos().getCenter(), player, 36.0, 5.5, AlertSoundData.LOW));
                        }
                    }
                })
        );

        registrar.playToServer(
                C2SAssassinationPacket.TYPE,
                C2SAssassinationPacket.STREAM_CODEC,
                C2SAssassinationPacket::handle
        );
    }

    private static void sendAndPostSound(StealthSoundEvent.Type type, ServerPlayer player, double volume, double radius, int threatLevel) {
        PacketDistributor.sendToPlayer(player, new S2CSoundPacket(volume));
        if (player.level().getGameTime() % 20 != 0) return;
        bus.post(new StealthSoundEvent(type, player.position(), player, volume, radius, threatLevel));
    }
}
