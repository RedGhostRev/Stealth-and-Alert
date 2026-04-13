package net.rev.stealthandalert.util;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.rev.stealthandalert.attachment.AlertData;
import net.rev.stealthandalert.config.CommonConfigs;

public class StealthUtils {
    private StealthUtils() {
    }

    // 视线检测——如果mob能看到target，则返回true
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
