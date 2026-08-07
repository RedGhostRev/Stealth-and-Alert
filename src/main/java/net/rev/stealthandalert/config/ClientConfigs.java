package net.rev.stealthandalert.config;

import net.neoforged.neoforge.common.ModConfigSpec;
import net.rev.stealthandalert.datagen.ConfigKeys;

public class ClientConfigs {
    public static final ModConfigSpec SPEC;
    public static final AlertIndicator ALERT_INDICATOR;
    public static final VisibilityIndicator VISIBILITY_INDICATOR;
    public static final SoundWaveIndicator SOUND_WAVE_INDICATOR;
    public static final AlertSymbol ALERT_SYMBOL;
    public static final SpyglassMark SPYGLASS_MARK;
    public static final Heatmap HEATMAP;
    public static final DebugMode DEBUG_MODE;

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

        ALERT_INDICATOR = new AlertIndicator(builder);
        VISIBILITY_INDICATOR = new VisibilityIndicator(builder);
        SOUND_WAVE_INDICATOR = new SoundWaveIndicator(builder);
        ALERT_SYMBOL = new AlertSymbol(builder);
        SPYGLASS_MARK = new SpyglassMark(builder);
        HEATMAP = new Heatmap(builder);
        DEBUG_MODE = new DebugMode(builder);

