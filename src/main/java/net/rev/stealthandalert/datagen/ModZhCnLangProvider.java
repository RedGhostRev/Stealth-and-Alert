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
        add(LangKeys.CLAMOR_BELL, "喧闹铃铛 [未完成]");
        add(LangKeys.SHADOW_CRYSTAL, "暗影水晶 [未完成]");
        add(LangKeys.SHADOW_CRYSTAL_SHARD, "暗影水晶碎片 [未完成]");
        add(LangKeys.SHADOW_BERRIES, "暗影浆果 [未完成]");
        add(LangKeys.SHADOW_CRYSTAL_DAGGER, "暗影水晶匕首 [未完成]");
        add(LangKeys.DEBUG_WAND, "调试手杖");

        // 方块
        add(LangKeys.PEBBLE_BLOCK, "石子块");
        add(LangKeys.SHADOW_CRYSTAL_ORE, "暗影水晶矿石 [未完成]");
        add(LangKeys.DEEPSLATE_SHADOW_ORE, "深层暗影水晶矿石 [未完成]");

        // 工具提示
        add(LangKeys.TOOLTIP_CAN_ASSASSINATE, "可进行刺杀");
        add(LangKeys.TOOLTIP_DEBUG_WAND, "右键以开关调试模式");
        add(LangKeys.TOOLTIP_DEBUG_WAND_DEBUG_MODE_DESC, "调试模式：显示带有SEEKERS标签的生物的警戒数据——全局警戒状态、对你的观测状态、主目标、对你的记忆时间、对你的警戒值、全局警戒状态切换计时和耐心值计时");

        // 创造模式物品栏
        add(LangKeys.STEALTH_AND_ALERT_ITEMS_TAB, "潜行与警戒 - 物品");
        add(LangKeys.STEALTH_AND_ALERT_BLOCKS_TAB, "潜行与警戒 - 方块");

        // 按键控制
        add(LangKeys.CATEGORY, "潜行与警戒");
        add(LangKeys.CRAWL, "匍匐");
        add(LangKeys.ASSASSINATE, "刺杀");

        // 字幕
        add(LangKeys.PEBBLE_LAND, "石子：落地");

        // 死亡消息
        add(LangKeys.ASSASSINATION, "%1$s再也看不到第二天的太阳");
        add(LangKeys.ASSASSINATION_PLAYER, "%1$s没能察觉到%2$s的接近");
        add(LangKeys.ASSASSINATION_ITEM, "%1$s的生命被%2$s用%3$s悄无声息地带走了");
        add(LangKeys.ASSASSINATION_ITEM_DUAL, "%1$s死前最后一眼只能看到%2$s的%3$s和%4$s");
        add(LangKeys.DAGGER_THROAT_SLIT, "%1$s被抹了喉");
        add(LangKeys.DAGGER_THROAT_SLIT_PLAYER, "%1$s被%2$s抹了喉");
        add(LangKeys.DAGGER_THROAT_SLIT_ITEM, "%1$s被%2$s用%3$s切开了喉管" );
        add(LangKeys.DAGGET_THROAT_SLIT_ITEM_DUAL, "%1$s被%2$s用%3$s和%4$s做了场喉部手术，尽管从未同意过");
        add(LangKeys.TRIDENT_IMPALE, "%1$s被捅了个对穿");
        add(LangKeys.TRIDENT_IMPALE_PLAYER, "%1$s被%2$s捅了个对穿");
        add(LangKeys.TRIDENT_IMPALE_ITEM, "%1$s被%2$s用%3$s捅了个对穿");
        add(LangKeys.TRIDENT_IMPALE_ITEM_DUAL, "%1$s的身体被%2$s用%3$s和%4$s扎出了两个窟窿");
        add(LangKeys.MACE_SMASH, "%1$s被砸成了肉泥");
        add(LangKeys.MACE_SMASH_PLAYER, "%1$s被%2$s砸成了肉泥");
        add(LangKeys.MACE_SMASH_ITEM, "%1$s被%2$s用%3$s砸成了肉泥");
        add(LangKeys.MACE_SMASH_ITEM_DUAL, "%1$s的脑袋在%2$s的%3$s和%4$s前一碰即碎");
        add(LangKeys.SWORD_SLASH, "%1$s的尸体上满是剑痕");
        add(LangKeys.SWORD_SLASH_PLAYER, "%1$s沦为了%2$s剑下的一段残躯");
        add(LangKeys.SWORD_SLASH_ITEM, "在意识到危险之前，%1$s已经被%2$s用%3$s撕裂");
        add(LangKeys.SWORD_SLASH_ITEM_DUAL, "%1$s在死前受尽了%2$s用%3$s和%4$s制造的切割酷刑");
        add(LangKeys.SWORD_THRUST, "%1$s被剑刃贯穿了");
        add(LangKeys.SWORD_THRUST_PLAYER, "%1$s被%2$s的剑刃贯穿了");
        add(LangKeys.SWORD_THRUST_ITEM, "%1$s被%2$s用%3$s精准地刺入了要害");
        add(LangKeys.SWORD_THRUST_ITEM_DUAL, "%1$s被%2$s用%3$s和%4$s刺穿了内脏");

        // GUI
        add(LangKeys.GUI_ASSASSINATE, "[%s] 刺杀");

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
