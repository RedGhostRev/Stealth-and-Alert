package net.rev.stealthandalert.util;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.rev.stealthandalert.attachment.AlertData;
import net.rev.stealthandalert.attachment.ModAttachments;
import net.rev.stealthandalert.config.CommonConfigs;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class StealthUtils {
    private StealthUtils() {
    }

    // 视线检测：如果mob能看到target，则返回true
    public static boolean hasLineOfSight(Mob observer, Entity target) {
        // 距离快速失败
        double distanceSqr = observer.distanceToSqr(target);
        double maxDistance = CommonConfigs.MAX_DETECTION_RANGE.get();
        if (distanceSqr > maxDistance * maxDistance) return false;

        // 获取观察方向与目标向量
        Vec3 eyePos = observer.getEyePosition();
        Vec3 lookVec = observer.getViewVector(1.0F);
        Vec3 targetVec = target.getEyePosition().subtract(eyePos);
        Vec3 targetDir = targetVec.normalize();

        // 水平与垂直FOV判定
        if (!isWithinFOV(lookVec, targetDir)) return false;

        // 多点物理遮挡检查
        return canSeeAnyPart(observer, target, eyePos);
    }

    // 感知系统
    // TODO 更标准、完善的状态机；将player对应参数的生物类型推广
    public static void tickPerception(Mob mob, Player player, boolean canSee) {
        if (player == null) return;
        AlertData data = mob.getData(ModAttachments.ALERT_DATA);
        UUID uuid = player.getUUID();

        // 敌人对单独玩家的状态
        Map<UUID, Float> newProgressMap = new HashMap<>(data.targetProgress());
        Map<UUID, Integer> newStatesMap = new HashMap<>(data.targetStates());
        Map<UUID, Integer> newReactionsMap = new HashMap<>(data.targetReactions());
        float currentLevel = newProgressMap.getOrDefault(uuid, 0.0F);
        int currentPState = newStatesMap.getOrDefault(uuid, AlertData.UNTRACKED);
        int currentReaction = newReactionsMap.getOrDefault(uuid, CommonConfigs.DETECTION_REACTION_TICKS.getAsInt());
        float newLevel = currentLevel;
        int newPState = currentPState;
        int newReaction = currentReaction;

        // 敌人的全局状态
        int newState = data.state();
        int newStateTicks = data.stateTicks(); // 状态切换计时器
        int newPatienceTicks = data.patienceTicks(); // 耐心值计时器
        Optional<Vec3> lkp = data.lastSeenPos(); // LKP（最后已知位置）
        Optional<UUID> newPrimary = data.primaryTarget(); // 主目标

        if (canSee) {
            // 如果看到了玩家
            newStateTicks = 0; // 重置计时器为0
            newPatienceTicks = CommonConfigs.PATIENCE_TICKS.getAsInt(); // 重置耐心值
            boolean shouldUpdateLKP = false; // 要不要更新LKP？

            // 对于每个单独的玩家来说
            if (currentPState == AlertData.UNTRACKED) {
                // 反应期
                if (currentReaction > 0) {
                    newReaction--;
                    newLevel = 0.0F;
                } else {
                    newPState = AlertData.AWARE;
                }
            }

            if (newPState == AlertData.AWARE) {
                // 涨条期
                newLevel = Math.min(100.0F, currentLevel + 1.0F);
                if (newLevel >= 100.0F) {
                    newPState = AlertData.TRACKING;
                }
            }

            if (newPState == AlertData.TRACKING) {
                // 追踪期
                newLevel = 100.0F;
                newState = AlertData.FIGHTING;
            }

            // 当玩家被察觉后，开始处理LKP竞争更新问题
            if (newPState >= AlertData.AWARE) {
                if (lkp.isEmpty()) {
                    shouldUpdateLKP = true; // 如果LKP为空，可以更新
                } else {
                    float globalMaxLevel = newLevel;
                    for (Float level : newProgressMap.values()) {
                        if (level > globalMaxLevel) globalMaxLevel = level;
                    }

                    // 如果我的警觉值达到了全场最高
                    if (newLevel >= globalMaxLevel) {
                        // 找出所有处于最高警觉值的玩家中最近的
                        double minNearbyDistSq = mob.distanceToSqr(player);
                        boolean anotherCloser = false;

                        for (Map.Entry<UUID, Float> entry : newProgressMap.entrySet()) {
                            UUID otherId = entry.getKey();
                            // 如果有人警觉值跟我一样
                            if (!otherId.equals(uuid) && entry.getValue() >= globalMaxLevel) {
                                Player otherPlayer = mob.level().getPlayerByUUID(otherId);
                                if (otherPlayer != null) {
                                    double otherDistSq = mob.distanceToSqr(otherPlayer);
                                    if (otherDistSq < minNearbyDistSq) {
                                        anotherCloser = true;
                                        break;
                                    }
                                }
                            }
                        }

                        // 如果没人比我更近，那么我获得LKP更新权，而且我成为了主目标
                        if (!anotherCloser) {
                            shouldUpdateLKP = true;
                            newPrimary = Optional.of(uuid);
                        }
                    }
                }
            }
            if (shouldUpdateLKP) {
                lkp = Optional.of(player.position());
            }

        } else {
            // 如果看不到玩家
            newLevel = Math.max(0.0F, currentLevel - 0.5F);

            if (currentPState == AlertData.TRACKING) {
                newPState = AlertData.AWARE;
            }

            if (newLevel <= 0.0F) {
                newPState = AlertData.UNTRACKED;
                newReaction = CommonConfigs.DETECTION_REACTION_TICKS.getAsInt();
            }
        }

        newProgressMap.put(uuid, newLevel);

        // TODO 实现警戒条和警戒状态的解耦
        // 对于怪物全局状态
        if (newPState >= AlertData.AWARE) {
            // 如果这个玩家让条满了，且怪物还没有进入战斗
            if (newLevel >= 100.0F && newState < AlertData.FIGHTING) {
                newState = AlertData.FIGHTING;
            }
            // 如果这个玩家条过半了，且怪物小于搜寻状态
            else if (newLevel >= 50.0F && newState < AlertData.SEARCHING) {
                newState = AlertData.SEARCHING;
            }
            // 如果这个玩家刚起条，且怪物还在闲逛
            else if (newLevel > 0.0F && newState < AlertData.SUSPICIOUS) {
                newState = AlertData.SUSPICIOUS;
            }
        }

        // 敌人警戒状态的回落

        float globalMaxLevel = 0.0F;
        for (Map.Entry<UUID, Float> entry : newProgressMap.entrySet()) {
            globalMaxLevel = Math.max(globalMaxLevel, entry.getValue());
        }

        boolean canStartReset = false; // 开始重置LKP记忆吗？
        // 如果敌人看不到玩家
        if (!canSee) {
            // 全局回落
            if (globalMaxLevel <= 0.0F && newState > AlertData.IDLE) {
                if (newState == AlertData.SEARCHING || newState == AlertData.FIGHTING) {
                    canStartReset = (lkp.isPresent() && mob.distanceToSqr(lkp.get()) < 4.0) || --newPatienceTicks <= 1;
                } else {
                    canStartReset = true;
                }
            }
        }

        if (canStartReset) {
            if (newStateTicks <= 0) {
                newStateTicks = 40; // 开始计时
            } else {
                newStateTicks--;
                if (newStateTicks <= 1) {
                    newState = AlertData.IDLE; // 计时结束，状态重置
                    lkp = Optional.empty(); // 清除LKP
                    newPrimary = Optional.empty(); // 清除主目标
                    newStateTicks = 0;
                    newPState = AlertData.UNTRACKED; // 强制默认化玩家的观测状态
                    newPatienceTicks = CommonConfigs.PATIENCE_TICKS.getAsInt(); // 重置耐心值
                }
            }
        }


        newStatesMap.put(uuid, newPState);
        newReactionsMap.put(uuid, newReaction);

        mob.setData(ModAttachments.ALERT_DATA, new AlertData(
                newState,
                newProgressMap,
                newStatesMap,
                newReactionsMap,
                lkp,
                newPrimary,
                newStateTicks,
                Math.max(0, newPatienceTicks)
        ));
    }

    // 三点检查
    private static boolean canSeeAnyPart(Mob observer, Entity target, Vec3 start) {
        Vec3[] checkPoints = {
                // 头
                target.getEyePosition(),
                // 胸
                target.position().add(0, target.getBbHeight() / 2.0, 0),
                // 脚
                target.position().add(0, 0.1, 0)
        };

        for (Vec3 end : checkPoints) {
            if (isLineClear(observer, start, end)) return true;
        }
        return false;
    }

    // 视线射线检测
    private static boolean isLineClear(Mob observer, Vec3 start, Vec3 end) {
        // TODO 能透过玻璃
        return observer.level().clip(new ClipContext(
                start, end,
                ClipContext.Block.COLLIDER,
                ClipContext.Fluid.NONE,
                observer
        )).getType() == HitResult.Type.MISS;
    }

    // FOV判定
    private static boolean isWithinFOV(Vec3 lookVec, Vec3 targetDir) {
        boolean isVerticalLooking = Math.abs(lookVec.x) < 0.0001 && Math.abs(lookVec.z) < 0.0001;

        // 水平角度判定
        if (!isVerticalLooking) {
            Vec3 lookHorizontal = new Vec3(lookVec.x, 0, lookVec.z).normalize();
            Vec3 targetHorizontal = new Vec3(targetDir.x, 0, targetDir.z).normalize();

            double horizontalDot = lookHorizontal.dot(targetHorizontal);
            double horizontalThreshold = Math.cos(Math.toRadians(CommonConfigs.DETECTION_HORIZONTAL_FOV.get() / 2.0));

            if (horizontalDot < horizontalThreshold) return false;
        }

        // 垂直角度判定
        double pitchToTargetDegrees = Math.toDegrees(Math.asin(targetDir.y));

        double maxUpPitch = CommonConfigs.DETECTION_VERTICAL_UP_FOV.get();
        double maxDownPitch = -CommonConfigs.DETECTION_VERTICAL_DOWN_FOV.get();

        return pitchToTargetDegrees >= maxDownPitch && pitchToTargetDegrees <= maxUpPitch;
    }
}
