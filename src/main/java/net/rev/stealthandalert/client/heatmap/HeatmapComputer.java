package net.rev.stealthandalert.client.heatmap;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.rev.stealthandalert.attachment.AlertData;
import net.rev.stealthandalert.attachment.ModAttachments;
import net.rev.stealthandalert.attribute.ModAttributes;
import net.rev.stealthandalert.config.CommonConfigs;
import net.rev.stealthandalert.config.EntityAlertConditionConfigLoader;
import net.rev.stealthandalert.config.EntityAlertConditionSettings;
import net.rev.stealthandalert.StealthAndAlert;
import net.rev.stealthandalert.util.ConeRaycaster;
import net.rev.stealthandalert.util.StealthUtils;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 危险度热力图计算器。
 * 对怪物视野锥内的每个采样点，计算 "被发现所需时长 → 归一化危险等级"。
 */
@OnlyIn(Dist.CLIENT)
public class HeatmapComputer {

    /** 热力图计算结果：数据数组 + 计算时使用的网格原点 + 该怪物的网格半边长 */
    public record HeatmapResult(float[] data, long originX, long originY, long originZ, int gridHalf) {}

    /** 热力图数据：怪物 UUID → 计算结果 */
    private final Map<UUID, HeatmapResult> results = new ConcurrentHashMap<>();

    /** 异步计算后台线程（单线程、daemon）：ConeRaycaster 射线检测在此执行，避免渲染线程卡顿 */
    private static final ExecutorService WORKER = Executors.newSingleThreadExecutor(r -> {
        Thread t = new Thread(r, "Stealth-Heatmap");
        t.setDaemon(true);
        return t;
    });

    /** 网格配置 */
    public static final int GRID_HALF_XZ = 16;       // 默认 XZ 半边长（格）
    public static final int MIN_GRID_HALF_XZ = 8;    // 热力图 XZ 半边长下限（格）
    public static final int MAX_GRID_HALF_XZ = 48;   // 热力图 XZ 半边长上限（格），与默认视距匹配
    /** 热力图视锥采样密度：渲染视锥用低密度(默认 16×6)保证每帧流畅，热力图用高密度减少插值失真 */
    public static final int HEATMAP_CONE_RAYS = 32;
    public static final int HEATMAP_PITCH_STEPS = 12;
    public static final int GRID_Y_UP = 6;           // Y 向上格数
    public static final int GRID_Y_DOWN = 4;         // Y 向下格数

    /** Danger value representing "not computed" or "out of range" */
    public static final float DANGER_NONE = -1.0F;

    /**
     * 异步提交热力图计算：后台线程执行 ConeRaycaster 射线检测，避免渲染线程卡顿。
     * 结果就绪后写入 results 缓存；渲染线程通过 get() 读取（期间返回上一次的结果）。
     */
    public void computeAsync(Mob mob, Player player) {
        WORKER.submit(() -> {
            try {
                computeSync(mob, player);
            } catch (Exception e) {
                StealthAndAlert.LOGGER.warn("[Heatmap] async compute failed for {}: {}",
                        mob, e.toString());
            }
        });
    }

