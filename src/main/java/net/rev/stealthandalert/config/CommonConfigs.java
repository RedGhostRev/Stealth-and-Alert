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

    public static final ModConfigSpec SPEC = BUILDER.build();
}
