package net.rev.stealthandalert.client.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.MeshData;
import com.mojang.blaze3d.vertex.VertexBuffer;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.platform.GlStateManager;
import foundry.veil.api.client.render.VeilRenderBridge;
import foundry.veil.api.client.render.VeilRenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.rev.stealthandalert.StealthAndAlert;
import net.rev.stealthandalert.attachment.AlertData;
import net.rev.stealthandalert.attachment.ModAttachments;
import net.rev.stealthandalert.client.heatmap.HeatmapComputer;
import net.rev.stealthandalert.config.ClientConfigs;
import net.rev.stealthandalert.util.CommonUtils;
import net.rev.stealthandalert.util.ConeRaycaster;
import net.rev.stealthandalert.util.ModTags;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 怪物视锥手电筒投影渲染器（Veil 版）。
 * 玩家潜行时绘制红色半透明视锥。以生物眼睛为点光源，
 * 沿视锥内每个方向做 3D 射线检测，光线打到第一个方块表面即为投影点，
 * 形成像手电筒照在地面/墙面/天花板上一样贴合实际方块表面的光斑。
 */
@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(modid = StealthAndAlert.MOD_ID, value = Dist.CLIENT)
public class VisionConeRenderer {

    /** 水平方向（yaw）射线采样数（与 ConeRaycaster 保持一致） */
    private static final int CONE_RAYS = ConeRaycaster.CONE_RAYS;
    /** 垂直方向（pitch）射线采样数 */
    private static final int PITCH_STEPS = ConeRaycaster.PITCH_STEPS;

    /** 热力图计算器（给锥形按危险度着色） */
    private static final HeatmapComputer COMPUTER = new HeatmapComputer();
    private static final Map<UUID, Long> LAST_COMPUTE_TICK = new HashMap<>();
    private static final Map<UUID, Long> LAST_RENDER_TICK = new HashMap<>();
    /** 热力图更新间隔（tick）：锁定玩家 / 移动中 / 静止 */
    private static final int INTERVAL_HOSTILE = 1;
    private static final int INTERVAL_MOVING = 2;
    private static final int INTERVAL_PASSIVE = 10;
    /** 每 tick 最多异步提交的计算数：错开各怪物计算时间，防止一次性算太多造成卡顿 */
    private static final int MAX_COMPUTE_PER_TICK = 4;
    private static long lastBudgetTick = Long.MIN_VALUE;
    private static int budgetUsed = 0;
    private static boolean outlineLogged = false;

