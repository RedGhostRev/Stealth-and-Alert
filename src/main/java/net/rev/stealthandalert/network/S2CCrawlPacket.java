package net.rev.stealthandalert.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.rev.stealthandalert.StealthAndAlert;
import net.rev.stealthandalert.attachment.CrawlData;
import net.rev.stealthandalert.attachment.ModAttachments;

public record S2CCrawlPacket(boolean isCrawling) implements CustomPacketPayload {
    public static final Type<S2CCrawlPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(StealthAndAlert.MOD_ID, "s2c_crawl_packet"));

    public static final StreamCodec<ByteBuf, S2CCrawlPacket> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.BOOL, S2CCrawlPacket::isCrawling,
                    S2CCrawlPacket::new
            );

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(S2CCrawlPacket payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();
            player.setData(ModAttachments.CRAWL_DATA, new CrawlData(payload.isCrawling()));
        });
    }
}
