package net.rev.stealthandalert.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.rev.stealthandalert.StealthAndAlert;

public record C2SCrawlPacket(boolean isCrawling) implements CustomPacketPayload {
    public static final Type<C2SCrawlPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(StealthAndAlert.MOD_ID, "c2s_crawl_packet"));

    public static final StreamCodec<ByteBuf, C2SCrawlPacket> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.BOOL, C2SCrawlPacket::isCrawling,
                    C2SCrawlPacket::new
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