    @SubscribeEvent
    public static void onRenderLevelStage(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_ENTITIES) return;
        if (!ClientConfigs.HEATMAP.enable.get()) return;
        ModShaders.ensureInit();
        if (ModShaders.frustumShader == null) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.options.hideGui) return;
        if (!mc.player.isCrouching()) return;

        Level level = mc.player.level();
        Vec3 cam = mc.gameRenderer.getMainCamera().getPosition();
        double searchRange = ClientConfigs.HEATMAP.searchRange.get();
        long gameTime = level.getGameTime();
        Player player = mc.player;

        // 每个游戏 tick 重置异步提交预算
        if (gameTime != lastBudgetTick) {
            lastBudgetTick = gameTime;
            budgetUsed = 0;
        }

        List<Mob> mobs = level.getEntitiesOfClass(
                Mob.class,
                mc.player.getBoundingBox().inflate(searchRange),
                m -> m.isAlive() && shouldShowVision(m)
        );

        var builder = Tesselator.getInstance().begin(
                VertexFormat.Mode.TRIANGLES, DefaultVertexFormat.POSITION_COLOR);

        boolean any = false;
        Vec3 firstOrigin = null;
        Vec3 firstBoundary = null;
        double firstRange = 0;
        List<ConeRaycaster.OccludedConeData> cones = new ArrayList<>();

        for (Mob mob : mobs) {
            // 使用玩家可见度修正后的有效视距：玩家越隐蔽，显示出的视锥越短，
            // 与实际 hasLineOfSight 检测距离保持一致
            var cone = ConeRaycaster.compute(mob, level, CommonUtils.getCorrectedViewRange(mob, player));
            if (cone == null || cone.grid().isEmpty()) continue;
            any = true;
            cones.add(cone);

            Vec3 origin = cone.origin();
            double range = cone.viewRange();

            if (firstOrigin == null) {
                firstOrigin = origin;
                firstRange = range;
                firstBoundary = cone.grid().get(0).get(0);
            }

            // 热力图（按生物威胁程度节流计算），用于给锥形按危险度着色
            HeatmapComputer.HeatmapResult heat = updateHeatmap(mob, player, gameTime);

            // ── 光束：眼睛 → 最近环（最陡向下射线打到的点），形成从头部射出的光锥 ──
            // 最近环同样内缩描边半宽，与光斑面边界衔接
            for (int yi = 0; yi < CONE_RAYS; yi++) {
                addVertex(builder, origin, origin, range, heat);
                addVertex(builder, origin, insetGridPoint(cone.grid(), yi, 0, OUTLINE_HALF_WIDTH), range, heat);
                addVertex(builder, origin, insetGridPoint(cone.grid(), yi + 1, 0, OUTLINE_HALF_WIDTH), range, heat);
            }

            // ── 光斑：射线网格打到的方块表面连成面（贴合地面/墙面/天花板）──
            // 网格顶点向内缩进描边半宽（OUTLINE_HALF_WIDTH），锥形面边界正好收在描边内侧，
            // 描边精准贴边：不再整带剔除（那会让锥形缩到描边之外）
            for (int yi = 0; yi < CONE_RAYS; yi++) {
                for (int pi = 0; pi < PITCH_STEPS; pi++) {
                    Vec3 p00 = insetGridPoint(cone.grid(), yi, pi, OUTLINE_HALF_WIDTH);
                    Vec3 p10 = insetGridPoint(cone.grid(), yi + 1, pi, OUTLINE_HALF_WIDTH);
                    Vec3 p11 = insetGridPoint(cone.grid(), yi + 1, pi + 1, OUTLINE_HALF_WIDTH);
                    Vec3 p01 = insetGridPoint(cone.grid(), yi, pi + 1, OUTLINE_HALF_WIDTH);
                    addVertex(builder, origin, p00, range, heat);
                    addVertex(builder, origin, p10, range, heat);
                    addVertex(builder, origin, p11, range, heat);
                    addVertex(builder, origin, p00, range, heat);
                    addVertex(builder, origin, p11, range, heat);
                    addVertex(builder, origin, p01, range, heat);
                }
            }
        }

        StealthAndAlert.LOGGER.info("[VisionCone] render mobs={} any={}", mobs.size(), any);
        if (firstOrigin != null) {
            StealthAndAlert.LOGGER.info(String.format(
                    "[VisionCone] origin=(%.1f,%.1f,%.1f) boundary0=(%.1f,%.1f,%.1f) range=%.1f cam=(%.1f,%.1f,%.1f)",
                    firstOrigin.x, firstOrigin.y, firstOrigin.z,
                    firstBoundary.x, firstBoundary.y, firstBoundary.z,
                    firstRange,
                    cam.x, cam.y, cam.z));
        }
        if (!any) return;

        // 锥形 mesh（先建，占用 Tesselator 单例）
        var mesh = builder.buildOrThrow();

        // ── 外轮廓描边 mesh（淡灰，透明度 70）──
        // 用 TRIANGLES 四边形生成宽线：GL_LINES 在 Core Profile 强制 1px、
        // lineWidth(2) 无效，1px 半透明淡灰线叠加在亮锥形面上几乎不可见，
        // 故改为每条线段生成一个 0.3 格宽的实心四边形，宽度可控、必可见。
        var outlineBuilder = Tesselator.getInstance().begin(
                VertexFormat.Mode.TRIANGLES, DefaultVertexFormat.POSITION_COLOR);
        for (ConeRaycaster.OccludedConeData cone : cones) {
            addOutline(outlineBuilder, cone.grid());
        }
        var outlineMesh = outlineBuilder.buildOrThrow();

        var shader = ModShaders.frustumShader;
        if (shader == null || !shader.isValid()) return;

        // AFTER_ENTITIES 阶段 PoseStack 已空，需手动从 Camera 构造视图矩阵
        // view = R(camera)^-1 * T(-cameraPos) → 将世界坐标转为眼空间
        Matrix4f modelView = new Matrix4f()
                .rotate(event.getCamera().rotation().conjugate(new org.joml.Quaternionf()))
                .translate((float)-cam.x, (float)-cam.y, (float)-cam.z);
        Matrix4f projection = new Matrix4f(event.getProjectionMatrix());
        shader.setDefaultUniforms(VertexFormat.Mode.TRIANGLES, modelView, projection);

        // 诊断：强制所有顶点 Y = debugForceY（默认 -9999 关闭），验证 ModelView/投影链路
        var debugYUniform = shader.getUniform("DebugForceY");
        if (debugYUniform != null && debugYUniform.isValid()) {
            double debugForceY = ClientConfigs.HEATMAP.debugForceY.get();
            debugYUniform.setFloat((float) debugForceY);
        }

        // 双路径设置：Veil setShader + RenderSystem setShader（触发 Veil mixin）
        VeilRenderSystem.setShader(shader);
        RenderSystem.setShader(() -> VeilRenderBridge.toShaderInstance(shader));

        RenderSystem.enableBlend();
        RenderSystem.blendFunc(
                GlStateManager.SourceFactor.SRC_ALPHA,
                GlStateManager.DestFactor.ONE);
        RenderSystem.disableDepthTest();
        RenderSystem.disableCull();

        // 先画彩色锥形（加色混合，光斑感）
        drawMesh(mesh, modelView, projection);

        // 描边：改用正常 alpha 混合，半透明淡灰宽线更清晰（加色混合下 70 透明度太淡几乎看不见）
        RenderSystem.blendFunc(
                GlStateManager.SourceFactor.SRC_ALPHA,
                GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        drawMesh(outlineMesh, modelView, projection);

        // 一次性确认描边网格确实有顶点被绘制
        if (!outlineLogged) {
            outlineLogged = true;
            StealthAndAlert.LOGGER.info("[VisionCone] outline verts={} (per mob ≈ {})",
                    outlineMesh.drawState().vertexCount(), (CONE_RAYS * 2 + PITCH_STEPS * 2) * 6);
        }

        RenderSystem.enableDepthTest();
        RenderSystem.enableCull();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableBlend();

        cleanUp(gameTime);
    }

    /**
     * 视野显示过滤器：
     * <ul>
     *   <li>敌对生物（SEEKERS 标签 / Enemy 接口）→ 自动显示视野</li>
     *   <li>中立生物（CONDITIONAL_SEEKERS 标签 / NeutralMob）与其他无害生物 →
     *       被玩家用望远镜标记（ClientMarkManager）后才显示视野</li>
     * </ul>
     */
    private static boolean shouldShowVision(Mob mob) {
        if (mob.getType().is(ModTags.Entities.SEEKERS) || mob instanceof Enemy) {
            return true; // 敌对：自动显示
        }
        return ClientMarkManager.isMarked(mob.getId()); // 中立/无害：标记后显示
    }

    // ─── 视锥射线检测已抽取到 ConeRaycaster（供渲染与热力图共用）────────────

    /**
     * 按到眼睛的距离设置锥形颜色（近浓远淡、近深灰→远浅灰白渐变底色）；
     * 有危险度数据的点叠加危险度色（绿→黄→红），无数据时显示灰色渐变底色。
     */
    private static void addVertex(BufferBuilder builder, Vec3 origin, Vec3 p, double range,
                                  HeatmapComputer.HeatmapResult heat) {
        double dx = p.x - origin.x, dy = p.y - origin.y, dz = p.z - origin.z;
        double dist = Math.sqrt(dx * dx + dy * dy + dz * dz);
        float t = (float) Math.min(dist / range, 1.0);
        // 锥形底色：近深灰 (70,70,70,90) → 远浅灰白 (240,240,240,10)
        int baseR = (int) (70 + (240 - 70) * t);
        int baseA = (int) (90 + (10 - 90) * t);

        int r, g, b, a;
        float danger = heatDanger(heat, p.x, p.y, p.z);
        if (danger < 0) {
            r = baseR;
            g = baseR;
            b = baseR;
            a = baseA;
        } else {
            // 危险度色与底色按 danger 渐变融合。注意不能用 mix=danger：
            // 绿色只出现在低危险区(danger~0~0.2)，mix 太小会被灰底完全吸收而消失，
            // 故上抬混合系数下限(danger=0→0.35, danger=1→1.0)，让绿色段也能显现
            int argb = dangerToColor(danger, 255);
            int dr = (argb >> 16) & 0xFF;
            int dg = (argb >> 8) & 0xFF;
            int db = argb & 0xFF;
            float mix = Math.min(Math.max(0.35F + 0.65F * danger, 0.0F), 1.0F);
            r = (int) (baseR + (dr - baseR) * mix);
            g = (int) (baseR + (dg - baseR) * mix);
            b = (int) (baseR + (db - baseR) * mix);
            a = baseA;
        }
        builder.addVertex((float) p.x, (float) p.y, (float) p.z)
                .setColor(r, g, b, a);
    }

    /**
     * 将网格点 (yi,pi) 沿网格参数方向向内缩进 inset 格（仅边缘顶点内缩），
     * 使锥形面边界刚好收在描边内侧，描边精准贴边、既不覆盖锥形也不脱离锥形。
     */
    private static Vec3 insetGridPoint(List<List<Vec3>> grid, int yi, int pi, double inset) {
        Vec3 p = grid.get(yi).get(pi);
        int cr = grid.size() - 1;
        int ps = grid.get(0).size() - 1;
        Vec3 shifted = p;
        // yaw 方向边缘（左右两侧列）→ 向内列方向缩进
        if (yi == 0 && cr > 0) {
            shifted = shiftToward(shifted, grid.get(0).get(pi), grid.get(1).get(pi), inset);
        } else if (yi == cr && cr > 0) {
            shifted = shiftToward(shifted, grid.get(cr).get(pi), grid.get(cr - 1).get(pi), inset);
        }
        // pitch 方向边缘（最近/最远环）→ 向内环方向缩进
        if (pi == 0 && ps > 0) {
            shifted = shiftToward(shifted, grid.get(yi).get(0), grid.get(yi).get(1), inset);
        } else if (pi == ps && ps > 0) {
            shifted = shiftToward(shifted, grid.get(yi).get(ps), grid.get(yi).get(ps - 1), inset);
        }
        return shifted;
    }

    /** 从 base 向 (to-from) 方向移动 amount 距离 */
    private static Vec3 shiftToward(Vec3 base, Vec3 from, Vec3 to, double amount) {
        Vec3 dir = to.subtract(from);
        double len = dir.length();
        if (len < 1e-5) return base;
        return base.add(dir.scale(amount / len));
    }

    /** 描边宽线半宽（格） */
    private static final double OUTLINE_HALF_WIDTH = 0.10;






    /**
     * 在锥形网格四周绘制深灰色（不透明）外轮廓描边。
     * 每条轮廓线段生成一个垂直于线段、宽 OUTLINE_HALF_WIDTH*2 的实心四边形
     * （2 个三角形），这样在 Core Profile 下也能得到固定宽度的可见线条。
     */
    private static void addOutline(BufferBuilder outline, List<List<Vec3>> grid) {
        int cr = grid.size() - 1;          // 最大 yaw 下标
        int ps = grid.get(0).size() - 1;   // 最大 pitch 下标
        int r = 70, g = 70, b = 70, a = 255;

        // 最近环 (pi=0)
        for (int yi = 0; yi < cr; yi++) {
            addOutlineQuad(outline, grid.get(yi).get(0), grid.get(yi + 1).get(0), r, g, b, a);
        }
        // 最远环 (pi=ps)
        for (int yi = 0; yi < cr; yi++) {
            addOutlineQuad(outline, grid.get(yi).get(ps), grid.get(yi + 1).get(ps), r, g, b, a);
        }
        // 两侧 (yi=0 / yi=cr)
        for (int pi = 0; pi < ps; pi++) {
            addOutlineQuad(outline, grid.get(0).get(pi), grid.get(0).get(pi + 1), r, g, b, a);
            addOutlineQuad(outline, grid.get(cr).get(pi), grid.get(cr).get(pi + 1), r, g, b, a);
        }
    }

    /** 为线段 ab 生成一个 6 顶点（2 三角形）的宽四边形 */
    private static void addOutlineQuad(BufferBuilder b, Vec3 a, Vec3 c, int r, int g, int bl, int a_) {
        Vec3 dir = c.subtract(a);
        double len = dir.length();
        if (len < 1e-4) return;
        dir = dir.scale(1.0 / len);
        // 展开方向：取线段与竖直方向叉积（水平法线），线段几乎竖直时退回水平右向
        Vec3 n = dir.cross(new Vec3(0, 1, 0));
        if (n.lengthSqr() < 1e-6) {
            n = new Vec3(1, 0, 0);
        } else {
            n = n.normalize();
        }
        Vec3 off = n.scale(OUTLINE_HALF_WIDTH);
        Vec3 p0 = a.add(off);
        Vec3 p1 = a.subtract(off);
        Vec3 p2 = c.subtract(off);
        Vec3 p3 = c.add(off);
        addVertexColor(b, p0, r, g, bl, a_);
        addVertexColor(b, p1, r, g, bl, a_);
        addVertexColor(b, p2, r, g, bl, a_);
        addVertexColor(b, p0, r, g, bl, a_);
        addVertexColor(b, p2, r, g, bl, a_);
        addVertexColor(b, p3, r, g, bl, a_);
    }

    private static void addVertexColor(BufferBuilder b, Vec3 p, int r, int g, int bl, int a) {
        b.addVertex((float) p.x, (float) p.y, (float) p.z).setColor(r, g, bl, a);
    }

    /**
     * 查询某世界坐标在热力图网格中的危险度；越界/无数据返回 -1。
     */
    private static float heatDanger(HeatmapComputer.HeatmapResult heat, double wx, double wy, double wz) {
        if (heat == null) return -1.0F;
        int dx = (int) Math.floor(wx) - (int) heat.originX();
        int dz = (int) Math.floor(wz) - (int) heat.originZ();
        int hx = heat.gridHalf();
        if (dx < -hx || dx > hx || dz < -hx || dz > hx) return -1.0F;
        // 跨高程投影到地面时忽略 Y：层号 clamp 到网格范围内，只按 XZ 定位
        // （与 HeatmapComputer.compute 的 clamp 一致，保证能查到）
        int dy = Math.clamp((int) Math.floor(wy) - (int) heat.originY() - 1,
                -HeatmapComputer.GRID_Y_DOWN, HeatmapComputer.GRID_Y_UP);
        int cellsX = hx * 2 + 1;
        int cellsZ = hx * 2 + 1;
        int idx = (dy + HeatmapComputer.GRID_Y_DOWN) * cellsX * cellsZ + (dz + hx) * cellsX + (dx + hx);
        return heat.data()[idx];
    }

    /**
     * 危险等级 [0,1] → ARGB 颜色 (绿→黄→红)，alpha 0-255。
     */
    private static int dangerToColor(float danger, int alpha) {
        if (danger <= 0.5F) {
            int r = (int) (danger * 2 * 255);
            return (alpha << 24) | (r << 16) | (255 << 8);
        } else {
            int g = (int) ((1.0F - (danger - 0.5F) * 2) * 255);
            return (alpha << 24) | (255 << 16) | (g << 8);
        }
    }

    // ─── 热力图更新（节流）─────────────────────────────

    /**
     * 按生物威胁程度节流计算热力图，返回最新结果（可能为 null）。
     * 计算为异步提交 + 每 tick 预算限流，避免一次性计算过多生物造成卡顿。
     */
    private static HeatmapComputer.HeatmapResult updateHeatmap(Mob mob, Player player, long gameTime) {
        UUID uuid = mob.getUUID();
        LAST_RENDER_TICK.put(uuid, gameTime);
        Long lastTick = LAST_COMPUTE_TICK.get(uuid);
        int interval = getUpdateInterval(mob, player);
        boolean shouldUpdate = (lastTick == null || gameTime - lastTick >= interval)
                && (gameTime % interval == staggerSlot(mob) % interval);
        if (shouldUpdate && budgetUsed < MAX_COMPUTE_PER_TICK) {
            COMPUTER.computeAsync(mob, player);
            budgetUsed++;
            LAST_COMPUTE_TICK.put(uuid, gameTime);
        }
        return COMPUTER.get(mob);
    }

    /** 根据生物对玩家的敌意程度和移动状态返回更新间隔（锁定 1 / 移动 2 / 静止 10 tick） */
    private static int getUpdateInterval(Mob mob, Player player) {
        var data = mob.getData(ModAttachments.ALERT_DATA);
        int playerState = data.targetStates().getOrDefault(player.getUUID(), AlertData.UNTRACKED);
        if (playerState >= AlertData.AWARE || data.state() >= AlertData.SUSPICIOUS) {
            return INTERVAL_HOSTILE;
        }
        if (mob.getDeltaMovement().lengthSqr() > 0.0001) {
            return INTERVAL_MOVING;
        }
        return INTERVAL_PASSIVE;
    }

    /** 按实体 ID 取哈希槽位，用于均匀分散更新 */
    private static int staggerSlot(Mob mob) {
        return mob.getId() & 0x7FFFFFFF;
    }

    /** 清理长时间未渲染的怪物热力图数据 */
    private static void cleanUp(long gameTime) {
        LAST_RENDER_TICK.entrySet().removeIf(entry -> {
            if (gameTime - entry.getValue() > 200) {
                COMPUTER.remove(entry.getKey());
                LAST_COMPUTE_TICK.remove(entry.getKey());
                return true;
            }
            return false;
        });
    }

    /**
     * 手动走 VertexBuffer 路径绘制一个 mesh（避免 BufferUploader 覆盖 ModelViewMat）。
     */
    private static void drawMesh(MeshData mesh, Matrix4f modelView, Matrix4f projection) {
        VertexBuffer vb = new VertexBuffer(VertexBuffer.Usage.DYNAMIC);
        try {
            vb.bind();
            vb.upload(mesh);
            vb.drawWithShader(modelView, projection, RenderSystem.getShader());
        } finally {
            vb.close();
        }
    }
}
