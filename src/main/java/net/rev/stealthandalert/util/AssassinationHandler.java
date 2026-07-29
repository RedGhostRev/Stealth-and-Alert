package net.rev.stealthandalert.util;

import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.Tags;
import net.rev.stealthandalert.attachment.AlertData;
import net.rev.stealthandalert.attachment.AssassinationData;
import net.rev.stealthandalert.attachment.CrawlData;
import net.rev.stealthandalert.attachment.ModAttachments;
import net.rev.stealthandalert.common.animation.IAnimationVisuals;
import net.rev.stealthandalert.config.CommonConfigs;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class AssassinationHandler {
    private static final Map<Integer, Long> LOCKED_TARGETS = new HashMap<>();

    public static void lockTarget(Level level, int targetId, long duration) {
        if (!level.isClientSide()) {
            long expireTick = level.getGameTime() + duration;
            LOCKED_TARGETS.put(targetId, expireTick);
        }
    }

    public static void unlockTarget(Level level, int targetId) {
        if (!level.isClientSide()) {
            LOCKED_TARGETS.remove(targetId);
        }
    }

    public static boolean isTargetLocked(Level level, int targetId) {
        if (LOCKED_TARGETS.containsKey(targetId) && level.getGameTime() > LOCKED_TARGETS.get(targetId)) {
            LOCKED_TARGETS.remove(targetId);
        }
        return LOCKED_TARGETS.containsKey(targetId);
    }

    public static void cleanupExpiredLocks(Level level) {
        if (level.isClientSide()) return;
        long currentTime = level.getGameTime();
        LOCKED_TARGETS.entrySet().removeIf(entry -> currentTime > entry.getValue());
    }

    /**
     * 获取两个实体碰撞箱外边缘之间的实际物理距离
     *
     * @param mode 1 表示对应原版 inflate 逻辑（轴向最大值）；2 表示真正的 3D 空间直线最短距离
     */

    public static double getBoxToBoxDistance(@NotNull Entity e1, @NotNull Entity e2, int mode) {
        AABB b1 = e1.getBoundingBox();
        AABB b2 = e2.getBoundingBox();

        // 1. 分别计算 X, Y, Z 三个轴向上的【净间距】（如果相交/重叠，则间距为 0）
        double dx = Math.max(0, Math.max(b1.minX, b2.minX) - Math.min(b1.maxX, b2.maxX));
        double dy = Math.max(0, Math.max(b1.minY, b2.minY) - Math.min(b1.maxY, b2.maxY));
        double dz = Math.max(0, Math.max(b1.minZ, b2.minZ) - Math.min(b1.maxZ, b2.maxZ));

        if (mode == 1) {
            // 方案 A
            return Math.max(dx, Math.max(dy, dz));
        } else {
            // 方案 B
            // 两个方块外边缘连线的直角三角形斜边
            return Math.sqrt(dx * dx + dy * dy + dz * dz);
        }
    }

    public static AssassinateHand getHand(Player player, ModTags.PriorityCategory category) {
        TagKey<Item> tag = category.tag();
        boolean hasRight = player.getItemInHand(InteractionHand.MAIN_HAND).is(tag);
        boolean hasLeft = player.getItemInHand(InteractionHand.OFF_HAND).is(tag);
        if (hasRight && hasLeft) return AssassinateHand.DUAL_HAND;
        if (hasRight) return AssassinateHand.RIGHT_HAND;
        if (hasLeft) return AssassinateHand.LEFT_HAND;
        return AssassinateHand.RIGHT_HAND;
    }

    @Nullable
    public static ModTags.PriorityCategory canAssassinate(Player player, Optional<UUID> playerUUID, int targetId, boolean isAssassinating) {
        if (!CommonConfigs.ASSASSINATION.enable.get()) return null;
        if (playerUUID.isEmpty() || targetId < 0 || isAssassinating) return null;
        if (player.isUsingItem()) return null;
        if (isTargetLocked(player.level(), targetId) || isTargetLocked(player.level(), player.getId())) return null;
        if (!player.getUUID().equals(playerUUID.get())) return null;
        Entity entity = player.level().getEntity(targetId);
        if (entity == null) return null;
        if (!(entity instanceof LivingEntity target)) return null;
        if (!target.getType().is(ModTags.Entities.CAN_BE_ASSASSINATED)) return null;
        if (!target.isAlive()) return null;
        if (target.isVehicle()) return null;
        if (CommonUtils.isPlayerPet(target, player, false)) {
            if (!CommonConfigs.ASSASSINATION.canPetsBeAssassinated.get()) return null;
        }
        if (target.getType().is(ModTags.Entities.ANIMALS)) {
            if (target.getType().is(ModTags.Entities.SEEKERS)) {
                if (!CommonConfigs.ASSASSINATION.canAnimalSeekersBeAssassinated.get()) return null;
            } else {
                if (!CommonConfigs.ASSASSINATION.canAnimalsBeAssassinated.get()) return null;
            }
        }
        if (target instanceof Villager || target instanceof WanderingTrader) {
            if (!CommonConfigs.ASSASSINATION.canVillagersBeAssassinated.getAsBoolean()) {
                return null;
            }
        }
        if (target.getType().is(Tags.EntityTypes.BOSSES)) {
            if (!CommonConfigs.ASSASSINATION.canBossesBeAssassinated.getAsBoolean()) return null;
        }
        if (target instanceof Player) {
            if (!CommonConfigs.ASSASSINATION.canPlayersBeAssassinated.getAsBoolean()) return null;
        }
        if (!player.hasLineOfSight(target)) return null;
        if (target.getType().is(ModTags.Entities.SEEKERS)) {
            if (target.getData(ModAttachments.ALERT_DATA).state() == AlertData.FIGHTING) return null;
        }
        if (!player.getItemInHand(InteractionHand.MAIN_HAND).is(ModTags.Items.CAN_ASSASSINATE) &&
                !player.getItemInHand(InteractionHand.OFF_HAND).is(ModTags.Items.CAN_ASSASSINATE)) return null;
        ModTags.PriorityCategory finalCategory = getFinalCategory(player);
        if (finalCategory == null) return null;
        double maxRange = finalCategory.maxDistance();
        double actualDistance = getBoxToBoxDistance(player, target, 1);
        boolean isWithin = actualDistance <= maxRange;
        if (finalCategory.tag().location().equals(ModTags.Items.CAN_ASSASSINATE_DAGGERS.location())) {
            if (!isWithin) return null;
        }
        return finalCategory;
    }

    public static int canAssassinate(Player player, Optional<UUID> playerUUID, boolean isAssassinating) {
        if (!CommonConfigs.ASSASSINATION.enable.get()) return -1;
        if (playerUUID.isEmpty() || isAssassinating) return -1;
        if (player.isUsingItem()) return -1;
        if (!player.getUUID().equals(playerUUID.get())) return -1;
        if (!player.getItemInHand(InteractionHand.MAIN_HAND).is(ModTags.Items.CAN_ASSASSINATE) &&
                !player.getItemInHand(InteractionHand.OFF_HAND).is(ModTags.Items.CAN_ASSASSINATE)) return -1;
        ModTags.PriorityCategory finalCategory = getFinalCategory(player);
        if (finalCategory == null) return -1;
        Optional<LivingEntity> targetOpt = getBestAssassinationTarget(player, finalCategory);
        if (targetOpt.isEmpty()) return -1;
        LivingEntity target = targetOpt.get();
        if (!target.isAlive()) return -1;
        if (target.getData(ModAttachments.ALERT_DATA).state() == AlertData.FIGHTING) return -1;
        double maxRange = finalCategory.maxDistance();
        double actualDistance = getBoxToBoxDistance(player, target, 1);
        boolean isWithin = actualDistance <= maxRange;
        if (!isWithin) return -1;
        return target.getId();
    }

    public static Optional<LivingEntity> getBestAssassinationTarget(Player player, ModTags.PriorityCategory category) {
        double reach = category.maxDistance();
        AABB detectionBox = player.getBoundingBox().inflate(reach);

        List<LivingEntity> candidates = player.level().getEntitiesOfClass(
                LivingEntity.class,
                detectionBox,
                target -> {
                    if (!target.getType().is(ModTags.Entities.CAN_BE_ASSASSINATED)) return false;
                    if (!target.isAlive() || isTargetLocked(target.level(), target.getId())) return false;
                    if (target.isVehicle()) return false;
                    if (CommonUtils.isPlayerPet(target, player, false)) {
                        if (!CommonConfigs.ASSASSINATION.canPetsBeAssassinated.get()) return false;
                    }
                    if (target.getType().is(ModTags.Entities.ANIMALS)) {
                        if (target.getType().is(ModTags.Entities.SEEKERS)) {
                            if (!CommonConfigs.ASSASSINATION.canAnimalSeekersBeAssassinated.get()) return false;
                        } else {
                            if (!CommonConfigs.ASSASSINATION.canAnimalsBeAssassinated.get()) return false;
                        }
                    }
                    if (target instanceof Villager || target instanceof WanderingTrader) {
                        if (!CommonConfigs.ASSASSINATION.canVillagersBeAssassinated.getAsBoolean()) {
                            return false;
                        }
                    }
                    if (target.getType().is(Tags.EntityTypes.BOSSES)) {
                        if (!CommonConfigs.ASSASSINATION.canBossesBeAssassinated.getAsBoolean()) return false;
                    }
                    if (target instanceof Player) {
                        return CommonConfigs.ASSASSINATION.canPlayersBeAssassinated.getAsBoolean();
                    }
                    return true;
                }
        );

        LivingEntity bestTarget = null;

        double bestDot = -1.0;

        // 获取玩家视线
        Vec3 lookVec = player.getLookAngle();
        Vec3 lookFlat = new Vec3(lookVec.x, 0, lookVec.z).normalize();
        Vec3 eyePos = player.getEyePosition();

        for (LivingEntity target : candidates) {
            // 碰撞箱交叠判定
            if (!detectionBox.intersects(target.getBoundingBox())) continue;

            if (!player.hasLineOfSight(target)) continue;

            // 获取怪物中心
            Vec3 mobCenter = target.getBoundingBox().getCenter();

            // 将“眼睛到怪物”的向量拍扁到 X-Z 平面
            Vec3 toMob = mobCenter.subtract(eyePos);
            Vec3 toMobFlat = new Vec3(toMob.x, 0, toMob.z).normalize();

            // 计算点积
            double dot = lookFlat.dot(toMobFlat);

            // 怪物越宽，容错率越高 (threshold 越小)
            // 基础阈值是 0.85
            // 每个单位的宽度降低 0.15 的门槛
            double width = target.getBbWidth();
            double dynamicThreshold = Math.max(0.4, 0.85 - (width * 0.15));

            // 如果在动态门槛范围内，且是目前所有目标里最正对准星的
            if (dot > dynamicThreshold && dot > bestDot) {
                bestDot = dot;
                bestTarget = target;
            }
        }
        return Optional.ofNullable(bestTarget);
    }

    private static ModTags.PriorityCategory getFinalCategory(Player player) {
        ItemStack rightItem = player.getItemInHand(InteractionHand.MAIN_HAND);
        ItemStack leftItem = player.getItemInHand(InteractionHand.OFF_HAND);
        boolean hasRight = rightItem.is(ModTags.Items.CAN_ASSASSINATE);
        boolean hasLeft = leftItem.is(ModTags.Items.CAN_ASSASSINATE);
        if (!hasRight && !hasLeft) return null;
        if (hasRight && hasLeft) {
            for (ModTags.PriorityCategory category : ModTags.PriorityCategory.values()) {
                if (category.tag() != null && rightItem.is(category.tag())) {
                    return category;
                }
            }
        } else if (hasRight) {
            for (ModTags.PriorityCategory category : ModTags.PriorityCategory.values()) {
                if (category.tag() != null && rightItem.is(category.tag())) {
                    return category;
                }
            }
            return null;
        } else {
            for (ModTags.PriorityCategory category : ModTags.PriorityCategory.values()) {
                if (category.tag() != null && leftItem.is(category.tag())) {
                    return category;
                }
            }
            return null;
        }
        return null;
    }

    public static void start(Player player, LivingEntity target) {
        player.setData(ModAttachments.CRAWL_DATA, CrawlData.DEFAULT);
        if (!player.level().isClientSide()) {
            if (target instanceof Mob mob) {
                mob.setNoAi(true);
            }
            if (target.isPassenger()) {
                if (target.getVehicle() instanceof Mob mob) {
                    mob.setNoAi(true);
                }
            }
            if (target instanceof Creeper creeper) {
                creeper.setSwellDir(-1);
            }
            target.setInvulnerable(true);
            return;
        }
        player.setData(ModAttachments.CRAWL_DATA, CrawlData.DEFAULT);
        player.setPose(Pose.STANDING);
        AssassinationData data = player.getData(ModAttachments.ASSASSINATION_DATA);
        long elapsedTicks = player.level().getGameTime() - data.startTick();
        if (elapsedTicks < 0) elapsedTicks = 0L;
        IAnimationVisuals.INSTANCE.playPlayerAnimation(player, data.animRL(), elapsedTicks);
    }

    public static void end(Player player, LivingEntity target) {
        if (player.level().isClientSide()) {
            player.setData(ModAttachments.ASSASSINATION_DATA,
                    AssassinationData.getDefaultExceptHand(player.getData(ModAttachments.ASSASSINATION_DATA), player.getUUID()));
        }
        if (target.isAlive() && target instanceof Mob mob) {
            mob.setNoAi(false);
        }
        if (target.isPassenger()) {
            if (target.getVehicle() instanceof Mob mob) {
                mob.setNoAi(false);
            }
        }
        target.setInvulnerable(false);
        AssassinationHandler.unlockTarget(player.level(), target.getId());
    }

    public static void execute(Player player, LivingEntity target, DamageSource source) {
        if (player.level().isClientSide()) return;
        target.hurt(source, Integer.MAX_VALUE);
    }

    public static void execute(Player player, LivingEntity target, DamageSource source, float amount) {
        if (player.level().isClientSide()) return;
        if (amount < 0) amount = 0;
        target.hurt(source, amount);
    }

    public static void executeByPercentage(Player player, LivingEntity target, DamageSource source, float percentage) {
        if (player.level().isClientSide()) return;
        if (percentage < 0) percentage = 0.1F;
        float toHurt = target.getMaxHealth() * percentage;
        if (target.getHealth() <= toHurt) {
            toHurt = target.getHealth() * percentage;
        }
        target.hurt(source, toHurt);
    }

    public static void move(ServerPlayer player, LivingEntity target, double speed) {
        double diffX = target.getX() - player.getX();
        double diffZ = target.getZ() - player.getZ();
        Vec3 rawDirection = new Vec3(diffX, 0, diffZ);
        if (rawDirection.lengthSqr() > 1.0E-4D) {
            Vec3 moveVec = rawDirection.normalize().scale(speed);
            player.setDeltaMovement(moveVec.x, player.getDeltaMovement().y, moveVec.z);
            player.hasImpulse = true;
            player.connection.send(new ClientboundSetEntityMotionPacket(player));
        }
    }

    public enum AssassinateHand {
        RIGHT_HAND,
        LEFT_HAND,
        DUAL_HAND
    }
}
