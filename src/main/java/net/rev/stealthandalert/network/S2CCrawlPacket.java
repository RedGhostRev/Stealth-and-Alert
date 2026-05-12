package net.rev.stealthandalert.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.rev.stealthandalert.StealthAndAlert;

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
}
