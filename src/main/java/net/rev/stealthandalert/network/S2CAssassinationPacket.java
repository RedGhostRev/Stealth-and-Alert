package net.rev.stealthandalert.network;

import net.minecraft.core.UUIDUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;
import net.rev.stealthandalert.StealthAndAlert;
import net.rev.stealthandalert.attachment.AssassinationData;
import net.rev.stealthandalert.util.AssassinationHandler;

public record S2CAssassinationPacket(AssassinationData data) implements CustomPacketPayload {
    public static final Type<S2CAssassinationPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(
            StealthAndAlert.MOD_ID, "s2c_assassination_packet"
    ));

    public static final StreamCodec<FriendlyByteBuf, S2CAssassinationPacket> STREAM_CODEC =
            StreamCodec.composite(
                    StreamCodec.composite(
                            ByteBufCodecs.optional(UUIDUtil.STREAM_CODEC), AssassinationData::playerUUID,
                            ByteBufCodecs.VAR_INT, AssassinationData::targetId,
                            ByteBufCodecs.BOOL, AssassinationData::isAssassinating,
                            ByteBufCodecs.VAR_LONG, AssassinationData::startTick,
                            ResourceLocation.STREAM_CODEC, AssassinationData::animRL,
                            NeoForgeStreamCodecs.enumCodec(AssassinationHandler.AssassinateHand.class), AssassinationData::hand,
                            AssassinationData::new
                    ), S2CAssassinationPacket::data,
                    S2CAssassinationPacket::new
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
