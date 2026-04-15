package net.rev.stealthandalert.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.rev.stealthandalert.StealthAndAlert;
import net.rev.stealthandalert.attachment.AlertData;
import net.rev.stealthandalert.attachment.ModAttachments;

public record S2CAlertDataPacket(int entityId, AlertData data) implements CustomPacketPayload {
    public static final Type<S2CAlertDataPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(StealthAndAlert.MOD_ID, "s2c_alert_data_packet"));

    public static final StreamCodec<ByteBuf, S2CAlertDataPacket> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.VAR_INT, S2CAlertDataPacket::entityId,
                    AlertData.STREAM_CODEC, S2CAlertDataPacket::data,
                    S2CAlertDataPacket::new
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(S2CAlertDataPacket payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            Level level = Minecraft.getInstance().level;
            if (level != null && level.getEntity(payload.entityId()) instanceof Mob mob) {
                mob.setData(ModAttachments.ALERT_DATA, payload.data());
            }
        });
    }
}
