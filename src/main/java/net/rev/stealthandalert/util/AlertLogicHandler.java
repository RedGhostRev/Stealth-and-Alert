package net.rev.stealthandalert.util;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.rev.stealthandalert.StealthAndAlert;
import net.rev.stealthandalert.attachment.AlertData;
import net.rev.stealthandalert.attachment.ModAttachments;
import net.rev.stealthandalert.config.EntityAlertSettings;

import java.util.List;

public class AlertLogicHandler {
    // 对条件敌对生物敌意产生的断言
    // 适用于CONDITIONAL_SEEKERS标签实体
    private static final String MOD_ID = StealthAndAlert.MOD_ID;

    public static boolean checkLogic(String type, Mob mob, Player player, EntityAlertSettings settings) {
        return switch (type) {
            case MOD_ID + ":provocation" -> isPhysicallyProvoked(mob, player);
            case MOD_ID + ":social_revenge" -> checkSocialRevenge(mob, player, settings);
            case MOD_ID + ":item_wearing" -> checkItemLogic(player, settings);
            case MOD_ID + ":action_on_container" -> checkContainerAction(mob, player, settings);
            case MOD_ID + ":light_sensitive" -> checkLightLogic(mob, settings);
            case MOD_ID + ":eye_contact" -> checkEyeContact(mob, player, settings);
            case MOD_ID + ":proximity_to_child" -> checkProximityToChild(mob, player, settings);
            case MOD_ID + ":protect_villager" -> checkVillagerProtection(mob, player, settings);
            case MOD_ID + ":village_reputation" -> checkVillageReputation(mob, player, settings);
            default -> false;
        };
    }

    private static boolean checkVillagerProtection(Mob mob, Player player, EntityAlertSettings settings) {
        return false;
    }

    private static boolean checkVillageReputation(Mob mob, Player player, EntityAlertSettings settings) {
        if (!(mob instanceof IronGolem golem)) return false;
        int threshold = settings.getLogicInt("village_reputation", "threshold", -15);
        double range = settings.getLogicDouble("village_reputation", "range", 16.0);
        List<Villager> nearbyVillagers = golem.level().getEntitiesOfClass(Villager.class,
                golem.getBoundingBox().inflate(range));

        if (nearbyVillagers.isEmpty()) return false;

        for (Villager villager : nearbyVillagers) {
            if (getReputation(villager, player) <= threshold) {
                return true;
            }
        }
        return false;
    }

    private static int getReputation(Villager villager, Player player) {
        return villager.getPlayerReputation(player);
    }

    private static boolean checkContainerAction(Mob mob, Player player, EntityAlertSettings settings) {
        settings.getLogicDouble("action_on_container", "range", settings.viewRange());
        if (mob.distanceToSqr(player) > settings.viewRange() * settings.viewRange()) {
            return false;
        }
        if (mob instanceof Piglin piglin) {
            return piglin.getBrain().getMemory(MemoryModuleType.ANGRY_AT).map(uuid -> uuid.equals(player.getUUID())).orElse(false);
        }
        return false;
    }

    private static boolean checkItemLogic(Player player, EntityAlertSettings settings) {
        String tagId = settings.getLogicString("item_wearing", "forbidden_tag", "minecraft:piglin_loved");

        for (ItemStack armor : player.getArmorSlots()) {
            if (!armor.isEmpty() && isItemInTag(armor, tagId)) {
                return false;
            }
        }
        return true;
    }

    private static boolean isItemInTag(ItemStack stack, String tagOrId) {
        ResourceLocation res = ResourceLocation.tryParse(tagOrId);
        if (res == null) return false;

        TagKey<Item> tagKey = TagKey.create(BuiltInRegistries.ITEM.key(), res);
        if (stack.is(tagKey)) return true;

        return BuiltInRegistries.ITEM.getKey(stack.getItem()).equals(res);
    }


