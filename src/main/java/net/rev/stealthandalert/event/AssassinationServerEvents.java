package net.rev.stealthandalert.event;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.rev.stealthandalert.StealthAndAlert;
import net.rev.stealthandalert.attachment.AssassinationData;
import net.rev.stealthandalert.attachment.ModAttachments;
import net.rev.stealthandalert.common.animation.AssassinationManager;
import net.rev.stealthandalert.network.S2CAssassinationPacket;
import net.rev.stealthandalert.util.AssassinationHandler;

@EventBusSubscriber(modid = StealthAndAlert.MOD_ID)
public class AssassinationServerEvents {
    private static int cleanupTimer = 0;

    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {
        AssassinationManager.onServerGameTick();

        cleanupTimer++;
        if (cleanupTimer >= 20) {
            cleanupTimer = 0;
            MinecraftServer server = event.getServer();
            for (ServerLevel level : server.getAllLevels()) {
                AssassinationHandler.cleanupExpiredLocks(level);
            }
        }
    }

    @SubscribeEvent
    public static void onStartTracking(PlayerEvent.StartTracking event) {
        Entity target = event.getTarget();
        if (target.level().isClientSide()) return;
        if (target instanceof Player player) {
            AssassinationData data = player.getData(ModAttachments.ASSASSINATION_DATA);
            if (data.isAssassinating()) {
                ServerPlayer observer = ((ServerPlayer) event.getEntity());
                PacketDistributor.sendToPlayer(observer, new S2CAssassinationPacket(data));
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerDamage(LivingIncomingDamageEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (player.level().isClientSide()) return;
            if (player.getData(ModAttachments.ASSASSINATION_DATA).isAssassinating()) {
                event.setCanceled(true);
            }
        }
    }
}
