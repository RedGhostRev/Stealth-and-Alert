package net.rev.stealthandalert.util;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.rev.stealthandalert.config.EntityAlertConditionConfigLoader;
import net.rev.stealthandalert.config.EntityAlertConditionSettings;

import java.util.ArrayList;
import java.util.List;

/**
 * 视锥"手电筒光斑"的 3D 射线检测工具。
 * <p>
 * 以生物眼睛为点光源，沿视锥（水平 FOV + 向下垂直 FOV）内每个方向做 3D 射线检测，
 * 只保留朝向地面的射线，光线打到第一个方块表面即为投影点；没打中的射线垂直下压到该 XZ 的地面，
 * 从而只生成"地面投影"上的点，不生成空中/天空部分。
 * <p>
 * 供 {@code VisionConeRenderer}（渲染）与 {@code HeatmapComputer}（危险度计算）共用，
 * 避免两者互相引用造成循环依赖。
 */
public final class ConeRaycaster {

    /** 水平方向（yaw）射线采样数 */
    public static final int CONE_RAYS = 16;
    /** 垂直方向（pitch）射线采样数 */
    public static final int PITCH_STEPS = 6;

    private static final Vec3 UP = new Vec3(0, 1, 0);

    private ConeRaycaster() {
    }

    /**
     * 3D 视锥网格数据：origin = 眼睛；grid = [yaw][pitch] 网格，
     * 每个点是该方向射线打到第一个方块表面（地面）的位置（或最大视野距离处）。
     */
    public record OccludedConeData(Vec3 origin, List<List<Vec3>> grid, double viewRange) {}

    /**
     * 计算生物视锥的地面投影网格（默认仅向下投影，供渲染手电筒光斑使用）。
     */
    public static OccludedConeData compute(Mob mob, Level level) {
        return compute(mob, level, false, CONE_RAYS, PITCH_STEPS);
    }

    /**
     * 计算生物视锥的地面投影网格（默认采样密度）。
     *
     * @param includeUp 为 {@code true} 时包含向上视野角（verticalUpFov），供危险热力图
     *                  完整覆盖上/下视野；为 {@code false} 时仅向下，保持"只贴地"渲染效果
     * @return 视锥网格数据（grid 永不为空）
     */
    public static OccludedConeData compute(Mob mob, Level level, boolean includeUp) {
        return compute(mob, level, includeUp, CONE_RAYS, PITCH_STEPS);
    }

    /**
     * 计算生物视锥的地面投影网格，可指定采样密度。
     *
     * @param includeUp  为 {@code true} 时包含向上视野角（verticalUpFov）
     * @param coneRays   水平方向（yaw）射线采样数
     * @param pitchSteps 垂直方向（pitch）射线采样数
     * @return 视锥网格数据（grid 永不为空）
     */
    public static OccludedConeData compute(Mob mob, Level level, boolean includeUp,
                                           int coneRays, int pitchSteps) {
        Vec3 eye = mob.getEyePosition(1.0F);
        Vec3 look = mob.getViewVector(1.0F).normalize();

        EntityAlertConditionSettings settings = EntityAlertConditionConfigLoader.get(mob.getType());
        double range = settings.getViewRange();
        double halfHFov = Math.toRadians(settings.getHorizontalFov() / 2.0);
        double maxDown = Math.toRadians(settings.getMaxDownPitch());
        double maxUp = includeUp ? Math.toRadians(settings.getMaxUpPitch()) : 0.0;

        // 水平朝向 + 右向量
        Vec3 lookH = new Vec3(look.x, 0, look.z);
        if (lookH.lengthSqr() < 1e-6) lookH = new Vec3(0, 0, 1);
        else lookH = lookH.normalize();
        Vec3 rightH = lookH.cross(UP).normalize();

        // 视线相对水平面的绝对仰角
        double lookElev = Math.asin(Math.clamp(look.y, -1.0, 1.0));

        List<List<Vec3>> grid = new ArrayList<>(coneRays + 1);
        for (int yi = 0; yi <= coneRays; yi++) {
            double yaw = -halfHFov + (2.0 * halfHFov) * yi / coneRays;
            double cy = Math.cos(yaw), sy = Math.sin(yaw);
            Vec3 dirH = new Vec3(
                    lookH.x * cy + rightH.x * sy,
                    0,
                    lookH.z * cy + rightH.z * sy
            ).normalize();

            List<Vec3> column = new ArrayList<>(pitchSteps + 1);
            // relPitch ∈ [-maxDown, +maxUp]：向下到向上完整视野角（includeUp=false 时仅向下）
            for (int pi = 0; pi <= pitchSteps; pi++) {
                double relPitch = -maxDown + (maxDown + maxUp) * ((double) pi / pitchSteps);
                double elev = lookElev + relPitch;
                double ce = Math.cos(elev), se = Math.sin(elev);
                Vec3 dir = new Vec3(dirH.x * ce, se, dirH.z * ce);

                Vec3 end = new Vec3(eye.x + dir.x * range, eye.y + dir.y * range, eye.z + dir.z * range);
                ClipContext ctx = CommonUtils.getClipContext(mob, eye, end);
                BlockHitResult hit = level.clip(ctx);

                Vec3 point;
                if (hit.getType() == HitResult.Type.MISS) {
                    // 射线过浅没打到方块（会抬到空中/射向天）：垂直向下找该 XZ 的地面，保持贴地
                    point = clampToGround(level, mob, end.x, end.z, end.y);
                } else {
                    point = hit.getLocation();
                }
                column.add(point);
            }
            grid.add(column);
        }

        return new OccludedConeData(eye, grid, range);
    }

    /**
     * 从 fromY 垂直向下找该 (x,z) 处的地面，返回地面命中点；找不到则返回原位置。
     */
    private static Vec3 clampToGround(Level level, Mob mob, double x, double z, double fromY) {
        Vec3 start = new Vec3(x, fromY, z);
        Vec3 end = new Vec3(x, level.getMinBuildHeight(), z);
        ClipContext ctx = CommonUtils.getClipContext(mob, start, end);
        BlockHitResult hit = level.clip(ctx);
        return hit.getType() == HitResult.Type.MISS ? new Vec3(x, fromY, z) : hit.getLocation();
    }
}
