package net.rev.stealthandalert.screen.custom;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.rev.stealthandalert.client.gui.overlay.SoundWaveOverlay;
import net.rev.stealthandalert.client.gui.overlay.VisibilityBarOverlay;
import net.rev.stealthandalert.config.ClientConfigs;
import org.jetbrains.annotations.NotNull;

public class EditHudsScreen extends Screen {
    private enum EditTarget {
        NONE,
        VISIBILITY_MOVE,
        VISIBILITY_SCALE,
        SOUND_WAVE_MOVE,
        SOUND_WAVE_SCALE
    }

    public EditHudsScreen() {
        super(Component.literal("Edit HUDs"));
    }

    private EditTarget activeTarget = EditTarget.NONE;

    private double visX, visY;
    private double soundX, soundY;

    private double initialMouseX, initialMouseY;
    private double initialScale;
    private double initialTargetX, initialTargetY;

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        VisibilityBarOverlay.displayedVisibility = 1.0;
        VisibilityBarOverlay.render(guiGraphics, null);

        SoundWaveOverlay.renderAmplitude = 0.8;
        SoundWaveOverlay.render(guiGraphics, null);

        drawHitboxes(guiGraphics);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            double centerX = this.width / 2.0;
            double centerY = 30.0;

            // ==================== 可见度指示器 ====================
            double visScale = ClientConfigs.VISIBILITY_INDICATOR.scale.get();
            double visLocalX = (mouseX - centerX) / visScale;
            double visLocalY = (mouseY - centerY) / visScale;

            int visConfigX = ClientConfigs.VISIBILITY_INDICATOR.x.get();
            int visConfigY = ClientConfigs.VISIBILITY_INDICATOR.y.get();

            double visLeft = -57 + visConfigX;
            double visRight = visLeft + 114;
            double visTop = -10 + visConfigY;
            double visBottom = visTop + 28;

            double visHandleLeft = visRight - 12;
            double visHandleTop = visBottom - 12;

            if (visLocalX >= visHandleLeft && visLocalX <= visRight && visLocalY >= visHandleTop && visLocalY <= visBottom) {
                this.activeTarget = EditTarget.VISIBILITY_SCALE;
                this.initialMouseX = mouseX;
                this.initialMouseY = mouseY;
                this.initialScale = visScale;
                this.initialTargetX = visConfigX;
                this.initialTargetY = visConfigY;
                return true;
            } else if (visLocalX >= visLeft && visLocalX <= visRight && visLocalY >= visTop && visLocalY <= visBottom) {
                this.activeTarget = EditTarget.VISIBILITY_MOVE;
                this.visX = visConfigX;
                this.visY = visConfigY;
                return true;
            }

            // ==================== 声波指示器====================
            double soundScale = ClientConfigs.SOUND_WAVE_INDICATOR.scale.get();
            double soundLocalX = (mouseX - centerX) / soundScale;
            double soundLocalY = (mouseY - centerY) / soundScale;

            int soundConfigX = ClientConfigs.SOUND_WAVE_INDICATOR.x.get();
            int soundConfigY = ClientConfigs.SOUND_WAVE_INDICATOR.y.get();

            double soundLeft = -57 + soundConfigX;
            double soundRight = soundLeft + 114;
            double soundTop = -47 + soundConfigY;
            double soundBottom = soundTop + 48;

            double soundHandleLeft = soundRight - 12;
            double soundHandleTop = soundBottom - 12;

