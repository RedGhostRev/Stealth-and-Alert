package net.rev.stealthandalert.common.animation;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;
import net.rev.stealthandalert.attachment.AssassinationData;
import net.rev.stealthandalert.attachment.ModAttachments;
import net.rev.stealthandalert.network.S2CAssassinationPacket;

public class AssassinationSession {
    private final Player player;
    private final LivingEntity target;
    public final AssassinationScript script;

    private long currentTick = 0;
    private boolean isFinished = false;
    private final boolean isClient;

    public AssassinationSession(Player player, LivingEntity target, AssassinationScript script) {
        this.player = player;
        this.target = target;
        this.script = script;
        this.isClient = player.level().isClientSide();

        this.script.onStart(player, target);
    }

    public void tick() {
        if (isFinished) return;
        if (player == null || target == null || player.isRemoved() || !player.isAlive() || !player.getData(ModAttachments.ASSASSINATION_DATA).isAssassinating()) {
            abort();
            return;
        }
        if (currentTick < script.getTotalTicks()) {
            if (isClient) {
                script.onClientTick(player, target, currentTick);
            } else {
                script.onServerTick((ServerPlayer) player, target, currentTick);
            }
            currentTick++;
        } else {
            complete();
        }
    }

    private void complete() {
        script.onEnd(player, target);
        isFinished = true;
        AssassinationData newData = AssassinationData.getDefaultExceptHand(player.getData(ModAttachments.ASSASSINATION_DATA), player.getUUID());
        if (player.level().isClientSide()) return;
        player.setData(ModAttachments.ASSASSINATION_DATA, newData);
        PacketDistributor.sendToAllPlayers(new S2CAssassinationPacket(newData));
    }

    private void abort() {
        script.onEnd(player, target);
        isFinished = true;
        AssassinationData newData = AssassinationData.getDefaultExceptHand(player.getData(ModAttachments.ASSASSINATION_DATA), player.getUUID());
        if (player.level().isClientSide()) return;
        player.setData(ModAttachments.ASSASSINATION_DATA, newData);
        PacketDistributor.sendToAllPlayers(new S2CAssassinationPacket(newData));
    }

    public boolean isFinished() {
        return isFinished;
    }

    public boolean isClientSide() {
        return isClient;
    }
}
