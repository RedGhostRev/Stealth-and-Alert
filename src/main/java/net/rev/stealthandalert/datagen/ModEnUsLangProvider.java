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
        add(LangKeys.SHADOW_CRYSTAL, "Shadow Crystal [WIP]");
        add(LangKeys.SHADOW_CRYSTAL_SHARD, "Shadow Crystal Shard [WIP]");
        add(LangKeys.SHADOW_BERRIES, "Shadow Berries [WIP]");
        add(LangKeys.SHADOW_CRYSTAL_DAGGER, "Shadow Crystal Dagger [WIP]");
        add(LangKeys.DEBUG_WAND, "Debug Wand");

        // Blocks
        add(LangKeys.PEBBLE_BLOCK, "Pebble Block");
        add(LangKeys.SHADOW_CRYSTAL_ORE, "Shadow Crystal Ore [WIP]");
        add(LangKeys.DEEPSLATE_SHADOW_ORE, "Deepslate Shadow Crystal Ore [WIP]");

        // Tooltips
        add(LangKeys.TOOLTIP_CAN_ASSASSINATE, "Can perform assassinations");
        add(LangKeys.TOOLTIP_DEBUG_WAND, "Right-click to toggle debug mode");
        add(LangKeys.TOOLTIP_DEBUG_WAND_DEBUG_MODE_DESC, "Debug Mode: Displays alert data for mobs with the SEEKERS tag — Global Alert State, Observation State towards you, Primary Target, Memory Time towards you, Alert Level towards you, Global State Transition Timer, and Patience Timer.");

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