            if (soundLocalX >= soundHandleLeft && soundLocalX <= soundRight && soundLocalY >= soundHandleTop && soundLocalY <= soundBottom) {
                this.activeTarget = EditTarget.SOUND_WAVE_SCALE;
                this.initialMouseX = mouseX;
                this.initialMouseY = mouseY;
                this.initialScale = soundScale;
                this.initialTargetX = soundConfigX;
                this.initialTargetY = soundConfigY;
                return true;
            } else if (soundLocalX >= soundLeft && soundLocalX <= soundRight && soundLocalY >= soundTop && soundLocalY <= soundBottom) {
                this.activeTarget = EditTarget.SOUND_WAVE_MOVE;
                this.soundX = soundConfigX;
                this.soundY = soundConfigY;
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (button == 0) {
            if (activeTarget == EditTarget.VISIBILITY_MOVE) {
                double scale = ClientConfigs.VISIBILITY_INDICATOR.scale.get();
                this.visX += (dragX / scale);
                this.visY += (dragY / scale);
                ClientConfigs.VISIBILITY_INDICATOR.x.set((int) Math.round(this.visX));
                ClientConfigs.VISIBILITY_INDICATOR.y.set((int) Math.round(this.visY));
                return true;
            } else if (activeTarget == EditTarget.VISIBILITY_SCALE) {
                double totalDeltaX = mouseX - initialMouseX;
                double totalDeltaY = mouseY - initialMouseY;

                int initialCents = (int) Math.round(initialScale * 100.0);
                int deltaCents = (int) Math.round((totalDeltaX + totalDeltaY) / 5.0);
                int newCents = Mth.clamp(initialCents + deltaCents, 20, 200); // 0.20 ~ 2.00

                double newScale = newCents / 100.0;

                double newX = 57.0 + (initialTargetX - 57.0) * (initialScale / newScale);
                double newY = 10.0 + (initialTargetY - 10.0) * (initialScale / newScale);

                ClientConfigs.VISIBILITY_INDICATOR.scale.set(newScale);
                ClientConfigs.VISIBILITY_INDICATOR.x.set((int) Math.round(newX));
                ClientConfigs.VISIBILITY_INDICATOR.y.set((int) Math.round(newY));
                return true;
            } else if (activeTarget == EditTarget.SOUND_WAVE_MOVE) {
                double scale = ClientConfigs.SOUND_WAVE_INDICATOR.scale.get();
                this.soundX += (dragX / scale);
                this.soundY += (dragY / scale);
                ClientConfigs.SOUND_WAVE_INDICATOR.x.set((int) Math.round(this.soundX));
                ClientConfigs.SOUND_WAVE_INDICATOR.y.set((int) Math.round(this.soundY));
                return true;
            } else if (activeTarget == EditTarget.SOUND_WAVE_SCALE) {
                double totalDeltaX = mouseX - initialMouseX;
                double totalDeltaY = mouseY - initialMouseY;

                int initialCents = (int) Math.round(initialScale * 100.0);
                int deltaCents = (int) Math.round((totalDeltaX + totalDeltaY) / 5.0);
                int newCents = Mth.clamp(initialCents + deltaCents, 20, 200);

                double newScale = newCents / 100.0;

                double newX = 57.0 + (initialTargetX - 57.0) * (initialScale / newScale);
                double newY = 47.0 + (initialTargetY - 47.0) * (initialScale / newScale);

                ClientConfigs.SOUND_WAVE_INDICATOR.scale.set(newScale);
                ClientConfigs.SOUND_WAVE_INDICATOR.x.set((int) Math.round(newX));
                ClientConfigs.SOUND_WAVE_INDICATOR.y.set((int) Math.round(newY));
                return true;
            }
        }
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (activeTarget != EditTarget.NONE && button == 0) {
            this.activeTarget = EditTarget.NONE;

            ClientConfigs.VISIBILITY_INDICATOR.x.save();
            ClientConfigs.VISIBILITY_INDICATOR.y.save();
            ClientConfigs.VISIBILITY_INDICATOR.scale.save();

            ClientConfigs.SOUND_WAVE_INDICATOR.x.save();
            ClientConfigs.SOUND_WAVE_INDICATOR.y.save();
            ClientConfigs.SOUND_WAVE_INDICATOR.scale.save();
            return true;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    private void drawHitboxes(GuiGraphics guiGraphics) {
        if (minecraft == null) return;
        double centerX = this.width / 2.0;
        double centerY = 30.0;

        double visScale = ClientConfigs.VISIBILITY_INDICATOR.scale.get();
        int visConfigX = ClientConfigs.VISIBILITY_INDICATOR.x.get();
        int visConfigY = ClientConfigs.VISIBILITY_INDICATOR.y.get();
        int visLeft = -57 + visConfigX;
        int visTop = -10 + visConfigY;
        int visRight = visLeft + 114;
        int visBottom = visTop + 28;

        double soundScale = ClientConfigs.SOUND_WAVE_INDICATOR.scale.get();
        int soundConfigX = ClientConfigs.SOUND_WAVE_INDICATOR.x.get();
        int soundConfigY = ClientConfigs.SOUND_WAVE_INDICATOR.y.get();
        int soundLeft = -57 + soundConfigX;
        int soundTop = -47 + soundConfigY;
        int soundRight = soundLeft + 114;
        int soundBottom = soundTop + 48;

        String visText = String.format("x: %d, y: %d, scale: %.2f", visConfigX, visConfigY, visScale);
        String soundText = String.format("x: %d, y: %d, scale: %.2f", soundConfigX, soundConfigY, soundScale);

        int textHeight = font.lineHeight;
        int visTextWidth = minecraft.font.width(visText);
        int soundTextWidth = minecraft.font.width(soundText);

        int visTextLocalX = visLeft + 114 + 4;
        int visTextLocalY = visTop + 28;
        int soundTextLocalX = soundLeft + 114 + 4;
        int soundTextLocalY = soundTop + 48;

        double absVisTextX = centerX + visTextLocalX * visScale;
        double absVisTextY = centerY + visTextLocalY * visScale;
        double absVisTextRight = absVisTextX + visTextWidth * visScale;
        double absVisTextBottom = absVisTextY + textHeight * visScale;

        double absSoundTextX = centerX + soundTextLocalX * soundScale;
        double absSoundTextY = centerY + soundTextLocalY * soundScale;
        double absSoundTextRight = absSoundTextX + soundTextWidth * soundScale;
        double absSoundTextBottom = absSoundTextY + textHeight * soundScale;

        if (absVisTextRight > this.width) {
            visTextLocalX = visLeft - visTextWidth - 4;
        }
        if (absVisTextBottom > this.height) {
            visTextLocalY = visTop - textHeight - 4;
        }

        if (absSoundTextRight > this.width) {
            soundTextLocalX = soundLeft - soundTextWidth - 4;
        }
        if (absSoundTextBottom > this.height) {
            soundTextLocalY = soundTop - textHeight - 4;
        }

        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(centerX, centerY, 0);
        guiGraphics.pose().scale((float) visScale, (float) visScale, 1.0F);
        guiGraphics.fill(visLeft, visTop, visRight, visBottom, 0x4400FF00);
        guiGraphics.fill(visRight - 12, visBottom - 12, visRight, visBottom, 0x8800FFFF);
        guiGraphics.drawString(minecraft.font, visText, visTextLocalX, visTextLocalY, 0xFFFFFF);
        guiGraphics.pose().popPose();

        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(centerX, centerY, 0);
        guiGraphics.pose().scale((float) soundScale, (float) soundScale, 1.0F);
        guiGraphics.fill(soundLeft, soundTop, soundRight, soundBottom, 0x440088FF);
        guiGraphics.fill(soundRight - 12, soundBottom - 12, soundRight, soundBottom, 0x8800FFFF);
        guiGraphics.drawString(minecraft.font, soundText, soundTextLocalX, soundTextLocalY, 0xFFFFFF);
        guiGraphics.pose().popPose();
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