    /** 后台线程中执行的实际计算（同步逻辑，不接触渲染线程） */
    private void computeSync(Mob mob, Player player) {
        Vec3 eye = mob.getEyePosition(1.0F);
        Level level = mob.level();

        EntityAlertConditionSettings settings = EntityAlertConditionConfigLoader.get(mob.getType());

        // 玩家暴露度
        double visibility = player.getAttributeValue(ModAttributes.VISIBILITY);
        double threshold = StealthUtils.VISIBILITY_THRESHOLD;

        // 怪物状态修正
        int state = mob.getData(ModAttachments.ALERT_DATA).state();
        double stateMod = getStateModifier(state);

        // 风险最大时间 (用于归一化)
        double rateMin = CommonConfigs.AWARENESS.increaseBasicRate.get()
                * (0.6 * CommonConfigs.AWARENESS.increaseVisibilityFactor.get())
                * 1.0
                * stateMod;
        double tMax = settings.getReactionTicks() + 100.0 / Math.max(rateMin, 0.001);

        // 复用视锥光斑的 3D 射线检测，覆盖完整上下视野角（includeUp=true），
        // 使用高密度采样(HEATMAP_CONE_RAYS×HEATMAP_PITCH_STEPS)减少插值失真，
        // 网格范围按怪物视距动态调整，保证热力图距离与视距匹配
        var cone = ConeRaycaster.compute(mob, level, true,
                HEATMAP_CONE_RAYS, HEATMAP_PITCH_STEPS);
        if (cone == null) return;

        int hx = (int) Math.clamp(Math.ceil(cone.viewRange()),
                MIN_GRID_HALF_XZ, MAX_GRID_HALF_XZ);
        int cellsX = hx * 2 + 1;
        int cellsZ = hx * 2 + 1;
        int totalCells = cellsX * cellsZ * (GRID_Y_UP + GRID_Y_DOWN + 1);
        float[] dangerField = new float[totalCells];
        for (int i = 0; i < totalCells; i++) dangerField[i] = DANGER_NONE;

        long baseX = (long) Math.floor(eye.x);
        long baseY = (long) Math.floor(eye.y);
        long baseZ = (long) Math.floor(eye.z);
        int reactionTicks = settings.getReactionTicks();

        // 视锥网格每个四边形单元内做双线性插值，填充单元覆盖的所有格子，
        // 使热力图危险区域连续（否则只有离散射线采样点，呈现不连续光带）
        int cr = HEATMAP_CONE_RAYS;
        int ps = HEATMAP_PITCH_STEPS;
        for (int yi = 0; yi < cr; yi++) {
            List<Vec3> c0 = cone.grid().get(yi);
            List<Vec3> c1 = cone.grid().get(yi + 1);
            for (int pi = 0; pi < ps; pi++) {
                Vec3 p00 = c0.get(pi);
                Vec3 p10 = c1.get(pi);
                Vec3 p11 = c1.get(pi + 1);
                Vec3 p01 = c0.get(pi + 1);

                double d00 = dangerAt(eye, p00, visibility, threshold, stateMod, reactionTicks, tMax);
                double d10 = dangerAt(eye, p10, visibility, threshold, stateMod, reactionTicks, tMax);
                double d11 = dangerAt(eye, p11, visibility, threshold, stateMod, reactionTicks, tMax);
                double d01 = dangerAt(eye, p01, visibility, threshold, stateMod, reactionTicks, tMax);

                double minX = Math.min(Math.min(p00.x, p10.x), Math.min(p11.x, p01.x));
                double maxX = Math.max(Math.max(p00.x, p10.x), Math.max(p11.x, p01.x));
                double minZ = Math.min(Math.min(p00.z, p10.z), Math.min(p11.z, p01.z));
                double maxZ = Math.max(Math.max(p00.z, p10.z), Math.max(p11.z, p01.z));

                int minDX = (int) Math.floor(minX) - (int) baseX;
                int maxDX = (int) Math.floor(maxX) - (int) baseX;
                int minDZ = (int) Math.floor(minZ) - (int) baseZ;
                int maxDZ = (int) Math.floor(maxZ) - (int) baseZ;

                double spanX = Math.max(maxX - minX, 1e-5);
                double spanZ = Math.max(maxZ - minZ, 1e-5);

                for (int dx = minDX; dx <= maxDX; dx++) {
                    if (dx < -hx || dx > hx) continue;
                    for (int dz = minDZ; dz <= maxDZ; dz++) {
                        if (dz < -hx || dz > hx) continue;
                        double wx = baseX + dx + 0.5;
                        double wz = baseZ + dz + 0.5;
                        double u = (wx - minX) / spanX;
                        double v = (wz - minZ) / spanZ;
                        // 双线性插值危险度
                        double dTop = d00 + (d10 - d00) * u;
                        double dBot = d01 + (d11 - d01) * u;
                        double danger = dTop + (dBot - dTop) * v;
                        // 地面高度同样插值，得出该格应落的热力图层
                        double hTop = p00.y + (p10.y - p00.y) * u;
                        double hBot = p01.y + (p11.y - p01.y) * u;
                        double hy = hTop + (hBot - hTop) * v;
                        int dy = Math.clamp((int) Math.floor(hy) - (int) baseY - 1,
                                -GRID_Y_DOWN, GRID_Y_UP);
                        // 填该层及上下各一层，提高渲染端(顶点内缩/取整)命中率
                        for (int ly = -1; ly <= 1; ly++) {
                            int ldy = Math.clamp(dy + ly, -GRID_Y_DOWN, GRID_Y_UP);
                            int idx = (ldy + GRID_Y_DOWN) * cellsX * cellsZ + (dz + hx) * cellsX + (dx + hx);
                            dangerField[idx] = Math.max(dangerField[idx], (float) danger);
                        }
                    }
                }
            }
        }

        results.put(mob.getUUID(), new HeatmapResult(dangerField, baseX, baseY, baseZ, hx));
    }

