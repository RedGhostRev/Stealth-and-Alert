package net.rev.stealthandalert.event;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.rev.stealthandalert.StealthAndAlert;
import net.rev.stealthandalert.network.C2SBreakPacket;
import net.rev.stealthandalert.network.C2SSpeedPacket;

import java.lang.reflect.Field;

@EventBusSubscriber(modid = StealthAndAlert.MOD_ID, value = Dist.CLIENT)
public class StealthSoundClientEvents {
    private static Field destroyBlockPosField = null;

    @SubscribeEvent
    public static void onPlayerWalkOrSwim(PlayerTickEvent.Post event) {
        if (event.getEntity() instanceof LocalPlayer player) {
            double realMoveX = player.getX() - player.xo; // 当前X 减去 上一刻X
            double realMoveY = player.getY() - player.yo; // 当前Y 减去 上一刻Y
            double realMoveZ = player.getZ() - player.zo; // 当前Z 减去 上一刻Z

            if (player.isInWater() || player.isFallFlying()) {
                double speedPerTick = Math.sqrt(realMoveX * realMoveX + realMoveY * realMoveY + realMoveZ * realMoveZ);
                double speedPerSecond = speedPerTick * 20;

                if (speedPerSecond >= 0.5) {
                    PacketDistributor.sendToServer(new C2SSpeedPacket(speedPerSecond));
                }
            } else if (player.onGround()) {
                double speedPerTick = Math.sqrt(realMoveX * realMoveX + realMoveZ * realMoveZ);
                double speedPerSecond = speedPerTick * 20;

                if (speedPerSecond >= 0.5) {
                    PacketDistributor.sendToServer(new C2SSpeedPacket(speedPerSecond));
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerBreak(ClientTickEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || mc.player == null) return;
        MultiPlayerGameMode gameMode = mc.gameMode;
        if (gameMode != null) {
            if (gameMode.isDestroying()) {
                try {
                    if (destroyBlockPosField == null) {
                        destroyBlockPosField = MultiPlayerGameMode.class.getDeclaredField("destroyBlockPos");
                        destroyBlockPosField.setAccessible(true);
                    }
                    BlockPos pos = (BlockPos) destroyBlockPosField.get(gameMode);
                    if (pos != null) {
                        PacketDistributor.sendToServer(new C2SBreakPacket(pos));
                    }
                } catch (Exception e) {
                    StealthAndAlert.LOGGER.warn("Could not get destroyBlockPos", e);
                }
            }
        }
        destroyBlockPosField = null;
    }
}
