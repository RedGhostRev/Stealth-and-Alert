package net.rev.stealthandalert.client.gui.overlay;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Items;
import net.rev.stealthandalert.client.key.ModKeyMappings;
import net.rev.stealthandalert.client.renderer.ClientMarkManager;
import net.rev.stealthandalert.config.ClientConfigs;
import net.rev.stealthandalert.datagen.LangKeys;

public class MarkOverlay {
    public static void render(GuiGraphics graphics, DeltaTracker deltaTracker) {
        if (!ClientConfigs.SPYGLASS_MARK.enable.get()) return;
        Minecraft mc = Minecraft.getInstance();
        if (mc.options.hideGui) return;
        LocalPlayer player = mc.player;
        if (player == null || !player.isUsingItem() || !player.getUseItem().is(Items.SPYGLASS)) return;
        if (player.isSpectator()) return;
        int result = ClientMarkManager.canSeeAny(player);
        if (result == -1) return;
        Component key = ModKeyMappings.MARK_KEY.getTranslatedKeyMessage();
        MutableComponent text0 = Component.translatable(LangKeys.GUI_MARK, key);
        MutableComponent text1 = Component.translatable(LangKeys.GUI_UNMARK, key);
        Font font = mc.font;
        int maxWidth = Math.max(font.width(text0), font.width(text1));
        MutableComponent text = result == 0 ? text0 : text1;
        graphics.drawString(font,
                text,
                (graphics.guiWidth() - maxWidth) / 2 - maxWidth,
                graphics.guiHeight() / 2 - font.lineHeight / 2,
                0xFFFFFF,
                true);
    }
}
