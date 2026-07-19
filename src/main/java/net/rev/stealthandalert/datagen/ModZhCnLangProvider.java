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
        add(LangKeys.SHADOW_CRYSTAL, "暗影水晶");
        add(LangKeys.SHADOW_CRYSTAL_SHARD, "暗影水晶碎片");
        add(LangKeys.SHADOW_BERRIES, "暗影浆果");
        add(LangKeys.SHADOW_CRYSTAL_DAGGER, "暗影水晶匕首");
        add(LangKeys.DEBUG_WAND, "调试手杖");

        // 方块
        add(LangKeys.PEBBLE_BLOCK, "石子块");
        add(LangKeys.SHADOW_CRYSTAL_ORE, "暗影水晶矿石");
        add(LangKeys.DEEPSLATE_SHADOW_ORE, "深层暗影水晶矿石");

        // 药水效果
        add(LangKeys.ETHEREAL, "幽虚");

        // 药水
        add(LangKeys.ETHEREAL_POTION, "幽虚药水");
        add(LangKeys.ETHEREAL_SPLASH_POTION, "喷溅型幽虚药水");
        add(LangKeys.ETHEREAL_LINGERING_POTION, "滞留型幽虚药水");
        add(LangKeys.ETHEREAL_ARROW, "幽虚之箭");

        // 附魔
        add(LangKeys.VITAL_PIERCE, "贯命");

        // 属性
        add(LangKeys.VISIBILITY, "可见度");
        add(LangKeys.SOUND_MULTIPLIER, "声音系数");

        // 工具提示
        add(LangKeys.TOOLTIP_ASSASSINATION_DAMAGE, "刺杀伤害");
        add(LangKeys.TOOLTIP_CAN_ASSASSINATE, "可施展刺杀");
        add(LangKeys.TOOLTIP_DEBUG_WAND, "右键以开关调试模式");
        add(LangKeys.TOOLTIP_DEBUG_WAND_DEBUG_MODE_DESC, "调试模式：显示带有SEEKERS标签的生物的警戒数据：§c\n全局警戒状态\n观测状态\n主目标\n记忆时长\n警戒值\n警戒状态切换计时\n耐心时长");

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
        add(LangKeys.DAGGER_THROAT_SLIT_ITEM, "%1$s被%2$s用%3$s切开了喉管");
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

        // 命令
        add(LangKeys.COMMAND_MOD_ID, "[潜行与警戒]");
        add(LangKeys.COMMAND_RELOAD, "已重载配置（未生成预设）");
        add(LangKeys.COMMAND_REGENERATE, "已补全缺失预设并重载配置");
        add(LangKeys.COMMAND_FORCE_REGENERATE, "已强制覆盖所有预设并重载配置");

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

        // 配置文件
        // 通用
        add(LangKeys.DETECTION, "侦测");
        add(LangKeys.DETECTION_TOOLTIP, "敌人视觉、视野与侦测距离相关设置");
        add(LangKeys.AWARENESS, "警戒值");
        add(LangKeys.AWARENESS_TOOLTIP, "敌人警戒值增减速率相关设置");
        add(LangKeys.ASSASSINATION_C, "刺杀");
        add(LangKeys.ASSASSINATION_C_TOOLTIP, "刺杀机制相关设置");
        add(LangKeys.MAX_DETECTION_RANGE, "最大侦测距离");
        add(LangKeys.MAX_DETECTION_RANGE_TOOLTIP, "敌人能看到玩家的最大距离");
        add(LangKeys.HORIZONTAL_FOV, "水平FOV");
        add(LangKeys.HORIZONTAL_FOV_TOOLTIP, "敌人的水平视野范围（角度）");
        add(LangKeys.VERTICAL_UP_FOV, "向上FOV");
        add(LangKeys.VERTICAL_UP_FOV_TOOLTIP, "敌人向上看的垂直视野范围（角度）");
        add(LangKeys.VERTICAL_DOWN_FOV, "向下FOV");
        add(LangKeys.VERTICAL_DOWN_FOV_TOOLTIP, "敌人向下看的垂直视野范围（角度）");
        add(LangKeys.PATIENCE_TICKS, "耐心时长");
        add(LangKeys.PATIENCE_TICKS_TOOLTIP, "敌人对LKP（最后已知位置）失去耐心的时间（tick）");
        add(LangKeys.REACTION_TICKS, "反应时长");
        add(LangKeys.REACTION_TICKS_TOOLTIP, "敌人从看到到真正察觉玩家所需的反应时间（tick）");
        add(LangKeys.TRACKING_TICKS, "追踪时长");
        add(LangKeys.TRACKING_TICKS_TOOLTIP, "敌人在看不到玩家时，对玩家保持追踪状态的时间（tick）");
        add(LangKeys.MEMORY_TICKS, "记忆时长");
        add(LangKeys.MEMORY_TICKS_TOOLTIP, "敌人在被玩家激怒后对玩家的记忆时间（tick）");
        add(LangKeys.VISIBILITY_THRESHOLD, "可见度阈值");
        add(LangKeys.VISIBILITY_THRESHOLD_TOOLTIP, "玩家进入完全隐蔽状态的可见度阈值（*100%）");
        add(LangKeys.MIN_INVISIBLE_DISTANCE, "完全隐蔽失效距离");
        add(LangKeys.MIN_INVISIBLE_DISTANCE_TOOLTIP, "对于敌人，玩家完全隐蔽作用失效的距离");
        add(LangKeys.MIN_INVISIBLE_DISTANCE_TO_TRACKING, "完全隐蔽失效距离（对追踪者）");
        add(LangKeys.MIN_INVISIBLE_DISTANCE_TO_TRACKING_TOOLTIP, "对于正在追踪的敌人，玩家完全隐蔽作用失效的距离");
        add(LangKeys.INCREASE_BASIC_RATE, "基本增长速率");
        add(LangKeys.INCREASE_BASIC_RATE_TOOLTIP, "敌人察觉玩家时，警戒值增长的基本速率");
        add(LangKeys.INCREASE_VISIBILITY_FACTOR, "增长速率可见度影响因子");
        add(LangKeys.INCREASE_VISIBILITY_FACTOR_TOOLTIP, "玩家可见度对警戒值增长速率的影响程度");
        add(LangKeys.INCREASE_DISTANCE_FACTOR, "增长速率距离影响因子");
        add(LangKeys.INCREASE_DISTANCE_FACTOR_TOOLTIP, "玩家与敌人之间的距离对警戒值增长速率的影响程度");
        add(LangKeys.INCREASE_SUSPICIOUS_FACTOR, "增长速率怀疑状态影响因子");
        add(LangKeys.INCREASE_SUSPICIOUS_FACTOR_TOOLTIP, "敌人的怀疑状态对警戒值增长速率的影响程度");
        add(LangKeys.INCREASE_SEARCHING_FACTOR, "增长速率搜寻状态影响因子");
        add(LangKeys.INCREASE_SEARCHING_FACTOR_TOOLTIP, "敌人的搜寻状态对警戒值增长速率的影响程度");
        add(LangKeys.DECREASE_BASIC_RATE, "基本下降速率");
        add(LangKeys.DECREASE_BASIC_RATE_TOOLTIP, "敌人失去对玩家的追踪时，警戒值下降的基本速率");
        add(LangKeys.DECREASE_SUSPICIOUS_FACTOR, "下降速率怀疑状态影响因子");
        add(LangKeys.DECREASE_SUSPICIOUS_FACTOR_TOOLTIP, "敌人的怀疑状态对警戒值下降速率的影响程度");
        add(LangKeys.DECREASE_SEARCHING_FACTOR, "下降速率搜寻状态影响因子");
        add(LangKeys.DECREASE_SEARCHING_FACTOR_TOOLTIP, "敌人的搜寻状态对警戒值下降速率的影响程度");
        add(LangKeys.ALWAYS_SUCCESS, "必定成功");
        add(LangKeys.ALWAYS_SUCCESS_TOOLTIP, "刺杀SEEKERS是否每次都能成功");
        add(LangKeys.SUCCESS_CHANCE, "成功几率");
        add(LangKeys.SUCCESS_CHANCE_TOOLTIP, "如果刺杀必定成功被关闭，对SEEKERS成功施展刺杀的几率");
        add(LangKeys.CAN_PETS_BE_ASSASSINATED, "刺杀宠物");
        add(LangKeys.CAN_PETS_BE_ASSASSINATED_TOOLTIP, "宠物能否被主人刺杀");
        add(LangKeys.CAN_ANIMALS_BE_ASSASSINATED, "刺杀动物（普通）");
        add(LangKeys.CAN_ANIMALS_BE_ASSASSINATED_TOOLTIP, "普通动物（不包括SEEKERS标签内的动物）能否被刺杀");
        add(LangKeys.CAN_ANIMAL_SEEKERS_BE_ASSASSINATED, "刺杀动物（SEEKERS）");
        add(LangKeys.CAN_ANIMAL_SEEKERS_BE_ASSASSINATED_TOOLTIP, "SEEKERS标签内的动物能否被刺杀");
        add(LangKeys.CAN_VILLAGERS_BE_ASSASSINATED, "刺杀村民");
        add(LangKeys.CAN_VILLAGERS_BE_ASSASSINATED_TOOLTIP, "村民（包括流浪商人）能否被刺杀");
        add(LangKeys.CAN_BOSSES_BE_ASSASSINATED, "刺杀BOSS");
        add(LangKeys.CAN_BOSSES_BE_ASSASSINATED_TOOLTIP, "BOSS（如果在CAN_BE_ASSASSINATED标签内）能否被刺杀");
        add(LangKeys.CAN_PLAYERS_BE_ASSASSINATED, "刺杀玩家");
        add(LangKeys.CAN_PLAYERS_BE_ASSASSINATED_TOOLTIP, "玩家能否被刺杀");
        // 客户端
        add(LangKeys.ALERT_INDICATOR, "警戒指示器");
        add(LangKeys.ALERT_INDICATOR_TOOLTIP, "标示周围敌人对玩家当前警戒值的警戒指示器HUD相关设置");
        add(LangKeys.VISIBILITY_INDICATOR, "可见度指示器");
        add(LangKeys.VISIBILITY_INDICATOR_TOOLTIP, "标示玩家当前可见度的可见度指示器HUD相关设置");
        add(LangKeys.SOUND_WAVE_INDICATOR, "声波指示器");
        add(LangKeys.SOUND_WAVE_INDICATOR_TOOLTIP, "标示玩家自身产生声音的声波指示器HUD相关设置");
        add(LangKeys.ALERT_SYMBOL, "警戒标记");
        add(LangKeys.ALERT_SYMBOL_TOOLTIP, "标示敌人当前警戒状态的世界中警戒标记相关设置");
        add(LangKeys.DEBUG_MODE, "调试模式");
        add(LangKeys.DEBUG_MODE_TOOLTIP, "调试模式相关设置");
        add(LangKeys.ALERT_INDICATOR_TURN_ON, "开启警戒指示器");
        add(LangKeys.ALERT_INDICATOR_TURN_ON_TOOLTIP, "是否在准星周围显示警戒指示器");
        add(LangKeys.RADIUS, "半径");
        add(LangKeys.RADIUS_TOOLTIP, "警戒指示器与准星之间的距离");
        add(LangKeys.VISIBILITY_INDICATOR_TURN_ON, "开启可见度指示器");
        add(LangKeys.VISIBILITY_INDICATOR_TURN_ON_TOOLTIP, "是否在屏幕上显示可见度指示器");
        add(LangKeys.VISIBILITY_SCALE, "缩放");
        add(LangKeys.VISIBILITY_SCALE_TOOLTIP, "可见度指示器的缩放大小");
        add(LangKeys.VISIBILITY_INDICATOR_POSITION, "位置");
        add(LangKeys.VISIBILITY_INDICATOR_POSITION_TOOLTIP, "可见度指示器在屏幕上的位置偏移");
        add(LangKeys.VISIBILITY_INDICATOR_POSITION_X, "X");
        add(LangKeys.VISIBILITY_INDICATOR_POSITION_X_TOOLTIP, "可见度指示器在屏幕上位置的X偏移");
        add(LangKeys.VISIBILITY_INDICATOR_POSITION_Y, "Y");
        add(LangKeys.VISIBILITY_INDICATOR_POSITION_Y_TOOLTIP, "可见度指示器在屏幕上位置的Y偏移");
        add(LangKeys.VISIBILITY_INDICATOR_BOSS_BAR, "随BOSS血条偏移");
        add(LangKeys.VISIBILITY_INDICATOR_BOSS_BAR_TOOLTIP, "屏幕上增加BOSS血条时，可见度指示器是否随之偏移；当你已经调整过指示器位置时，建议关闭");
        add(LangKeys.SOUND_WAVE_INDICATOR_TURN_ON, "开启声波指示器");
        add(LangKeys.SOUND_WAVE_INDICATOR_TURN_ON_TOOLTIP, "是否在屏幕上显示声波指示器");
        add(LangKeys.SOUND_SCALE, "缩放");
        add(LangKeys.SOUND_SCALE_TOOLTIP, "声波指示器的缩放大小");
        add(LangKeys.SOUND_WAVE_INDICATOR_POSITION, "位置");
        add(LangKeys.SOUND_WAVE_INDICATOR_POSITION_TOOLTIP, "声波指示器在屏幕上的位置偏移");
        add(LangKeys.SOUND_WAVE_INDICATOR_POSITION_X, "X");
        add(LangKeys.SOUND_WAVE_INDICATOR_POSITION_X_TOOLTIP, "声波指示器在屏幕上位置的X偏移");
        add(LangKeys.SOUND_WAVE_INDICATOR_POSITION_Y, "Y");
        add(LangKeys.SOUND_WAVE_INDICATOR_POSITION_Y_TOOLTIP, "声波指示器在屏幕上位置的Y偏移");
        add(LangKeys.SOUND_BOSS_BAR, "随BOSS血条偏移");
        add(LangKeys.SOUND_BOSS_BAR_TOOLTIP, "屏幕上增加BOSS血条时，声波指示器是否随之偏移；当你已经调整过指示器位置时，建议关闭");
        add(LangKeys.ALERT_SYMBOL_TURN_ON, "开启警戒标记");
        add(LangKeys.ALERT_SYMBOL_TURN_ON_TOOLTIP, "是否在敌人头顶显示警戒标记");
        add(LangKeys.ALERT_SYMBOL_SCALE, "缩放");
        add(LangKeys.ALERT_SYMBOL_SCALE_TOOLTIP, "警戒标记的缩放大小");
        add(LangKeys.DEBUG_TURN_ON, "开启调试模式");
        add(LangKeys.DEBUG_TURN_ON_TOOLTIP, "是否在敌人头顶显示警戒状态详细信息");
    }
}
