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
        add(LangKeys.MUSIC_DISC_DAISY_BELL, "音乐唱片");
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
        add(LangKeys.TOOLTIP_MUSIC_DISC_DAISY_BELL, "Harry Dacre - Daisy Bell");

        // 创造模式物品栏
        add(LangKeys.STEALTH_AND_ALERT_ITEMS_TAB, "潜行与警戒 - 物品");
        add(LangKeys.STEALTH_AND_ALERT_BLOCKS_TAB, "潜行与警戒 - 方块");

        // 按键控制
        add(LangKeys.CATEGORY, "潜行与警戒");
        add(LangKeys.CRAWL, "匍匐");
        add(LangKeys.ASSASSINATE, "刺杀");
        add(LangKeys.MARK, "用望远镜标记");
        add(LangKeys.EDIT_HUDS, "可视化编辑HUD");

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
        add(LangKeys.GUI_MARK, "[%s] 标记");
        add(LangKeys.GUI_UNMARK, "[%s] 取消标记");
        // Jade
        add(LangKeys.JADE_CONFIG, "警戒信息");
        add(LangKeys.JADE_ALERT_STATE, "警戒状态：");
        add(LangKeys.JADE_ALERT_AWARENESS, "警戒值：%s%%");

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
        add(LangKeys.DEBUG_ALERT_STATE_SEARCHING, "§e搜寻");
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
        add(ConfigKeys.Detection.DETECTION.key(), "侦测");
        add(ConfigKeys.Detection.DETECTION.tooltip(), "敌人视觉、视野与侦测距离相关设置");
        add(ConfigKeys.Detection.MAX_RANGE.key(), "最大侦测距离");
        add(ConfigKeys.Detection.MAX_RANGE.tooltip(), "敌人能看到玩家的最大距离");
        add(ConfigKeys.Detection.HORIZONTAL_FOV.key(), "水平FOV");
        add(ConfigKeys.Detection.HORIZONTAL_FOV.tooltip(), "敌人的水平视野范围（角度）");
        add(ConfigKeys.Detection.VERTICAL_UP_FOV.key(), "向上FOV");
        add(ConfigKeys.Detection.VERTICAL_UP_FOV.tooltip(), "敌人向上看的垂直视野范围（角度）");
        add(ConfigKeys.Detection.VERTICAL_DOWN_FOV.key(), "向下FOV");
        add(ConfigKeys.Detection.VERTICAL_DOWN_FOV.tooltip(), "敌人向下看的垂直视野范围（角度）");
        add(ConfigKeys.Detection.PATIENCE_TICKS.key(), "耐心时长");
        add(ConfigKeys.Detection.PATIENCE_TICKS.tooltip(), "敌人对LKP（最后已知位置）失去耐心的时间（tick）");
        add(ConfigKeys.Detection.REACTION_TICKS.key(), "反应时长");
        add(ConfigKeys.Detection.REACTION_TICKS.tooltip(), "敌人从看到到真正察觉玩家所需的反应时间（tick）");
        add(ConfigKeys.Detection.TRACKING_TICKS.key(), "追踪时长");
        add(ConfigKeys.Detection.TRACKING_TICKS.tooltip(), "敌人在看不到玩家时，对玩家保持追踪状态的时间（tick）");
        add(ConfigKeys.Detection.MEMORY_TICKS.key(), "记忆时长");
        add(ConfigKeys.Detection.MEMORY_TICKS.tooltip(), "敌人在被玩家激怒后对玩家的记忆时间（tick）");
        add(ConfigKeys.Detection.VISIBILITY_THRESHOLD.key(), "可见度阈值");
        add(ConfigKeys.Detection.VISIBILITY_THRESHOLD.tooltip(), "玩家进入完全隐蔽状态的可见度阈值（*100%）");
        add(ConfigKeys.Detection.VISIBILITY_MAX_DETECTION_RANGE_REDUCTION_PERCENTAGE.key(), "可见度对侦测距离最大衰减比例");
        add(ConfigKeys.Detection.VISIBILITY_MAX_DETECTION_RANGE_REDUCTION_PERCENTAGE.tooltip(), "可见度对敌人侦测距离能够衰减的最大百分比");
        add(ConfigKeys.Detection.VISIBILITY_DETECTION_RANGE_REDUCTION_MODEL.key(), "可见度对侦测距离衰减模型");
        add(ConfigKeys.Detection.VISIBILITY_DETECTION_RANGE_REDUCTION_MODEL.tooltip(), """
                可见度对敌人侦测距离进行衰减的数学模型
                LINEAR：平稳衰减
                SQUARE ROOT：衰减开始快，随后慢
                SMOOTHSTEP：衰减在开始和末尾慢，中间快""");
        add(ConfigKeys.Detection.MIN_INVISIBLE_DISTANCE.key(), "完全隐蔽失效距离");
        add(ConfigKeys.Detection.MIN_INVISIBLE_DISTANCE.tooltip(), "对于敌人，玩家完全隐蔽作用失效的距离");
        add(ConfigKeys.Detection.MIN_INVISIBLE_DISTANCE_TO_TRACKING.key(), "完全隐蔽失效距离（对追踪者）");
        add(ConfigKeys.Detection.MIN_INVISIBLE_DISTANCE_TO_TRACKING.tooltip(), "对于正在追踪的敌人，玩家完全隐蔽作用失效的距离");

        add(ConfigKeys.Awareness.AWARENESS.key(), "警戒值");
        add(ConfigKeys.Awareness.AWARENESS.tooltip(), "敌人警戒值增减速率相关设置");
        add(ConfigKeys.Awareness.INCREASE_BASIC_RATE.key(), "基本增长速率");
        add(ConfigKeys.Awareness.INCREASE_BASIC_RATE.tooltip(), "敌人察觉玩家时，警戒值增长的基本速率");
        add(ConfigKeys.Awareness.INCREASE_VISIBILITY_FACTOR.key(), "增长速率可见度影响因子");
        add(ConfigKeys.Awareness.INCREASE_VISIBILITY_FACTOR.tooltip(), "玩家可见度对警戒值增长速率的影响程度");
        add(ConfigKeys.Awareness.INCREASE_DISTANCE_FACTOR.key(), "增长速率距离影响因子");
        add(ConfigKeys.Awareness.INCREASE_DISTANCE_FACTOR.tooltip(), "玩家与敌人之间的距离对警戒值增长速率的影响程度");
        add(ConfigKeys.Awareness.INCREASE_SUSPICIOUS_FACTOR.key(), "增长速率怀疑状态影响因子");
        add(ConfigKeys.Awareness.INCREASE_SUSPICIOUS_FACTOR.tooltip(), "敌人的怀疑状态对警戒值增长速率的影响程度");
        add(ConfigKeys.Awareness.INCREASE_SEARCHING_FACTOR.key(), "增长速率搜寻状态影响因子");
        add(ConfigKeys.Awareness.INCREASE_SEARCHING_FACTOR.tooltip(), "敌人的搜寻状态对警戒值增长速率的影响程度");
        add(ConfigKeys.Awareness.DECREASE_BASIC_RATE.key(), "基本下降速率");
        add(ConfigKeys.Awareness.DECREASE_BASIC_RATE.tooltip(), "敌人失去对玩家的追踪时，警戒值下降的基本速率");
        add(ConfigKeys.Awareness.DECREASE_SUSPICIOUS_FACTOR.key(), "下降速率怀疑状态影响因子");
        add(ConfigKeys.Awareness.DECREASE_SUSPICIOUS_FACTOR.tooltip(), "敌人的怀疑状态对警戒值下降速率的影响程度");
        add(ConfigKeys.Awareness.DECREASE_SEARCHING_FACTOR.key(), "下降速率搜寻状态影响因子");
        add(ConfigKeys.Awareness.DECREASE_SEARCHING_FACTOR.tooltip(), "敌人的搜寻状态对警戒值下降速率的影响程度");

        add(ConfigKeys.Assassination.ASSASSINATION.key(), "刺杀");
        add(ConfigKeys.Assassination.ASSASSINATION.tooltip(), "刺杀机制相关设置");
        add(ConfigKeys.Assassination.ENABLE.key(), "开启刺杀");
        add(ConfigKeys.Assassination.ENABLE.tooltip(), "是否开启刺杀功能");
        add(ConfigKeys.Assassination.ALWAYS_SUCCESS.key(), "必定成功");
        add(ConfigKeys.Assassination.ALWAYS_SUCCESS.tooltip(), "刺杀SEEKERS是否每次都能成功");
        add(ConfigKeys.Assassination.SUCCESS_CHANCE.key(), "成功几率");
        add(ConfigKeys.Assassination.SUCCESS_CHANCE.tooltip(), "如果刺杀必定成功被关闭，对SEEKERS成功施展刺杀的几率");
        add(ConfigKeys.Assassination.CAN_PETS_BE_ASSASSINATED.key(), "刺杀宠物");
        add(ConfigKeys.Assassination.CAN_PETS_BE_ASSASSINATED.tooltip(), "宠物能否被主人刺杀");
        add(ConfigKeys.Assassination.CAN_ANIMALS_BE_ASSASSINATED.key(), "刺杀动物（普通）");
        add(ConfigKeys.Assassination.CAN_ANIMALS_BE_ASSASSINATED.tooltip(), "普通动物（不包括SEEKERS标签内的动物）能否被刺杀");
        add(ConfigKeys.Assassination.CAN_ANIMAL_SEEKERS_BE_ASSASSINATED.key(), "刺杀动物（SEEKERS）");
        add(ConfigKeys.Assassination.CAN_ANIMAL_SEEKERS_BE_ASSASSINATED.tooltip(), "SEEKERS标签内的动物能否被刺杀");
        add(ConfigKeys.Assassination.CAN_VILLAGERS_BE_ASSASSINATED.key(), "刺杀村民");
        add(ConfigKeys.Assassination.CAN_VILLAGERS_BE_ASSASSINATED.tooltip(), "村民（包括流浪商人）能否被刺杀");
        add(ConfigKeys.Assassination.CAN_BOSSES_BE_ASSASSINATED.key(), "刺杀BOSS");
        add(ConfigKeys.Assassination.CAN_BOSSES_BE_ASSASSINATED.tooltip(), "BOSS（如果在CAN_BE_ASSASSINATED标签内）能否被刺杀");
        add(ConfigKeys.Assassination.CAN_PLAYERS_BE_ASSASSINATED.key(), "刺杀玩家");
        add(ConfigKeys.Assassination.CAN_PLAYERS_BE_ASSASSINATED.tooltip(), "玩家能否被刺杀");

        add(ConfigKeys.Compat.COMPAT.key(), "兼容性");
        add(ConfigKeys.Compat.COMPAT.tooltip(), "对其他模组的兼容性配置");
        add(ConfigKeys.Compat.GUARDVILLAGERS.key(), "Guard Villagers（警卫村民）");
        add(ConfigKeys.Compat.GUARDVILLAGERS.tooltip(), "对Guard Villagers的兼容");
        add(ConfigKeys.Compat.GuardVillagers.APPLY_GUARDVILLAGERS_REPUTATION_CONFIG.key(), "应用Guard Villagers声望配置");
        add(ConfigKeys.Compat.GuardVillagers.APPLY_GUARDVILLAGERS_REPUTATION_CONFIG.tooltip(), "是否应用Guard Villagers模组对村民声望阈值的配置，声望低于此阈值的玩家会被警卫村民攻击");
        // 客户端
        add(ConfigKeys.AlertIndicator.ALERT_INDICATOR.key(), "警戒指示器");
        add(ConfigKeys.AlertIndicator.ALERT_INDICATOR.tooltip(), "标示周围敌人对玩家当前警戒值的警戒指示器HUD相关设置");
        add(ConfigKeys.AlertIndicator.ENABLE.key(), "开启警戒指示器");
        add(ConfigKeys.AlertIndicator.ENABLE.tooltip(), "是否在准星周围显示警戒指示器");
        add(ConfigKeys.AlertIndicator.RADIUS.key(), "半径");
        add(ConfigKeys.AlertIndicator.RADIUS.tooltip(), "警戒指示器与准星之间的距离");

        add(ConfigKeys.VisibilityIndicator.VISIBILITY_INDICATOR.key(), "可见度指示器");
        add(ConfigKeys.VisibilityIndicator.VISIBILITY_INDICATOR.tooltip(), "标示玩家当前可见度的可见度指示器HUD相关设置");
        add(ConfigKeys.VisibilityIndicator.TURN_ON.key(), "开启可见度指示器");
        add(ConfigKeys.VisibilityIndicator.TURN_ON.tooltip(), "是否在屏幕上显示可见度指示器");
        add(ConfigKeys.VisibilityIndicator.SCALE.key(), "缩放");
        add(ConfigKeys.VisibilityIndicator.SCALE.tooltip(), "可见度指示器的缩放大小");
        add(ConfigKeys.VisibilityIndicator.POSITION.key(), "位置");
        add(ConfigKeys.VisibilityIndicator.POSITION.tooltip(), "可见度指示器在屏幕上的位置偏移");
        add(ConfigKeys.VisibilityIndicator.POSITION_X.key(), "X");
        add(ConfigKeys.VisibilityIndicator.POSITION_X.tooltip(), "可见度指示器在屏幕上位置的X偏移");
        add(ConfigKeys.VisibilityIndicator.POSITION_Y.key(), "Y");
        add(ConfigKeys.VisibilityIndicator.POSITION_Y.tooltip(), "可见度指示器在屏幕上位置的Y偏移");
        add(ConfigKeys.VisibilityIndicator.OFFSET_FROM_BOSS_BAR.key(), "随BOSS血条偏移");
        add(ConfigKeys.VisibilityIndicator.OFFSET_FROM_BOSS_BAR.tooltip(), "屏幕上增加BOSS血条时，可见度指示器是否随之偏移；当你已经调整过指示器位置或缩放时，建议关闭");
        add(ConfigKeys.VisibilityIndicator.OFFSET_FROM_JADE.key(), "随Jade提示框偏移");
        add(ConfigKeys.VisibilityIndicator.OFFSET_FROM_JADE.tooltip(), "屏幕上显示Jade提示框时，可见度指示器是否随之偏移；当你已经调整过指示器或Jade提示框的位置或缩放时，建议关闭");

        add(ConfigKeys.SoundWaveIndicator.SOUND_WAVE_INDICATOR.key(), "声波指示器");
        add(ConfigKeys.SoundWaveIndicator.SOUND_WAVE_INDICATOR.tooltip(), "标示玩家自身产生声音的声波指示器HUD相关设置");
        add(ConfigKeys.SoundWaveIndicator.TURN_ON.key(), "开启声波指示器");
        add(ConfigKeys.SoundWaveIndicator.TURN_ON.tooltip(), "是否在屏幕上显示声波指示器");
        add(ConfigKeys.SoundWaveIndicator.SCALE.key(), "缩放");
        add(ConfigKeys.SoundWaveIndicator.SCALE.tooltip(), "声波指示器的缩放大小");
        add(ConfigKeys.SoundWaveIndicator.POSITION.key(), "位置");
        add(ConfigKeys.SoundWaveIndicator.POSITION.tooltip(), "声波指示器在屏幕上的位置偏移");
        add(ConfigKeys.SoundWaveIndicator.POSITION_X.key(), "X");
        add(ConfigKeys.SoundWaveIndicator.POSITION_X.tooltip(), "声波指示器在屏幕上位置的X偏移");
        add(ConfigKeys.SoundWaveIndicator.POSITION_Y.key(), "Y");
        add(ConfigKeys.SoundWaveIndicator.POSITION_Y.tooltip(), "声波指示器在屏幕上位置的Y偏移");
        add(ConfigKeys.SoundWaveIndicator.OFFSET_FROM_BOSS_BAR.key(), "随BOSS血条偏移");
        add(ConfigKeys.SoundWaveIndicator.OFFSET_FROM_BOSS_BAR.tooltip(), "屏幕上增加BOSS血条时，声波指示器是否随之偏移；当你已经调整过指示器位置或缩放时，建议关闭");
        add(ConfigKeys.SoundWaveIndicator.OFFSET_FROM_JADE.key(), "随Jade提示框偏移");
        add(ConfigKeys.SoundWaveIndicator.OFFSET_FROM_JADE.tooltip(), "屏幕上显示Jade提示框时，声波指示器是否随之偏移；当你已经调整过指示器或Jade提示框的位置或缩放时，建议关闭");

        add(ConfigKeys.AlertSymbol.ALERT_SYMBOL.key(), "警戒标志");
        add(ConfigKeys.AlertSymbol.ALERT_SYMBOL.tooltip(), "标示敌人当前警戒状态的世界中警戒标志相关设置");
        add(ConfigKeys.AlertSymbol.ENABLE.key(), "开启警戒标志");
        add(ConfigKeys.AlertSymbol.ENABLE.tooltip(), "是否在敌人头顶显示警戒标志");
        add(ConfigKeys.AlertSymbol.SCALE.key(), "缩放");
        add(ConfigKeys.AlertSymbol.SCALE.tooltip(), "警戒标志的缩放大小");

        add(ConfigKeys.SpyglassMark.SPYGLASS_MARK.key(), "望远镜标记");
        add(ConfigKeys.SpyglassMark.SPYGLASS_MARK.tooltip(), "使用望远镜标记生物的相关设置");
        add(ConfigKeys.SpyglassMark.ENABLE.key(), "开启望远镜标记");
        add(ConfigKeys.SpyglassMark.ENABLE.tooltip(), "是否开启使用望远镜时的标记功能");
        add(ConfigKeys.SpyglassMark.MAX_DISTANCE.key(), "最大标记距离");
        add(ConfigKeys.SpyglassMark.MAX_DISTANCE.tooltip(), "能标记生物的最大距离");
        add(ConfigKeys.SpyglassMark.HOSTILE_COLOR.key(), "敌对生物颜色");
        add(ConfigKeys.SpyglassMark.HOSTILE_COLOR.tooltip(), "敌对生物的标记颜色");
        add(ConfigKeys.SpyglassMark.NEUTRAL_COLOR.key(), "中立生物颜色");
        add(ConfigKeys.SpyglassMark.NEUTRAL_COLOR.tooltip(), "中立生物的标记颜色");
        add(ConfigKeys.SpyglassMark.ALLY_COLOR.key(), "友军颜色");
        add(ConfigKeys.SpyglassMark.ALLY_COLOR.tooltip(), "玩家友军的标记颜色，如玩家创造的铁傀儡，以及雪傀儡，和宠物（仅对其主人而言）");
        add(ConfigKeys.SpyglassMark.NPC_COLOR.key(), "NPC颜色");
        add(ConfigKeys.SpyglassMark.NPC_COLOR.tooltip(), "NPC的标记颜色，如村民和流浪商人");
        add(ConfigKeys.SpyglassMark.PASSIVE_COLOR.key(), "被动生物颜色");
        add(ConfigKeys.SpyglassMark.PASSIVE_COLOR.tooltip(), "被动生物的标记颜色，如普通动物");

        add(ConfigKeys.Heatmap.HEATMAP.key(), "危险度热力图");
        add(ConfigKeys.Heatmap.HEATMAP.tooltip(), "危险度热力图可视化的相关设置");
        add(ConfigKeys.Heatmap.ENABLE.key(), "开启热力图");
        add(ConfigKeys.Heatmap.ENABLE.tooltip(), "是否在敌人视野锥中显示危险度热力图");
        add(ConfigKeys.Heatmap.SEARCH_RANGE.key(), "搜索范围");
        add(ConfigKeys.Heatmap.SEARCH_RANGE.tooltip(), "寻找敌人并显示其热力图的最大搜索范围");
        add(ConfigKeys.Heatmap.MAX_OPACITY.key(), "最大不透明度");
        add(ConfigKeys.Heatmap.MAX_OPACITY.tooltip(), "热力图方格的最大不透明度（0=透明，1=完全不透明）");

        add(ConfigKeys.DebugMode.DEBUG_MODE.key(), "调试模式");
        add(ConfigKeys.DebugMode.DEBUG_MODE.tooltip(), "调试模式相关设置");
        add(ConfigKeys.DebugMode.ENABLE.key(), "开启调试模式");
        add(ConfigKeys.DebugMode.ENABLE.tooltip(), "是否在敌人头顶显示警戒状态详细信息");
    }
}
