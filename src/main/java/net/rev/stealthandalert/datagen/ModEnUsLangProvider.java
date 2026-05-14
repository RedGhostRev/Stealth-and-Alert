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
        add(LangKeys.CLAMOR_BELL, "Clamor Bell");
        add(LangKeys.SHADOW_CRYSTAL, "Shadow Crystal");
        add(LangKeys.SHADOW_CRYSTAL_SHARD, "Shadow Crystal Shard");
        add(LangKeys.SHADOW_BERRIES, "Shadow Berries");
        add(LangKeys.SHADOW_CRYSTAL_DAGGER, "Shadow Crystal Dagger");
        add(LangKeys.DEBUG_WAND, "Debug Wand");

        // Blocks
        add(LangKeys.PEBBLE_BLOCK, "Pebble Block");
        add(LangKeys.SHADOW_CRYSTAL_ORE, "Shadow Crystal Ore");
        add(LangKeys.DEEPSLATE_SHADOW_ORE, "Deepslate Shadow Crystal Ore");

        // Tooltips
        add(LangKeys.TOOLTIP_CAN_STAB, "§eDeels double damage when §cbackstabbing");
        add(LangKeys.TOOLTIP_DEBUG_WAND, "Right-click to toggle debug mode");
        add(LangKeys.TOOLTIP_DEBUG_WAND_DEBUG_MODE_DESC, "Debug Mode: Displays alert data for mobs with the SEEKERS tag — Global Alert State, Observation State towards you, Primary Target, Memory Time towards you, Alert Level towards you, Global State Transition Timer, and Patience Timer.");

        // Creative Tabs
        add(LangKeys.STEALTH_AND_ALERT_ITEMS_TAB, "Stealth and Alert - Items");
        add(LangKeys.STEALTH_AND_ALERT_BLOCKS_TAB, "Stealth and Alert - Blocks");

        // Key Mappings
        add(LangKeys.CATEGORY, "Stealth and Alert");
        add(LangKeys.CRAWL, "Crawl");

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
    }
}
