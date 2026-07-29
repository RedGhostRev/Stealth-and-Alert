package net.rev.stealthandalert.client.renderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.*;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.rev.stealthandalert.config.ClientConfigs;
import net.rev.stealthandalert.util.CommonUtils;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

@OnlyIn(Dist.CLIENT)
public class ClientMarkManager {
    private static final Set<Integer> MARKED_ENTITIES = new HashSet<>();

    public static void toggleMark(int entityId) {
        if (MARKED_ENTITIES.contains(entityId)) {
            MARKED_ENTITIES.remove(entityId);
        } else {
            MARKED_ENTITIES.add(entityId);
        }
    }

    public static boolean isMarked(int entityId) {
        return MARKED_ENTITIES.contains(entityId);
    }

    public static void clear() {
        MARKED_ENTITIES.clear();
    }

    public static void remove(int entityId) {
        MARKED_ENTITIES.remove(entityId);
    }

    public static int canSeeAny(@NotNull Player player) {
        double maxDistance = ClientConfigs.SPYGLASS_MARK.maxDistance.get();
        Vec3 eyePos = player.getEyePosition(1.0F);
        Vec3 lookVec = player.getViewVector(1.0F);
        Vec3 endPos = eyePos.add(lookVec.scale(maxDistance));
        AABB searchBox = player.getBoundingBox().expandTowards(lookVec.scale(maxDistance)).inflate(1.0);

        ClipContext context = CommonUtils.getClipContext(player, eyePos, endPos);
        BlockHitResult blockHit = player.level().clip(context);
        double blockHitDistanceSqr = (blockHit.getType() != HitResult.Type.MISS)
                ? blockHit.getLocation().distanceToSqr(eyePos)
                : Double.MAX_VALUE;

        EntityHitResult entityHit = ProjectileUtil.getEntityHitResult(
                player, eyePos, endPos, searchBox,
                entity -> entity instanceof LivingEntity && !entity.isSpectator(),
                maxDistance * maxDistance
        );

        if (entityHit != null && entityHit.getEntity() instanceof LivingEntity target) {
            int entityId = target.getId();
            boolean isAlreadyMarked = isMarked(entityId);
            if (isAlreadyMarked) {
                return 1;
            } else {
                double entityHitDistanceSqr = entityHit.getLocation().distanceToSqr(eyePos);
                if (blockHitDistanceSqr < entityHitDistanceSqr) {
                    return -1;
                }
                return 0;
            }
        }
        return -1;
    }

    public static void mark() {
        if (!ClientConfigs.SPYGLASS_MARK.enable.get()) return;
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null || !player.isUsingItem() || !player.getUseItem().is(Items.SPYGLASS)) return;

        double maxDistance = ClientConfigs.SPYGLASS_MARK.maxDistance.get();
        Vec3 eyePos = player.getEyePosition(1.0F);
        Vec3 lookVec = player.getViewVector(1.0F);
        Vec3 endPos = eyePos.add(lookVec.scale(maxDistance));
        AABB searchBox = player.getBoundingBox().expandTowards(lookVec.scale(maxDistance)).inflate(1.0);

        // 进行方块射线检测
        ClipContext context = CommonUtils.getClipContext(player, eyePos, endPos);
        BlockHitResult blockHit = player.level().clip(context);
        double blockHitDistanceSqr = (blockHit.getType() != HitResult.Type.MISS)
                ? blockHit.getLocation().distanceToSqr(eyePos)
                : Double.MAX_VALUE;

        // 进行实体射线检测
        EntityHitResult entityHit = ProjectileUtil.getEntityHitResult(
                player, eyePos, endPos, searchBox,
                entity -> entity instanceof LivingEntity && !entity.isSpectator(),
                maxDistance * maxDistance
        );

        if (entityHit != null && entityHit.getEntity() instanceof LivingEntity target) {
            int entityId = target.getId();
            boolean isAlreadyMarked = isMarked(entityId);

            if (isAlreadyMarked) {
                // 已经标记过了：无视墙壁，直接取消标记
                toggleMark(entityId);
                mc.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.5F));

            } else {
                // 没有标记：判断是否隔了墙
                double entityHitDistanceSqr = entityHit.getLocation().distanceToSqr(eyePos);

                // 如果玩家眼睛到墙壁的距离，小于眼睛到实体的距离，说明墙挡在了实体前面
                if (blockHitDistanceSqr < entityHitDistanceSqr) {
                    // 视线被墙挡住，什么都不做，直接退出
                    return;
                }

                // 视线畅通无阻，允许标记
                toggleMark(entityId);
                mc.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 2.0F));
            }
        }
    }
}
