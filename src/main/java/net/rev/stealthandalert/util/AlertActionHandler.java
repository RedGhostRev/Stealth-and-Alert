package net.rev.stealthandalert.util;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.rev.stealthandalert.attachment.AlertData;


public class AlertActionHandler {
    public static void execute(Mob mob, AlertData data, boolean canSeePrimary) {
        // 若为闲逛
        if (data.state() == AlertData.IDLE) return;

        // 获取当前主目标
        Player primary = data.primaryTarget()
                .map(uuid -> mob.level().getPlayerByUUID(uuid))
                .orElse(null);
        Vec3 lkp = data.lastKnownPos().orElse(null);
        if (canSeePrimary && primary != null) {
            // 如果看见了主目标
            int pState = data.targetStates().getOrDefault(primary.getUUID(), AlertData.UNTRACKED);

            if (pState >= AlertData.AWARE) {
                mob.getLookControl().setLookAt(primary, 30.0F, 30.0F);

                if (data.state() == AlertData.FIGHTING && pState == AlertData.TRACKING) {
                    if (mob instanceof Piglin piglin) {
                        piglin.getBrain().eraseMemory(MemoryModuleType.PACIFIED);
                        piglin.getBrain().setMemory(MemoryModuleType.ANGRY_AT, primary.getUUID());
                    }
                    mob.setTarget(primary);
                } else {
                    if (mob.getTarget() == primary) mob.setTarget(null);
                    if (!mob.getNavigation().isDone()) mob.getNavigation().stop();
                }
            }

        } else {
            // 如果没看见主目标
            if (mob.getTarget() != null && mob.getTarget().equals(primary)) {
                // 如果仍处于 FIGHTING 状态，继续保持锁定，直到降级为 SEARCHING 状态
                // 因此，必须确保 FIGHTING 状态，在丢失视野后，只能持续很短的一段时间
                // 附加条件：主目标必须处于 TRACKING 状态
                // 因此 TRACKING 状态也应在丢失视野后只能持续很短的一段时间
                // 由于没有为观测状态设定计时器，所以用别的办法来实现

                // 但是，若目标处于非生存模式，则无视这些条件
                if (primary instanceof Player player) {
                    if ((player.isInvisible() && StealthUtils.isFullyNaked(player)) || player.isCreative() || player.isSpectator()) {
                        mob.setTarget(null);
                    }
                } else if (data.state() < AlertData.FIGHTING && data.targetStates().getOrDefault(primary.getUUID(), AlertData.UNTRACKED) < AlertData.TRACKING) {
                    mob.setTarget(null);
                } else {
                    mob.setTarget(primary);
                }
            }
            if (lkp == null) return;
            if (data.state() == AlertData.SUSPICIOUS) {
                mob.getLookControl().setLookAt(lkp.x, lkp.y + Player.DEFAULT_EYE_HEIGHT, lkp.z, 30.0F, 30.0F);
                if (!mob.getNavigation().isDone()) mob.getNavigation().stop();
            }
        }
    }
}
