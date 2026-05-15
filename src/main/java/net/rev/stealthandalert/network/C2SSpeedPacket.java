package net.rev.stealthandalert.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.rev.stealthandalert.StealthAndAlert;

public record C2SSpeedPacket(double speed) implements CustomPacketPayload {
    public static final Type<C2SSpeedPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(StealthAndAlert.MOD_ID, "c2s_speed_packet"));

    public static final StreamCodec<ByteBuf, C2SSpeedPacket> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.DOUBLE, C2SSpeedPacket::speed,
                    C2SSpeedPacket::new
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
