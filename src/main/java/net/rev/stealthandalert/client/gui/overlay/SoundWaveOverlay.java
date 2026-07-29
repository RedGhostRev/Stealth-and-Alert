package net.rev.stealthandalert.client.gui.overlay;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.rev.stealthandalert.StealthAndAlert;
import net.rev.stealthandalert.compat.SupportedMods;
import net.rev.stealthandalert.compat.jade.JadeCompat;
import net.rev.stealthandalert.config.ClientConfigs;
import net.rev.stealthandalert.event.ModClientEvents;
import net.rev.stealthandalert.screen.custom.EditHudsScreen;

// TODO 改进 HUD
@EventBusSubscriber(modid = StealthAndAlert.MOD_ID, value = Dist.CLIENT)
public class SoundWaveOverlay {
    public static long lastRenderTime = 0;

    public static int lastSoundTick = 0;
    public static double targetAmplitude = 0.0;
    public static double renderAmplitude = 0.0;
    public static double timeTracker = 0.0;

    public static double tickMaxAmplitude = 0.0;
    public static boolean hasNewSoundThisTick = false;

    public static void receiveRawSound(double rawVolume) {
        double minVolume = 20.0;
        double maxVolume = 60.0;
        double clamped = Mth.clamp(rawVolume, minVolume, maxVolume);
        double normalized = (clamped - minVolume) / (maxVolume - minVolume);
        double scaledAmplitude = Math.log(1 + normalized * 9) / Math.log(10);

        if (scaledAmplitude <= 0.0) return;

        if (!hasNewSoundThisTick || scaledAmplitude > tickMaxAmplitude) {
            tickMaxAmplitude = scaledAmplitude;
            hasNewSoundThisTick = true;
        }
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        if (hasNewSoundThisTick) {
            targetAmplitude = tickMaxAmplitude;

            lastSoundTick = mc.player.tickCount;

            tickMaxAmplitude = 0.0;
            hasNewSoundThisTick = false;
        } else {
            int elapsedTicks = mc.player.tickCount - lastSoundTick;

            if (elapsedTicks >= 2) {
                if (targetAmplitude > 0.0) {
                    targetAmplitude -= 0.09;
                    if (targetAmplitude < 0.0) targetAmplitude = 0.0;
                }
            }
        }
    }

    public static void render(GuiGraphics graphics, DeltaTracker deltaTracker) {
        if (!ClientConfigs.SOUND_WAVE_INDICATOR.enable.get()) return;
        Minecraft mc = Minecraft.getInstance();
        if (mc.options.hideGui) return;
        LocalPlayer player = mc.player;
        if (player == null) return;
        if (player.isSpectator()) return;

        if (!mc.isPaused()) {
            long currentTime = System.currentTimeMillis();
            if (lastRenderTime == 0) lastRenderTime = currentTime;

            float deltaFrames = (currentTime - lastRenderTime) / 16.666f;
            lastRenderTime = currentTime;
            if (deltaFrames > 5.0f) deltaFrames = 5.0f;

            double baseSpeed = 0.1;
            double speedFactor = renderAmplitude > 0 ? (baseSpeed + renderAmplitude * 0.1) : 0.0;
            timeTracker += speedFactor * deltaFrames;

            float upSpeed = (float) (1.0 - Math.pow(1.0 - 0.3, deltaFrames));
            float downSpeed = (float) (1.0 - Math.pow(1.0 - 0.4, deltaFrames));

            if (targetAmplitude >= renderAmplitude) {
                renderAmplitude = Mth.lerp(upSpeed, renderAmplitude, targetAmplitude);
            } else {
                renderAmplitude = Mth.lerp(downSpeed, renderAmplitude, targetAmplitude);
            }

            if (renderAmplitude < 0.01) {
                renderAmplitude = 0.0;
            }
        } else {
            lastRenderTime = System.currentTimeMillis();
        }

        PoseStack poseStack = graphics.pose();
        float scale = ClientConfigs.SOUND_WAVE_INDICATOR.scale.get().floatValue(); // 缩放比例
        int screenWidth = graphics.guiWidth();
        int targetY = 30;

        poseStack.pushPose();

        poseStack.translate(screenWidth / 2.0F, (float) targetY, 0.0F);
        poseStack.scale(scale, scale, 1.0F);
        drawSoundWave(graphics);

        poseStack.popPose();
    }

