package net.rev.stealthandalert.event;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingChangeTargetEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.rev.stealthandalert.StealthAndAlert;
import net.rev.stealthandalert.attachment.AlertData;
import net.rev.stealthandalert.attachment.ModAttachments;
import net.rev.stealthandalert.config.CommonConfigs;
import net.rev.stealthandalert.datagen.LangKeys;
import net.rev.stealthandalert.util.ModTags;
import net.rev.stealthandalert.util.StealthUtils;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@EventBusSubscriber(modid = StealthAndAlert.MOD_ID)
public class StealthEvents {

    @SubscribeEvent
    public static void onMobTarget(LivingChangeTargetEvent event) {
        LivingEntity target = event.getNewAboutToBeSetTarget();
        if (target == null) return;
        if (target instanceof Player player && (player.isCreative() || player.isSpectator())) return;

        if (!(event.getEntity() instanceof Mob mob)) return;
        if (!(mob.getType().is(ModTags.Entities.SEEKERS))) return;

        if (!target.getType().is(ModTags.Entities.DETECTABLE)) return;

        AlertData data = mob.getData(ModAttachments.ALERT_DATA);
        if (data.state() < AlertData.FIGHTING) {
            event.setNewAboutToBeSetTarget(null);
            return;
        }

        int pState = data.targetStates().getOrDefault(target.getUUID(), AlertData.UNTRACKED);
        if (pState < AlertData.TRACKING) {
            event.setNewAboutToBeSetTarget(null);
            return;
        }

        UUID primaryUUID = data.primaryTarget().orElse(null);
        if (primaryUUID != null && !primaryUUID.equals(target.getUUID())) {
            event.setNewAboutToBeSetTarget(null);
        }
    }

    @SubscribeEvent
    // FIXME 可能有潜在的性能问题
    public static void onEntityTick(EntityTickEvent.Post event) {
        // 快速失败
        if (!(event.getEntity() instanceof Mob mob)) return;
        if (mob.level().isClientSide()) return;
        if (!(mob.getType().is(ModTags.Entities.SEEKERS))) return;

        // TODO 扩展目标实体为整个SEEKERS标签内的实体
        double range = CommonConfigs.MAX_DETECTION_RANGE.get();
        // 玩家能被处理，当且仅当且玩家位于以生物最大视距为半径的球体内
        List<Player> players = mob.level().getEntitiesOfClass(Player.class, mob.getBoundingBox().inflate(range), player -> mob.distanceToSqr(player) <= range * range);
        for (Player player : players) {
            boolean canSee = player != null && !player.isCreative() && !player.isSpectator() && StealthUtils.hasLineOfSight(mob, player);
            // 处理警戒AI
            if (player != null) StealthUtils.tickPerception(mob, player, canSee);
            debug(mob, player);
        }

        AlertData data = mob.getData(ModAttachments.ALERT_DATA);
        UUID primaryUUID = data.primaryTarget().orElseGet(() -> null);

        if (primaryUUID != null) {
            Player primaryPlayer = mob.level().getPlayerByUUID(primaryUUID);
            if (primaryPlayer != null) {
                boolean canSeePrimary = !primaryPlayer.isCreative() && !primaryPlayer.isSpectator() && StealthUtils.hasLineOfSight(mob, primaryPlayer);
                handleAlert(mob, primaryPlayer, canSeePrimary);
            }
        }
    }