    /**
     * 获取指定怪物的热力图数据。若无则返回 null。
     */
    public HeatmapResult get(Mob mob) {
        return results.get(mob.getUUID());
    }

    /**
     * 清除指定怪物的结果（实体被移除时）。
     */
    public void remove(UUID uuid) {
        results.remove(uuid);
    }

    /** 清除所有结果 */
    public void clear() {
        results.clear();
    }

    // ─── 内部计算工具 ─────────────────────────────────────

    /** 计算某投影点（忽略 Y，只用 XZ 水平距离）的危险度 */
    private static double dangerAt(Vec3 eye, Vec3 p, double visibility,
                                   double threshold, double stateMod,
                                   int reactionTicks, double tMax) {
        double dx = p.x - eye.x, dz = p.z - eye.z;
        double dist = Math.sqrt(dx * dx + dz * dz);
        return calculateDanger(dist, visibility, threshold, stateMod, reactionTicks, tMax);
    }

    /**
     * 计算归一化危险等级 [0, 1]，完全对齐 StealthEngine.updateIndividual 的涨条公式。
     */
    private static double calculateDanger(double dist, double visibility,
                                           double threshold, double stateMod,
                                           int reactionTicks, double tMax) {
        // 可见度系数
        double t = Math.clamp((visibility - threshold) / (1.0 - threshold), 0.0, 1.0);
        double visMod = 0.6 + 0.4 * Math.sqrt(t);
        visMod *= CommonConfigs.AWARENESS.increaseVisibilityFactor.get();
        visMod = Math.clamp(visMod, 0.1, 10.0);

        // 距离系数
        double distMod = 1.0;
        if (dist <= 16.0) {
            double d = Math.clamp(dist, 0.4, 16.0);
            distMod = Math.pow(8.0 / d, 2.5);
            distMod = Math.clamp(distMod, 1.0, 300.0);
            distMod *= CommonConfigs.AWARENESS.increaseDistanceFactor.get();
        }

        // 涨条速率
        double basicRate = CommonConfigs.AWARENESS.increaseBasicRate.get();
        double rate = basicRate * visMod * distMod * stateMod;
        if (rate < 0.001) return 0.0;

        // 被发现所需时间 (tick)
        double tDiscover = reactionTicks + 100.0 / rate;

        // 归一化: 越小越危险→危险等级越高
        return Math.clamp(1.0 - (tDiscover / tMax), 0.0, 1.0);
    }

    /** 获取全局状态对涨条速率的修正因子 */
    private static double getStateModifier(int state) {
        double mod = 1.0;
        if (state == AlertData.SUSPICIOUS) {
            mod *= CommonConfigs.AWARENESS.increaseSuspiciousFactor.get();
        } else if (state == AlertData.SEARCHING) {
            mod *= CommonConfigs.AWARENESS.increaseSearchingFactor.get();
        }
        return mod;
    }

    // ─── 调试 ─────────────────────────────────────────────

    public int getResultCount() {
        return results.size();
    }
}