    private static void drawSoundWave(GuiGraphics graphics) {
        int waveWidth = 112; // 横条长
        int waveHeight = 48; // 竖条最大高度

        int barWidth = 1;           // 横条宽
        int gap = 1;                // 竖条间接
        int totalBars = waveWidth / (barWidth + gap);

        double displayAmp = renderAmplitude > 0 ? renderAmplitude : 0.04;

        int baseColor = 0xFFFFFFFF; // 条色
        int borderColor = 0x337F7F7F; // 边框颜色（r:0.5, g:0.5, b:0.5, alpha:0.2）

        int localStartX = -waveWidth / 2 + ClientConfigs.SOUND_WAVE_INDICATOR.x.get();
        int middleY = -23 + ClientConfigs.SOUND_WAVE_INDICATOR.y.get();
        if (!(Minecraft.getInstance().screen instanceof EditHudsScreen)) {
            int maxYOffset = 0;
            float scale = ClientConfigs.SOUND_WAVE_INDICATOR.scale.get().floatValue();
            if (ClientConfigs.VISIBILITY_INDICATOR.canOffsetFromBossBar.get()) {
                if (ModClientEvents.bossbarShown) {
                    maxYOffset = ModClientEvents.bossbarHeight - 5;
                }
            }
            if (SupportedMods.JADE.isLoaded()) {
                if (ClientConfigs.VISIBILITY_INDICATOR.canOffsetFromJade.get()) {
                    if (JadeCompat.isJadeOverlayVisible()) {
                        int jadeHeight = JadeCompat.getJadeOverlayBottomY();
                        maxYOffset = Math.max(maxYOffset, jadeHeight + 5);
                    }
                }
            }
            if (maxYOffset > 0) {
                middleY = middleY + maxYOffset;
                middleY = ((int) (middleY / scale));
            }
        }

        graphics.fill(localStartX, middleY, localStartX + waveWidth, middleY + 1, baseColor);
        graphics.fill(localStartX, middleY - 1, localStartX + waveWidth, middleY, borderColor);
        graphics.fill(localStartX, middleY + 1, localStartX + waveWidth, middleY + 2, borderColor);
        graphics.fill(localStartX - 1, middleY - 1, localStartX, middleY + 2, borderColor);

        int[] dynamicHeights = new int[totalBars];
        for (int i = 0; i < totalBars; i++) {
            double progress = (double) i / totalBars;
            double randomSeed = Math.sin(i * 12.9898) * 43758.5453;
            double localFrequency = (randomSeed - Math.floor(randomSeed)) * 0.15 + 0.9;

            double rawNoise = (Math.sin(timeTracker * localFrequency + randomSeed) + 1.0) / 2.0;

            double microNoise = (randomSeed - Math.floor(randomSeed));
            double randomHeightFactor = Mth.lerp(0.4, rawNoise, microNoise);

            if (displayAmp < 0.25) {
                double fixWeight = displayAmp / 0.25;
                double noiseFlattenValue = 0.65;
                randomHeightFactor = Mth.lerp(fixWeight, noiseFlattenValue, randomHeightFactor);
            }

            double envelope = Math.sin(progress * Math.PI);
            dynamicHeights[i] = (int) (randomHeightFactor * envelope * (waveHeight / 2.0) * displayAmp);
        }

        for (int i = 0; i < totalBars; i++) {
            int dynamicHeight = dynamicHeights[i];

            if (dynamicHeight > 0) {
                int top = middleY - dynamicHeight;
                int bottom = (middleY + 1) + dynamicHeight;

                int xPos = localStartX + i * (barWidth + gap);

                graphics.fill(xPos, top - 1, xPos + barWidth, top, borderColor);
                graphics.fill(xPos, bottom, xPos + barWidth, bottom + 1, borderColor);

                graphics.fill(xPos - 1, top - 1, xPos, middleY - 1, borderColor);
                graphics.fill(xPos - 1, middleY + 2, xPos, bottom + 1, borderColor);

                int nextHeight = (i < totalBars - 1) ? dynamicHeights[i + 1] : 0;
                if (nextHeight < dynamicHeight) {
                    int nextTop = middleY - nextHeight;
                    int nextBottom = (middleY + 1) + nextHeight;

                    graphics.fill(xPos + barWidth, top - 1, xPos + barWidth + 1, nextTop - 1, borderColor);
                    graphics.fill(xPos + barWidth, nextBottom + 1, xPos + barWidth + 1, bottom + 1, borderColor);
                }

                graphics.fill(xPos, top, xPos + barWidth, middleY, baseColor);
                graphics.fill(xPos, middleY + 1, xPos + barWidth, bottom, baseColor);
            }
        }
        int rightEdge = localStartX + waveWidth;
        graphics.fill(rightEdge, middleY - 1, rightEdge + 1, middleY + 2, borderColor);
    }
}
