package net.rev.stealthandalert.client.gui.overlay;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.rev.stealthandalert.attachment.ModAttachments;
import net.rev.stealthandalert.client.key.ModKeyMappings;
import net.rev.stealthandalert.config.CommonConfigs;
import net.rev.stealthandalert.datagen.LangKeys;
import net.rev.stealthandalert.util.AssassinationHandler;

import java.util.Optional;

public class AssassinationOverlay {
    public static void render(GuiGraphics graphics, DeltaTracker deltaTracker) {
        if (!CommonConfigs.ASSASSINATION.enable.get()) return;
        Minecraft mc = Minecraft.getInstance();
        if (mc.options.hideGui) return;
        LocalPlayer player = mc.player;
        if (player == null) return;
        if (player.isSpectator()) return;
        if (mc.level == null) return;
        int targetId = AssassinationHandler.canAssassinate(mc.player, Optional.of(mc.player.getUUID()),
                mc.player.getData(ModAttachments.ASSASSINATION_DATA).isAssassinating());
        if (targetId < 0) return;

        Font font = mc.font;
        Component key = ModKeyMappings.ASSASSINATE_KEY.getTranslatedKeyMessage();
        MutableComponent text = Component.translatable(LangKeys.GUI_ASSASSINATE, key);
        //String text = "[F] 刺杀";
        int textWidth = font.width(text);
        // int textHeight = font.lineHeight;
        graphics.drawString(font,
                text,
                (graphics.guiWidth() - textWidth) / 2,
                graphics.guiHeight() / 2 + 7 + 12,
                0xFFFFFF,
                true);
    }
}
