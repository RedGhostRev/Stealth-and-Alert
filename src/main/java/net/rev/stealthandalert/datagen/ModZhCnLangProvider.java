package net.rev.stealthandalert.datagen;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.rev.stealthandalert.StealthAndAlert;

public class ModZhCnLangProvider extends LanguageProvider {
    public ModZhCnLangProvider(PackOutput output, String locale) {
        super(output, StealthAndAlert.MOD_ID, locale);
    }

    @Override
    protected void addTranslations() {
        // 物品
        add(LangKeys.PEBBLE, "石子");
        add(LangKeys.CLAMOR_BELL, "喧闹铃铛");
        add(LangKeys.SHADOW_CRYSTAL, "暗影水晶");
        add(LangKeys.SHADOW_CRYSTAL_SHARD, "暗影水晶碎片");
        add(LangKeys.SHADOW_BERRIES, "暗影浆果");

        // 方块
        add(LangKeys.PEBBLE_BLOCK, "石子块");
        add(LangKeys.SHADOW_CRYSTAL_ORE, "暗影水晶矿石");
        add(LangKeys.DEEPSLATE_SHADOW_ORE, "深层暗影水晶矿石");

        // 创造模式物品栏
        add(LangKeys.STEALTH_AND_ALERT_ITEMS_TAB, "潜行与警戒 - 物品");
        add(LangKeys.STEALTH_AND_ALERT_BLOCKS_TAB, "潜行与警戒 - 方块");
    }
}
