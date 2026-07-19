package net.rev.stealthandalert.datagen;

import net.rev.stealthandalert.StealthAndAlert;

public class LangKeys {
    private static final String MOD_ID = StealthAndAlert.MOD_ID;

    // Items
    public static final String PEBBLE = "item." + MOD_ID + ".pebble";
    public static final String CLAMOR_BELL = "item." + MOD_ID + ".clamor_bell";
    public static final String SHADOW_CRYSTAL = "item." + MOD_ID + ".shadow_crystal";
    public static final String SHADOW_CRYSTAL_SHARD = "item." + MOD_ID + ".shadow_crystal_shard";
    public static final String SHADOW_BERRIES = "item." + MOD_ID + ".shadow_berries";
    public static final String SHADOW_CRYSTAL_DAGGER = "item." + MOD_ID + ".shadow_crystal_dagger";
    public static final String DEBUG_WAND = "item." + MOD_ID + ".debug_wand";

    // Blocks
    public static final String PEBBLE_BLOCK = "block." + MOD_ID + ".pebble_block";
    public static final String SHADOW_CRYSTAL_ORE = "block." + MOD_ID + ".shadow_crystal_ore";
    public static final String DEEPSLATE_SHADOW_ORE = "block." + MOD_ID + ".deepslate_shadow_crystal_ore";

    // Effects
    public static final String ETHEREAL = "effect." + MOD_ID + ".ethereal";

    // Potions
    public static final String ETHEREAL_POTION = potion("ethereal");
    public static final String ETHEREAL_SPLASH_POTION = splashPotion("ethereal");
    public static final String ETHEREAL_LINGERING_POTION = lingeringPotion("ethereal");
    public static final String ETHEREAL_ARROW = arrow("ethereal");

    // Enchantments
    public static final String VITAL_PIERCE = "enchantment." + MOD_ID + ".vital_pierce";

    // Attributes
    public static final String VISIBILITY = "attribute.name." + MOD_ID + ".visibility";
    public static final String SOUND_MULTIPLIER = "attribute.name." + MOD_ID + ".sound_multiplier";

    // Tooltips
    public static final String TOOLTIP_ASSASSINATION_DAMAGE = "tooltip." + MOD_ID + ".assassination_damage";
    public static final String TOOLTIP_CAN_ASSASSINATE = "tooltip." + MOD_ID + ".can_assassinate";
    public static final String TOOLTIP_DEBUG_WAND = "tooltip." + MOD_ID + ".debug_wand";
    public static final String TOOLTIP_DEBUG_WAND_DEBUG_MODE_DESC = "tooltip." + MOD_ID + ".debug_wand.debug_mode_desc";

    // Creative Tabs
    public static final String STEALTH_AND_ALERT_ITEMS_TAB = "itemGroup." + MOD_ID + ".items_tab";
    public static final String STEALTH_AND_ALERT_BLOCKS_TAB = "itemGroup." + MOD_ID + ".blocks_tab";

    // Key Mappings
    public static final String CATEGORY = "category." + MOD_ID + "stealth_and_alert";
    public static final String CRAWL = "key." + MOD_ID + ".crawl";
    public static final String ASSASSINATE = "key." + MOD_ID + ".assassinate";

    // Subtitles
    public static final String PEBBLE_LAND = "subtitles." + MOD_ID + ".pebble_land";

