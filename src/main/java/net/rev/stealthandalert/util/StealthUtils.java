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

        Map<UUID, Float> newProgressMap = new HashMap<>(data.targetProgress());
        float currentLevel = newProgressMap.getOrDefault(uuid, 0.0F);
        float newLevel;
        int newState = data.state();
        int newStateTicks = data.stateTicks(); // 状态切换计时器
        int newPatienceTicks = data.patienceTicks(); // 耐心值计时器
        // LKP（最后已知位置）
        Optional<Vec3> lkp = data.lastSeenPos();

        if (canSee) {
            // 如果看到了玩家
            // 警戒值每刻涨1.0点（每秒涨20.0点），最大100.0
            newLevel = Math.min(100.0F, currentLevel + 1.0F);
            // 更新LKP
            lkp = Optional.of(player.position());
            // 重置计时器为0
            newStateTicks = 0;
            // 重置耐心值
            newPatienceTicks = CommonConfigs.PATIENCE_TICKS.getAsInt();
        } else {
            // 如果看不到玩家，每刻回落0.5点
            newLevel = Math.max(0.0F, currentLevel - 0.5F);
        }
        newProgressMap.put(uuid, newLevel);

        // TODO 实现警戒条和警戒状态的解耦
        if (newLevel >= 100.0F) {
            // 如果玩家的警戒条满了，使怪物进入战斗状态
            newState = AlertData.FIGHTING;
        } else if (newLevel >= 50.0F && newState < AlertData.SEARCHING) {
            // 如果玩家的警戒条过半，且怪物处于怀疑或更低状态，使怪物进入搜寻状态
            newState = AlertData.SEARCHING;
        } else if (newLevel > 0.0F && newState <= AlertData.IDLE) {
            // 如果玩家的警戒条不空，且怪物处于闲逛状态，使怪物进入怀疑状态
            newState = AlertData.SUSPICIOUS;
        }

        boolean canStartReset = false; // 开始重置LKP记忆吗？
        // 如果敌人看不到玩家
        if (!canSee) {
            if (newLevel <= 0.0F && newState > AlertData.IDLE) {
                if (newState == AlertData.SEARCHING || newState == AlertData.FIGHTING) {
                    // 如果是搜寻或战斗状态，必须满足：走到了LKP附近或者敌人耐心值耗尽
                    canStartReset = (lkp.isPresent() && mob.distanceToSqr(lkp.get()) < 4.0) || --newPatienceTicks <= 1;
                } else {
                    // 怀疑状态：只有条空且没看到人，
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
                    newStateTicks = 0;
                    newPatienceTicks = CommonConfigs.PATIENCE_TICKS.getAsInt(); // 重置耐心值
                }
            }
        }


        mob.setData(ModAttachments.ALERT_DATA, new AlertData(
                newState,
                newProgressMap,
                data.targetStates(),
                lkp,
                data.reactionTicks(),
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
