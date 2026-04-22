package net.rev.stealthandalert.util;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
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
        Vec3 lkp = data.lastSeenPos().orElse(null);
        if (canSeePrimary && primary != null) {
            int pState = data.targetStates().getOrDefault(primary.getUUID(), AlertData.UNTRACKED);

            if (pState >= AlertData.AWARE) {
                removeLookIdleGoals(mob);
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
            if (lkp == null) return;
            if (data.state() == AlertData.SEARCHING || data.state() == AlertData.FIGHTING) {
                double distSq = lkp.distanceToSqr(mob.position());
                if (distSq > 2.25) {
                    removeLookIdleGoals(mob);
                    mob.getLookControl().setLookAt(lkp.x, lkp.y + 1.6, lkp.z, 30.0F, 30.0F);
                    mob.getNavigation().moveTo(lkp.x, lkp.y, lkp.z, 1.1);
                } else {
                    if (!mob.getNavigation().isDone()) mob.getNavigation().stop();
                }
            } else if (data.state() == AlertData.SUSPICIOUS) {
                removeLookIdleGoals(mob);
                mob.getLookControl().setLookAt(lkp.x, lkp.y + 1.6, lkp.z, 30.0F, 30.0F);
                if (!mob.getNavigation().isDone()) mob.getNavigation().stop();
            }
        }
    }

    private static void removeLookIdleGoals(Mob mob) {
        mob.goalSelector.getAvailableGoals().removeIf(wrappedGoal ->
                wrappedGoal.getGoal() instanceof RandomLookAroundGoal ||
                        wrappedGoal.getGoal() instanceof LookAtPlayerGoal
        );
    }
}