    // Death Messages
    public static final String ASSASSINATION = "death.attack.assassination";
    public static final String ASSASSINATION_PLAYER = ASSASSINATION + ".player";
    public static final String ASSASSINATION_ITEM = ASSASSINATION + ".item";
    public static final String ASSASSINATION_ITEM_DUAL = ASSASSINATION + ".item.dual";
    public static final String DAGGER_THROAT_SLIT = getDeath("dagger.throat_slit");
    public static final String DAGGER_THROAT_SLIT_PLAYER = DAGGER_THROAT_SLIT + ".player";
    public static final String DAGGER_THROAT_SLIT_ITEM = DAGGER_THROAT_SLIT + ".item";
    public static final String DAGGET_THROAT_SLIT_ITEM_DUAL = DAGGER_THROAT_SLIT + ".item.dual";
    public static final String TRIDENT_IMPALE = getDeath("trident.impale");
    public static final String TRIDENT_IMPALE_PLAYER = TRIDENT_IMPALE + ".player";
    public static final String TRIDENT_IMPALE_ITEM = TRIDENT_IMPALE + ".item";
    public static final String TRIDENT_IMPALE_ITEM_DUAL = TRIDENT_IMPALE + ".item.dual";
    public static final String MACE_SMASH = getDeath("mace.smash");
    public static final String MACE_SMASH_PLAYER = MACE_SMASH + ".player";
    public static final String MACE_SMASH_ITEM = MACE_SMASH + ".item";
    public static final String MACE_SMASH_ITEM_DUAL = MACE_SMASH + ".item.dual";
    public static final String SWORD_SLASH = getDeath("sword.slash");
    public static final String SWORD_SLASH_PLAYER = SWORD_SLASH + ".player";
    public static final String SWORD_SLASH_ITEM = SWORD_SLASH + ".item";
    public static final String SWORD_SLASH_ITEM_DUAL = SWORD_SLASH + ".item.dual";
    public static final String SWORD_THRUST = getDeath("sword.thrust");
    public static final String SWORD_THRUST_PLAYER = SWORD_THRUST + ".player";
    public static final String SWORD_THRUST_ITEM = SWORD_THRUST + ".item";
    public static final String SWORD_THRUST_ITEM_DUAL = SWORD_THRUST + ".item.dual";
    // GUI Texts
    public static final String GUI_ASSASSINATE = "gui." + MOD_ID + "assassinate";

    // Command Texts
    public static final String COMMAND_MOD_ID = command("mod_id");
    public static final String COMMAND_RELOAD = command("reload");
    public static final String COMMAND_REGENERATE = command("regenerate");
    public static final String COMMAND_FORCE_REGENERATE = command("force_regenerate");

    // Debug Texts
    public static final String DEBUG_MODE_ON = "debug." + MOD_ID + "debug_mode_on";
    public static final String DEBUG_MODE_OFF = "debug." + MOD_ID + "debug_mode_off";
    public static final String DEBUG_ALERT_STATE_IDLE = "debug." + MOD_ID + "alert_state_idle";
    public static final String DEBUG_ALERT_STATE_SUSPICIOUS = "debug." + MOD_ID + "alert_state_suspicious";
    public static final String DEBUG_ALERT_STATE_SEARCHING = "debug." + MOD_ID + "alert_state_searching";
    public static final String DEBUG_ALERT_STATE_FIGHTING = "debug." + MOD_ID + "alert_state_fighting";
    public static final String DEBUG_TARGET_ALERT_STATE_UNTRACKED = "debug." + MOD_ID + "target_alert_state_untracked";
    public static final String DEBUG_TARGET_ALERT_STATE_AWARE = "debug." + MOD_ID + "target_alert_state_aware";
    public static final String DEBUG_TARGET_ALERT_STATE_TRACKING = "debug." + MOD_ID + "target_alert_state_tracking";
    public static final String DEBUG_PRIMARY_TARGET_NULL = "debug." + MOD_ID + "primary_target_null";
    public static final String DEBUG_UNKNOWN = "debug." + MOD_ID + "alert_unknown";
    public static final String DEBUG_HATRED_MEMORY = "debug." + MOD_ID + "hatred_memory";
    public static final String DEBUG_TARGET_ALERT_LEVEL = "debug." + MOD_ID + "target_alert_level";
    public static final String DEBUG_ALERT_STATE_TICKS = "debug." + MOD_ID + "alert_state_ticks";
    public static final String DEBUG_PATIENCE_TICKS = "debug." + MOD_ID + "debug_patience_ticks";

