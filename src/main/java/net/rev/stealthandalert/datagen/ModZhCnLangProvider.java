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
        add(LangKeys.SHADOW_CRYSTAL_DAGGER, "暗影水晶匕首");
        add(LangKeys.DEBUG_WAND, "调试手杖");

        // 方块
        add(LangKeys.PEBBLE_BLOCK, "石子块");
        add(LangKeys.SHADOW_CRYSTAL_ORE, "暗影水晶矿石");
        add(LangKeys.DEEPSLATE_SHADOW_ORE, "深层暗影水晶矿石");

        // 工具提示
        add(LangKeys.TOOLTIP_CAN_STAB, "§c背刺§e时造成双倍伤害");
        add(LangKeys.TOOLTIP_DEBUG_WAND, "右键以开关调试模式");
        add(LangKeys.TOOLTIP_DEBUG_WAND_DEBUG_MODE_DESC, "调试模式：显示带有SEEKERS标签的生物的警戒数据——全局警戒状态、对你的观测状态、主目标、对你的记忆时间、对你的警戒值、全局警戒状态切换计时和耐心值计时");

        // 创造模式物品栏
        add(LangKeys.STEALTH_AND_ALERT_ITEMS_TAB, "潜行与警戒 - 物品");
        add(LangKeys.STEALTH_AND_ALERT_BLOCKS_TAB, "潜行与警戒 - 方块");

        // 按键控制
        add(LangKeys.CATEGORY, "潜行与警戒");
        add(LangKeys.CRAWL, "匍匐");

        // 调试文本
        add(LangKeys.DEBUG_MODE_ON, "§a调试模式：开启");
        add(LangKeys.DEBUG_MODE_OFF, "§c调试模式：关闭");
        add(LangKeys.DEBUG_ALERT_STATE_IDLE, "§7空闲");
        add(LangKeys.DEBUG_ALERT_STATE_SUSPICIOUS, "§f怀疑");
        add(LangKeys.DEBUG_ALERT_STATE_SEARCHING, "§6搜寻");
        add(LangKeys.DEBUG_ALERT_STATE_FIGHTING, "§c战斗");
        add(LangKeys.DEBUG_TARGET_ALERT_STATE_UNTRACKED, "§7未察觉");
        add(LangKeys.DEBUG_TARGET_ALERT_STATE_AWARE, "§f察觉");
        add(LangKeys.DEBUG_TARGET_ALERT_STATE_TRACKING, "§c追踪");
        add(LangKeys.DEBUG_PRIMARY_TARGET_NULL, "无主要目标");
        add(LangKeys.DEBUG_UNKNOWN, "未知");
        add(LangKeys.DEBUG_HATRED_MEMORY, "仇恨记忆：%d");
        add(LangKeys.DEBUG_TARGET_ALERT_LEVEL, "§b警觉度：%.1f%%");
        add(LangKeys.DEBUG_ALERT_STATE_TICKS, "§e状态切换计时：%d");
        add(LangKeys.DEBUG_PATIENCE_TICKS, "§d耐心值：%d");
    }
}
