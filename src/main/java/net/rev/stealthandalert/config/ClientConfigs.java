package net.rev.stealthandalert.config;

import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;

public class ClientConfigs {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue ALERT_INDICATOR =
            BUILDER.comment("Whether to display the alert indicator around the crosshair. | 是否在准星周围显示警戒条。")
                    .define("alertIndicator", true);

    public static final ModConfigSpec.IntValue ALERT_INDICATOR_RADIUS =
            BUILDER.comment("The radius of the alert indicator from the crosshair. | 警戒条距离准星的半径。")
                    .defineInRange("alertIndicatorRadius", 100, 60, 200);

    public static final ModConfigSpec.BooleanValue VISIBILITY_INDICATOR =
            BUILDER.comment("Whether to display the visibility indicator. | 是否显示可见度指示器。")
                    .define("visibilityIndicator", true);

    public static final ModConfigSpec.DoubleValue VISIBILITY_INDICATOR_SCALE =
            BUILDER.comment("The scale of the visibility indicator. | 可见度指示器的缩放。")
                    .defineInRange("visibilityIndicatorScale", 0.7, 0.2, 1.5);

    public static final ModConfigSpec.ConfigValue<List<? extends Integer>> VISIBILITY_INDICATOR_POSITION =
            BUILDER.comment("The position of the visibility indicator. Upper middle by default. | 可见度指示器的位置。默认为中间偏上。")
                    .defineList("visibilityIndicatorPosition", List.of(0, 0), () -> 0, o -> o instanceof Integer integer && integer >= Integer.MIN_VALUE && integer <= Integer.MAX_VALUE);

    public static final ModConfigSpec.BooleanValue ALERT_SYMBOL =
            BUILDER.comment("Whether to display the alert symbol above enemies' heads. | 是否在敌人头顶显示警戒标志。")
                    .define("alertSymbol", true);

    public static final ModConfigSpec.BooleanValue DEBUG_MODE =
            BUILDER.comment("Enable debug mode to show alert status information above enemies' heads. | 是否开启调试模式。开启后，敌人头顶会显示警戒状态信息。")
                    .define("debugMode", false);

    public static final ModConfigSpec.DoubleValue ALERT_SYMBOL_SCALE =
            BUILDER.comment("The scale of the alert symbol above enemies' heads. | 敌人头顶警戒标志的缩放。")
                    .defineInRange("alertSymbolScale", 0.005, 0.001, 0.01);

    public static final ModConfigSpec SPEC = BUILDER.build();
}
