package net.rev.stealthandalert.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class ClientConfigs {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue ALERT_INDICATOR =
            BUILDER.comment("Whether to display the alert indicator around the crosshair. | 是否在准星周围显示警戒条。")
                    .define("alertIndicator", true);

    public static final ModConfigSpec.IntValue ALERT_INDICATOR_RADIUS =
            BUILDER.comment("The radius of the alert indicator from the crosshair. | 警戒条距离准星的半径。")
                    .defineInRange("alertIndicatorRadius", 100, 60, 200);

    public static final ModConfigSpec.BooleanValue ALERT_SYMBOL =
            BUILDER.comment("Whether to display the alert symbol above enemies' heads. | 是否在敌人头顶显示警戒标志。")
                    .define("alertSymbol", true);

    public static final ModConfigSpec.BooleanValue DEBUG_MODE =
            BUILDER.comment("Enable debug mode to show alert status information above enemies' heads. | 是否开启调试模式。开启后，敌人头顶会显示警戒状态信息。")
                    .define("debugMode", false);

    public static final ModConfigSpec.DoubleValue ALERT_SYMBOL_SCALE =
            BUILDER.comment("The scale of the alert symbol above enemies' heads. | 敌人头顶警戒标志的缩放。")
                    .defineInRange("alertSymbolScale", 0.0025, 0.001, 0.01);

    public static final ModConfigSpec SPEC = BUILDER.build();
}
