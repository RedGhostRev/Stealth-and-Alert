package net.rev.stealthandalert.client.gui.overlay;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.math.Axis;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.rev.stealthandalert.StealthAndAlert;
import net.rev.stealthandalert.attachment.AlertData;
import net.rev.stealthandalert.attachment.ModAttachments;
import net.rev.stealthandalert.config.ClientConfigs;
import net.rev.stealthandalert.util.ModTags;

import java.util.*;

public class AlertIndicatorOverlay {
    private static final ResourceLocation ALERT_INDICATOR = ResourceLocation.fromNamespaceAndPath(StealthAndAlert.MOD_ID,
            "textures/gui/alert_indicator.png");
    private static final ResourceLocation ALERT_INDICATOR_FRAME = ResourceLocation.fromNamespaceAndPath(StealthAndAlert.MOD_ID,
            "textures/gui/alert_indicator_frame.png");

    public static final Map<UUID, Integer> FULL_AWARENESS_TICKS = new HashMap<>();

    public static final List<IndicatorData> ACTIVE_POOL = new ArrayList<>();

    public static final Set<UUID> EXPIRED_GHOSTS = new HashSet<>();

    public static class IndicatorData {
        final UUID uuid;
        float level;
        float angle;      // 精确角度，用于平滑渲染
        int sectorIndex;  // 当前帧位于的扇区号 (0~23)
        int joinedTick;

        int outAnimTick = -1;
        boolean disappearing = false;

        IndicatorData(UUID uuid, float level, float angle, int sectorIndex, int joinedTick) {
            this.uuid = uuid;
            this.level = level;
            this.angle = angle;
            this.sectorIndex = sectorIndex;
            this.joinedTick = joinedTick;
        }
    }

