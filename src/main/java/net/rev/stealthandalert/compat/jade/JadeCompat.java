package net.rev.stealthandalert.compat.jade;

import snownee.jade.api.ui.TooltipRect;
import snownee.jade.overlay.OverlayRenderer;

public class JadeCompat {
    public static boolean isJadeOverlayVisible() {
        return OverlayRenderer.shown;
    }

    public static int getJadeOverlayBottomY() {
        TooltipRect rect = OverlayRenderer.rect;
        return rect.rect.getHeight() + rect.rect.getY();
    }
}
