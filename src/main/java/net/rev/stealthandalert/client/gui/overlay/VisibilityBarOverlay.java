package net.rev.stealthandalert.client.gui.overlay;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.BossHealthOverlay;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.rev.stealthandalert.StealthAndAlert;
import net.rev.stealthandalert.attachment.ModAttachments;
import net.rev.stealthandalert.attachment.VisibilityData;
import net.rev.stealthandalert.config.ClientConfigs;
import net.rev.stealthandalert.util.StealthUtils;


public class VisibilityBarOverlay {
    private static final ResourceLocation VISIBILITY_BAR = ResourceLocation.fromNamespaceAndPath(StealthAndAlert.MOD_ID,
            "textures/gui/visibility_bar.png");
    private static final ResourceLocation VISIBILITY_EYE = ResourceLocation.fromNamespaceAndPath(StealthAndAlert.MOD_ID,
            "textures/gui/visibility_eye.png");
    private static final ResourceLocation VISIBILITY_SLASH = ResourceLocation.fromNamespaceAndPath(StealthAndAlert.MOD_ID,
            "textures/gui/visibility_slash.png");
    private static final ResourceLocation VISIBILITY_BAR_FRAME = ResourceLocation.fromNamespaceAndPath(StealthAndAlert.MOD_ID,
            "textures/gui/visibility_bar_frame.png");
    private static final ResourceLocation VISIBILITY_EYE_FRAME = ResourceLocation.fromNamespaceAndPath(StealthAndAlert.MOD_ID,
            "textures/gui/visibility_eye_frame.png");
    private static final ResourceLocation VISIBILITY_SLASH_FRAME = ResourceLocation.fromNamespaceAndPath(StealthAndAlert.MOD_ID,
            "textures/gui/visibility_slash_frame.png");
    private static float displayedVisibility = 0F;

    public static void render(GuiGraphics graphics, DeltaTracker deltaTracker) {
        if (!ClientConfigs.VISIBILITY_INDICATOR.get()) return;
        Minecraft mc = Minecraft.getInstance();
        if (mc.options.hideGui) return;
        if (mc.player == null) return;
        VisibilityData data = mc.player.getData(ModAttachments.VISIBILITY_DATA);
        float visibility = 0;
        if (!mc.isPaused()) {
            visibility = data.visibility();
            float lerpSpeed = 0.01F;

            displayedVisibility = Mth.lerp(lerpSpeed, displayedVisibility, visibility);
        }


        double centerX = graphics.guiWidth() / 2.0;
        double centerY = 30;

        int width = 38;
        int imageSize = 128;
        int uOffsetMid = imageSize / 2 - width / 2;
        int x = -width / 2 + ClientConfigs.VISIBILITY_INDICATOR_POSITION.get().getFirst();
        int y = -imageSize / 2 +4 + ClientConfigs.VISIBILITY_INDICATOR_POSITION.get().getLast();
        BossHealthOverlay bossOverlay = mc.gui.getBossOverlay();
        if (ClientConfigs.VISIBILITY_INDICATOR_CAN_OFFSET_FROM_BOSS_BAR.get() && bossOverlay != null) {
            try {
                if (SoundWaveOverlay.bossEventsField == null) {
                    SoundWaveOverlay.bossEventsField = BossHealthOverlay.class.getDeclaredField("events");
                    SoundWaveOverlay.bossEventsField.setAccessible(true);
                }

                java.util.Map<?, ?> eventsMap = (java.util.Map<?, ?>) SoundWaveOverlay.bossEventsField.get(bossOverlay);

                if (eventsMap != null && !eventsMap.isEmpty()) {
                    int bossCount = eventsMap.size();

                    int allBossBarsHeight = 12 + (bossCount - 1) * 30;
                    y = y + allBossBarsHeight + 5;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        graphics.flush();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        PoseStack pose = graphics.pose();
        pose.pushPose();
        pose.translate(centerX, centerY, 0F);
        float scale = ClientConfigs.VISIBILITY_INDICATOR_SCALE.get().floatValue();
        pose.scale(scale, scale, 1.0F);

        if (displayedVisibility > StealthUtils.VISIBILITY_THRESHOLD) {
            graphics.blit(VISIBILITY_BAR, x, y, uOffsetMid, 0, width, imageSize, imageSize, imageSize);

            int uOffsetLeft = 8;
            int uOffsetRight = 83;
            graphics.setColor(0F, 0F, 0F, 0.5F);
            graphics.blit(VISIBILITY_BAR, x - 37, y, uOffsetLeft, 0, 37, imageSize, imageSize, imageSize);
            graphics.blit(VISIBILITY_BAR, x + width, y, uOffsetRight, 0, 37, imageSize, imageSize, imageSize);
            graphics.setColor(1F, 1F, 1F, 1F);

            pose.pushPose();
            pose.translate(0F, 0F, 0.1F);
            // float ease = 1 - (1 - displayedVisibility) * (1 - displayedVisibility);
            int currentWidth = ((int) (37 * Math.clamp(displayedVisibility - StealthUtils.VISIBILITY_THRESHOLD, 0F, 1F - StealthUtils.VISIBILITY_THRESHOLD) / (1F - StealthUtils.VISIBILITY_THRESHOLD)));
            if (visibility >= 0.99F && Math.abs(displayedVisibility - 1F) < 0.02F) {
                currentWidth = 37;
            }
            graphics.blit(VISIBILITY_BAR, x + width, y, uOffsetLeft, 0, currentWidth, imageSize, imageSize, imageSize);
            graphics.blit(VISIBILITY_BAR, x - currentWidth, y, uOffsetRight, 0, currentWidth, imageSize, imageSize, imageSize);

            graphics.setColor(0.5F, 0.5F, 0.5F, 0.2F);
            graphics.blit(VISIBILITY_BAR_FRAME, x - 38, y, uOffsetLeft - 1, 0, 114, imageSize, imageSize, imageSize);
            graphics.setColor(1F, 1F, 1F, 1F);
            pose.popPose();
        }

        int uOffsetEye = 53;
        graphics.blit(VISIBILITY_EYE, x + 8, y, uOffsetEye, 0, 22, imageSize, imageSize, imageSize);
        graphics.setColor(0.5F, 0.5F, 0.5F, 0.2F);
        graphics.blit(VISIBILITY_EYE_FRAME, x + 7, y, uOffsetEye - 1, 0, 24, imageSize, imageSize, imageSize);
        graphics.setColor(1F, 1F, 1F, 1F);

        if (displayedVisibility <= StealthUtils.VISIBILITY_THRESHOLD) {
            pose.pushPose();
            pose.translate(0F, 0F, 0.1F);
            int uOffsetSlash = 57;
            graphics.blit(VISIBILITY_SLASH, x + 12, y, uOffsetSlash, 0, 14, imageSize, imageSize, imageSize);
            graphics.setColor(0.5F, 0.5F, 0.5F, 0.2F);
            graphics.blit(VISIBILITY_SLASH_FRAME, x + 11, y, uOffsetSlash - 1, 0, 16, imageSize, imageSize, imageSize);
            graphics.setColor(1F, 1F, 1F, 1F);
            pose.popPose();
        }

        graphics.flush();
        RenderSystem.disableBlend();
        pose.popPose();
    }
}
