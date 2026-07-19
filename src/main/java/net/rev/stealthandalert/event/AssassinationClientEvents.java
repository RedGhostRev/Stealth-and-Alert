package net.rev.stealthandalert.event;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import net.rev.stealthandalert.StealthAndAlert;
import net.rev.stealthandalert.attachment.AssassinationData;
import net.rev.stealthandalert.attachment.ModAttachments;
import net.rev.stealthandalert.client.camera.CameraShakeManager;
import net.rev.stealthandalert.common.animation.AssassinationManager;
import net.rev.stealthandalert.datagen.LangKeys;
import net.rev.stealthandalert.util.CommonUtils;
import net.rev.stealthandalert.util.ModTags;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@EventBusSubscriber(modid = StealthAndAlert.MOD_ID, value = Dist.CLIENT)
public class AssassinationClientEvents {

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        //AssassinateManager.onClientGameTick();
        CameraShakeManager.clientTick();
    }

    @SubscribeEvent
    public static void onMovementInputUpdate(MovementInputUpdateEvent event) {
        AssassinationManager.onClientGameTick();
    }

    // 用于在 Render 结束时恢复玩家原本视角
    private static final Map<UUID, RotationBackup> backupMap = new HashMap<>();

    // 虚拟轴心缓存，用于平滑过渡
    private static final Map<UUID, Float> renderYawMap = new HashMap<>();
    private static final Map<UUID, Float> renderPitchMap = new HashMap<>();

    @SubscribeEvent
    public static void onRenderPlayerPre(RenderPlayerEvent.Pre event) {
        Player player = event.getEntity();
        AssassinationData data = player.getData(ModAttachments.ASSASSINATION_DATA);
        // 1. 如果没在刺杀，清理第三人称缓存并放行
        if (!data.isAssassinating()) {
            renderYawMap.remove(player.getUUID());
            renderPitchMap.remove(player.getUUID());
            return;
        }

        // 2. 备份原始属性
        backupMap.put(player.getUUID(), new RotationBackup(player));

        float targetYaw;
        float targetPitch;
        Entity targetMonster = player.level().getEntity(data.targetId());
        // 获取当前帧的帧渲染间隔时间
        float partialTicks = event.getPartialTick();

        // 3. 计算目标角度
        if (targetMonster != null) {
            // 利用 partialTicks 线性插值，算出玩家和怪物当前帧的坐标
            double pX = Mth.lerp(partialTicks, player.xo, player.getX());
            double pY = Mth.lerp(partialTicks, player.yo, player.getY()) + player.getEyeHeight();
            double pZ = Mth.lerp(partialTicks, player.zo, player.getZ());

            double tX = Mth.lerp(partialTicks, targetMonster.xo, targetMonster.getX());
            double tY = Mth.lerp(partialTicks, targetMonster.yo, targetMonster.getY()) + targetMonster.getEyeHeight();
            double tZ = Mth.lerp(partialTicks, targetMonster.zo, targetMonster.getZ());

            double deltaX = tX - pX;
            double deltaY = tY - pY;
            double deltaZ = tZ - pZ;
            double distanceHorizontal = Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);

            targetYaw = (float) Math.toDegrees(Math.atan2(deltaZ, deltaX)) - 90.0F;
            targetPitch = (float) -Math.toDegrees(Math.atan2(deltaY, distanceHorizontal));
        } else {
            // 怪物消失，死锁在最后的虚拟轴心上
            targetYaw = renderYawMap.getOrDefault(player.getUUID(), player.yBodyRot);
            targetPitch = renderPitchMap.getOrDefault(player.getUUID(), player.getXRot());
        }

        // 4. 初始化第三人称虚拟轴心，第一帧时等于玩家当前身体朝向
        if (!renderYawMap.containsKey(player.getUUID())) {
            renderYawMap.put(player.getUUID(), player.yBodyRot);
            renderPitchMap.put(player.getUUID(), player.getXRot());
        }

        // 5. 让第三人称模型的角度平滑逼近目标
        float currentYaw = renderYawMap.get(player.getUUID());
        float currentPitch = renderPitchMap.get(player.getUUID());

        float lerpFactor = 0.25F; // 数值越小，转体和抬头的动作越柔和

        currentYaw += Mth.wrapDegrees(targetYaw - currentYaw) * lerpFactor;
        currentPitch += (targetPitch - currentPitch) * lerpFactor;

        // 更新第三人称缓存
        renderYawMap.put(player.getUUID(), currentYaw);
        renderPitchMap.put(player.getUUID(), currentPitch);

        // 6. 把平滑后的最终角度，赋与当前帧的身体、头部和俯仰角
        player.yBodyRot = currentYaw;
        player.yBodyRotO = currentYaw;
        player.yHeadRot = currentYaw;
        player.yHeadRotO = currentYaw;

        player.setXRot(currentPitch);
        player.xRotO = currentPitch;
    }

    @SubscribeEvent
    public static void onRenderPlayerPost(RenderPlayerEvent.Post event) {
        Player player = event.getEntity();

        // 立刻把真实的鼠标朝向还给玩家的逻辑主体
        RotationBackup backup = backupMap.remove(player.getUUID());
        if (backup != null) {
            backup.restore(player);
        }
    }

    // 备份类
    private static class RotationBackup {
        final float yBodyRot;
        final float yBodyRotO;
        final float yHeadRot;
        final float yHeadRotO;
        final float xRot;
        final float xRotO;

        RotationBackup(Player p) {
            this.yBodyRot = p.yBodyRot;
            this.yBodyRotO = p.yBodyRotO;
            this.yHeadRot = p.yHeadRot;
            this.yHeadRotO = p.yHeadRotO;
            this.xRot = p.getXRot();
            this.xRotO = p.xRotO;
        }

        void restore(Player p) {
            p.yBodyRot = this.yBodyRot;
            p.yBodyRotO = this.yBodyRotO;
            p.yHeadRot = this.yHeadRot;
            p.yHeadRotO = this.yHeadRotO;
            p.setXRot(this.xRot);
            p.xRotO = this.xRotO;
        }
    }

    // 相机的最后一帧视角缓存，防止怪物中途死亡消失导致第一人称镜头回弹
    private static final Map<UUID, Float> lastCameraYawMap = new HashMap<>();
    private static final Map<UUID, Float> lastCameraPitchMap = new HashMap<>();

    @SubscribeEvent
    public static void onComputeCameraAngles(ViewportEvent.ComputeCameraAngles event) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player == null) return;

        float partialTicks = (float) event.getPartialTick();

        // 对准怪物身体
        AssassinationData data = player.getData(ModAttachments.ASSASSINATION_DATA);
        boolean isAssassinating = data.isAssassinating() && mc.options.getCameraType().isFirstPerson();

        float baselineYaw = event.getYaw();
        float baselinePitch = event.getPitch();

        if (isAssassinating) {
            Entity target = player.level().getEntity(data.targetId());
            float targetYaw;
            float targetPitch;

            if (target != null) {
                double playerX = Mth.lerp(partialTicks, player.xo, player.getX());
                double playerY = Mth.lerp(partialTicks, player.yo, player.getY()) + player.getEyeHeight();
                double playerZ = Mth.lerp(partialTicks, player.zo, player.getZ());

                double targetX = Mth.lerp(partialTicks, target.xo, target.getX());
                double targetY = Mth.lerp(partialTicks, target.yo, target.getY()) + target.getBbHeight() * 0.75;
                double targetZ = Mth.lerp(partialTicks, target.zo, target.getZ());

                double deltaX = targetX - playerX;
                double deltaY = targetY - playerY;
                double deltaZ = targetZ - playerZ;
                double distanceHorizontal = Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);

                targetYaw = (float) Math.toDegrees(Math.atan2(deltaZ, deltaX)) - 90.0F;
                targetPitch = (float) -Math.toDegrees(Math.atan2(deltaY, distanceHorizontal));
            } else {
                targetYaw = lastCameraYawMap.getOrDefault(player.getUUID(), player.getYRot());
                targetPitch = lastCameraPitchMap.getOrDefault(player.getUUID(), player.getXRot());
            }

            if (!lastCameraYawMap.containsKey(player.getUUID())) {
                lastCameraYawMap.put(player.getUUID(), player.getYRot());
                lastCameraPitchMap.put(player.getUUID(), player.getXRot());
            }

            float currentYaw = lastCameraYawMap.get(player.getUUID());
            float currentPitch = lastCameraPitchMap.get(player.getUUID());
            float lerpFactor = 0.25F;

            currentYaw += Mth.wrapDegrees(targetYaw - currentYaw) * lerpFactor;
            currentPitch += (targetPitch - currentPitch) * lerpFactor;

            lastCameraYawMap.put(player.getUUID(), currentYaw);
            lastCameraPitchMap.put(player.getUUID(), currentPitch);

            // 确立不带震动的基准视轨
            baselineYaw = currentYaw;
            baselinePitch = currentPitch;

            // 同步给实体
            player.setYRot(currentYaw);
            player.yRotO = currentYaw;
            player.setXRot(currentPitch);
            player.xRotO = currentPitch;
        } else {
            lastCameraYawMap.remove(player.getUUID());
            lastCameraPitchMap.remove(player.getUUID());
        }

        if (CameraShakeManager.isShaking()) {
            // 1. 插值计算当前的 角度 震颤量
            float shakeYaw = Mth.lerp(partialTicks, CameraShakeManager.lastYawOffset, CameraShakeManager.targetYawOffset);
            float shakePitch = Mth.lerp(partialTicks, CameraShakeManager.lastPitchOffset, CameraShakeManager.targetPitchOffset);

            // 应用平滑后的角度
            event.setYaw(baselineYaw + shakeYaw);
            event.setPitch(baselinePitch + shakePitch);
            event.setRoll(event.getRoll() + (shakeYaw * 0.3F)); // Roll 倾斜

            // 2. 插值计算当前的 3D空间平移 震颤量
            double shakeX = Mth.lerp(partialTicks, CameraShakeManager.lastXOffset, CameraShakeManager.targetXOffset);
            double shakeY = Mth.lerp(partialTicks, CameraShakeManager.lastYOffset, CameraShakeManager.targetYOffset);
            double shakeZ = Mth.lerp(partialTicks, CameraShakeManager.lastZOffset, CameraShakeManager.targetZOffset);

            event.getCamera().move((float) shakeX, (float) shakeY, (float) shakeZ);

        } else {
            // 如果没在震动，但处于刺杀状态，维持纯净基准线
            if (isAssassinating) {
                event.setYaw(baselineYaw);
                event.setPitch(baselinePitch);
            }
        }
    }

    // 在刺杀时屏蔽各项操作

    @SubscribeEvent
    public static void onInteractionKey(InputEvent.InteractionKeyMappingTriggered event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.screen != null) return;
        AssassinationData data = mc.player.getData(ModAttachments.ASSASSINATION_DATA);
        if (data.isAssassinating()) {
            event.setCanceled(true);
            event.setSwingHand(false);
        }
    }

    @SubscribeEvent
    public static void onScreenOpen(ScreenEvent.Opening event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;
        AssassinationData data = mc.player.getData(ModAttachments.ASSASSINATION_DATA);
        if (data.isAssassinating()) {
            if (event.getNewScreen() != null && event.getNewScreen().isPauseScreen()) return;
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onMouseScroll(InputEvent.MouseScrollingEvent event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;
        AssassinationData data = mc.player.getData(ModAttachments.ASSASSINATION_DATA);
        if (data.isAssassinating()) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        List<Component> tooltip = event.getToolTip();

        float baseDamage = CommonUtils.getWeaponBaseDamage(stack);
        float totalMultiplier = CommonUtils.getAssassinationTotalMultiplier(stack);
        float assassinationDamage = CommonUtils.getAssassinationDamage(baseDamage, totalMultiplier);
        if (assassinationDamage <= 0F) return;
        for (int i = 0; i < tooltip.size(); i++) {
            String text = tooltip.get(i).getString();
            String attackDamageTranslation = Component.translatable("attribute.name.generic.attack_damage").getString();
            if (text.contains(attackDamageTranslation)) {
                String displayAssassination = ItemAttributeModifiers.ATTRIBUTE_MODIFIER_FORMAT.format(assassinationDamage);
                MutableComponent assassinateComponent = Component.literal(" ")
                        .append(Component.literal(displayAssassination))
                        .append(CommonComponents.SPACE)
                        .append(Component.translatable(LangKeys.TOOLTIP_ASSASSINATION_DAMAGE))
                        .withStyle(ChatFormatting.DARK_GREEN);
                if (event.getFlags().isAdvanced()) {
                    String displayBase = ItemAttributeModifiers.ATTRIBUTE_MODIFIER_FORMAT.format(baseDamage);
                    String displayMultiplier = ItemAttributeModifiers.ATTRIBUTE_MODIFIER_FORMAT.format(totalMultiplier);
                    Component formulaComponent = Component.literal(" [" + displayBase + " * " + displayMultiplier + "]")
                            .withStyle(ChatFormatting.GRAY);
                    assassinateComponent.append(formulaComponent);
                }
                tooltip.add(i + 1, assassinateComponent);
                break;
            }
        }

        if (stack.is(ModTags.Items.CAN_ASSASSINATE)) {
            MutableComponent tip = Component.translatable(LangKeys.TOOLTIP_CAN_ASSASSINATE).withStyle(style ->
                    style.withColor(ChatFormatting.RED));
            if (tooltip.size() > 1) {
                tooltip.add(1, tip);
            } else {
                tooltip.add(tip);
            }
        }
    }
}
