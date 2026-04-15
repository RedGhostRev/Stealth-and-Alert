package net.rev.stealthandalert.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class ClientConfigs {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue DEBUG_MODE =
            BUILDER.comment("是否开启调试模式。开启后，敌人头顶会显示警戒状态信息。")
                    .define("debugMode", false);

    public static final ModConfigSpec SPEC = BUILDER.build();
}
