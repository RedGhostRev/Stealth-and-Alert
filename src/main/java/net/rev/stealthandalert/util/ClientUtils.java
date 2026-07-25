package net.rev.stealthandalert.util;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.rev.stealthandalert.client.gui.overlay.AlertIndicatorOverlay;
import net.rev.stealthandalert.client.gui.overlay.SoundWaveOverlay;
import net.rev.stealthandalert.client.renderer.ClientMarkManager;

@OnlyIn(Dist.CLIENT)
public class ClientUtils {
    public static void clearData() {
        AlertIndicatorOverlay.FULL_AWARENESS_TICKS.clear();
        AlertIndicatorOverlay.ACTIVE_POOL.clear();
        AlertIndicatorOverlay.EXPIRED_GHOSTS.clear();
        SoundWaveOverlay.lastSoundTick = 0;
        SoundWaveOverlay.targetAmplitude = 0.0;
        SoundWaveOverlay.renderAmplitude = 0.0;
        SoundWaveOverlay.timeTracker = 0.0;
        SoundWaveOverlay.tickMaxAmplitude = 0.0;
        SoundWaveOverlay.hasNewSoundThisTick = false;
        ClientMarkManager.clear();
    }
}
