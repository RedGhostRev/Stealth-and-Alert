package net.rev.stealthandalert.client.gui.overlay;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.rev.stealthandalert.attachment.AssassinationData;
import net.rev.stealthandalert.attachment.ModAttachments;
import net.rev.stealthandalert.client.key.ModKeyMappings;
import net.rev.stealthandalert.datagen.LangKeys;
import net.rev.stealthandalert.util.AssassinationHandler;

import java.util.Optional;

public class AssassinationOverlay {
    public static void render(GuiGraphics graphics, DeltaTracker deltaTracker) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player == null) return;
//        HitResult hit = mc.hitResult;
//        if (!(hit instanceof EntityHitResult entityHit)) {
//            return;
//        }
        AssassinationData data = player.getData(ModAttachments.ASSASSINATION_DATA);
        if (mc.level == null) return;
//        long elapsedTicks = mc.level.getGameTime() - data.startTick();
//        if (elapsedTicks < -5 || elapsedTicks >= 36)
//            player.setData(ModAttachments.ASSASSINATE_DATA, AssassinateData.DEFAULT);
//        if (!AssassinateHandler.canAssassinate(player, Optional.of(player.getUUID()), entityHit.getEntity().getId(),
//                player.getData(ModAttachments.ASSASSINATE_DATA).isAssassinating()))
//            return;
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