        SPEC = builder.build();
    }

    public static class AlertIndicator {
        public final ModConfigSpec.BooleanValue enable;
        public final ModConfigSpec.IntValue radius;

        public AlertIndicator(ModConfigSpec.Builder builder) {
            builder.comment("Settings for the alert indicator HUD, which displays the alert level of surrounding enemies")
                    .translation(ConfigKeys.AlertIndicator.ALERT_INDICATOR.key())
                    .push("alertIndicator");
            enable = builder.comment("Whether to display the alert indicator around the crosshair")
                    .translation(ConfigKeys.AlertIndicator.ENABLE.key())
                    .define("enable", true);
            radius = builder.comment("The distance between the alert indicator and the crosshair")
                    .translation(ConfigKeys.AlertIndicator.RADIUS.key())
                    .defineInRange("radius", 100, 60, 200);

            builder.pop();
        }
    }

    public static class VisibilityIndicator {
        public final ModConfigSpec.BooleanValue enable;
        public final ModConfigSpec.DoubleValue scale;
        public final ModConfigSpec.IntValue x;
        public final ModConfigSpec.IntValue y;
        public final ModConfigSpec.BooleanValue canOffsetFromBossBar;
        public final ModConfigSpec.BooleanValue canOffsetFromJade;

        public VisibilityIndicator(ModConfigSpec.Builder builder) {
            builder.comment("Settings for the visibility indicator HUD, which displays your current visibility")
                    .translation(ConfigKeys.VisibilityIndicator.VISIBILITY_INDICATOR.key())
                    .push("visibilityIndicator");
            enable = builder.comment("Whether to display the visibility indicator on the screen")
                    .translation(ConfigKeys.VisibilityIndicator.TURN_ON.key())
                    .define("enable", true);
            scale = builder.comment("The scale of the visibility indicator")
                    .translation(ConfigKeys.VisibilityIndicator.SCALE.key())
                    .defineInRange("visibilityIndicatorScale", 0.7, 0.2, 2.0);
            builder.comment("The position offset of the visibility indicator on the screen")
                    .translation(ConfigKeys.VisibilityIndicator.POSITION.key())
                    .push("position");
            x = builder.comment("The X offset of the visibility indicator on the screen")
                    .translation(ConfigKeys.VisibilityIndicator.POSITION_X.key())
                    .defineInRange("x", 0, -32768, 32767);
            y = builder.comment("The Y offset of the visibility indicator on the screen")
                    .translation(ConfigKeys.VisibilityIndicator.POSITION_Y.key())
                    .defineInRange("y", 0, -32768, 32767);
            builder.pop();
            canOffsetFromBossBar = builder.comment("Whether the visibility indicator offsets when a boss bar is visible. Recommended to turn off if you have manually adjusted position or scale of indicator")
                    .translation(ConfigKeys.VisibilityIndicator.OFFSET_FROM_BOSS_BAR.key())
                    .define("canOffsetFromBossBar", true);
            canOffsetFromJade = builder.comment("Whether the visibility indicator offsets when Jade mod's tooltip is visible. Recommended to turn off if you have manually adjusted position or scale of indicator or Jade tooltip")
                    .translation(ConfigKeys.VisibilityIndicator.OFFSET_FROM_JADE.key())
                    .define("canOffsetFromJade", true);

            builder.pop();
        }
    }

    public static class SoundWaveIndicator {
        public final ModConfigSpec.BooleanValue enable;
        public final ModConfigSpec.DoubleValue scale;
        public final ModConfigSpec.IntValue x;
        public final ModConfigSpec.IntValue y;
        public final ModConfigSpec.BooleanValue canOffsetFromBossBar;
        public final ModConfigSpec.BooleanValue canOffsetFromJade;

        public SoundWaveIndicator(ModConfigSpec.Builder builder) {
            builder.comment("Settings for the sound wave indicator HUD, which displays the sound you are producing")
                    .translation(ConfigKeys.SoundWaveIndicator.SOUND_WAVE_INDICATOR.key())
                    .push("soundWaveIndicator");
            enable = builder.comment("Whether to display the sound wave indicator on the screen")
                    .translation(ConfigKeys.SoundWaveIndicator.TURN_ON.key())
                    .define("enable", true);
            scale = builder.comment("The scale of the sound wave indicator")
                    .translation(ConfigKeys.SoundWaveIndicator.SCALE.key())
                    .defineInRange("scale", 0.7, 0.2, 2.0);

            builder.comment("The position offset of the sound wave indicator on the screen")
                    .translation(ConfigKeys.SoundWaveIndicator.POSITION.key())
                    .push("position");
            x = builder.comment("The X offset of the sound wave indicator on the screen")
                    .translation(ConfigKeys.SoundWaveIndicator.POSITION_X.key())
                    .defineInRange("x", 0, -32768, 32767);
            y = builder.comment("The Y offset of the sound wave indicator on the screen")
                    .translation(ConfigKeys.SoundWaveIndicator.POSITION_Y.key())
                    .defineInRange("y", 0, -32768, 32767);
            builder.pop();

            canOffsetFromBossBar = builder.comment("Whether the sound wave indicator offsets when a boss bar is visible. Recommended to turn off if you have manually adjusted position or scale of indicator")
                    .translation(ConfigKeys.SoundWaveIndicator.OFFSET_FROM_BOSS_BAR.key())
                    .define("canOffsetFromBossBar", true);
            canOffsetFromJade = builder.comment("Whether the sound wave indicator offsets when Jade mod's tooltip is visible. Recommended to turn off if you have manually adjusted position or scale of indicator or Jade tooltip")
                    .translation(ConfigKeys.SoundWaveIndicator.OFFSET_FROM_JADE.key())
                    .define("canOffsetFromJade", true);

            builder.pop();
        }
    }

    public static class AlertSymbol {
        public final ModConfigSpec.BooleanValue enable;
        public final ModConfigSpec.DoubleValue scale;

        public AlertSymbol(ModConfigSpec.Builder builder) {
            builder.comment("Settings for the world-space alert symbol, which displays the current alert status of enemies")
                    .translation(ConfigKeys.AlertSymbol.ALERT_SYMBOL.key())
                    .push("alertSymbol");
            enable = builder.comment("Whether to display the alert symbol above enemies' heads")
                    .translation(ConfigKeys.AlertSymbol.ENABLE.key())
                    .define("enable", true);
            scale = builder.comment("The scale of the alert symbol")
                    .translation(ConfigKeys.AlertSymbol.SCALE.key())
                    .defineInRange("scale", 0.005, 0.001, 0.01);

            builder.pop();
        }
    }

    public static class SpyglassMark {
        public final ModConfigSpec.BooleanValue enable;
        public final ModConfigSpec.DoubleValue maxDistance;
        public final ModConfigSpec.IntValue hostileColor;
        public final ModConfigSpec.IntValue neutralColor;
        public final ModConfigSpec.IntValue allyColor;
        public final ModConfigSpec.IntValue npcColor;
        public final ModConfigSpec.IntValue passiveColor;

        public SpyglassMark(ModConfigSpec.Builder builder) {
            builder.comment("Settings for marking mobs through spyglass")
                    .translation(ConfigKeys.SpyglassMark.SPYGLASS_MARK.key())
                    .push("spyglassMark");
            enable = builder.comment("Whether to turn on mark feature when using spyglass")
                    .translation(ConfigKeys.SpyglassMark.ENABLE.key())
                    .define("enable", true);

            maxDistance = builder.comment("The maximum distance at which a mob can be marked")
                    .translation(ConfigKeys.SpyglassMark.MAX_DISTANCE.key())
                    .defineInRange("maxDistance", 128.0, 16.0, 2048.0);

            hostileColor = builder.comment("Mark's color for hostile mobs")
                    .translation(ConfigKeys.SpyglassMark.HOSTILE_COLOR.key())
                    .defineInRange("hostileColor", 0xFF0000, 0x000000, 0xFFFFFF);
            neutralColor = builder.comment("Mark's color for neutral mobs")
                    .translation(ConfigKeys.SpyglassMark.NEUTRAL_COLOR.key())
                    .defineInRange("neutralColor", 0xFF8C00, 0x000000, 0xFFFFFF);
            allyColor = builder.comment("Mark's color for allies of players, such as Iron Golems created by players, Snow Golems, and pets (only to their owners)")
                    .translation(ConfigKeys.SpyglassMark.ALLY_COLOR.key())
                    .defineInRange("allyColor", 0x0000FF, 0x000000, 0xFFFFFF);
            npcColor = builder.comment("Mark's color for NPCs, such as Villagers and Wandering Traders")
                    .translation(ConfigKeys.SpyglassMark.NPC_COLOR.key())
                    .defineInRange("npcColor", 0x00FF00, 0x000000, 0xFFFFFF);
            passiveColor = builder.comment("Mark's color for passive mobs, such as normal animals")
                    .translation(ConfigKeys.SpyglassMark.PASSIVE_COLOR.key())
                    .defineInRange("passiveColor", 0xFFFFFF, 0x000000, 0xFFFFFF);

            builder.pop();
        }
    }

    public static class Heatmap {
        public final ModConfigSpec.BooleanValue enable;
        public final ModConfigSpec.DoubleValue searchRange;
        public final ModConfigSpec.DoubleValue maxOpacity;
        public final ModConfigSpec.DoubleValue debugForceY;

        public Heatmap(ModConfigSpec.Builder builder) {
            builder.comment("Settings for the danger heatmap visualization, which displays danger levels in enemy vision cones")
                    .translation(ConfigKeys.Heatmap.HEATMAP.key())
                    .push("heatmap");
            enable = builder.comment("Whether to display the danger heatmap in enemy vision cones")
                    .translation(ConfigKeys.Heatmap.ENABLE.key())
                    .define("enable", true);
            searchRange = builder.comment("The maximum search range to find enemies and display their heatmaps")
                    .translation(ConfigKeys.Heatmap.SEARCH_RANGE.key())
                    .defineInRange("searchRange", 32.0, 8.0, 128.0);
            maxOpacity = builder.comment("The maximum opacity of heatmap cells (0=transparent, 1=fully opaque)")
                    .translation(ConfigKeys.Heatmap.MAX_OPACITY.key())
                    .defineInRange("maxOpacity", 0.4, 0.05, 1.0);
            debugForceY = builder.comment("Debug: force all vision cone vertices to this Y (e.g. 100) to verify shader projection is working. Set to -9999 to disable.")
                    .translation(ConfigKeys.Heatmap.DEBUG_FORCE_Y.key())
                    .defineInRange("debugForceY", -9999.0, -10000.0, 10000.0);

            builder.pop();
        }
    }

    public static class DebugMode {
        public final ModConfigSpec.BooleanValue enable;

        public DebugMode(ModConfigSpec.Builder builder) {
            builder.comment("Settings for Debug Mode")
                    .translation(ConfigKeys.DebugMode.DEBUG_MODE.key())
                    .push("debugMode");
            enable = builder.comment("Whether to display detailed alert status information above enemies' heads")
                    .translation(ConfigKeys.DebugMode.ENABLE.key())
                    .define("enable", false);

            builder.pop();
        }
    }
}
