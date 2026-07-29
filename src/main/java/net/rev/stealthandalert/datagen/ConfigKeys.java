package net.rev.stealthandalert.datagen;

import net.rev.stealthandalert.StealthAndAlert;

public class ConfigKeys {
    // Config Texts
    // common
    public enum Detection {
        DETECTION,
        MAX_RANGE,
        HORIZONTAL_FOV,
        VERTICAL_UP_FOV,
        VERTICAL_DOWN_FOV,
        PATIENCE_TICKS,
        REACTION_TICKS,
        TRACKING_TICKS,
        MEMORY_TICKS,
        VISIBILITY_THRESHOLD,
        VISIBILITY_MAX_DETECTION_RANGE_REDUCTION_PERCENTAGE,
        VISIBILITY_DETECTION_RANGE_REDUCTION_MODEL,
        MIN_INVISIBLE_DISTANCE,
        MIN_INVISIBLE_DISTANCE_TO_TRACKING;

        private final String key;
        private final String tooltip;

        public String key() {
            return key;
        }

        public String tooltip() {
            return tooltip;
        }

        Detection() {
            key = getKey(this, "DETECTION");
            tooltip = getTooltip(key);
        }
    }

    public enum Awareness {
        AWARENESS,
        INCREASE_BASIC_RATE,
        INCREASE_VISIBILITY_FACTOR,
        INCREASE_DISTANCE_FACTOR,
        INCREASE_SUSPICIOUS_FACTOR,
        INCREASE_SEARCHING_FACTOR,
        DECREASE_BASIC_RATE,
        DECREASE_SUSPICIOUS_FACTOR,
        DECREASE_SEARCHING_FACTOR;

        private final String key;
        private final String tooltip;

        public String key() {
            return key;
        }

        public String tooltip() {
            return tooltip;
        }

        Awareness() {
            key = getKey(this, "AWARENESS");
            tooltip = getTooltip(this.key);
        }
    }

    public enum Assassination {
        ASSASSINATION,
        ENABLE,
        ALWAYS_SUCCESS,
        SUCCESS_CHANCE,
        CAN_PETS_BE_ASSASSINATED,
        CAN_ANIMALS_BE_ASSASSINATED,
        CAN_ANIMAL_SEEKERS_BE_ASSASSINATED,
        CAN_VILLAGERS_BE_ASSASSINATED,
        CAN_BOSSES_BE_ASSASSINATED,
        CAN_PLAYERS_BE_ASSASSINATED;

        private final String key;
        private final String tooltip;

        public String key() {
            return key;
        }

        public String tooltip() {
            return tooltip;
        }

        Assassination() {
            key = getKey(this, "ASSASSINATION");
            tooltip = getTooltip(key);
        }
    }

    public enum Compat {
        COMPAT(Compat.class),
        GUARDVILLAGERS(GuardVillagers.class);

        private final String key;
        private final String tooltip;

        public String key() {
            return key;
        }

        public String tooltip() {
            return tooltip;
        }

        Compat(Class<? extends Enum<?>> mod) {
            key = getKey(this, "COMPAT");
            tooltip = getTooltip(key);
        }

        public enum GuardVillagers {
            APPLY_GUARDVILLAGERS_REPUTATION_CONFIG;
            private final String key;
            private final String tooltip;
            public String key() {return key;}
            public String tooltip() {return tooltip;}
            GuardVillagers() {
                key = getKey(this, "compat.guardvillagers");
                tooltip = getTooltip(key);
            }
        }
    }


    // client
    public enum AlertIndicator {
        ALERT_INDICATOR,
        ENABLE,
        RADIUS;

        private final String key;
        private final String tooltip;

        public String key() {
            return key;
        }

        public String tooltip() {
            return tooltip;
        }

        AlertIndicator() {
            key = getKey(this, "ALERT_INDICATOR");
            tooltip = getTooltip(key);
        }
    }

    public enum VisibilityIndicator {
        VISIBILITY_INDICATOR,
        TURN_ON,
        SCALE,
        POSITION,
        POSITION_X(config("visibility_indicator", "position.x")),
        POSITION_Y(config("visibility_indicator", "position.y")),
        OFFSET_FROM_BOSS_BAR,
        OFFSET_FROM_JADE;

        private final String key;
        private final String tooltip;

        public String key() {
            return key;
        }

        public String tooltip() {
            return tooltip;
        }

        VisibilityIndicator(String key) {
            this.key = key;
            this.tooltip = getTooltip(key);
        }

        VisibilityIndicator() {
            key = getKey(this, "VISIBILITY_INDICATOR");
            tooltip = getTooltip(key);
        }
    }

    public enum SoundWaveIndicator {
        SOUND_WAVE_INDICATOR,
        TURN_ON,
        SCALE,
        POSITION,
        POSITION_X(config("sound_wave_indicator", "position.x")),
        POSITION_Y(config("sound_wave_indicator", "position.y")),
        OFFSET_FROM_BOSS_BAR,
        OFFSET_FROM_JADE;

        private final String key;
        private final String tooltip;

        public String key() {
            return key;
        }

        public String tooltip() {
            return tooltip;
        }

        SoundWaveIndicator(String key) {
            this.key = key;
            this.tooltip = getTooltip(key);
        }

        SoundWaveIndicator() {
            key = getKey(this, "SOUND_WAVE_INDICATOR");
            tooltip = getTooltip(key);
        }
    }

    public enum AlertSymbol {
        ALERT_SYMBOL,
        ENABLE,
        SCALE;

        private final String key;
        private final String tooltip;

        public String key() {
            return key;
        }

        public String tooltip() {
            return tooltip;
        }

        AlertSymbol() {
            key = getKey(this, "ALERT_SYMBOL");
            tooltip = getTooltip(key);
        }
    }

    public enum SpyglassMark {
        SPYGLASS_MARK,
        ENABLE,
        MAX_DISTANCE,
        HOSTILE_COLOR,
        NEUTRAL_COLOR,
        ALLY_COLOR,
        NPC_COLOR,
        PASSIVE_COLOR;

        private final String key;
        private final String tooltip;

        public String key() {
            return key;
        }

        public String tooltip() {
            return tooltip;
        }

        SpyglassMark() {
            key = getKey(this, "SPYGLASS_MARK");
            tooltip = getTooltip(key);
        }
    }

    public enum DebugMode {
        DEBUG_MODE,
        ENABLE;

        private final String key;
        private final String tooltip;

        public String key() {
            return key;
        }

        public String tooltip() {
            return tooltip;
        }

        DebugMode() {
            key = getKey(this, "DEBUG_MODE");
            tooltip = getTooltip(key);
        }
    }

    private static String config(String category, String id) {
        return "config." + StealthAndAlert.MOD_ID + "." + category + "." + id;
    }

    private static String config(String name) {
        return "config." + StealthAndAlert.MOD_ID + "." + name;
    }

    private static String getTooltip(String prefix) {
        return prefix + ".tooltip";
    }

    private static String getKey(Enum<?> e, String first) {
        if (e.name().equals(first.toUpperCase())) {
            return config(first.toLowerCase());
        } else {
            return config(first, e.name().toLowerCase());
        }
    }
}
