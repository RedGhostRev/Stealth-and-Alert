package net.rev.stealthandalert.client.camera;

import net.minecraft.util.RandomSource;

public class CameraShakeManager {
    private static final RandomSource RANDOM = RandomSource.create();

    private static int shakeTicks = 0;
    private static int totalTicks = 0;      // 记录触发时的总 Tick 数
    private static float maxIntensity = 0f;
    private static float currentIntensity = 0f;

    // 旋转插值缓存
    public static float lastYawOffset = 0f;
    public static float lastPitchOffset = 0f;
    public static float targetYawOffset = 0f;
    public static float targetPitchOffset = 0f;

    // 3D 空间平移插值缓存
    public static float lastXOffset = 0f;
    public static float lastYOffset = 0f;
    public static float lastZOffset = 0f;
    public static float targetXOffset = 0f;
    public static float targetYOffset = 0f;
    public static float targetZOffset = 0f;

    public static void triggerShake(int ticks, float intensity) {
        shakeTicks = ticks;
        totalTicks = ticks;       // 初始总时间
        maxIntensity = intensity;
        currentIntensity = intensity;
    }

    public static void clientTick() {
        // 滚动历史记录
        lastYawOffset = targetYawOffset;
        lastPitchOffset = targetPitchOffset;
        lastXOffset = targetXOffset;
        lastYOffset = targetYOffset;
        lastZOffset = targetZOffset;

        if (shakeTicks > 0) {
            shakeTicks--;

            // 线性衰减：当前烈度 = 最大烈度 * (剩余时间 / 总时间)
            if (totalTicks > 0) {
                currentIntensity = maxIntensity * ((float) shakeTicks / (float) totalTicks);
            }

            // 1. 计算角度震颤目标
            targetYawOffset = (RANDOM.nextFloat() * 2f - 1f) * currentIntensity;
            targetPitchOffset = (RANDOM.nextFloat() * 2f - 1f) * currentIntensity;

            // 2. 计算空间平移震颤目标（局部坐标方块位移）
            float transScale = 0.04F;
            targetXOffset = (RANDOM.nextFloat() * 2f - 1f) * currentIntensity * transScale;
            targetYOffset = (RANDOM.nextFloat() * 2f - 1f) * currentIntensity * transScale;
            targetZOffset = (RANDOM.nextFloat() * 2f - 1f) * currentIntensity * transScale;

            if (shakeTicks == 0) {
                clear();
            }
        } else {
            targetYawOffset = 0f;
            targetPitchOffset = 0f;
            targetXOffset = 0f;
            targetYOffset = 0f;
            targetZOffset = 0f;
        }
    }

    private static void clear() {
        shakeTicks = 0;
        totalTicks = 0; // 🌟 顺手清理
        maxIntensity = 0f;
        currentIntensity = 0f;
        targetYawOffset = 0f;
        targetPitchOffset = 0f;
        targetXOffset = 0f;
        targetYOffset = 0f;
        targetZOffset = 0f;
    }

    public static boolean isShaking() {
        return currentIntensity > 0f || targetYawOffset != 0f || lastYawOffset != 0f || targetXOffset != 0f || lastXOffset != 0f;
    }
}
