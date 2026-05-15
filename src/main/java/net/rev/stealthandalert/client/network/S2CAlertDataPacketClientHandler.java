package net.rev.stealthandalert.client.network;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.rev.stealthandalert.attachment.ModAttachments;
import net.rev.stealthandalert.network.S2CAlertDataPacket;

public class S2CAlertDataPacketClientHandler {
    public static void handle(S2CAlertDataPacket payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            Level level = Minecraft.getInstance().level;
            if (level != null && level.getEntity(payload.entityId()) instanceof Mob mob) {
                mob.setData(ModAttachments.ALERT_DATA, payload.data());
            }
        });
    }
}