    public static void render(GuiGraphics graphics, DeltaTracker deltaTracker) {
        if (!ClientConfigs.ALERT_INDICATOR.turnOn.get()) return;
        Minecraft mc = Minecraft.getInstance();
        if (mc.options.hideGui) return;
        if (mc.player == null || mc.level == null) return;

        UUID myUUID = mc.player.getUUID();
        int radius = ClientConfigs.ALERT_INDICATOR.radius.getAsInt();

        class CurrentMobInfo {
            final Mob mob;
            final float level;
            final float angle;
            final int sector;

            CurrentMobInfo(Mob mob, float level, float angle, int sector) {
                this.mob = mob;
                this.level = level;
                this.angle = angle;
                this.sector = sector;
            }
        }

        List<CurrentMobInfo> currentInfos = new ArrayList<>();
        Set<UUID> aliveMobUuids = new HashSet<>();

        // 获取实体
        for (Entity entity : mc.level.entitiesForRendering()) {
            if (entity instanceof Mob mob && mob.getType().is(ModTags.Entities.SEEKERS)) {
                if (!mob.isAlive() || mob.isRemoved() || mob.isDeadOrDying()) continue;
                AlertData data = mob.getData(ModAttachments.ALERT_DATA);
                float level = data.targetAwareness().getOrDefault(myUUID, 0.0F);
                aliveMobUuids.add(mob.getUUID());

                if (level > 0F) {
                    // 计算这个怪物的相对角度
                    double dX = mob.getX() - mc.player.getX();
                    double dZ = mob.getZ() - mc.player.getZ();
                    double angleToMob = Mth.atan2(dZ, dX);
                    float playerRot = mc.player.getViewYRot(1.0F);
                    float relativeAngle = (float) Math.toDegrees(angleToMob) - playerRot - 90.0F;
                    // 将角度标准化到 0 ~ 360 之间
                    float normalizedAngle = (relativeAngle % 360 + 360) % 360;

                    int sectorIndex = (int) (normalizedAngle / 15.0F);
                    currentInfos.add(new CurrentMobInfo(mob, level, normalizedAngle, sectorIndex));

                    if (level >= 100F) {
                        FULL_AWARENESS_TICKS.putIfAbsent(mob.getUUID(), mc.player.tickCount);

                        int startTick = FULL_AWARENESS_TICKS.get(mob.getUUID());
                        if (mc.player.tickCount - startTick > 15) {
                            EXPIRED_GHOSTS.add(mob.getUUID());
                        }
                    } else {
                        FULL_AWARENESS_TICKS.remove(mob.getUUID());
                        EXPIRED_GHOSTS.remove(mob.getUUID());
                    }

                    if (!EXPIRED_GHOSTS.contains(mob.getUUID())) {
                        currentInfos.add(new CurrentMobInfo(mob, level, normalizedAngle, sectorIndex));
                    }
                } else {
                    FULL_AWARENESS_TICKS.remove(mob.getUUID());
                    EXPIRED_GHOSTS.add(mob.getUUID());
                }
            }
        }

        if (mc.player.tickCount % 20 == 0) {
            FULL_AWARENESS_TICKS.keySet().removeIf(uuid -> !aliveMobUuids.contains(uuid));
            EXPIRED_GHOSTS.removeIf(uuid -> !aliveMobUuids.contains(uuid));
        }

        // 维护历史锁定池（更新老条）
        for (int i = ACTIVE_POOL.size() - 1; i >= 0; i--) {
            IndicatorData oldData = ACTIVE_POOL.get(i);
            boolean found = false;

            for (CurrentMobInfo info : currentInfos) {
                if (info.mob.getUUID().equals(oldData.uuid)) {
                    if (oldData.level >= 100.0F && info.level < 100.0F) {
                        oldData.joinedTick = mc.player.tickCount;
                    }
                    // 更新它的最新状态
                    oldData.level = info.level;
                    oldData.angle = info.angle;             // 保持平滑转动
                    oldData.sectorIndex = info.sector;
                    if (info.mob.isAlive() && !info.mob.isDeadOrDying() && !info.mob.isRemoved()) {// 玩家转身时，它可能会掉进相邻的扇区
                        found = true;
                    }
                    break;
                }
            }

            if (!found || oldData.level <= 0) {
                if (!oldData.disappearing) {
                    oldData.disappearing = true;
                    oldData.outAnimTick = mc.player.tickCount;
                }
                // 延迟删除以播放动画
                // ACTIVE_POOL.remove(i);
            }
        }

        // 处理新条
        for (CurrentMobInfo info : currentInfos) {
            // 如果已经在池子里了，跳过
            boolean alreadyInPool = ACTIVE_POOL.stream().anyMatch(old -> old.uuid.equals(info.mob.getUUID()));
            if (alreadyInPool) continue;

            boolean suppressed = false;
            for (IndicatorData oldData : ACTIVE_POOL) {
                if (oldData.sectorIndex == info.sector) {
                    if (info.level < oldData.level) {
                        suppressed = true;
                        break;
                    }
                }
            }

            if (!suppressed) {
                ACTIVE_POOL.add(new IndicatorData(info.mob.getUUID(), info.level, info.angle, info.sector, mc.player.tickCount));
            }
        }
        // 渲染
        Iterator<IndicatorData> iterator = ACTIVE_POOL.iterator();
        while (iterator.hasNext()) {
            IndicatorData data = iterator.next();

            float unfoldProgress;
            boolean removeNow = false;

            if (data.disappearing) {
                // 离场动画
                float animDuration = 10F; // 动画帧长
                float ticksGone = mc.player.tickCount - data.outAnimTick + Minecraft.getInstance().getTimer().getGameTimeDeltaPartialTick(true);
                unfoldProgress = Math.max(0F, 1F - (ticksGone / animDuration));
                unfoldProgress = unfoldProgress * unfoldProgress; // ease-in
                if (unfoldProgress <= 0.01F || FULL_AWARENESS_TICKS.containsKey(data.uuid)) {
                    removeNow = true;
                }
            } else {
                // 未离场，进场动画
                float ticksAlive = mc.player.tickCount - data.joinedTick + Minecraft.getInstance().getTimer().getGameTimeDeltaPartialTick(true);
                float unfoldDuration = 10F;
                unfoldProgress = Math.min(1F, ticksAlive / unfoldDuration);
                unfoldProgress = 1.0F - (1.0F - unfoldProgress) * (1.0F - unfoldProgress);
            }

            if (removeNow) {
                iterator.remove();
            } else if (unfoldProgress > 0.01F) {
                drawIndicator(graphics, mc.player, data.uuid, data.level, data.angle, radius, unfoldProgress);
            }
        }
    }

