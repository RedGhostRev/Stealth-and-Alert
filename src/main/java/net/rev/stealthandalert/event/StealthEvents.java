package net.rev.stealthandalert.event;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingChangeTargetEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.rev.stealthandalert.StealthAndAlert;
import net.rev.stealthandalert.attachment.AlertData;
import net.rev.stealthandalert.attachment.ModAttachments;
import net.rev.stealthandalert.config.CommonConfigs;
import net.rev.stealthandalert.network.S2CAlertDataPacket;
import net.rev.stealthandalert.util.ModTags;
import net.rev.stealthandalert.util.StealthUtils;

import java.util.*;

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
    public static void onMobHurt(LivingIncomingDamageEvent event) {
        if (!(event.getEntity() instanceof Mob mob) || mob.level().isClientSide()) return;
        if (!mob.getType().is(ModTags.Entities.SEEKERS)) return;

        if (event.getSource().getEntity() instanceof Player player) {
            if (player.isCreative() || player.isSpectator()) return;

            AlertData data = mob.getData(ModAttachments.ALERT_DATA);
            UUID uuid = player.getUUID();

            Map<UUID, Float> progressMap = new HashMap<>(data.targetProgress());
            Map<UUID, Integer> statesMap = new HashMap<>(data.targetStates());
            Map<UUID, Integer> reactionsMap = new HashMap<>(data.targetReactions());

            LivingEntity currentTarget = mob.getTarget();
            int currentPState = statesMap.getOrDefault(uuid, AlertData.UNTRACKED);

            // 判定：是否在打别人
            boolean isFightingOthers = (data.state() == AlertData.FIGHTING) && currentTarget != null && currentTarget != player;

            // 只有当该玩家还没达到 TRACKING 状态时才更新数据
            if (currentPState < AlertData.TRACKING) {
                progressMap.put(uuid, 100.0F);
                statesMap.put(uuid, AlertData.TRACKING);
                reactionsMap.put(uuid, 0);

                // 根据是否正在打别人，决定全局状态
                int nextState = isFightingOthers ? data.state() : AlertData.SEARCHING;
                Optional<Vec3> nextLKP = isFightingOthers ? data.lastSeenPos() : Optional.of(player.position());
                Optional<UUID> nextPrimary = isFightingOthers ? data.primaryTarget() : Optional.of(uuid);

                // 写回
                AlertData newData = new AlertData(
                        nextState,
                        progressMap,
                        statesMap,
                        reactionsMap,
                        nextLKP,
                        nextPrimary,
                        data.stateTicks(),
                        data.patienceTicks()
                );

                mob.setData(ModAttachments.ALERT_DATA, newData);

                // 确保客户端UI响应
                PacketDistributor.sendToPlayersTrackingEntity(mob, new S2CAlertDataPacket(mob.getId(), newData));
            }
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

        // 处理记忆
        Set<UUID> trackedPlayers = new HashSet<>(mob.getData(ModAttachments.ALERT_DATA).targetStates().keySet());
        // 玩家能被处理，当且仅当且玩家位于以生物最大视距为半径的球体内
        List<Player> players = mob.level().getEntitiesOfClass(Player.class, mob.getBoundingBox().inflate(range), player -> mob.distanceToSqr(player) <= range * range);
        for (Player player : players) {
            trackedPlayers.add(player.getUUID());
        }

        for (UUID uuid : trackedPlayers) {
            Player player = mob.level().getPlayerByUUID(uuid);
            if (player == null) continue;

            boolean canSee = players.contains(player) && !player.isCreative() && !player.isSpectator() && StealthUtils.hasLineOfSight(mob, player);
            StealthUtils.tickPerception(mob, player, canSee);
        }

        // 主要目标
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
}