    private static boolean checkProximityToChild(Mob mob, Player player, EntityAlertSettings settings) {
        double horizontalRange = settings.getLogicDouble("proximity_to_child", "horizontal_range", 8.0);
        double verticalRange = settings.getLogicDouble("proximity_to_child", "vertical_range", 4.0);

        AABB searchBox = mob.getBoundingBox().inflate(horizontalRange, verticalRange, horizontalRange);
        List<? extends Mob> children = mob.level().getEntitiesOfClass(mob.getClass(), searchBox, Mob::isBaby);

        for (Mob child : children) {
            if (child.distanceToSqr(player) < horizontalRange * horizontalRange) {
                if (mob.hasLineOfSight(player)) {
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean isPlayerPet(Mob mob, Player player) {
        if (mob instanceof OwnableEntity ownable) {
            return player.getUUID().equals(ownable.getOwnerUUID());
        }

        if (mob instanceof IronGolem golem) {
            return golem.isPlayerCreated();
        }
        return false;
    }

    private static boolean isPhysicallyProvoked(Mob mob, Player player) {
        AlertData data = mob.getData(ModAttachments.ALERT_DATA);
        if (mob.getTarget() == player || data.targetStates().getOrDefault(player.getUUID(), AlertData.UNTRACKED) > AlertData.UNTRACKED) {
            return true;
        }
        if (data.lastDamageTicks().getOrDefault(player.getUUID(), 0) > 0) {
            return true;
        }
        return false;
    }

    private static boolean checkLightLogic(Mob mob, EntityAlertSettings settings) {
        int threshold = settings.getLogicInt("light_sensitive", "threshold", 11);

        boolean invert = settings.getLogicBool("light_sensitive", "invert", false);

        int currentLight = mob.level().getMaxLocalRawBrightness(mob.blockPosition());
        return invert ? (currentLight > threshold) : (currentLight <= threshold);
    }

    private static boolean checkEyeContact(Mob mob, Player player, EntityAlertSettings settings) {
        // 物理检查：南瓜头遮蔽判定
        boolean checkPumpkin = settings.getLogicBool("eye_contact", "pumpkin_mask", false);
        if (checkPumpkin) {
            // 检查头盔栏 (slot 3) 是否为南瓜头
            if (player.getItemBySlot(EquipmentSlot.HEAD).is(Items.CARVED_PUMPKIN)) {
                return false; // 戴了南瓜头，对视断言直接失效
            }
        }

        // 灵敏度参数
        double sensitivity = 0.025;

        // 几何运算
        Vec3 playerLookVec = player.getViewVector(1.0F).normalize();
        Vec3 toMobVec = new Vec3(mob.getX() - player.getX(), mob.getEyeY() - player.getEyeY(), mob.getZ() - player.getZ());
        double distance = toMobVec.length();

        if (distance < 0.001) return true; // 极近距离视为对视

        toMobVec = toMobVec.normalize();
        double dotProduct = playerLookVec.dot(toMobVec);

        return dotProduct > 1.0 - sensitivity / distance;
    }

    private static boolean checkSocialRevenge(Mob mob, Player player, EntityAlertSettings settings) {
        double horizontalRange = settings.getLogicDouble("social_revenge", "horizontal_range", 16.0);
        double verticalRange = settings.getLogicDouble("social_revenge", "vertical_range", 10.0);
        String relatableStr = settings.getLogicString("social_revenge", "relatables", "");

        List<? extends Mob> neighbors = mob.level().getEntitiesOfClass(Mob.class,
                mob.getBoundingBox().inflate(horizontalRange, verticalRange, horizontalRange),
                m -> m.getType().is(ModTags.Entities.SEEKERS));

        for (Mob neighbor : neighbors) {
            if (neighbor == mob) continue;
            if (isSociallyRelated(mob, neighbor, relatableStr)) {
                AlertData neighborData = neighbor.getData(ModAttachments.ALERT_DATA);
                if (neighbor.getTarget() == player ||
                        neighborData.lastDamageTicks().getOrDefault(player.getUUID(), 0) > 0) {
                    return true;
                }
            }
        }

        return false;
    }

    private static boolean isSociallyRelated(Mob self, Mob neighbor, String relatableStr) {
        if (self.getType() == neighbor.getType()) {
            return true;
        }
        if (!relatableStr.isEmpty()) {
            String neighborId = BuiltInRegistries.ENTITY_TYPE.getKey(neighbor.getType()).toString();
            boolean match = relatableStr.contains(neighborId);
            return match;
        }
        return false;
    }
}
