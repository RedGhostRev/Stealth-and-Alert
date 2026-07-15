package net.rev.stealthandalert.config;

import net.neoforged.neoforge.common.ModConfigSpec;
import net.rev.stealthandalert.datagen.LangKeys;

public class ClientConfigs {
    public static final ModConfigSpec SPEC;
    public static final AlertIndicator ALERT_INDICATOR;
    public static final VisibilityIndicator VISIBILITY_INDICATOR;
    public static final SoundWaveIndicator SOUND_WAVE_INDICATOR;
    public static final AlertSymbol ALERT_SYMBOL;
    public static final DebugMode DEBUG_MODE;

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

        ALERT_INDICATOR = new AlertIndicator(builder);
        VISIBILITY_INDICATOR = new VisibilityIndicator(builder);
        SOUND_WAVE_INDICATOR = new SoundWaveIndicator(builder);
        ALERT_SYMBOL = new AlertSymbol(builder);
        DEBUG_MODE = new DebugMode(builder);

        SPEC = builder.build();
    }

    public static class AlertIndicator {
        public final ModConfigSpec.BooleanValue turnOn;
        public final ModConfigSpec.IntValue radius;

        public AlertIndicator(ModConfigSpec.Builder builder) {
            builder.comment("Settings for the alert indicator HUD, which displays the alert level of surrounding enemies")
                    .translation(LangKeys.ALERT_INDICATOR)
                    .push("alertIndicator");
            turnOn = builder.comment("Whether to display the alert indicator around the crosshair")
                    .translation(LangKeys.ALERT_INDICATOR_TURN_ON)
                    .define("alertIndicator", true);
            radius = builder.comment("The distance between the alert indicator and the crosshair")
                    .translation(LangKeys.RADIUS)
                    .defineInRange("alertIndicatorRadius", 100, 60, 200);

            builder.pop();
        }
    }

    public static class VisibilityIndicator {
        public final ModConfigSpec.BooleanValue turnOn;
        public final ModConfigSpec.DoubleValue scale;
        public final ModConfigSpec.IntValue x;
        public final ModConfigSpec.IntValue y;
        public final ModConfigSpec.BooleanValue canOffsetFromBossBar;

        public VisibilityIndicator(ModConfigSpec.Builder builder) {
            builder.comment("Settings for the visibility indicator HUD, which displays your current visibility")
                    .translation(LangKeys.VISIBILITY_INDICATOR)
                    .push("visibilityIndicator");
            turnOn = builder.comment("Whether to display the visibility indicator on the screen")
                    .translation(LangKeys.VISIBILITY_INDICATOR_TURN_ON)
                    .define("visibilityIndicator", true);
            scale = builder.comment("The scale of the visibility indicator")
                    .translation(LangKeys.VISIBILITY_SCALE)
                    .defineInRange("visibilityIndicatorScale", 0.7, 0.2, 2.0);
            builder.comment("The position offset of the visibility indicator on the screen")
                    .translation(LangKeys.VISIBILITY_INDICATOR_POSITION)
                    .push("visibilityIndicatorPosition");
            x = builder.comment("The X offset of the visibility indicator on the screen")
                    .translation(LangKeys.VISIBILITY_INDICATOR_POSITION_X)
                    .defineInRange("visibilityIndicatorPositionX", 0, -32768, 32767);
            y = builder.comment("The Y offset of the visibility indicator on the screen")
                    .translation(LangKeys.VISIBILITY_INDICATOR_POSITION_Y)
                    .defineInRange("visibilityIndicatorPositionY", 0, -32768, 32767);
            builder.pop();
            canOffsetFromBossBar = builder.comment("Whether the visibility indicator offsets when a boss bar is visible. Recommended to turn off if you have manually adjusted the position")
                    .translation(LangKeys.VISIBILITY_INDICATOR_BOSS_BAR)
                    .define("visibilityIndicatorCanOffsetFromBossBar", true);

            builder.pop();
        }
    }

    public static class SoundWaveIndicator {
        public final ModConfigSpec.BooleanValue turnOn;
        public final ModConfigSpec.DoubleValue scale;
        public final ModConfigSpec.IntValue x;
        public final ModConfigSpec.IntValue y;
        public final ModConfigSpec.BooleanValue canOffsetFromBossBar;

        public SoundWaveIndicator(ModConfigSpec.Builder builder) {
            builder.comment("Settings for the sound wave indicator HUD, which displays the sound you are producing")
                    .translation(LangKeys.SOUND_WAVE_INDICATOR)
                    .push("soundWaveIndicator");
            turnOn = builder.comment("Whether to display the sound wave indicator on the screen")
                    .translation(LangKeys.SOUND_WAVE_INDICATOR_TURN_ON)
                    .define("soundWaveIndicator", true);
            scale = builder.comment("The scale of the sound wave indicator")
                    .translation(LangKeys.SOUND_SCALE)
                    .defineInRange("soundWaveIndicatorScale", 0.7, 0.2, 2.0);

            builder.comment("The position offset of the sound wave indicator on the screen")
                    .translation(LangKeys.SOUND_WAVE_INDICATOR_POSITION)
                    .push("soundWaveIndicatorPosition");
            x = builder.comment("The X offset of the sound wave indicator on the screen")
                    .translation(LangKeys.SOUND_WAVE_INDICATOR_POSITION_X)
                    .defineInRange("soundWaveIndicatorPositionX", 0, -32768, 32767);
            y = builder.comment("The Y offset of the sound wave indicator on the screen")
                    .translation(LangKeys.SOUND_WAVE_INDICATOR_POSITION_Y)
                    .defineInRange("soundWaveIndicatorPositionY", 0, -32768, 32767);
            builder.pop();

            canOffsetFromBossBar = builder.comment("Whether the sound wave indicator offsets when a boss bar is visible. Recommended to turn off if you have manually adjusted the position")
                    .translation(LangKeys.SOUND_BOSS_BAR)
                    .define("soundWaveIndicatorCanOffsetFromBossBar", true);

            builder.pop();
        }
    }

    public static class AlertSymbol {
        public final ModConfigSpec.BooleanValue turnOn;
        public final ModConfigSpec.DoubleValue scale;

        public AlertSymbol(ModConfigSpec.Builder builder) {
            builder.comment("Settings for the world-space alert symbol, which displays the current alert status of enemies")
                    .translation(LangKeys.ALERT_SYMBOL)
                    .push("alertSymbol");
            turnOn = builder.comment("Whether to display the alert symbol above enemies' heads")
                    .translation(LangKeys.ALERT_SYMBOL_TURN_ON)
                    .define("alertSymbol", true);
            scale = builder.comment("The scale of the alert symbol")
                    .translation(LangKeys.ALERT_SYMBOL_SCALE)
                    .defineInRange("alertSymbolScale", 0.005, 0.001, 0.01);

            builder.pop();
        }
    }

    public static class DebugMode {
        public final ModConfigSpec.BooleanValue turnOn;

        public DebugMode(ModConfigSpec.Builder builder) {
            builder.comment("Settings for Debug Mode")
                    .translation(LangKeys.DEBUG_MODE)
                    .push("debugMode");
            turnOn = builder.comment("Whether to display detailed alert status information above enemies' heads")
                    .translation(LangKeys.DEBUG_TURN_ON)
                    .define("debugMode", false);

            builder.pop();
        }
    }
}
