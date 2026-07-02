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

    // Tooltips
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

    private static String getDeath(String message) {
        return "death.attack." + ".assassination." + message;
    }
}
