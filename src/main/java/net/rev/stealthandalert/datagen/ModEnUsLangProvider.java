package net.rev.stealthandalert.datagen;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.rev.stealthandalert.StealthAndAlert;

public class ModEnUsLangProvider extends LanguageProvider {
    public ModEnUsLangProvider(PackOutput output, String locale) {
        super(output, StealthAndAlert.MOD_ID, locale);
    }

    @Override
    protected void addTranslations() {
        // Items
        add(LangKeys.PEBBLE, "Pebble");
        add(LangKeys.CLAMOR_BELL, "Clamor Bell [WIP]");
        add(LangKeys.SHADOW_CRYSTAL, "Shadow Crystal");
        add(LangKeys.SHADOW_CRYSTAL_SHARD, "Shadow Crystal Shard");
        add(LangKeys.SHADOW_BERRIES, "Shadow Berries");
        add(LangKeys.SHADOW_CRYSTAL_DAGGER, "Shadow Crystal Dagger");
        add(LangKeys.DEBUG_WAND, "Debug Wand");

        // Blocks
        add(LangKeys.PEBBLE_BLOCK, "Pebble Block");
        add(LangKeys.SHADOW_CRYSTAL_ORE, "Shadow Crystal Ore");
        add(LangKeys.DEEPSLATE_SHADOW_ORE, "Deepslate Shadow Crystal Ore");

        // Effects
        add(LangKeys.ETHEREAL, "Ethereal");

        // Potions
        add(LangKeys.ETHEREAL_POTION, "Potion of Ethereal");
        add(LangKeys.ETHEREAL_SPLASH_POTION, "Splash Potion of Ethereal");
        add(LangKeys.ETHEREAL_LINGERING_POTION, "Lingering Potion of Ethereal");
        add(LangKeys.ETHEREAL_ARROW, "Arrow of Ethereal");

        // Enchantments
        add(LangKeys.VITAL_PIERCE, "Vital Pierce");

        // Attributes
        add(LangKeys.VISIBILITY, "Visibility");
        add(LangKeys.SOUND_MULTIPLIER, "Sound Multiplier");

        // Tooltips
        add(LangKeys.TOOLTIP_ASSASSINATION_DAMAGE, "Assassination Damage");
        add(LangKeys.TOOLTIP_CAN_ASSASSINATE, "Assassination-Capable");
        add(LangKeys.TOOLTIP_DEBUG_WAND, "Right-click to toggle debug mode");
        add(LangKeys.TOOLTIP_DEBUG_WAND_DEBUG_MODE_DESC, "Debug Mode: Displays alert information for mobs with the SEEKERS tag:§c\nGlobal Alert State\nObservation State\nPrimary Target\nMemory Ticks\nAwareness Progress\nState Transition Ticks\nPatience Ticks");

        // Creative Tabs
        add(LangKeys.STEALTH_AND_ALERT_ITEMS_TAB, "Stealth and Alert - Items");
        add(LangKeys.STEALTH_AND_ALERT_BLOCKS_TAB, "Stealth and Alert - Blocks");

        // Key Mappings
        add(LangKeys.CATEGORY, "Stealth and Alert");
        add(LangKeys.CRAWL, "Crawl");
        add(LangKeys.ASSASSINATE, "Assassinate");

        // Subtitles
        add(LangKeys.PEBBLE_LAND, "Pebble lands");

        // Death Messages
        add(LangKeys.ASSASSINATION, "%1$s didn't live to see tomorrow");
        add(LangKeys.ASSASSINATION_PLAYER, "%1$s was completely oblivious to %2$s creeping up");
        add(LangKeys.ASSASSINATION_ITEM, "%1$s's life was quietly stolen away by %2$s with %3$s");
        add(LangKeys.ASSASSINATION_ITEM_DUAL, "%1$s's final sight was %2$s brandishing %3$s and %4$s");
        add(LangKeys.DAGGER_THROAT_SLIT, "%1$s's throat was slitted");
        add(LangKeys.DAGGER_THROAT_SLIT_PLAYER, "%1$s's throat was slitted by %2$s");
        add(LangKeys.DAGGER_THROAT_SLIT_ITEM, "%1$s's throat was slitted by %2$s using %3$s");
        add(LangKeys.DAGGET_THROAT_SLIT_ITEM_DUAL, "%1$s received an unconsented throat surgery from %2$s using %3$s and %4$s");
        add(LangKeys.TRIDENT_IMPALE, "%1$s was skewered clean through");
        add(LangKeys.TRIDENT_IMPALE_PLAYER, "%1$s was skewered clean through by %2$s");
        add(LangKeys.TRIDENT_IMPALE_ITEM, "%1$s was skewered clean through by %2$s using %3$s");
        add(LangKeys.TRIDENT_IMPALE_ITEM_DUAL, "%1$s was left with two gaping holes by %2$s using %3$s and %4$s");
        add(LangKeys.MACE_SMASH, "%1$s was smashed into a bloody pulp");
        add(LangKeys.MACE_SMASH_PLAYER, "%1$s was smashed into a bloody pulp by %2$s");
        add(LangKeys.MACE_SMASH_ITEM, "%1$s was smashed into a bloody pulp by %2$s using %3$s");
        add(LangKeys.MACE_SMASH_ITEM_DUAL, "%1$s's skull was shattered instantly by %2$s's %3$s and %4$s");
        add(LangKeys.SWORD_SLASH, "%1$s was left covered in slash marks");
        add(LangKeys.SWORD_SLASH_PLAYER, "%1$s was cut to pieces by %2$s's blade");
        add(LangKeys.SWORD_SLASH_ITEM, "%1$s was torn apart by %2$s using %3$s before even realizing the danger");
        add(LangKeys.SWORD_SLASH_ITEM_DUAL, "%1$s was tortured to death by %2$s's relentless slicing with %3$s and %4$s");
        add(LangKeys.SWORD_THRUST, "%1$s was impaled by a blade");
        add(LangKeys.SWORD_THRUST_PLAYER, "%1$s was impaled by %2$s's blade");
        add(LangKeys.SWORD_THRUST_ITEM, "%1$s was precisely struck in the vitals by %2$s using %3$s");
        add(LangKeys.SWORD_THRUST_ITEM_DUAL, "%1$s had their vitals pierced by %2$s using %3$s and %4$s");

        // GUI
        add(LangKeys.GUI_ASSASSINATE, "[%s] Assassinate");

        // Commands
        add(LangKeys.COMMAND_MOD_ID, "[Stealth & Alert]");
        add(LangKeys.COMMAND_RELOAD, "Reloaded configuration (presets not generated)");
        add(LangKeys.COMMAND_REGENERATE, "Generated missing presets and reloaded configuration");
        add(LangKeys.COMMAND_FORCE_REGENERATE, "Forcefully overwritten all presets and reloaded configuration");

        // Debug Texts
        add(LangKeys.DEBUG_MODE_ON, "§aDEBUG MODE: ON");
        add(LangKeys.DEBUG_MODE_OFF, "§cDEBUG MODE: OFF");
        add(LangKeys.DEBUG_ALERT_STATE_IDLE, "§7IDLE");
        add(LangKeys.DEBUG_ALERT_STATE_SUSPICIOUS, "§fSUSPICIOUS");
        add(LangKeys.DEBUG_ALERT_STATE_SEARCHING, "§6SEARCHING");
        add(LangKeys.DEBUG_ALERT_STATE_FIGHTING, "§cFIGHTING");
        add(LangKeys.DEBUG_TARGET_ALERT_STATE_UNTRACKED, "§7UNTRACKED");
        add(LangKeys.DEBUG_TARGET_ALERT_STATE_AWARE, "§fAWARE");
        add(LangKeys.DEBUG_TARGET_ALERT_STATE_TRACKING, "§cTRACKING");
        add(LangKeys.DEBUG_PRIMARY_TARGET_NULL, "NO_PRIMARY_TARGET");
        add(LangKeys.DEBUG_UNKNOWN, "UNKNOWN");
        add(LangKeys.DEBUG_HATRED_MEMORY, "HATRED_MEMORY: %d");
        add(LangKeys.DEBUG_TARGET_ALERT_LEVEL, "§bALERT_LEVEL: %.1f%%");
        add(LangKeys.DEBUG_ALERT_STATE_TICKS, "§eALERT_STATE_TICKS: %d");
        add(LangKeys.DEBUG_PATIENCE_TICKS, "§dPATIENCE_TICKS: %d");

        // Configs
        // Common
        add(LangKeys.DETECTION, "Detection");
        add(LangKeys.DETECTION_TOOLTIP, "Settings related to enemy vision, FOV, and detection ranges");
        add(LangKeys.AWARENESS, "Awareness");
        add(LangKeys.AWARENESS_TOOLTIP, "Settings related to the increase and decrease rates of enemy awareness");
        add(LangKeys.ASSASSINATION_C, "Assassination");
        add(LangKeys.ASSASSINATION_C_TOOLTIP, "Settings related to assassination mechanics");
        add(LangKeys.MAX_DETECTION_RANGE, "Max Detection Range");
        add(LangKeys.MAX_DETECTION_RANGE_TOOLTIP, "The maximum distance at which enemies can see the player");
        add(LangKeys.HORIZONTAL_FOV, "Horizontal FOV");
        add(LangKeys.HORIZONTAL_FOV_TOOLTIP, "The horizontal field of view of enemies (in degrees)");
        add(LangKeys.VERTICAL_UP_FOV, "Upward FOV");
        add(LangKeys.VERTICAL_UP_FOV_TOOLTIP, "The vertical upward field of view of enemies (in degrees)");
        add(LangKeys.VERTICAL_DOWN_FOV, "Downward FOV");
        add(LangKeys.VERTICAL_DOWN_FOV_TOOLTIP, "The vertical downward field of view of enemies (in degrees)");
        add(LangKeys.PATIENCE_TICKS, "Patience Duration");
        add(LangKeys.PATIENCE_TICKS_TOOLTIP, "The duration of patience before an enemy loses interest in an LKP(Last Known Position) (in ticks)");
        add(LangKeys.REACTION_TICKS, "Reaction Duration");
        add(LangKeys.REACTION_TICKS_TOOLTIP, "The reaction time required for an enemy to fully perceive a player after spotting them (in ticks)");
        add(LangKeys.TRACKING_TICKS, "Tracking Duration");
        add(LangKeys.TRACKING_TICKS_TOOLTIP, "The duration before an enemy loses track of a player since unable to see them (in ticks)");
        add(LangKeys.MEMORY_TICKS, "Memory Duration");
        add(LangKeys.MEMORY_TICKS_TOOLTIP, "The duration of an enemy's memory towards a player who has enraged it (in ticks)");
        add(LangKeys.VISIBILITY_THRESHOLD, "Visibility Threshold");
        add(LangKeys.VISIBILITY_THRESHOLD_TOOLTIP, "The visibility threshold for players to enter a fully concealed state (*100%)");
        add(LangKeys.MIN_INVISIBLE_DISTANCE, "Min Invisible Distance");
        add(LangKeys.MIN_INVISIBLE_DISTANCE_TOOLTIP, "The distance within which complete concealment fails against enemies");
        add(LangKeys.MIN_INVISIBLE_DISTANCE_TO_TRACKING, "Min Invisible Distance (Tracking)");
        add(LangKeys.MIN_INVISIBLE_DISTANCE_TO_TRACKING_TOOLTIP, "The distance within which complete concealment fails against tracking enemies");
        add(LangKeys.INCREASE_BASIC_RATE, "Basic Increase Rate");
        add(LangKeys.INCREASE_BASIC_RATE_TOOLTIP, "The basic rate at which awareness increases when an enemy spots a player");
        add(LangKeys.INCREASE_VISIBILITY_FACTOR, "Increase Visibility Factor");
        add(LangKeys.INCREASE_VISIBILITY_FACTOR_TOOLTIP, "The factor by which the player's visibility affects the awareness increase rate");
        add(LangKeys.INCREASE_DISTANCE_FACTOR, "Increase Distance Factor");
        add(LangKeys.INCREASE_DISTANCE_FACTOR_TOOLTIP, "The factor by which the distance between the player and enemy affects the awareness increase rate");
        add(LangKeys.INCREASE_SUSPICIOUS_FACTOR, "Increase Suspicious Factor");
        add(LangKeys.INCREASE_SUSPICIOUS_FACTOR_TOOLTIP, "The factor by which the enemy's suspicious state affects the awareness increase rate");
        add(LangKeys.INCREASE_SEARCHING_FACTOR, "Increase Searching Factor");
        add(LangKeys.INCREASE_SEARCHING_FACTOR_TOOLTIP, "The factor by which the enemy's searching state affects the awareness increase rate");
        add(LangKeys.DECREASE_BASIC_RATE, "Basic Decrease Rate");
        add(LangKeys.DECREASE_BASIC_RATE_TOOLTIP, "The basic rate at which awareness decreases when an enemy loses track of the player");
        add(LangKeys.DECREASE_SUSPICIOUS_FACTOR, "Decrease Suspicious Factor");
        add(LangKeys.DECREASE_SUSPICIOUS_FACTOR_TOOLTIP, "The factor by which the enemy's suspicious state affects the awareness decrease rate");
        add(LangKeys.DECREASE_SEARCHING_FACTOR, "Decrease Searching Factor");
        add(LangKeys.DECREASE_SEARCHING_FACTOR_TOOLTIP, "The factor by which the enemy's searching state affects the awareness decrease rate");
        add(LangKeys.ALWAYS_SUCCESS, "Always Success");
        add(LangKeys.ALWAYS_SUCCESS_TOOLTIP, "Whether assassinations to SEEKERS can be performed successfully all the time");
        add(LangKeys.SUCCESS_CHANCE, "Success Chance");
        add(LangKeys.SUCCESS_CHANCE_TOOLTIP, "If Assassination Always Success is false, the chance of successfully performing an assassination to SEEKERS");
        add(LangKeys.CAN_PETS_BE_ASSASSINATED, "Assassinate Pets");
        add(LangKeys.CAN_PETS_BE_ASSASSINATED_TOOLTIP, "Whether pets can be assassinated by their owners");
        add(LangKeys.CAN_ANIMALS_BE_ASSASSINATED, "Assassinate Animals (Normal)");
        add(LangKeys.CAN_ANIMALS_BE_ASSASSINATED_TOOLTIP, "Whether normal animals (excluding those in the SEEKERS tag) can be assassinated");
        add(LangKeys.CAN_ANIMAL_SEEKERS_BE_ASSASSINATED, "Assassinate Animals (SEEKERS)");
        add(LangKeys.CAN_ANIMAL_SEEKERS_BE_ASSASSINATED_TOOLTIP, "Whether animals in the SEEKERS tag can be assassinated");
        add(LangKeys.CAN_VILLAGERS_BE_ASSASSINATED, "Assassinate Villagers");
        add(LangKeys.CAN_VILLAGERS_BE_ASSASSINATED_TOOLTIP, "Whether villagers (including wandering traders) can be assassinated");
        add(LangKeys.CAN_BOSSES_BE_ASSASSINATED, "Assassinate Bosses");
        add(LangKeys.CAN_BOSSES_BE_ASSASSINATED_TOOLTIP, "Whether bosses (if in the CAN_BE_ASSASSINATED tag) can be assassinated");
        add(LangKeys.CAN_PLAYERS_BE_ASSASSINATED, "Assassinate Players");
        add(LangKeys.CAN_PLAYERS_BE_ASSASSINATED_TOOLTIP, "Whether players can assassinate each other");
        // Client
        add(LangKeys.ALERT_INDICATOR, "Alert Indicator");
        add(LangKeys.ALERT_INDICATOR_TOOLTIP, "Settings for the alert indicator HUD, which displays the alert level of surrounding enemies");
        add(LangKeys.VISIBILITY_INDICATOR, "Visibility Indicator");
        add(LangKeys.VISIBILITY_INDICATOR_TOOLTIP, "Settings for the visibility indicator HUD, which displays your current visibility");
        add(LangKeys.SOUND_WAVE_INDICATOR, "Sound Wave Indicator");
        add(LangKeys.SOUND_WAVE_INDICATOR_TOOLTIP, "Settings for the sound wave indicator HUD, which displays the sound you are producing");
        add(LangKeys.ALERT_SYMBOL, "Alert Symbol");
        add(LangKeys.ALERT_SYMBOL_TOOLTIP, "Settings for the world-space alert symbol, which displays the current alert status of enemies");
        add(LangKeys.DEBUG_MODE, "Debug Mode");
        add(LangKeys.DEBUG_MODE_TOOLTIP, "Settings for Debug Mode");
        add(LangKeys.ALERT_INDICATOR_TURN_ON, "Enable Alert Indicator");
        add(LangKeys.ALERT_INDICATOR_TURN_ON_TOOLTIP, "Whether to display the alert indicator around the crosshair");
        add(LangKeys.RADIUS, "Radius");
        add(LangKeys.RADIUS_TOOLTIP, "The distance between the alert indicator and the crosshair");
        add(LangKeys.VISIBILITY_INDICATOR_TURN_ON, "Enable Visibility Indicator");
        add(LangKeys.VISIBILITY_INDICATOR_TURN_ON_TOOLTIP, "Whether to display the visibility indicator on the screen");
        add(LangKeys.VISIBILITY_SCALE, "Scale");
        add(LangKeys.VISIBILITY_SCALE_TOOLTIP, "The scale of the visibility indicator");
        add(LangKeys.VISIBILITY_INDICATOR_POSITION, "Position");
        add(LangKeys.VISIBILITY_INDICATOR_POSITION_TOOLTIP, "The position offset of the visibility indicator on the screen");
        add(LangKeys.VISIBILITY_INDICATOR_POSITION_X, "X");
        add(LangKeys.VISIBILITY_INDICATOR_POSITION_X_TOOLTIP, "The X offset of the visibility indicator on the screen");
        add(LangKeys.VISIBILITY_INDICATOR_POSITION_Y, "Y");
        add(LangKeys.VISIBILITY_INDICATOR_POSITION_Y_TOOLTIP, "The Y offset of the visibility indicator on the screen");
        add(LangKeys.VISIBILITY_INDICATOR_BOSS_BAR, "Offset from Boss Bar");
        add(LangKeys.VISIBILITY_INDICATOR_BOSS_BAR_TOOLTIP, "Whether the visibility indicator offsets when a boss bar is visible. Recommended to turn off if you have manually adjusted the position");
        add(LangKeys.SOUND_WAVE_INDICATOR_TURN_ON, "Enable Sound Wave Indicator");
        add(LangKeys.SOUND_WAVE_INDICATOR_TURN_ON_TOOLTIP, "Whether to display the sound wave indicator on the screen");
        add(LangKeys.SOUND_SCALE, "Scale");
        add(LangKeys.SOUND_SCALE_TOOLTIP, "The scale of the sound wave indicator");
        add(LangKeys.SOUND_WAVE_INDICATOR_POSITION, "Position");
        add(LangKeys.SOUND_WAVE_INDICATOR_POSITION_TOOLTIP, "The position offset of the sound wave indicator on the screen");
        add(LangKeys.SOUND_WAVE_INDICATOR_POSITION_X, "X");
        add(LangKeys.SOUND_WAVE_INDICATOR_POSITION_X_TOOLTIP, "The X offset of the sound wave indicator on the screen");
        add(LangKeys.SOUND_WAVE_INDICATOR_POSITION_Y, "Y");
        add(LangKeys.SOUND_WAVE_INDICATOR_POSITION_Y_TOOLTIP, "The Y offset of the sound wave indicator on the screen");
        add(LangKeys.SOUND_BOSS_BAR, "Offset from Boss Bar");
        add(LangKeys.SOUND_BOSS_BAR_TOOLTIP, "Whether the sound wave indicator offsets when a boss bar is visible. Recommended to turn off if you have manually adjusted the position");
        add(LangKeys.ALERT_SYMBOL_TURN_ON, "Enable Alert Symbol");
        add(LangKeys.ALERT_SYMBOL_TURN_ON_TOOLTIP, "Whether to display the alert symbol above enemies' heads");
        add(LangKeys.ALERT_SYMBOL_SCALE, "Scale");
        add(LangKeys.ALERT_SYMBOL_SCALE_TOOLTIP, "The scale of the alert symbol");
        add(LangKeys.DEBUG_TURN_ON, "Enable Debug Mode");
        add(LangKeys.DEBUG_TURN_ON_TOOLTIP, "Whether to display detailed alert status information above enemies' heads");
    }
}
