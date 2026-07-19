package net.rev.stealthandalert.config;

import net.neoforged.neoforge.common.ModConfigSpec;
import net.rev.stealthandalert.datagen.LangKeys;

public class CommonConfigs {
    public static final ModConfigSpec SPEC;
    public static final Detection DETECTION;
    public static final Awareness AWARENESS;
    public static final Assassination ASSASSINATION;

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

        DETECTION = new Detection(builder);
        AWARENESS = new Awareness(builder);
        ASSASSINATION = new Assassination(builder);

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
        public final ModConfigSpec.DoubleValue minInvisibleDistance;
        public final ModConfigSpec.DoubleValue minInvisibleDistanceToTracking;

        public Detection(ModConfigSpec.Builder builder) {
            builder.comment("Settings related to enemy vision, FOV, and detection ranges")
                    .translation(LangKeys.DETECTION)
                    .push("Detection"); // 进入 Detection 类别

            maxDetectionRange = builder
                    .comment("The maximum distance at which enemies can see the player")
                    .translation(LangKeys.MAX_DETECTION_RANGE)
                    .defineInRange("maxDetectionRange", 32.0, 1.0, 128.0);

            horizontalFov = builder
                    .comment("The horizontal field of view of enemies (in degrees)")
                    .translation(LangKeys.HORIZONTAL_FOV)
                    .defineInRange("horizontalFOV", 120.0, 1.0, 360.0);

            verticalUpFov = builder
                    .comment("The vertical upward field of view of enemies (in degrees)")
                    .translation(LangKeys.VERTICAL_UP_FOV)
                    .defineInRange("verticalUpFOV", 45.0, 1.0, 90.0);

            verticalDownFov = builder
                    .comment("The vertical downward field of view of enemies (in degrees)")
                    .translation(LangKeys.VERTICAL_DOWN_FOV)
                    .defineInRange("verticalDownFOV", 60.0, 1.0, 90.0);

            reactionTicks = builder
                    .comment("The reaction time required for an enemy to fully perceive a player after spotting them (in ticks)")
                    .translation(LangKeys.REACTION_TICKS)
                    .defineInRange("reactionTicks", 10, 0, 100);

            trackingTicks = builder
                    .comment("The duration before an enemy loses track of a player since unable to see them (in ticks)")
                    .translation(LangKeys.TRACKING_TICKS)
                    .defineInRange("trackingTicks", 0, 30, 1200);

            patienceTicks = builder
                    .comment("The duration of patience before an enemy loses interest in an LKP(Last Known Position) (in ticks)")
                    .translation(LangKeys.PATIENCE_TICKS)
                    .defineInRange("patienceTicks", 600, 300, 1200);

            memoryTicks = builder
                    .comment("The duration of an enemy's memory towards a player who has enraged it (in ticks)")
                    .translation(LangKeys.MEMORY_TICKS)
                    .defineInRange("memoryTicks", 1200, 100,  12000);

            visibilityThreshold = builder
                    .comment("The visibility threshold for players to enter a fully concealed state (*100%)")
                    .translation(LangKeys.VISIBILITY_THRESHOLD)
                    .defineInRange("visibilityThreshold", 0.05, 0.01, 0.5);

            minInvisibleDistance = builder
                    .comment("The distance within which complete concealment fails against enemies")
                    .translation(LangKeys.MIN_INVISIBLE_DISTANCE)
                    .defineInRange("minInvisibleDistance", 2.0, 2.0, 8.0);

            minInvisibleDistanceToTracking = builder
                    .comment("The distance within which complete concealment fails against tracking enemies")
                    .translation(LangKeys.MIN_INVISIBLE_DISTANCE_TO_TRACKING)
                    .defineInRange("minInvisibleDistanceToTracking", 5.0, 1.0, 10.0);

            builder.pop();
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
                    .translation(LangKeys.AWARENESS)
                    .push("Awareness"); // 进入 Awareness 类别

            increaseBasicRate = builder
                    .comment("The basic rate at which awareness increases when an enemy spots a player")
                    .translation(LangKeys.INCREASE_BASIC_RATE)
                    .defineInRange("increaseBasicRate", 2.5, 0.1, 5.0);

            increaseVisibilityFactor = builder
                    .comment("The factor by which the player's visibility affects the awareness increase rate")
                    .translation(LangKeys.INCREASE_VISIBILITY_FACTOR)
                    .defineInRange("increaseVisibilityFactor", 1.0, 0.1, 5.0);

            increaseDistanceFactor = builder
                    .comment("The factor by which the distance between the player and enemy affects the awareness increase rate")
                    .translation(LangKeys.INCREASE_DISTANCE_FACTOR)
                    .defineInRange("increaseDistanceFactor", 1.0, 0.1, 5.0);

            increaseSuspiciousFactor = builder
                    .comment("The factor by which the enemy's suspicious state affects the awareness increase rate")
                    .translation(LangKeys.INCREASE_SUSPICIOUS_FACTOR)
                    .defineInRange("increaseSuspiciousFactor", 1.0, 0.1, 5.0);

            increaseSearchingFactor = builder
                    .comment("The factor by which the enemy's searching state affects the awareness increase rate")
                    .translation(LangKeys.INCREASE_SEARCHING_FACTOR)
                    .defineInRange("increaseSearchingFactor", 1.2, 0.1, 5.0);

            decreaseBasicRate = builder
                    .comment("The basic rate at which awareness decreases when an enemy loses track of the player")
                    .translation(LangKeys.DECREASE_BASIC_RATE)
                    .defineInRange("decreaseBasicRate", 1.8, 0.1, 5.0);

            decreaseSuspiciousFactor = builder
                    .comment("The factor by which the enemy's suspicious state affects the awareness decrease rate")
                    .translation(LangKeys.DECREASE_SUSPICIOUS_FACTOR)
                    .defineInRange("decreaseSuspiciousFactor", 1.0, 0.1, 5.0);

            decreaseSearchingFactor = builder
                    .comment("The factor by which the enemy's searching state affects the awareness decrease rate")
                    .translation(LangKeys.DECREASE_SEARCHING_FACTOR)
                    .defineInRange("decreaseSearchingFactor", 0.6, 0.1, 5.0);

            builder.pop();
        }
    }

    public static class Assassination {
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
                    .translation(LangKeys.ASSASSINATION_C)
                    .push("Assassination"); // 进入 Assassination 类别

            alwaysSuccess = builder
                    .comment("Whether assassinations to SEEKERS can be performed successfully all the time")
                    .translation(LangKeys.ALWAYS_SUCCESS)
                    .define("alwaysSuccess", true);

            successChance = builder
                    .comment("If alwaysSuccess is false, the chance of successfully performing an assassination to SEEKERS")
                    .translation(LangKeys.SUCCESS_CHANCE)
                    .defineInRange("successChance", 0.7, 0.0, 1.0);

            canPetsBeAssassinated = builder
                    .comment("Whether pets can be assassinated by their owners")
                    .translation(LangKeys.CAN_PETS_BE_ASSASSINATED)
                    .define("canPetsBeAssassinated", false);

            canAnimalsBeAssassinated = builder
                    .comment("Whether normal animals (excluding those in the SEEKERS tag) can be assassinated")
                    .translation(LangKeys.CAN_ANIMALS_BE_ASSASSINATED)
                    .define("canAnimalsBeAssassinated", false);

            canAnimalSeekersBeAssassinated = builder
                    .comment("Whether animals in the SEEKERS tag can be assassinated")
                    .translation(LangKeys.CAN_ANIMAL_SEEKERS_BE_ASSASSINATED)
                    .define("canAnimalSeekersBeAssassinated", true);

            canVillagersBeAssassinated = builder
                    .comment("Whether villagers (including wandering traders) can be assassinated")
                    .translation(LangKeys.CAN_VILLAGERS_BE_ASSASSINATED)
                    .define("canVillagersBeAssassinated", false);

            canBossesBeAssassinated = builder
                    .comment("Whether bosses (if in the CAN_BE_ASSASSINATED tag) can be assassinated")
                    .translation(LangKeys.CAN_BOSSES_BE_ASSASSINATED)
                    .define("canBossesBeAssassinated", false);

            canPlayersBeAssassinated = builder
                    .comment("Whether players can assassinate each other")
                    .translation(LangKeys.CAN_PLAYERS_BE_ASSASSINATED)
                    .define("canPlayersBeAssassinated", false);

            builder.pop();
        }
    }
}