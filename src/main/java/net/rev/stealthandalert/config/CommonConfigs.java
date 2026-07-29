package net.rev.stealthandalert.config;

import net.neoforged.neoforge.common.ModConfigSpec;
import net.rev.stealthandalert.datagen.ConfigKeys;

public class CommonConfigs {
    public static final ModConfigSpec SPEC;
    public static final Detection DETECTION;
    public static final Awareness AWARENESS;
    public static final Assassination ASSASSINATION;
    public static final Compat COMPAT;

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

        DETECTION = new Detection(builder);
        AWARENESS = new Awareness(builder);
        ASSASSINATION = new Assassination(builder);
        COMPAT = new Compat(builder);

        SPEC = builder.build();
    }

    public static class Detection {
        public final ModConfigSpec.DoubleValue maxDetectionRange;
        public final ModConfigSpec.DoubleValue horizontalFov;
        public final ModConfigSpec.DoubleValue verticalUpFov;
        public final ModConfigSpec.DoubleValue verticalDownFov;

        public final ModConfigSpec.IntValue patienceTicks;
        public final ModConfigSpec.IntValue reactionTicks;
        public final ModConfigSpec.IntValue trackingTicks;
        public final ModConfigSpec.IntValue memoryTicks;

        public final ModConfigSpec.DoubleValue visibilityThreshold;
        public final ModConfigSpec.DoubleValue visibilityMaxDetectionRangeReductionPercentage;
        public final ModConfigSpec.EnumValue<DetectionRangeReduction> visibilityDetectionRangeReductionModel;
        public final ModConfigSpec.DoubleValue minInvisibleDistance;
        public final ModConfigSpec.DoubleValue minInvisibleDistanceToTracking;

        public Detection(ModConfigSpec.Builder builder) {
            builder.comment("Settings related to enemy vision, FOV, and detection ranges")
                    .translation(ConfigKeys.Detection.DETECTION.key())
                    .push("detection"); // 进入 Detection 类别

            maxDetectionRange = builder
                    .comment("The maximum distance at which enemies can see the player")
                    .translation(ConfigKeys.Detection.MAX_RANGE.key())
                    .defineInRange("maxDetectionRange", 48.0, 1.0, 256.0);

            horizontalFov = builder
                    .comment("The horizontal field of view of enemies (in degrees)")
                    .translation(ConfigKeys.Detection.HORIZONTAL_FOV.key())
                    .defineInRange("horizontalFOV", 120.0, 1.0, 360.0);

            verticalUpFov = builder
                    .comment("The vertical upward field of view of enemies (in degrees)")
                    .translation(ConfigKeys.Detection.VERTICAL_UP_FOV.key())
                    .defineInRange("verticalUpFOV", 45.0, 1.0, 90.0);

            verticalDownFov = builder
                    .comment("The vertical downward field of view of enemies (in degrees)")
                    .translation(ConfigKeys.Detection.VERTICAL_DOWN_FOV.key())
                    .defineInRange("verticalDownFOV", 60.0, 1.0, 90.0);

            reactionTicks = builder
                    .comment("The reaction time required for an enemy to fully perceive a player after spotting them (in ticks)")
                    .translation(ConfigKeys.Detection.REACTION_TICKS.key())
                    .defineInRange("reactionTicks", 10, 0, 100);

            trackingTicks = builder
                    .comment("The duration before an enemy loses track of a player since unable to see them (in ticks)")
                    .translation(ConfigKeys.Detection.TRACKING_TICKS.key())
                    .defineInRange("trackingTicks", 30, 0, 1200);

            patienceTicks = builder
                    .comment("The duration of patience before an enemy loses interest in an LKP(Last Known Position) (in ticks)")
                    .translation(ConfigKeys.Detection.PATIENCE_TICKS.key())
                    .defineInRange("patienceTicks", 800, 300, 2400);

            memoryTicks = builder
                    .comment("The duration of an enemy's memory towards a player who has enraged it (in ticks)")
                    .translation(ConfigKeys.Detection.MEMORY_TICKS.key())
                    .defineInRange("memoryTicks", 2000, 100, 12000);

            visibilityThreshold = builder
                    .comment("The visibility threshold for players to enter a fully concealed state (*100%)")
                    .translation(ConfigKeys.Detection.VISIBILITY_THRESHOLD.key())
                    .defineInRange("visibilityThreshold", 0.05, 0.01, 0.5);

            visibilityMaxDetectionRangeReductionPercentage = builder
                    .comment("The max reduction percentage of an enemy's detection range influenced by visibility")
                    .translation(ConfigKeys.Detection.VISIBILITY_MAX_DETECTION_RANGE_REDUCTION_PERCENTAGE.key())
                    .defineInRange("visibilityMaxDetectionRangeReductionPercentage", 0.6, 0.1, 1.0);

            visibilityDetectionRangeReductionModel = builder
                    .comment("The mathematical model for reduction of enemies' detection range influenced by visibility",
                            "LINEAR: Steadily reducing",
                            "SQUARE ROOT: Reducing quickly at first, then slowly",
                            "SMOOTHSTEP: Reducing slowly at first and end, quickly at medium")
                    .translation(ConfigKeys.Detection.VISIBILITY_DETECTION_RANGE_REDUCTION_MODEL.key())
                    .defineEnum("visibilityDetectionRangeReductionModel", DetectionRangeReduction.SMOOTHSTEP);

            minInvisibleDistance = builder
                    .comment("The distance within which complete concealment fails against enemies")
                    .translation(ConfigKeys.Detection.MIN_INVISIBLE_DISTANCE.key())
                    .defineInRange("minInvisibleDistance", 2.0, 1.0, 8.0);

            minInvisibleDistanceToTracking = builder
                    .comment("The distance within which complete concealment fails against tracking enemies")
                    .translation(ConfigKeys.Detection.MIN_INVISIBLE_DISTANCE_TO_TRACKING.key())
                    .defineInRange("minInvisibleDistanceToTracking", 5.0, 1.0, 16.0);

            builder.pop();
        }

        public enum DetectionRangeReduction {
            LINEAR,
            SQUARE_ROOT,
            SMOOTHSTEP
        }
    }

    public static class Awareness {
        public final ModConfigSpec.DoubleValue increaseBasicRate;
        public final ModConfigSpec.DoubleValue increaseVisibilityFactor;
        public final ModConfigSpec.DoubleValue increaseDistanceFactor;
        public final ModConfigSpec.DoubleValue increaseSuspiciousFactor;
        public final ModConfigSpec.DoubleValue increaseSearchingFactor;

        public final ModConfigSpec.DoubleValue decreaseBasicRate;
        public final ModConfigSpec.DoubleValue decreaseSuspiciousFactor;
        public final ModConfigSpec.DoubleValue decreaseSearchingFactor;

        public Awareness(ModConfigSpec.Builder builder) {
            builder.comment("Settings related to the increase and decrease rates of enemy awareness")
                    .translation(ConfigKeys.Awareness.AWARENESS.key())
                    .push("awareness"); // 进入 Awareness 类别

            increaseBasicRate = builder
                    .comment("The basic rate at which awareness increases when an enemy spots a player")
                    .translation(ConfigKeys.Awareness.INCREASE_BASIC_RATE.key())
                    .defineInRange("increaseBasicRate", 2.8, 0.1, 5.0);

            increaseVisibilityFactor = builder
                    .comment("The factor by which the player's visibility affects the awareness increase rate")
                    .translation(ConfigKeys.Awareness.INCREASE_VISIBILITY_FACTOR.key())
                    .defineInRange("increaseVisibilityFactor", 1.0, 0.1, 5.0);

            increaseDistanceFactor = builder
                    .comment("The factor by which the distance between the player and enemy affects the awareness increase rate")
                    .translation(ConfigKeys.Awareness.INCREASE_DISTANCE_FACTOR.key())
                    .defineInRange("increaseDistanceFactor", 1.0, 0.1, 5.0);

            increaseSuspiciousFactor = builder
                    .comment("The factor by which the enemy's suspicious state affects the awareness increase rate")
                    .translation(ConfigKeys.Awareness.INCREASE_SUSPICIOUS_FACTOR.key())
                    .defineInRange("increaseSuspiciousFactor", 1.0, 0.1, 5.0);

            increaseSearchingFactor = builder
                    .comment("The factor by which the enemy's searching state affects the awareness increase rate")
                    .translation(ConfigKeys.Awareness.INCREASE_SEARCHING_FACTOR.key())
                    .defineInRange("increaseSearchingFactor", 1.3, 0.1, 5.0);

            decreaseBasicRate = builder
                    .comment("The basic rate at which awareness decreases when an enemy loses track of the player")
                    .translation(ConfigKeys.Awareness.DECREASE_BASIC_RATE.key())
                    .defineInRange("decreaseBasicRate", 1.8, 0.1, 5.0);

            decreaseSuspiciousFactor = builder
                    .comment("The factor by which the enemy's suspicious state affects the awareness decrease rate")
                    .translation(ConfigKeys.Awareness.DECREASE_SUSPICIOUS_FACTOR.key())
                    .defineInRange("decreaseSuspiciousFactor", 1.0, 0.1, 5.0);

            decreaseSearchingFactor = builder
                    .comment("The factor by which the enemy's searching state affects the awareness decrease rate")
                    .translation(ConfigKeys.Awareness.DECREASE_SEARCHING_FACTOR.key())
                    .defineInRange("decreaseSearchingFactor", 0.5, 0.1, 5.0);

            builder.pop();
        }
    }

    public static class Assassination {
        public final ModConfigSpec.BooleanValue enable;
        public final ModConfigSpec.BooleanValue alwaysSuccess;
        public final ModConfigSpec.DoubleValue successChance;

        public final ModConfigSpec.BooleanValue canPetsBeAssassinated;
        public final ModConfigSpec.BooleanValue canAnimalsBeAssassinated;
        public final ModConfigSpec.BooleanValue canAnimalSeekersBeAssassinated;
        public final ModConfigSpec.BooleanValue canVillagersBeAssassinated;
        public final ModConfigSpec.BooleanValue canBossesBeAssassinated;
        public final ModConfigSpec.BooleanValue canPlayersBeAssassinated;

        public Assassination(ModConfigSpec.Builder builder) {
            builder.comment("Settings related to assassination mechanics")
                    .translation(ConfigKeys.Assassination.ASSASSINATION.key())
                    .push("assassination"); // 进入 Assassination 类别

            enable = builder
                    .comment("Whether to enable assassination feature")
                    .translation(ConfigKeys.Assassination.ENABLE.key())
                    .define("enable", true);

            alwaysSuccess = builder
                    .comment("Whether assassinations to SEEKERS can be performed successfully all the time")
                    .translation(ConfigKeys.Assassination.ALWAYS_SUCCESS.key())
                    .define("alwaysSuccess", true);

            successChance = builder
                    .comment("If alwaysSuccess is false, the chance of successfully performing an assassination to SEEKERS")
                    .translation(ConfigKeys.Assassination.SUCCESS_CHANCE.key())
                    .defineInRange("successChance", 0.7, 0.0, 1.0);

            canPetsBeAssassinated = builder
                    .comment("Whether pets can be assassinated by their owners")
                    .translation(ConfigKeys.Assassination.CAN_PETS_BE_ASSASSINATED.key())
                    .define("canPetsBeAssassinated", false);

            canAnimalsBeAssassinated = builder
                    .comment("Whether normal animals (excluding those in the SEEKERS tag) can be assassinated")
                    .translation(ConfigKeys.Assassination.CAN_ANIMALS_BE_ASSASSINATED.key())
                    .define("canAnimalsBeAssassinated", false);

            canAnimalSeekersBeAssassinated = builder
                    .comment("Whether animals in the SEEKERS tag can be assassinated")
                    .translation(ConfigKeys.Assassination.CAN_ANIMAL_SEEKERS_BE_ASSASSINATED.key())
                    .define("canAnimalSeekersBeAssassinated", true);

            canVillagersBeAssassinated = builder
                    .comment("Whether villagers (including wandering traders) can be assassinated")
                    .translation(ConfigKeys.Assassination.CAN_VILLAGERS_BE_ASSASSINATED.key())
                    .define("canVillagersBeAssassinated", false);

            canBossesBeAssassinated = builder
                    .comment("Whether bosses (if in the CAN_BE_ASSASSINATED tag) can be assassinated")
                    .translation(ConfigKeys.Assassination.CAN_BOSSES_BE_ASSASSINATED.key())
                    .define("canBossesBeAssassinated", false);

            canPlayersBeAssassinated = builder
                    .comment("Whether players can assassinate each other")
                    .translation(ConfigKeys.Assassination.CAN_PLAYERS_BE_ASSASSINATED.key())
                    .define("canPlayersBeAssassinated", false);

            builder.pop();
        }
    }

    public static class Compat {
        public final GuardVillagers GUARDVILLAGERS;

        public static class GuardVillagers {
            public final ModConfigSpec.BooleanValue applyGuardVillagerReputationConfig;

            public GuardVillagers(ModConfigSpec.Builder builder) {
                applyGuardVillagerReputationConfig = builder.comment("Whether to apply Guard Villagers mod's config of villager reputation threshold, below which the player will get attacked by Guard Villagers")
                        .translation(ConfigKeys.Compat.GuardVillagers.APPLY_GUARDVILLAGERS_REPUTATION_CONFIG.key())
                        .define("applyGuardVillagersReputationConfig", true);
            }
        }

        Compat(ModConfigSpec.Builder builder) {
            builder.comment("Compatibility settings for other mods")
                    .translation(ConfigKeys.Compat.COMPAT.key())
                    .push("compat");

            builder.comment("Compatibility for Guard Villagers")
                    .translation(ConfigKeys.Compat.GUARDVILLAGERS.key())
                    .push("guardVillagers");
            GUARDVILLAGERS = new GuardVillagers(builder);
            builder.pop();

            builder.pop();
        }
    }
}