    // 画出警戒条
    private static void drawIndicator(GuiGraphics graphics, Player player, UUID mobUuid, float level,
                                      float preciseAngle, int radius, float unfoldProgress) {
        // 计算XZ平面上怪物相对于玩家的水平向量

        // 计算怪物在世界中的绝对角度
        // 从X正半轴逆时针旋转的角度

        // 计算玩家当前的视线水平角度

        // 计算相对夹角
        // 如果怪物在正前方，角度为0；正右方，角度为90

        // 渲染变换部分
        int centerX = graphics.guiWidth() / 2;
        int centerY = graphics.guiHeight() / 2;
        graphics.pose().pushPose();
        // 将坐标系移到准星中心
        graphics.pose().translate(centerX, centerY, 0);
        // 绕Z轴旋转
        graphics.pose().mulPose(Axis.ZP.rotationDegrees(preciseAngle));

        // 缩小
        graphics.pose().scale(0.7F, 0.7F, 1.0F);
        // 作画
        //int individualRadius = 100; // 警戒条离准星的距离
        int imgSize = 64; // 贴图尺寸

        graphics.flush();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        if (level < 50F) {
            // 画出底色
            drawIndicatorLayer(graphics, radius, imgSize, unfoldProgress, 0F, 0F, 0F, 0.5F, 0.0F);
            // 填充白色
            float fillPercent = level / 50.0F;
            float actualFill = Math.min(fillPercent, unfoldProgress);
            drawIndicatorLayer(graphics, radius, imgSize, actualFill, 1F, 1F, 1F, 0.9F, 0.01F);
        } else if (level < 100) {
            // 画出底色
            drawIndicatorLayer(graphics, radius, imgSize, unfoldProgress, 1F, 1F, 1F, 0.9F, 0.0F);
            // 填充黄色
            float fillPercent = (level - 50.0F) / 50.0F;
            float actualFill = Math.min(fillPercent, unfoldProgress);
            drawIndicatorLayer(graphics, radius, imgSize, actualFill, 1.0F, 0.8F, 0.0F, 0.9F, 0.01F);
        } else {
            // 红色
            int startTick = FULL_AWARENESS_TICKS.getOrDefault(mobUuid, player.tickCount);
            float ticksSinceFull = player.tickCount - startTick + Minecraft.getInstance().getTimer().getGameTimeDeltaPartialTick(true);

            float animDuration = 15.0F;
            if (ticksSinceFull <= animDuration) {
                float progress = ticksSinceFull / animDuration; // 0.0 -> 1.0
                float easeOut = 1.0F - (progress * progress);   // 1.0 -> 0.0

                float coreAlpha = easeOut;
                drawIndicatorLayer(graphics, radius, imgSize, 1.0F, 1.0F, 0.0F, 0.0F, coreAlpha, 0.0F);

                float spread = progress * 15.0F;
                float glowAlpha = easeOut * 0.15F;

                if (spread > 0) {
                    graphics.pose().pushPose();

                    float glowScale = 1.0F + (progress * 0.3F); // 放大 1.3 倍
                    float invScale = 1.0F / glowScale;

                    // 围绕 360 度画 8 个方向的红色残影
                    for (int i = 0; i < 8; i++) {
                        double angle = Math.PI / 4.0 * i;
                        float offsetX = (float) Math.cos(angle) * spread;
                        float offsetY = (float) Math.sin(angle) * spread;

                        // 往八个方向散开
                        graphics.pose().translate(offsetX, offsetY, 0);
                        // 放大
                        graphics.pose().translate(0, -radius, 0);
                        graphics.pose().scale(glowScale, glowScale, 1.0F);
                        graphics.pose().translate(0, radius, 0);

                        // 画出红色的扩散层
                        drawIndicatorLayer(graphics, radius, imgSize, 1.0F, 1.0F, 0.0F, 0.0F, glowAlpha, 0.01F + i * 0.001F);

                        // 逆变换
                        graphics.pose().translate(0, -radius, 0);
                        graphics.pose().scale(invScale, invScale, 1.0F); // 乘以倒数，恢复缩放
                        graphics.pose().translate(0, radius, 0);
                        graphics.pose().translate(-offsetX, -offsetY, 0);

                    }
                    graphics.pose().popPose();
                }
            }
        }

        if (level < 100) {
            drawIndicatorLayerFrame(graphics, radius, imgSize, unfoldProgress, 0.5F, 0.5F, 0.5F, 0.2F, 0.02F);
        }

        // 重置颜色
        graphics.setColor(1F, 1F, 1F, 1F);

        // drawX为负的一半，实现水平居中
        // drawY为负的半径再减去一半，实现垂直居中在圆周上
        // int drawX = -imgSize / 2;
        // int drawY = -radius - imgSize / 2;

        // 绘制白色底图
        // graphics.blit(INDICATOR, drawX, drawY, 0, 0, imgSize, imgSize, imgSize, imgSize);
        graphics.flush();
        RenderSystem.disableBlend();
        graphics.pose().popPose();
    }

    // 填充颜色
    private static void drawIndicatorLayer(GuiGraphics graphics, int radius, int imgSize, float levelPercent,
                                           float r, float g, float b, float a, float zOffSet) {
        int maxW = 62;
        int currentW = Math.max(2, (int) (maxW * levelPercent) & ~1);
        int uOffSet = (imgSize / 2) - (currentW / 2);

        int drawX = -currentW / 2;
        int drawY = -radius - imgSize / 2;

        // 设置颜色
        graphics.setColor(r, g, b, a);

        graphics.pose().translate(0, 0, zOffSet);
        // 渲染
        graphics.blit(ALERT_INDICATOR, drawX, drawY, uOffSet, 0, currentW, imgSize, imgSize, imgSize);
        graphics.pose().translate(0, 0, -zOffSet);
    }

    private static void drawIndicatorLayerFrame(GuiGraphics graphics, int radius, int imgSize, float levelPercent,
                                                float r, float g, float b, float a, float zOffSet) {
        int maxW = 64;
        int currentW = Math.max(2, (int) (maxW * levelPercent) & ~1);
        int uOffSet = (imgSize / 2) - (currentW / 2);

        int drawX = -currentW / 2;
        int drawY = -radius - imgSize / 2;

        // 设置颜色
        graphics.setColor(r, g, b, a);

        graphics.pose().translate(0, 0, zOffSet);
        // 渲染
        graphics.blit(ALERT_INDICATOR_FRAME, drawX, drawY, uOffSet, 0, currentW, imgSize, imgSize, imgSize);
        graphics.pose().translate(0, 0, -zOffSet);
    }
}
