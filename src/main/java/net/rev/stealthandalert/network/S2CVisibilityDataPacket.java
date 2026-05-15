package net.rev.stealthandalert.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.rev.stealthandalert.StealthAndAlert;
import net.rev.stealthandalert.attachment.ModAttachments;
import net.rev.stealthandalert.attachment.VisibilityData;

public record S2CVisibilityDataPacket(float visibility, boolean isVisible) implements CustomPacketPayload {
    public static final Type<S2CVisibilityDataPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(StealthAndAlert.MOD_ID, "s2c_visibility_data_packet"));

    public static final StreamCodec<ByteBuf, S2CVisibilityDataPacket> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.FLOAT, S2CVisibilityDataPacket::visibility,
                    ByteBufCodecs.BOOL, S2CVisibilityDataPacket::isVisible,
                    S2CVisibilityDataPacket::new
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(S2CVisibilityDataPacket payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();
            player.setData(ModAttachments.VISIBILITY_DATA, new VisibilityData(payload.visibility(), payload.isVisible()));
        });
    }
}
