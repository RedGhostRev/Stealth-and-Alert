package net.rev.stealthandalert.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class CommonConfigs {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.DoubleValue MAX_DETECTION_RANGE =
            BUILDER.comment("The maximum distance at which enemies can detect players. | 敌人能看到玩家的最大距离")
                    .defineInRange("maxDetectionRange", 32.0, 1.0, 128.0);

    public static final ModConfigSpec.DoubleValue DETECTION_HORIZONTAL_FOV =
            BUILDER.comment("The horizontal field of view (FOV) of enemies in degrees. (e.g., 120 means a 120-degree sector). | 敌人的水平视野范围（角度）。例如，120代表水平扇形视野为120度。")
                    .defineInRange("detectionHorizontalFOV", 120.0, 1.0, 360.0);

    public static final ModConfigSpec.DoubleValue DETECTION_VERTICAL_UP_FOV =
            BUILDER.comment("The vertical upward FOV of enemies. (e.g., 45 means the enemy can see 45 degrees above them). | 敌人向上看的垂直视野范围（角度）。例如，45代表敌人能看到在它上方45度范围内的玩家。")
                    .defineInRange("detectionVerticalUpFOV", 45.0, 1.0, 90.0);

    public static final ModConfigSpec.DoubleValue DETECTION_VERTICAL_DOWN_FOV =
            BUILDER.comment("The vertical downward FOV of enemies. (e.g., 60 means the enemy can see 60 degrees below them). | 敌人向下看的垂直视野范围（角度）。例如，60代表敌人能看到在它下方60度范围内的玩家。")
                    .defineInRange("detectionVerticalDownFOV", 60.0, 1.0, 90.0);

    public static final ModConfigSpec.IntValue PATIENCE_TICKS =
            BUILDER.comment("The duration (in ticks) an enemy remains interested before losing track, especially when the Last Known Position (LKP) is unreachable. (20 ticks = 1s) | 敌人失去对玩家的兴趣前的耐心时间，主要用于敌人无法到达最后已知位置时。单位：tick（0.05秒）")
                    .defineInRange("patienceTicks", 600, 300, 1200);

    public static final ModConfigSpec.IntValue DETECTION_REACTION_TICKS =
            BUILDER.comment("The reaction time (in ticks) required for an enemy to fully perceive a player after spotting them. | 敌人从看到到真正察觉玩家所需的反应时间。单位：tick（0.05秒）")
                    .defineInRange("detectionReactionTicks", 10, 0, 100);

    public static final ModConfigSpec.DoubleValue VISIBILITY_THRESHOLD =
            BUILDER.comment("The visibility threshold below which players are considered invisible to enemies. (Visibility is between 0.0 and 1.0). | 玩家被认为对敌人不可见的可见度阈值。可见度的范围是0.0到1.0。")
                    .defineInRange("visibilityThreshold", 0.05, 0.01, 0.5);

    public static final ModConfigSpec.DoubleValue MIN_INVISIBLE_DISTANCE =
            BUILDER.comment("The minimum distance for players to be invisible to enemies. Within this distance, players will always be visible regardless of their visibility level. | 玩家能对敌人保持不可见状态的最小距离。在这个距离内，玩家将始终对敌人可见，无论可见度如何。")
                    .defineInRange("maxInvisibleDistance", 2.0, 2.0, 8.0);

    public static final ModConfigSpec.DoubleValue MIN_INVISIBLE_DISTANCE_TO_ENEMY_TRACKING =
            BUILDER.comment("The minimum distance for players to be invisible to enemy tracking them. Within this distance, players will always be visible regardless of their visibility level. | 玩家能对正在追踪他的敌人保持不可见状态的最小距离。在这个距离内，玩家将始终对敌人可见，无论可见度如何。")
                    .defineInRange("maxInvisibleDistanceToEnemyTracking", 5.0, 1.0, 10.0);


    public static final ModConfigSpec.DoubleValue AWARENESS_INCREASE_BASIC_RATE =
            BUILDER.comment("The basic rate at which awareness increases when an enemy detects a player.  | 当敌人察觉玩家时，警戒值增长的基本速率。")
                    .defineInRange("awarenessIncreaseBasicRate", 2.0, 0.5, 5.0);

    public static final ModConfigSpec.DoubleValue AWARENESS_INCREASE_VISIBILITY_FACTOR =
            BUILDER.comment("The factor by which visibility affects the awareness increase. Higher values mean that being more visible will significantly increase the awareness gain. | 可见度对警戒值增长的影响。数值越高，玩家的可见度对警戒值增长的影响越大。")
                    .defineInRange("awarenessIncreaseVisibilityFactor", 1.0, 0.5, 5.0);

    public static final ModConfigSpec.DoubleValue AWARENESS_INCREASE_DISTANCE_FACTOR =
            BUILDER.comment("The factor by which distance affects the awareness increase. Higher values mean that being farther away will significantly decrease the awareness gain. | 距离对警戒值增长的影响。数值越高，玩家与敌人的距离对警戒值增长的影响越大。")
                    .defineInRange("awarenessIncreaseDistanceFactor", 1.0, 0.5, 5.0);

    public static final ModConfigSpec.DoubleValue AWARENESS_INCREASE_SUSPICIOUS_STATE_FACTOR =
            BUILDER.comment("The factor by which being in the suspicious state affects the awareness increase. Higher values mean that if the enemy is in the suspicious state, it will gain awareness much faster. | 怀疑状态对警戒值增长的影响。数值越高，敌人如果处于怀疑状态，警戒值增长得越快。")
                    .defineInRange("awarenessIncreaseSuspiciousStateFactor", 1.0, 0.5, 5.0);

    public static final ModConfigSpec.DoubleValue AWARENESS_INCREASE_SEARCHING_STATE_FACTOR =
            BUILDER.comment("The factor by which being in the searching state affects the awareness increase. Higher values mean that if the enemy is in the searching state, it will gain awareness much faster. | 搜寻状态对警戒值增长的影响。数值越高，敌人如果处于搜寻状态，警戒值增长得越快。")
                    .defineInRange("awarenessIncreaseSearchingStateFactor", 1.2, 0.5, 5.0);

    public static final ModConfigSpec.DoubleValue AWARENESS_DECREASE_BASIC_RATE =
            BUILDER.comment("The basic rate at which awareness decreases when an enemy loses track of a player. | 当敌人失去对玩家的追踪时，警戒值下降的基本速率。")
                    .defineInRange("awarenessDecreaseBasicRate", 1.0, 0.5, 5.0);

    public static final ModConfigSpec.DoubleValue AWARENESS_DECREASE_SUSPICIOUS_STATE_FACTOR =
            BUILDER.comment("The factor by which being in the suspicious state affects the awareness decrease. Higher values mean that if the enemy is in the suspicious state, it will lose awareness much slower. | 怀疑状态对警戒值下降的影响。数值越高，敌人如果处于怀疑状态，警戒值下降得越慢。")
                    .defineInRange("awarenessDecreaseSuspiciousStateFactor", 1.0, 0.5, 5.0);

    public static final ModConfigSpec.DoubleValue AWARENESS_DECREASE_SEARCHING_STATE_FACTOR =
            BUILDER.comment("The factor by which being in the searching state affects the awareness decrease. Higher values mean that if the enemy is in the searching state, it will lose awareness much slower. | 搜寻状态对警戒值下降的影响。数值越高，敌人如果处于搜寻状态，警戒值下降得越慢。")
                    .defineInRange("awarenessDecreaseSearchingStateFactor", 0.6, 0.5, 5.0);

    public static final ModConfigSpec SPEC = BUILDER.build();
}
