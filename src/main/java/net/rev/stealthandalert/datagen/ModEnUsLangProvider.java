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

        // Blocks
        add(LangKeys.PEBBLE_BLOCK, "Pebble Block");
        add(LangKeys.SHADOW_CRYSTAL_ORE, "Shadow Crystal Ore");
        add(LangKeys.DEEPSLATE_SHADOW_ORE, "Deepslate Shadow Crystal Ore");

        // Creative Tabs
        add(LangKeys.STEALTH_AND_ALERT_ITEMS_TAB, "Stealth and Alert - Items");
        add(LangKeys.STEALTH_AND_ALERT_BLOCKS_TAB, "Stealth and Alert - Blocks");
    }
}
