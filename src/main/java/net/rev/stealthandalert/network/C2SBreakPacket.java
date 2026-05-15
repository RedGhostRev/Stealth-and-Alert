package net.rev.stealthandalert.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.rev.stealthandalert.StealthAndAlert;


public record C2SBreakPacket(BlockPos pos) implements CustomPacketPayload {
    public static final Type<C2SBreakPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(StealthAndAlert.MOD_ID, "c2s_break_packet"));

    public static final StreamCodec<ByteBuf, C2SBreakPacket> STREAM_CODEC =
            StreamCodec.composite(
                    BlockPos.STREAM_CODEC, C2SBreakPacket::pos,
                    C2SBreakPacket::new
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