    private static void handleAlert(Mob mob, Player player, Boolean canSee) {
        // 只要玩家理论上能被感知，则判断实际能否被观测到并执行感知系统

        AlertData data = mob.getData(ModAttachments.ALERT_DATA);
        int pState = data.targetStates().getOrDefault(player.getUUID(), AlertData.UNTRACKED); // 敌人对玩家的观测状态

        // 怪物看见玩家怎么办？
        if (canSee) {
            if (pState >= AlertData.AWARE) {
                // 如果玩家已经被察觉到了
                if (data.state() > AlertData.IDLE && data.state() < AlertData.FIGHTING) {
                    // 如果怪物处于怀疑或搜寻状态
                    mob.goalSelector.getAvailableGoals().removeIf(wrappedGoal ->
                            wrappedGoal.getGoal() instanceof RandomLookAroundGoal ||
                                    wrappedGoal.getGoal() instanceof LookAtPlayerGoal
                    );
                    mob.getLookControl().setLookAt(player, 30.0F, 30.0F);
                    if (!mob.getNavigation().isDone()) {
                        mob.getNavigation().stop();
                    }
                } else if (data.state() == AlertData.FIGHTING) {
                    // 如果怪物处于战斗状态
                    if (pState == AlertData.TRACKING) {
                        // 如果玩家处于TRACKING状态
                        mob.setTarget(player);
                    } else {
                        mob.goalSelector.getAvailableGoals().removeIf(wrappedGoal ->
                                wrappedGoal.getGoal() instanceof RandomLookAroundGoal ||
                                        wrappedGoal.getGoal() instanceof LookAtPlayerGoal
                        );
                        mob.getLookControl().setLookAt(player, 30.0F, 30.0F);
                        // 再次确保玩家在非TRACKING状态下不会被攻击
                        if (mob.getTarget() == player) mob.setTarget(null);
                        if (!mob.getNavigation().isDone()) {
                            mob.getNavigation().stop();
                        }
                    }
                }
            }
        }

        // AI行为接管：走向LKP（最后已知位置）
        // 怪物看不见玩家怎么办？
        if (!canSee) {
            // 如果有LKP
            // TODO 怪物能在LKP附近徘徊搜寻
            data.lastSeenPos().ifPresent(pos -> {
                if (data.state() == AlertData.SEARCHING || data.state() == AlertData.FIGHTING) {
                    // 如果怪物处于搜寻或战斗状态
                    // 如果距离LKP还有一段距离，则继续走
                    if (mob.distanceToSqr(pos) > 2.25) { // 1.5格的平方
                        mob.goalSelector.getAvailableGoals().removeIf(wrappedGoal ->
                                wrappedGoal.getGoal() instanceof RandomLookAroundGoal ||
                                        wrappedGoal.getGoal() instanceof LookAtPlayerGoal
                        );
                        mob.getLookControl().setLookAt(pos.x, pos.y + 1.6, pos.z, 30.0F, 30.0F);
                        mob.getNavigation().moveTo(pos.x, pos.y, pos.z, 1.1);
                    } else {
                        mob.getNavigation().stop();
                    }
                } else if (data.state() == AlertData.SUSPICIOUS) {
                    // 如果怪物处于怀疑状态，则凝视LKP
                    mob.goalSelector.getAvailableGoals().removeIf(wrappedGoal ->
                            wrappedGoal.getGoal() instanceof RandomLookAroundGoal ||
                                    wrappedGoal.getGoal() instanceof LookAtPlayerGoal
                    );
                    mob.getLookControl().setLookAt(pos.x, pos.y + 1.6, pos.z, 30.0F, 30.0F);
                }
            });
        }
    }

    // DEBUG内容
    private static void debug(Mob mob, Player player) {
        if (CommonConfigs.DEBUG_MODE.get()) {
            AlertData data = mob.getData(ModAttachments.ALERT_DATA);
            MutableComponent stateName = switch (data.state()) {
                case AlertData.IDLE -> Component.translatable(LangKeys.DEBUG_ALERT_STATE_IDLE);
                case AlertData.SUSPICIOUS -> Component.translatable(LangKeys.DEBUG_ALERT_STATE_SUSPICIOUS);
                case AlertData.SEARCHING -> Component.translatable(LangKeys.DEBUG_ALERT_STATE_SEARCHING);
                case AlertData.FIGHTING -> Component.translatable(LangKeys.DEBUG_ALERT_STATE_FIGHTING);
                default -> Component.translatable(LangKeys.DEBUG_UNKNOWN);
            };
            MutableComponent targetStateName = switch (data.targetStates().getOrDefault(player.getUUID(), AlertData.UNTRACKED)) {
                case AlertData.UNTRACKED -> Component.translatable(LangKeys.DEBUG_TARGET_ALERT_STATE_UNTRACKED);
                case AlertData.AWARE -> Component.translatable(LangKeys.DEBUG_TARGET_ALERT_STATE_AWARE);
                case AlertData.TRACKING -> Component.translatable(LangKeys.DEBUG_TARGET_ALERT_STATE_TRACKING);
                default -> Component.translatable(LangKeys.DEBUG_UNKNOWN);
            };
            Component primaryName;
            UUID uuid = data.primaryTarget().orElse(null);
            if (uuid == null) {
                primaryName = Component.translatable(LangKeys.DEBUG_PRIMARY_TARGET_NULL);
            } else {
                Player primaryPlayer = mob.level().getPlayerByUUID(uuid);
                primaryName = primaryPlayer == null ? Component.translatable(LangKeys.DEBUG_PRIMARY_TARGET_NULL) : primaryPlayer.getName();
            }

            float currentLevel = player != null ? data.targetProgress().getOrDefault(player.getUUID(), 0.0F) : 0.0F;

            Component debugText = stateName
                    .append(" ")
                    .append(targetStateName)
                    .append(" ")
                    .append(primaryName)
                    .append(" ")
                    .append(Component.translatable(LangKeys.DEBUG_TARGET_ALERT_LEVEL, currentLevel))
                    .append(" ")
                    .append(Component.translatable(LangKeys.DEBUG_ALERT_STATE_TICKS, data.stateTicks()))
                    .append(" ")
                    .append(Component.translatable(LangKeys.DEBUG_PATIENCE_TICKS, data.patienceTicks()));
            mob.setCustomName(debugText);
            mob.setCustomNameVisible(true);
        } else {
            if (mob.hasCustomName()) {
                mob.setCustomName(null);
                mob.setCustomNameVisible(false);
            }
        }
    }
}

