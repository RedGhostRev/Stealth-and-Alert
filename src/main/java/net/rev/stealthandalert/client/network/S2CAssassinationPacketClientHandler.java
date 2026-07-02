package net.rev.stealthandalert.client.network;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.rev.stealthandalert.attachment.AssassinationData;
import net.rev.stealthandalert.attachment.ModAttachments;
import net.rev.stealthandalert.client.animation.ClientAnimationVisuals;
import net.rev.stealthandalert.common.animation.*;
import net.rev.stealthandalert.network.S2CAssassinationPacket;

public class S2CAssassinationPacketClientHandler {
    public static void handle(S2CAssassinationPacket payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            AssassinationData newData = payload.data();
            if (newData.playerUUID().isEmpty()) return;

            Player player = context.player().level().getPlayerByUUID(newData.playerUUID().get());
            if (player == null) return;

            player.setData(ModAttachments.ASSASSINATION_DATA, newData);
            if (newData.animRL().equals(ModAnimations.EMPTY)) {
                ClientAnimationVisuals.INSTANCE.stopPlayerAnimation(player, newData.animRL());
                return;
            }
            LivingEntity target = findTargetEntity(player, newData);
            if (target == null) return;

            AssassinationScript clientScript = AssassinationScriptFactory.createScript(newData.animRL(), newData.hand());
            if (clientScript == null) return;

            AssassinationSession clientSession = new AssassinationSession(player, target, clientScript);
            AssassinationManager.startSession(clientSession);
        });
    }

    private static LivingEntity findTargetEntity(Player player, AssassinationData data) {
        return (LivingEntity) player.level().getEntity(data.targetId());
    }
}
