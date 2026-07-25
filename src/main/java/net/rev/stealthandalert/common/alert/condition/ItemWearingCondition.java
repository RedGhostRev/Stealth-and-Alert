package net.rev.stealthandalert.common.alert.condition;

import com.google.gson.JsonElement;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.rev.stealthandalert.StealthAndAlert;

import java.util.List;
import java.util.Map;

public class ItemWearingCondition implements IAlertCondition {
    @Override
    public boolean test(Mob mob, Player player, Map<String, JsonElement> params) {
        List<String> items = getStringList(params, "items");

        for (ItemStack stack : player.getArmorSlots()) {
            // 如果玩家穿着装备，且该装备匹配列表中的物品或标签，则返回 false
            if (!stack.isEmpty() && isWearingItem(stack, items)) return false;
        }
        return true;
    }

    private static boolean isWearingItem(ItemStack armor, List<String> items) {
        for (String entry : items) {
            if (entry.startsWith("#")) {
                // 处理标签
                String tagId = entry.substring(1);
                ResourceLocation rl = ResourceLocation.tryParse(tagId);
                if (rl != null) {
                    TagKey<Item> tagKey = TagKey.create(Registries.ITEM, rl);
                    if (armor.is(tagKey)) return true;
                } else {
                    StealthAndAlert.LOGGER.warn("检测到未定义的物品标签：{}", tagId);
                }
            } else {
                // 处理普通物品
                ResourceLocation rl = ResourceLocation.tryParse(entry);
                if (rl != null) {
                    Item item = BuiltInRegistries.ITEM.get(rl);
                    if (armor.is(item)) return true;
                } else {
                    StealthAndAlert.LOGGER.warn("检测到未注册的物品ID：{}", entry);
                }
            }
        }
        return false;
    }
}