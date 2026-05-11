package net.rev.stealthandalert.client.gui.overlay;

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

import java.util.UUID;

public class StealthHUDOverlay {
    private static final ResourceLocation INDICATOR = ResourceLocation.fromNamespaceAndPath(StealthAndAlert.MOD_ID,
            "textures/gui/alert_indicator.png");

    public static void render(GuiGraphics graphics, DeltaTracker deltaTracker) {
        if (!ClientConfigs.ALERT_INDICATOR.get()) return;
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null) return;

        UUID myUUID = mc.player.getUUID();

        // 获取实体
        for (Entity entity : mc.level.entitiesForRendering()) {
            if (entity instanceof Mob mob && mob.getType().is(ModTags.Entities.SEEKERS)) {
                AlertData data = mob.getData(ModAttachments.ALERT_DATA);
                float level = data.targetAwareness().getOrDefault(myUUID, 0.0F);
                if (level > 0F) {
                    // int individualRadius = 100 + (mob.getId() % 5) * 3;
                    drawIndicator(graphics, mc.player, mob, level, ClientConfigs.ALERT_INDICATOR_RADIUS.getAsInt());
                }
            }
        }
    }

    // 画出警戒条
    private static void drawIndicator(GuiGraphics graphics, Player player, Mob mob, float level, int radius) {
        // 计算XZ平面上怪物相对于玩家的水平向量
        double dX = mob.getX() - player.getX();
        double dZ = mob.getZ() - player.getZ();

        // 计算怪物在世界中的绝对角度
        // 从X正半轴逆时针旋转的角度
        double angleToMob = Mth.atan2(dZ, dX);

        // 计算玩家当前的视线水平角度
        float playerRotation = player.getViewYRot(1.0F);

        // 计算相对夹角
        // 如果怪物在正前方，角度为0；正右方，角度为90
        float relativeAngle = (float) Math.toDegrees(angleToMob) - playerRotation - 90.0F;

        // 渲染变换部分
        int centerX = graphics.guiWidth() / 2;
        int centerY = graphics.guiHeight() / 2;
        graphics.pose().pushPose();
        // 将坐标系移到准星中心
        graphics.pose().translate(centerX, centerY, 0);
        // 绕Z轴旋转
        graphics.pose().mulPose(Axis.ZP.rotationDegrees(relativeAngle));

        // 缩小
        graphics.pose().scale(0.7F, 0.7F, 1.0F);
        // 作画
        //int individualRadius = 100; // 警戒条离准星的距离
        int imgSize = 64; // 贴图尺寸

        if (level < 50F) {
            // 画出底色
            drawIndicatorLayer(graphics, radius, imgSize, 1.0F, 0F, 0F, 0F, 0.5F, 0.0F);
            // 填充白色
            float fillPercent = level / 50.0F;
            drawIndicatorLayer(graphics, radius, imgSize, fillPercent, 1F, 1F, 1F, 1F, 0.01F);
        } else if (level < 100) {
            // 画出底色
            drawIndicatorLayer(graphics, radius, imgSize, 1.0F, 1F, 1F, 1F, 1F, 0.0F);
            // 填充黄色
            float fillPercent = (level - 50.0F) / 50.0F;
            drawIndicatorLayer(graphics, radius, imgSize, fillPercent, 1.0F, 0.8F, 0.0F, 1.0F, 0.01F);
        } else {
            // 红色
            drawIndicatorLayer(graphics, radius, imgSize, 1.0F, 1.0F, 0.0F, 0.0F, 1.0F, 0.0F);
        }

        // 重置颜色
        graphics.setColor(1F, 1F, 1F, 1F);

        // drawX为负的一半，实现水平居中
        // drawY为负的半径再减去一半，实现垂直居中在圆周上
        // int drawX = -imgSize / 2;
        // int drawY = -radius - imgSize / 2;

        // 绘制白色底图
        // graphics.blit(INDICATOR, drawX, drawY, 0, 0, imgSize, imgSize, imgSize, imgSize);

        graphics.pose().popPose();
    }

    // 填充颜色
    private static void drawIndicatorLayer(GuiGraphics graphics, int radius, int imgSize, float levelPercent, float r, float g, float b, float a, float zOffSet) {
        int currentW = (int) (62 * levelPercent);
        int uOffSet = (imgSize / 2) - (currentW / 2);

        int drawX = -currentW / 2;
        int drawY = -radius - imgSize / 2;

        // 设置颜色
        graphics.setColor(r, g, b, a);

        graphics.pose().pushPose();
        graphics.pose().translate(0, 0, zOffSet);
        graphics.pose().popPose();
        // 渲染
        graphics.blit(INDICATOR, drawX, drawY, uOffSet, 0, currentW, imgSize, imgSize, imgSize);
    }
}
