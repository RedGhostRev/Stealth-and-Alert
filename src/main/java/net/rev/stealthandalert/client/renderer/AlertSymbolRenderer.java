package net.rev.stealthandalert.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.client.event.RenderNameTagEvent;
import net.rev.stealthandalert.StealthAndAlert;
import net.rev.stealthandalert.attachment.AlertData;
import net.rev.stealthandalert.attachment.ModAttachments;
import net.rev.stealthandalert.config.ClientConfigs;
import net.rev.stealthandalert.util.ModTags;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

// 绘制敌人头顶上的警戒标志
@EventBusSubscriber(modid = StealthAndAlert.MOD_ID, value = Dist.CLIENT)
public class AlertSymbolRenderer {
    private static final ResourceLocation BASE = ResourceLocation.fromNamespaceAndPath(StealthAndAlert.MOD_ID,
            "textures/gui/alert_symbol_base.png");
    private static final ResourceLocation EXCLAMATION = ResourceLocation.fromNamespaceAndPath(StealthAndAlert.MOD_ID,
            "textures/gui/alert_symbol_exclamation.png");
    private static final ResourceLocation QUESTION = ResourceLocation.fromNamespaceAndPath(StealthAndAlert.MOD_ID,
            "textures/gui/alert_symbol_question.png");

    // 延迟渲染队列
    private static final List<RenderTask> RENDER_QUEUE = new ArrayList<>();

    private static final WeakHashMap<Mob, Integer> LAST_SEEN_TICKS = new WeakHashMap<>();

    @SubscribeEvent
    public static void onRenderAlertSymbol(RenderNameTagEvent event) {
        if (!ClientConfigs.ALERT_SYMBOL.get()) return;
        if (!(event.getEntity() instanceof Mob mob) || !mob.getType().is(ModTags.Entities.SEEKERS)) return;

        AlertData data = mob.getData(ModAttachments.ALERT_DATA);
        int state = data.state();
        if (state != AlertData.SUSPICIOUS && state != AlertData.SEARCHING) return;

        PoseStack poseStack = event.getPoseStack();
        poseStack.pushPose();

        float yOffset = mob.getBbHeight();
        float base = ClientConfigs.DEBUG_MODE.get() ? 1.0F : 0.5F;
        yOffset += base;

        boolean hasName = mob.hasCustomName();
        boolean isAlwaysVisible = mob.isCustomNameVisible();
        boolean isLookingAt = Minecraft.getInstance().crosshairPickEntity == mob;
        boolean isNameTagRenderingNow = hasName && (isAlwaysVisible || isLookingAt);

        if (isNameTagRenderingNow) {
            yOffset += 0.5F;
        }
        poseStack.translate(0.0F, yOffset, 0.0F);
        poseStack.mulPose(Minecraft.getInstance().getEntityRenderDispatcher().cameraOrientation());
        float scale = ((float) ClientConfigs.ALERT_SYMBOL_SCALE.getAsDouble());
        poseStack.scale(scale, -scale, scale);
        Matrix4f savedMatrix = new Matrix4f(poseStack.last().pose());
        poseStack.popPose();

        float partialTick = Minecraft.getInstance().getTimer().getGameTimeDeltaPartialTick(true);
        int tickCount = mob.tickCount;

        boolean canSee = data.canSeeAnyone();
        float tempAlphaQ = 0.0F;
        float tempAlphaE = 0.0F;

        if (state == AlertData.SUSPICIOUS) {
            tempAlphaQ = 1.0F;
        } else {
            if (canSee) {
                LAST_SEEN_TICKS.put(mob, tickCount);
                tempAlphaE = 1.0F;
            } else {
                int lastSeenTick = LAST_SEEN_TICKS.getOrDefault(mob, tickCount);
                float ticksSinceLost = tickCount - lastSeenTick + partialTick;
                float period = 50.0F;
                float progress = (ticksSinceLost % period) / period;

                float fadeE;
                if (progress < 0.2F) {
                    fadeE = 1.0F;
                } else if (progress < 0.5F) {
                    fadeE = 1.0F - ((progress - 0.2F) / 0.3F);
                } else if (progress < 0.70F) {
                    fadeE = 0.0F;
                } else {
                    fadeE = (progress - 0.7F) / 0.3F;
                }

                float fadeQ = 1.0F - fadeE;

                tempAlphaQ = fadeQ;
                tempAlphaE = fadeE;
            }
        }

        float alphaQ = tempAlphaQ;
        float alphaE = tempAlphaE;

        RENDER_QUEUE.add(bufferSource -> {
            renderTextureQuad(savedMatrix, bufferSource, BASE, 0F, 0F, 0F, 0.5F, 15728880);
            if (alphaQ > 0.01F) {
                if (state == AlertData.SUSPICIOUS) {
                    renderTextureQuad(savedMatrix, bufferSource, QUESTION, 1.0F, 1.0F, 1.0F, alphaQ, 15728880);
                } else {
                    renderTextureQuad(savedMatrix, bufferSource, QUESTION, 1.0F, 0.8F, 0.0F, alphaQ, 15728880);
                }
            }
            if (alphaE > 0.01F) {
                renderTextureQuad(savedMatrix, bufferSource, EXCLAMATION, 1.0F, 0.8F, 0.0F, alphaE, 15728880);
            }
        });
    }

    @SubscribeEvent
    public static void onRenderLevelStage(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_ENTITIES) return;
        if (RENDER_QUEUE.isEmpty()) return;

        MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();

        for (RenderTask task : RENDER_QUEUE) {
            task.render(bufferSource);
        }

        RENDER_QUEUE.clear();
    }

    private static void renderTextureQuad(Matrix4f pose, MultiBufferSource bufferSource, ResourceLocation texture,
                                          float r, float g, float b, float a, int packedLight) {

        VertexConsumer buffer = bufferSource.getBuffer(RenderType.textSeeThrough(texture));

        float size = 64.0F;

        buffer.addVertex(pose, -size, size, 0.0F).setColor(r, g, b, a).setUv(0.0F, 1.0F)
                .setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(0, 1, 0);

        buffer.addVertex(pose, size, size, 0.0F).setColor(r, g, b, a).setUv(1.0F, 1.0F)
                .setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(0, 1, 0);

        buffer.addVertex(pose, size, -size, 0.0F).setColor(r, g, b, a).setUv(1.0F, 0.0F)
                .setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(0, 1, 0);

        buffer.addVertex(pose, -size, -size, 0.0F).setColor(r, g, b, a).setUv(0.0F, 0.0F)
                .setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(0, 1, 0);
    }

    private interface RenderTask {
        void render(MultiBufferSource.BufferSource bufferSource);
    }
}
