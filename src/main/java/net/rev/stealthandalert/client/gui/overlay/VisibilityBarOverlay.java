package net.rev.stealthandalert.client.gui.overlay;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.rev.stealthandalert.StealthAndAlert;
import net.rev.stealthandalert.attribute.ModAttributes;
import net.rev.stealthandalert.compat.CompatHandler;
import net.rev.stealthandalert.compat.jade.JadeCompat;
import net.rev.stealthandalert.config.ClientConfigs;
import net.rev.stealthandalert.event.ModClientEvents;
import net.rev.stealthandalert.screen.custom.EditHudsScreen;
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
    public static double displayedVisibility = 0;

    public static void render(GuiGraphics graphics, DeltaTracker deltaTracker) {
        if (!ClientConfigs.VISIBILITY_INDICATOR.turnOn.get()) return;
        Minecraft mc = Minecraft.getInstance();
        if (mc.options.hideGui) return;
        if (mc.player == null) return;
        if (mc.player.isSpectator()) return;
        double visibility = 0;
        if (!mc.isPaused()) {
            visibility = mc.player.getAttributeValue(ModAttributes.VISIBILITY);
            double lerpSpeed = 0.01;

            displayedVisibility = Mth.lerp(lerpSpeed, displayedVisibility, visibility);
        }


        double centerX = graphics.guiWidth() / 2.0;
        double centerY = 30;

        int width = 38;
        int imageSize = 128;
        int uOffsetMid = imageSize / 2 - width / 2;
        int x = -width / 2 + ClientConfigs.VISIBILITY_INDICATOR.x.get();
        int y = -imageSize / 2 + 4 + ClientConfigs.VISIBILITY_INDICATOR.y.get();
        if (!(mc.screen instanceof EditHudsScreen)) {
            int maxYOffset = 0;
            float scale = ClientConfigs.VISIBILITY_INDICATOR.scale.get().floatValue();
            if (ClientConfigs.VISIBILITY_INDICATOR.canOffsetFromBossBar.get()) {
                if (ModClientEvents.bossbarShown) {
                    maxYOffset = ModClientEvents.bossbarHeight - 5;
                }
            }
            if (CompatHandler.HAS_JADE) {
                if (ClientConfigs.VISIBILITY_INDICATOR.canOffsetFromJade.get()) {
                    if (JadeCompat.isJadeOverlayVisible()) {
                        int jadeHeight = JadeCompat.getJadeOverlayBottomY();
                        maxYOffset = Math.max(maxYOffset, jadeHeight + 5);
                    }
                }
            }
            if (maxYOffset > 0) {
                y = y + maxYOffset + 10;
                y = ((int) (y / scale));
            }
        }

        graphics.flush();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        PoseStack pose = graphics.pose();
        pose.pushPose();
        pose.translate(centerX, centerY, 0F);
        float scale = ClientConfigs.VISIBILITY_INDICATOR.scale.get().floatValue();
        pose.scale(scale, scale, 1.0F);

        if (displayedVisibility > StealthUtils.VISIBILITY_THRESHOLD + 0.01) {
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

        if (displayedVisibility <= StealthUtils.VISIBILITY_THRESHOLD + 0.01) {
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
