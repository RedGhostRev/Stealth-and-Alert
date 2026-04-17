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

    public static final ModConfigSpec.BooleanValue DEBUG_MODE =
            BUILDER.comment("Enable debug mode to show alert status information above enemies' heads. | 是否开启调试模式。开启后，敌人头顶会显示警戒状态信息。")
                    .define("debugMode", false);

    public static final ModConfigSpec SPEC = BUILDER.build();
}