    // Config Texts
    // common
    public static final String DETECTION = config("Detection");
    public static final String DETECTION_TOOLTIP = tooltip(DETECTION);
    public static final String AWARENESS = config("Awareness");
    public static final String AWARENESS_TOOLTIP = tooltip(AWARENESS);
    public static final String ASSASSINATION_C = config("Assassination");
    public static final String ASSASSINATION_C_TOOLTIP = tooltip(ASSASSINATION_C);
    public static final String MAX_DETECTION_RANGE = config("maxDetectionRange");
    public static final String MAX_DETECTION_RANGE_TOOLTIP = tooltip(MAX_DETECTION_RANGE);
    public static final String HORIZONTAL_FOV = config("horizontalFOV");
    public static final String HORIZONTAL_FOV_TOOLTIP = tooltip(HORIZONTAL_FOV);
    public static final String VERTICAL_UP_FOV = config("verticalUpFOV");
    public static final String VERTICAL_UP_FOV_TOOLTIP = tooltip(VERTICAL_UP_FOV);
    public static final String VERTICAL_DOWN_FOV = config("verticalDownFOV");
    public static final String VERTICAL_DOWN_FOV_TOOLTIP = tooltip(VERTICAL_DOWN_FOV);
    public static final String PATIENCE_TICKS = config("patienceTicks");
    public static final String PATIENCE_TICKS_TOOLTIP = tooltip(PATIENCE_TICKS);
    public static final String REACTION_TICKS = config("reactionTicks");
    public static final String REACTION_TICKS_TOOLTIP = tooltip(REACTION_TICKS);
    public static final String TRACKING_TICKS = config("trackingTicks");
    public static final String TRACKING_TICKS_TOOLTIP = tooltip(TRACKING_TICKS);
    public static final String MEMORY_TICKS = config("memoryTicks");
    public static final String MEMORY_TICKS_TOOLTIP = tooltip(MEMORY_TICKS);
    public static final String VISIBILITY_THRESHOLD = config("visibilityThreshold");
    public static final String VISIBILITY_THRESHOLD_TOOLTIP = tooltip(VISIBILITY_THRESHOLD);
    public static final String MIN_INVISIBLE_DISTANCE = config("minInvisibleDistance");
    public static final String MIN_INVISIBLE_DISTANCE_TOOLTIP = tooltip(MIN_INVISIBLE_DISTANCE);
    public static final String MIN_INVISIBLE_DISTANCE_TO_TRACKING = config("minInvisibleDistanceToTracking");
    public static final String MIN_INVISIBLE_DISTANCE_TO_TRACKING_TOOLTIP = tooltip(MIN_INVISIBLE_DISTANCE_TO_TRACKING);
    public static final String INCREASE_BASIC_RATE = config("increaseBasicRate");
    public static final String INCREASE_BASIC_RATE_TOOLTIP = tooltip(INCREASE_BASIC_RATE);
    public static final String INCREASE_VISIBILITY_FACTOR = config("increaseVisibilityFactor");
    public static final String INCREASE_VISIBILITY_FACTOR_TOOLTIP = tooltip(INCREASE_VISIBILITY_FACTOR);
    public static final String INCREASE_DISTANCE_FACTOR = config("increaseDistanceFactor");
    public static final String INCREASE_DISTANCE_FACTOR_TOOLTIP = tooltip(INCREASE_DISTANCE_FACTOR);
    public static final String INCREASE_SUSPICIOUS_FACTOR = config("increaseSuspiciousFactor");
    public static final String INCREASE_SUSPICIOUS_FACTOR_TOOLTIP = tooltip(INCREASE_SUSPICIOUS_FACTOR);
    public static final String INCREASE_SEARCHING_FACTOR = config("increaseSearchingFactor");
    public static final String INCREASE_SEARCHING_FACTOR_TOOLTIP = tooltip(INCREASE_SEARCHING_FACTOR);
    public static final String DECREASE_BASIC_RATE = config("decreaseBasicRate");
    public static final String DECREASE_BASIC_RATE_TOOLTIP = tooltip(DECREASE_BASIC_RATE);
    public static final String DECREASE_SUSPICIOUS_FACTOR = config("decreaseSuspiciousFactor");
    public static final String DECREASE_SUSPICIOUS_FACTOR_TOOLTIP = tooltip(DECREASE_SUSPICIOUS_FACTOR);
    public static final String DECREASE_SEARCHING_FACTOR = config("decreaseSearchingFactor");
    public static final String DECREASE_SEARCHING_FACTOR_TOOLTIP = tooltip(DECREASE_SEARCHING_FACTOR);
    public static final String ALWAYS_SUCCESS = config("alwaysSuccess");
    public static final String ALWAYS_SUCCESS_TOOLTIP = tooltip(ALWAYS_SUCCESS);
    public static final String SUCCESS_CHANCE = config("successChance");
    public static final String SUCCESS_CHANCE_TOOLTIP = tooltip(SUCCESS_CHANCE);
    public static final String CAN_PETS_BE_ASSASSINATED = config("canPetsBeAssassinated");
    public static final String CAN_PETS_BE_ASSASSINATED_TOOLTIP = tooltip(CAN_PETS_BE_ASSASSINATED);
    public static final String CAN_ANIMALS_BE_ASSASSINATED = config("canAnimalsBeAssassinated");
    public static final String CAN_ANIMALS_BE_ASSASSINATED_TOOLTIP = tooltip(CAN_ANIMALS_BE_ASSASSINATED);
    public static final String CAN_ANIMAL_SEEKERS_BE_ASSASSINATED = config("canAnimalSeekersBeAssassinated");
    public static final String CAN_ANIMAL_SEEKERS_BE_ASSASSINATED_TOOLTIP = tooltip(CAN_ANIMAL_SEEKERS_BE_ASSASSINATED);
    public static final String CAN_VILLAGERS_BE_ASSASSINATED = config("canVillagersBeAssassinated");
    public static final String CAN_VILLAGERS_BE_ASSASSINATED_TOOLTIP = tooltip(CAN_VILLAGERS_BE_ASSASSINATED);
    public static final String CAN_BOSSES_BE_ASSASSINATED = config("canBossesBeAssassinated");
    public static final String CAN_BOSSES_BE_ASSASSINATED_TOOLTIP = tooltip(CAN_BOSSES_BE_ASSASSINATED);
    public static final String CAN_PLAYERS_BE_ASSASSINATED = config("canPlayersBeAssassinated");
    public static final String CAN_PLAYERS_BE_ASSASSINATED_TOOLTIP = tooltip(CAN_PLAYERS_BE_ASSASSINATED);
    // client
    public static final String ALERT_INDICATOR = config("alertIndicator");
    public static final String ALERT_INDICATOR_TOOLTIP = tooltip(ALERT_INDICATOR);
    public static final String VISIBILITY_INDICATOR = config("visibilityIndicator");
    public static final String VISIBILITY_INDICATOR_TOOLTIP = tooltip(VISIBILITY_INDICATOR);
    public static final String SOUND_WAVE_INDICATOR = config("soundWaveIndicator");
    public static final String SOUND_WAVE_INDICATOR_TOOLTIP = tooltip(SOUND_WAVE_INDICATOR);
    public static final String ALERT_SYMBOL = config("alertSymbol");
    public static final String ALERT_SYMBOL_TOOLTIP = tooltip(ALERT_SYMBOL);
    public static final String DEBUG_MODE = config("debugMode");
    public static final String DEBUG_MODE_TOOLTIP = tooltip(DEBUG_MODE);
    public static final String ALERT_INDICATOR_TURN_ON = ALERT_INDICATOR + ".turnOn";
    public static final String ALERT_INDICATOR_TURN_ON_TOOLTIP = tooltip(ALERT_INDICATOR_TURN_ON);
    public static final String RADIUS = ALERT_INDICATOR + ".radius";
    public static final String RADIUS_TOOLTIP = tooltip(RADIUS);
    public static final String VISIBILITY_INDICATOR_TURN_ON = VISIBILITY_INDICATOR + ".turnOn";
    public static final String VISIBILITY_INDICATOR_TURN_ON_TOOLTIP = tooltip(VISIBILITY_INDICATOR_TURN_ON);
    public static final String VISIBILITY_SCALE = VISIBILITY_INDICATOR + ".scale";
    public static final String VISIBILITY_SCALE_TOOLTIP = tooltip(VISIBILITY_SCALE);
    public static final String VISIBILITY_INDICATOR_POSITION = VISIBILITY_INDICATOR + ".position";
    public static final String VISIBILITY_INDICATOR_POSITION_TOOLTIP = tooltip(VISIBILITY_INDICATOR_POSITION);
    public static final String VISIBILITY_INDICATOR_POSITION_X = VISIBILITY_INDICATOR + ".position.x";
    public static final String VISIBILITY_INDICATOR_POSITION_X_TOOLTIP = tooltip(VISIBILITY_INDICATOR_POSITION_X);
    public static final String VISIBILITY_INDICATOR_POSITION_Y = VISIBILITY_INDICATOR + ".position.y";
    public static final String VISIBILITY_INDICATOR_POSITION_Y_TOOLTIP = tooltip(VISIBILITY_INDICATOR_POSITION_Y);
    public static final String VISIBILITY_INDICATOR_BOSS_BAR = VISIBILITY_INDICATOR + ".canOffsetFromBossBar";
    public static final String VISIBILITY_INDICATOR_BOSS_BAR_TOOLTIP = tooltip(VISIBILITY_INDICATOR_BOSS_BAR);
    public static final String SOUND_WAVE_INDICATOR_TURN_ON = SOUND_WAVE_INDICATOR + ".turnOn";
    public static final String SOUND_WAVE_INDICATOR_TURN_ON_TOOLTIP = tooltip(SOUND_WAVE_INDICATOR_TURN_ON);
    public static final String SOUND_SCALE = SOUND_WAVE_INDICATOR + ".scale";
    public static final String SOUND_SCALE_TOOLTIP = tooltip(SOUND_SCALE);
    public static final String SOUND_WAVE_INDICATOR_POSITION = SOUND_WAVE_INDICATOR + ".position";
    public static final String SOUND_WAVE_INDICATOR_POSITION_TOOLTIP = tooltip(SOUND_WAVE_INDICATOR_POSITION);
    public static final String SOUND_WAVE_INDICATOR_POSITION_X = SOUND_WAVE_INDICATOR + ".position.x";
    public static final String SOUND_WAVE_INDICATOR_POSITION_X_TOOLTIP = tooltip(SOUND_WAVE_INDICATOR_POSITION_X);
    public static final String SOUND_WAVE_INDICATOR_POSITION_Y = SOUND_WAVE_INDICATOR + ".position.y";
    public static final String SOUND_WAVE_INDICATOR_POSITION_Y_TOOLTIP = tooltip(SOUND_WAVE_INDICATOR_POSITION_Y);
    public static final String SOUND_BOSS_BAR = SOUND_WAVE_INDICATOR + ".canOffsetFromBossBar";
    public static final String SOUND_BOSS_BAR_TOOLTIP = tooltip(SOUND_BOSS_BAR);
    public static final String ALERT_SYMBOL_TURN_ON = ALERT_SYMBOL + ".turnOn";
    public static final String ALERT_SYMBOL_TURN_ON_TOOLTIP = tooltip(ALERT_SYMBOL_TURN_ON);
    public static final String ALERT_SYMBOL_SCALE = ALERT_SYMBOL + ".scale";
    public static final String ALERT_SYMBOL_SCALE_TOOLTIP = tooltip(ALERT_SYMBOL_SCALE);
    public static final String DEBUG_TURN_ON = DEBUG_MODE + ".turnOn";
    public static final String DEBUG_TURN_ON_TOOLTIP = tooltip(DEBUG_TURN_ON);

    private static String command(String name) {
        return "commands." + MOD_ID + "." + name;
    }

    private static String config(String name) {
        return "config." + MOD_ID + "." + name;
    }

    private static String tooltip(String prefix) {
        return prefix + ".tooltip";
    }

    private static String potion(String name) {
        return "item.minecraft.potion.effect." + MOD_ID + "." + name;
    }

    private static String splashPotion(String name) {
        return "item.minecraft.splash_potion.effect." + MOD_ID + "." + name;
    }

    private static String lingeringPotion(String name) {
        return "item.minecraft.lingering_potion.effect." + MOD_ID + "." + name;
    }

    private static String arrow(String name) {
        return "item.minecraft.tipped_arrow.effect." + MOD_ID + "." + name;
    }

    private static String getDeath(String message) {
        return "death.attack." + ".assassination." + message;
    }
}
