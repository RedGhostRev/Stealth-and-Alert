package net.rev.stealthandalert.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class CommonConfigs {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.DoubleValue MAX_DETECTION_RANGE =
            BUILDER.comment("敌人能看到玩家的最大距离")
                    .defineInRange("maxDetectionRange", 32.0, 1.0, 128.0);

    public static final ModConfigSpec.DoubleValue DETECTION_HORIZONTAL_FOV =
            BUILDER.comment("敌人的水平视野范围（角度）。例如，120代表水平扇形视野为120度。")
                    .defineInRange("detectionHorizontalFOV", 120.0, 1.0, 360.0);

    public static final ModConfigSpec.DoubleValue DETECTION_VERTICAL_UP_FOV =
            BUILDER.comment("敌人向上看的垂直视野范围（角度）。例如，45代表敌人能看到在它上方45度范围内的玩家。")
                    .defineInRange("detectionVerticalUpFOV", 40.0, 1.0, 90.0);

    public static final ModConfigSpec.DoubleValue DETECTION_VERTICAL_DOWN_FOV =
            BUILDER.comment("敌人向下看的垂直视野范围（角度）。例如，60代表敌人能看到在它下方60度范围内的玩家。")
                    .defineInRange("detectionVerticalDownFOV", 60.0, 1.0, 90.0);

    public static final ModConfigSpec.IntValue PATIENCE_TICKS =
            BUILDER.comment("敌人失去对玩家的兴趣前的耐心时间，主要用于敌人无法到达最后已知位置时。单位：tick（0.05秒）")
                    .defineInRange("patienceTicks", 600, 300, 1200);

    public static final ModConfigSpec.IntValue DETECTION_REACTION_TICKS =
            BUILDER.comment("敌人从看到到真正察觉玩家所需的反应时间。单位：tick（0.05秒）")
                    .defineInRange("detectionReactionTicks", 10, 0, 100);

    public static final ModConfigSpec.BooleanValue DEBUG_MODE =
            BUILDER.comment("是否开启调试模式。开启后，敌人头顶会显示警戒状态信息。")
                    .define("debugMode", false);

    public static final ModConfigSpec SPEC = BUILDER.build();
}
