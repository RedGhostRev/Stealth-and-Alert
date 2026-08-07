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
        add(LangKeys.MUSIC_DISC_DAISY_BELL, "Music Disc");
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
        add(LangKeys.TOOLTIP_MUSIC_DISC_DAISY_BELL, "Harry Dacre - Daisy Bell");

        // Creative Tabs
        add(LangKeys.STEALTH_AND_ALERT_ITEMS_TAB, "Stealth and Alert - Items");
        add(LangKeys.STEALTH_AND_ALERT_BLOCKS_TAB, "Stealth and Alert - Blocks");

        // Key Mappings
        add(LangKeys.CATEGORY, "Stealth and Alert");
        add(LangKeys.CRAWL, "Crawl");
        add(LangKeys.ASSASSINATE, "Assassinate");
        add(LangKeys.MARK, "Mark with Spyglass");
        add(LangKeys.EDIT_HUDS, "Edit HUDs Visually");

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
        add(LangKeys.GUI_MARK, "[%s] Mark");
        add(LangKeys.GUI_UNMARK, "[%s] Unmark");
        // Jade
        add(LangKeys.JADE_CONFIG, "Alert Information");
        add(LangKeys.JADE_ALERT_STATE, "Alert State: ");
        add(LangKeys.JADE_ALERT_AWARENESS, "Alert Awareness: %s%%");

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
        add(LangKeys.DEBUG_ALERT_STATE_SEARCHING, "§eSEARCHING");
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
        add(ConfigKeys.Detection.DETECTION.key(), "Detection");
        add(ConfigKeys.Detection.DETECTION.tooltip(), "Settings related to enemy vision, FOV, and detection ranges");
        add(ConfigKeys.Detection.MAX_RANGE.key(), "Max Detection Range");
        add(ConfigKeys.Detection.MAX_RANGE.tooltip(), "The maximum distance at which enemies can see the player");
        add(ConfigKeys.Detection.HORIZONTAL_FOV.key(), "Horizontal FOV");
        add(ConfigKeys.Detection.HORIZONTAL_FOV.tooltip(), "The horizontal field of view of enemies (in degrees)");
        add(ConfigKeys.Detection.VERTICAL_UP_FOV.key(), "Upward FOV");
        add(ConfigKeys.Detection.VERTICAL_UP_FOV.tooltip(), "The vertical upward field of view of enemies (in degrees)");
        add(ConfigKeys.Detection.VERTICAL_DOWN_FOV.key(), "Downward FOV");
        add(ConfigKeys.Detection.VERTICAL_DOWN_FOV.tooltip(), "The vertical downward field of view of enemies (in degrees)");
        add(ConfigKeys.Detection.PATIENCE_TICKS.key(), "Patience Duration");
        add(ConfigKeys.Detection.PATIENCE_TICKS.tooltip(), "The duration of patience before an enemy loses interest in an LKP(Last Known Position) (in ticks)");
        add(ConfigKeys.Detection.REACTION_TICKS.key(), "Reaction Duration");
        add(ConfigKeys.Detection.REACTION_TICKS.tooltip(), "The reaction time required for an enemy to fully perceive a player after spotting them (in ticks)");
        add(ConfigKeys.Detection.TRACKING_TICKS.key(), "Tracking Duration");
        add(ConfigKeys.Detection.TRACKING_TICKS.tooltip(), "The duration before an enemy loses track of a player since unable to see them (in ticks)");
        add(ConfigKeys.Detection.MEMORY_TICKS.key(), "Memory Duration");
        add(ConfigKeys.Detection.MEMORY_TICKS.tooltip(), "The duration of an enemy's memory towards a player who has enraged it (in ticks)");
        add(ConfigKeys.Detection.VISIBILITY_THRESHOLD.key(), "Visibility Threshold");
        add(ConfigKeys.Detection.VISIBILITY_THRESHOLD.tooltip(), "The visibility threshold for players to enter a fully concealed state (*100%)");
        add(ConfigKeys.Detection.VISIBILITY_MAX_DETECTION_RANGE_REDUCTION_PERCENTAGE.key(), "Visibility Max Detection Range Reduction Percentage");
        add(ConfigKeys.Detection.VISIBILITY_MAX_DETECTION_RANGE_REDUCTION_PERCENTAGE.tooltip(), "The max reduction percentage of an enemy's detection range influenced by visibility");
        add(ConfigKeys.Detection.VISIBILITY_DETECTION_RANGE_REDUCTION_MODEL.key(), "Visibility Detection Range Reduction Model");
        add(ConfigKeys.Detection.VISIBILITY_DETECTION_RANGE_REDUCTION_MODEL.tooltip(), """
                The mathematical model for reduction of enemies' detection range influenced by visibility
                LINEAR: Steadily reducing
                SQUARE ROOT: Reducing quickly at first, then slowly
                SMOOTHSTEP: Reducing slowly at first and end, quickly at medium""");
        add(ConfigKeys.Detection.MIN_INVISIBLE_DISTANCE.key(), "Min Invisible Distance");
        add(ConfigKeys.Detection.MIN_INVISIBLE_DISTANCE.tooltip(), "The distance within which complete concealment fails against enemies");
        add(ConfigKeys.Detection.MIN_INVISIBLE_DISTANCE_TO_TRACKING.key(), "Min Invisible Distance (Tracking)");
        add(ConfigKeys.Detection.MIN_INVISIBLE_DISTANCE_TO_TRACKING.tooltip(), "The distance within which complete concealment fails against tracking enemies");

        add(ConfigKeys.Awareness.AWARENESS.key(), "Awareness");
        add(ConfigKeys.Awareness.AWARENESS.tooltip(), "Settings related to the increase and decrease rates of enemy awareness");
        add(ConfigKeys.Awareness.INCREASE_BASIC_RATE.key(), "Basic Increase Rate");
        add(ConfigKeys.Awareness.INCREASE_BASIC_RATE.tooltip(), "The basic rate at which awareness increases when an enemy spots a player");
        add(ConfigKeys.Awareness.INCREASE_VISIBILITY_FACTOR.key(), "Increase Visibility Factor");
        add(ConfigKeys.Awareness.INCREASE_VISIBILITY_FACTOR.tooltip(), "The factor by which the player's visibility affects the awareness increase rate");
        add(ConfigKeys.Awareness.INCREASE_DISTANCE_FACTOR.key(), "Increase Distance Factor");
        add(ConfigKeys.Awareness.INCREASE_DISTANCE_FACTOR.tooltip(), "The factor by which the distance between the player and enemy affects the awareness increase rate");
        add(ConfigKeys.Awareness.INCREASE_SUSPICIOUS_FACTOR.key(), "Increase Suspicious Factor");
        add(ConfigKeys.Awareness.INCREASE_SUSPICIOUS_FACTOR.tooltip(), "The factor by which the enemy's suspicious state affects the awareness increase rate");
        add(ConfigKeys.Awareness.INCREASE_SEARCHING_FACTOR.key(), "Increase Searching Factor");
        add(ConfigKeys.Awareness.INCREASE_SEARCHING_FACTOR.tooltip(), "The factor by which the enemy's searching state affects the awareness increase rate");
        add(ConfigKeys.Awareness.DECREASE_BASIC_RATE.key(), "Basic Decrease Rate");
        add(ConfigKeys.Awareness.DECREASE_BASIC_RATE.tooltip(), "The basic rate at which awareness decreases when an enemy loses track of the player");
        add(ConfigKeys.Awareness.DECREASE_SUSPICIOUS_FACTOR.key(), "Decrease Suspicious Factor");
        add(ConfigKeys.Awareness.DECREASE_SUSPICIOUS_FACTOR.tooltip(), "The factor by which the enemy's suspicious state affects the awareness decrease rate");
        add(ConfigKeys.Awareness.DECREASE_SEARCHING_FACTOR.key(), "Decrease Searching Factor");
        add(ConfigKeys.Awareness.DECREASE_SEARCHING_FACTOR.tooltip(), "The factor by which the enemy's searching state affects the awareness decrease rate");

        add(ConfigKeys.Assassination.ASSASSINATION.key(), "Assassination");
        add(ConfigKeys.Assassination.ASSASSINATION.tooltip(), "Settings related to assassination mechanics");
        add(ConfigKeys.Assassination.ENABLE.key(), "Enable Assassination");
        add(ConfigKeys.Assassination.ENABLE.tooltip(), "Whether to enable assassination feature");
        add(ConfigKeys.Assassination.ALWAYS_SUCCESS.key(), "Always Success");
        add(ConfigKeys.Assassination.ALWAYS_SUCCESS.tooltip(), "Whether assassinations to SEEKERS can be performed successfully all the time");
        add(ConfigKeys.Assassination.SUCCESS_CHANCE.key(), "Success Chance");
        add(ConfigKeys.Assassination.SUCCESS_CHANCE.tooltip(), "If Assassination Always Success is false, the chance of successfully performing an assassination to SEEKERS");
        add(ConfigKeys.Assassination.CAN_PETS_BE_ASSASSINATED.key(), "Assassinate Pets");
        add(ConfigKeys.Assassination.CAN_PETS_BE_ASSASSINATED.tooltip(), "Whether pets can be assassinated by their owners");
        add(ConfigKeys.Assassination.CAN_ANIMALS_BE_ASSASSINATED.key(), "Assassinate Animals (Normal)");
        add(ConfigKeys.Assassination.CAN_ANIMALS_BE_ASSASSINATED.tooltip(), "Whether normal animals (excluding those in the SEEKERS tag) can be assassinated");
        add(ConfigKeys.Assassination.CAN_ANIMAL_SEEKERS_BE_ASSASSINATED.key(), "Assassinate Animals (SEEKERS)");
        add(ConfigKeys.Assassination.CAN_ANIMAL_SEEKERS_BE_ASSASSINATED.tooltip(), "Whether animals in the SEEKERS tag can be assassinated");
        add(ConfigKeys.Assassination.CAN_VILLAGERS_BE_ASSASSINATED.key(), "Assassinate Villagers");
        add(ConfigKeys.Assassination.CAN_VILLAGERS_BE_ASSASSINATED.tooltip(), "Whether villagers (including wandering traders) can be assassinated");
        add(ConfigKeys.Assassination.CAN_BOSSES_BE_ASSASSINATED.key(), "Assassinate Bosses");
        add(ConfigKeys.Assassination.CAN_BOSSES_BE_ASSASSINATED.tooltip(), "Whether bosses (if in the CAN_BE_ASSASSINATED tag) can be assassinated");
        add(ConfigKeys.Assassination.CAN_PLAYERS_BE_ASSASSINATED.key(), "Assassinate Players");
        add(ConfigKeys.Assassination.CAN_PLAYERS_BE_ASSASSINATED.tooltip(), "Whether players can assassinate each other");

        add(ConfigKeys.Compat.COMPAT.key(), "Compatibility");
        add(ConfigKeys.Compat.COMPAT.tooltip(), "Compatibility settings for other mods");
        add(ConfigKeys.Compat.GUARDVILLAGERS.key(), "Guard Villagers");
        add(ConfigKeys.Compat.GUARDVILLAGERS.tooltip(), "Compatibility for Guard Villagers");
        add(ConfigKeys.Compat.GuardVillagers.APPLY_GUARDVILLAGERS_REPUTATION_CONFIG.key(), "Apply Guard Villagers Config of Reputation");
        add(ConfigKeys.Compat.GuardVillagers.APPLY_GUARDVILLAGERS_REPUTATION_CONFIG.tooltip(), "Whether to apply Guard Villagers mod's config of villager reputation threshold, below which the player will get attacked by Guard Villagers");
        // Client
        add(ConfigKeys.AlertIndicator.ALERT_INDICATOR.key(), "Alert Indicator");
        add(ConfigKeys.AlertIndicator.ALERT_INDICATOR.tooltip(), "Settings for the alert indicator HUD, which displays the alert level of surrounding enemies");
        add(ConfigKeys.AlertIndicator.ENABLE.key(), "Enable Alert Indicator");
        add(ConfigKeys.AlertIndicator.ENABLE.tooltip(), "Whether to display the alert indicator around the crosshair");
        add(ConfigKeys.AlertIndicator.RADIUS.key(), "Radius");
        add(ConfigKeys.AlertIndicator.RADIUS.tooltip(), "The distance between the alert indicator and the crosshair");

        add(ConfigKeys.VisibilityIndicator.VISIBILITY_INDICATOR.key(), "Visibility Indicator");
        add(ConfigKeys.VisibilityIndicator.VISIBILITY_INDICATOR.tooltip(), "Settings for the visibility indicator HUD, which displays your current visibility");
        add(ConfigKeys.VisibilityIndicator.TURN_ON.key(), "Enable Visibility Indicator");
        add(ConfigKeys.VisibilityIndicator.TURN_ON.tooltip(), "Whether to display the visibility indicator on the screen");
        add(ConfigKeys.VisibilityIndicator.SCALE.key(), "Scale");
        add(ConfigKeys.VisibilityIndicator.SCALE.tooltip(), "The scale of the visibility indicator");
        add(ConfigKeys.VisibilityIndicator.POSITION.key(), "Position");
        add(ConfigKeys.VisibilityIndicator.POSITION.tooltip(), "The position offset of the visibility indicator on the screen");
        add(ConfigKeys.VisibilityIndicator.POSITION_X.key(), "X");
        add(ConfigKeys.VisibilityIndicator.POSITION_X.tooltip(), "The X offset of the visibility indicator on the screen");
        add(ConfigKeys.VisibilityIndicator.POSITION_Y.key(), "Y");
        add(ConfigKeys.VisibilityIndicator.POSITION_Y.tooltip(), "The Y offset of the visibility indicator on the screen");
        add(ConfigKeys.VisibilityIndicator.OFFSET_FROM_BOSS_BAR.key(), "Offset from Boss Bar");
        add(ConfigKeys.VisibilityIndicator.OFFSET_FROM_BOSS_BAR.tooltip(), "Whether the visibility indicator offsets when a boss bar is visible. Recommended to turn off if you have manually adjusted position or scale of indicator");
        add(ConfigKeys.VisibilityIndicator.OFFSET_FROM_JADE.key(), "Offset from Jade Tooltip");
        add(ConfigKeys.VisibilityIndicator.OFFSET_FROM_JADE.tooltip(), "Whether the visibility indicator offsets when Jade mod's tooltip is visible. Recommended to turn off if you have manually adjusted position or scale of indicator or Jade tooltip");

        add(ConfigKeys.SoundWaveIndicator.SOUND_WAVE_INDICATOR.key(), "Sound Wave Indicator");
        add(ConfigKeys.SoundWaveIndicator.SOUND_WAVE_INDICATOR.tooltip(), "Settings for the sound wave indicator HUD, which displays the sound you are producing");
        add(ConfigKeys.SoundWaveIndicator.TURN_ON.key(), "Enable Sound Wave Indicator");
        add(ConfigKeys.SoundWaveIndicator.TURN_ON.tooltip(), "Whether to display the sound wave indicator on the screen");
        add(ConfigKeys.SoundWaveIndicator.SCALE.key(), "Scale");
        add(ConfigKeys.SoundWaveIndicator.SCALE.tooltip(), "The scale of the sound wave indicator");
        add(ConfigKeys.SoundWaveIndicator.POSITION.key(), "Position");
        add(ConfigKeys.SoundWaveIndicator.POSITION.tooltip(), "The position offset of the sound wave indicator on the screen");
        add(ConfigKeys.SoundWaveIndicator.POSITION_X.key(), "X");
        add(ConfigKeys.SoundWaveIndicator.POSITION_X.tooltip(), "The X offset of the sound wave indicator on the screen");
        add(ConfigKeys.SoundWaveIndicator.POSITION_Y.key(), "Y");
        add(ConfigKeys.SoundWaveIndicator.POSITION_Y.tooltip(), "The Y offset of the sound wave indicator on the screen");
        add(ConfigKeys.SoundWaveIndicator.OFFSET_FROM_BOSS_BAR.key(), "Offset from Boss Bar");
        add(ConfigKeys.SoundWaveIndicator.OFFSET_FROM_BOSS_BAR.tooltip(), "Whether the sound wave indicator offsets when a boss bar is visible. Recommended to turn off if you have manually adjusted position or scale of indicator");
        add(ConfigKeys.SoundWaveIndicator.OFFSET_FROM_JADE.key(), "Offset from Jade Tooltip");
        add(ConfigKeys.SoundWaveIndicator.OFFSET_FROM_JADE.tooltip(), "Whether the sound wave indicator offsets when Jade mod's tooltip is visible. Recommended to turn off if you have manually adjusted position or scale of indicator or Jade tooltip");

        add(ConfigKeys.AlertSymbol.ALERT_SYMBOL.key(), "Alert Symbol");
        add(ConfigKeys.AlertSymbol.ALERT_SYMBOL.tooltip(), "Settings for the world-space alert symbol, which displays the current alert status of enemies");
        add(ConfigKeys.AlertSymbol.ENABLE.key(), "Enable Alert Symbol");
        add(ConfigKeys.AlertSymbol.ENABLE.tooltip(), "Whether to display the alert symbol above enemies' heads");
        add(ConfigKeys.AlertSymbol.SCALE.key(), "Scale");
        add(ConfigKeys.AlertSymbol.SCALE.tooltip(), "The scale of the alert symbol");

        add(ConfigKeys.SpyglassMark.SPYGLASS_MARK.key(), "Spyglass Mark");
        add(ConfigKeys.SpyglassMark.SPYGLASS_MARK.tooltip(), "Settings for marking mobs through spyglass");
        add(ConfigKeys.SpyglassMark.ENABLE.key(), "Enable Spyglass Mark");
        add(ConfigKeys.SpyglassMark.ENABLE.tooltip(), "Whether to turn on mark feature when using spyglass");
        add(ConfigKeys.SpyglassMark.MAX_DISTANCE.key(), "Max Mark Distance");
        add(ConfigKeys.SpyglassMark.MAX_DISTANCE.tooltip(), "The maximum distance at which a mob can be marked");
        add(ConfigKeys.SpyglassMark.HOSTILE_COLOR.key(), "Hostile Color");
        add(ConfigKeys.SpyglassMark.HOSTILE_COLOR.tooltip(), "Mark's color for hostile mobs");
        add(ConfigKeys.SpyglassMark.NEUTRAL_COLOR.key(), "Neutral Color");
        add(ConfigKeys.SpyglassMark.NEUTRAL_COLOR.tooltip(), "Mark's color for neutral mobs");
        add(ConfigKeys.SpyglassMark.ALLY_COLOR.key(), "Ally Color");
        add(ConfigKeys.SpyglassMark.ALLY_COLOR.tooltip(), "Mark's color for allies of players, such as Iron Golems created by players, Snow Golems, and pets (only to their owners)");
        add(ConfigKeys.SpyglassMark.NPC_COLOR.key(), "NPC Color");
        add(ConfigKeys.SpyglassMark.NPC_COLOR.tooltip(), "Mark's color for NPCs, such as Villagers and Wandering Traders");
        add(ConfigKeys.SpyglassMark.PASSIVE_COLOR.key(), "Passive Color");
        add(ConfigKeys.SpyglassMark.PASSIVE_COLOR.tooltip(), "Mark's color for passive mobs, such as normal animals");

        add(ConfigKeys.Heatmap.HEATMAP.key(), "Danger Heatmap");
        add(ConfigKeys.Heatmap.HEATMAP.tooltip(), "Settings for the danger heatmap visualization");
        add(ConfigKeys.Heatmap.ENABLE.key(), "Enable Heatmap");
        add(ConfigKeys.Heatmap.ENABLE.tooltip(), "Whether to display the danger heatmap in enemy vision cones");
        add(ConfigKeys.Heatmap.SEARCH_RANGE.key(), "Search Range");
        add(ConfigKeys.Heatmap.SEARCH_RANGE.tooltip(), "The maximum search range to find enemies and display their heatmaps");
        add(ConfigKeys.Heatmap.MAX_OPACITY.key(), "Max Opacity");
        add(ConfigKeys.Heatmap.MAX_OPACITY.tooltip(), "The maximum opacity of heatmap cells (0=transparent, 1=fully opaque)");

        add(ConfigKeys.DebugMode.DEBUG_MODE.key(), "Debug Mode");
        add(ConfigKeys.DebugMode.DEBUG_MODE.tooltip(), "Settings for Debug Mode");
        add(ConfigKeys.DebugMode.ENABLE.key(), "Enable Debug Mode");
        add(ConfigKeys.DebugMode.ENABLE.tooltip(), "Whether to display detailed alert status information above enemies' heads");
    }
}
