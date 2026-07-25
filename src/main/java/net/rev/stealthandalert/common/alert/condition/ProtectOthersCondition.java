package net.rev.stealthandalert.common.alert.condition;

import com.google.gson.JsonElement;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.rev.stealthandalert.StealthAndAlert;
import net.rev.stealthandalert.attachment.AlertData;
import net.rev.stealthandalert.attachment.ModAttachments;
import net.rev.stealthandalert.util.ModTags;

import java.util.*;

public class ProtectOthersCondition implements IAlertCondition { //
    public static final String ID = StealthAndAlert.MOD_ID + ":protect_others";

    @Override
    public boolean test(Mob mob, Player player, Map<String, JsonElement> params) {
        double horiz = getDouble(params, "horizontal_range", 16.0);
        double vert = getDouble(params, "vertical_range", 10.0);
        List<String> relatableList = getStringList(params, "entities");

        Set<EntityType<?>> exactTypes = new HashSet<>();
        Set<TagKey<EntityType<?>>> tagTypes = new HashSet<>();

        for (String entry : relatableList) {
            if (entry.startsWith("#")) {
                String tagId = entry.substring(1);
                ResourceLocation rl = ResourceLocation.tryParse(tagId);
                if (rl != null) {
                    tagTypes.add(TagKey.create(Registries.ENTITY_TYPE, rl));
                } else {
                    StealthAndAlert.LOGGER.warn("检测到未定义的实体标签：{}", tagId);
                }
            } else {
                ResourceLocation rl = ResourceLocation.tryParse(entry);
                if (rl != null) {
                    exactTypes.add(BuiltInRegistries.ENTITY_TYPE.get(rl));
                } else {
                    StealthAndAlert.LOGGER.warn("检测到未注册的实体ID：{}", entry);
                }
            }
        }

        // 搜索附近属于 SEEKERS 或 PROTECTED 标签的邻居
        List<? extends Mob> neighbors = mob.level().getEntitiesOfClass(Mob.class,
                mob.getBoundingBox().inflate(horiz, vert, horiz),
                m -> m.getType().is(ModTags.Entities.SEEKERS) || m.getType().is(ModTags.Entities.PROTECTED));

        for (Mob neighbor : neighbors) {
            if (neighbor == mob) continue;
            
            if (isSociallyRelated(neighbor, exactTypes, tagTypes)) {
                AlertData neighborData = neighbor.getData(ModAttachments.ALERT_DATA);
                
                Map<UUID, Long> map = neighbor.getData(ModAttachments.EVENT_LISTENER_DATA).eventStates().get("stealth_and_alert:protect_others");
                boolean isJustHurt = false;
                if (map != null) {
                    Long lastTime = map.getOrDefault(player.getUUID(), -1L);
                    if (mob.level().getGameTime() - lastTime < 20) {
                        isJustHurt = true;
                    }
                }
                boolean isTargeting = (neighbor.getTarget() == player);
                boolean hasMemory = neighborData.targetMemoryTicks().getOrDefault(player.getUUID(), 0) > 0;
                if (isTargeting || isJustHurt || hasMemory) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isSociallyRelated(Mob neighbor, Set<EntityType<?>> exactTypes, Set<TagKey<EntityType<?>>> tagTypes) {
        if (exactTypes.contains(neighbor.getType())) {
            return true;
        }
        
        for (TagKey<EntityType<?>> tag : tagTypes) {
            if (neighbor.getType().is(tag)) {
                return true;
            }
        }

        return false;
    }
}