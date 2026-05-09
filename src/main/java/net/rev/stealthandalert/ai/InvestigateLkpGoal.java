package net.rev.stealthandalert.ai;

import java.util.EnumSet;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.rev.stealthandalert.attachment.AlertData;
import net.rev.stealthandalert.attachment.ModAttachments;
import net.rev.stealthandalert.util.StealthUtils;

public class InvestigateLkpGoal extends Goal {
    private final Mob mob;
    private final double speedModifier;

    private boolean isSearchingAround;
    private int stayTicks;
    private double targetX, targetY, targetZ;
    private double lkpAnchorX, lkpAnchorY, lkpAnchorZ;

    public InvestigateLkpGoal(Mob mob, double speedModifier) {
        this.mob = mob;
        this.speedModifier = speedModifier;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        AlertData data = this.mob.getData(ModAttachments.ALERT_DATA);
        if (data.lastSeenPos().isEmpty()) return false;

        if (data.state() < AlertData.SEARCHING) {
            return false;
        }

        if (data.primaryTarget().isPresent()) {
            Player p = this.mob.level().getPlayerByUUID(data.primaryTarget().get());
            if (p != null && StealthUtils.shouldArouseAlert(this.mob, p)) {
                int pState = data.targetStates().getOrDefault(p.getUUID(), AlertData.UNTRACKED);
                if (pState >= AlertData.AWARE) return false;
            }
        }

        return true;
    }

    @Override
    public boolean canContinueToUse() {
        AlertData data = this.mob.getData(ModAttachments.ALERT_DATA);

        if (data.state() < AlertData.SEARCHING || data.lastSeenPos().isEmpty()) {
            return false;
        }

        if (data.primaryTarget().isPresent()) {
            Player p = this.mob.level().getPlayerByUUID(data.primaryTarget().get());
            if (p != null && StealthUtils.shouldArouseAlert(this.mob, p)) {
                int pState = data.targetStates().getOrDefault(p.getUUID(), AlertData.UNTRACKED);
                if (pState >= AlertData.AWARE) return false;
            }
        }

        return true;
    }

    @Override
    public void start() {
        AlertData data = this.mob.getData(ModAttachments.ALERT_DATA);
        Vec3 lkp = data.lastSeenPos().get();

        this.isSearchingAround = false;
        this.stayTicks = 0;

        this.setTargetPos(lkp.x, lkp.y, lkp.z);
        this.lkpAnchorX = lkp.x;
        this.lkpAnchorY = lkp.y;
        this.lkpAnchorZ = lkp.z;

        this.mob.getNavigation().moveTo(this.targetX, this.targetY, this.targetZ, this.speedModifier);
    }

    @Override
    public void stop() {
        this.mob.getNavigation().stop();
    }

    @Override
    public void tick() {
        AlertData data = this.mob.getData(ModAttachments.ALERT_DATA);
        if (data.lastSeenPos().isEmpty()) return;

        Vec3 currentLkp = data.lastSeenPos().get();

        if (!this.isSearchingAround) {
            this.setTargetPos(currentLkp.x, currentLkp.y, currentLkp.z);
            this.lkpAnchorX = currentLkp.x;
            this.lkpAnchorY = currentLkp.y;
            this.lkpAnchorZ = currentLkp.z;
        }

        double distSqr = this.mob.distanceToSqr(this.targetX, this.targetY, this.targetZ);

        if (distSqr > 2.25) {
            // 前往 LKP
            this.mob.getLookControl().setLookAt(this.targetX, this.targetY + 1.6, this.targetZ, 30.0F, 30.0F);
            this.mob.getNavigation().moveTo(this.targetX, this.targetY, this.targetZ, this.speedModifier);
        } else {
            this.mob.getNavigation().stop();

            if (this.stayTicks <= 0) {
                this.stayTicks = 30 + this.mob.getRandom().nextInt(30); // 1.5 ~ 3 秒
                this.isSearchingAround = true;
            } else {
                this.stayTicks--;
                if (this.stayTicks == 0) {
                    this.pickNextSearchPoint();
                    this.mob.getNavigation().moveTo(this.targetX, this.targetY, this.targetZ, this.speedModifier);
                }
            }
        }
    }

    private void setTargetPos(double x, double y, double z) {
        this.targetX = x;
        this.targetY = y;
        this.targetZ = z;
    }

    // 在 4 ~ 10 格范围内选取搜寻点
    private void pickNextSearchPoint() {
        double radius = 4.0 + this.mob.getRandom().nextDouble() * 6.0;
        double angle = this.mob.getRandom().nextDouble() * Math.PI * 2;

        double newX = this.lkpAnchorX + Math.cos(angle) * radius;
        double newZ = this.lkpAnchorZ + Math.sin(angle) * radius;

        this.setTargetPos(newX, this.lkpAnchorY, newZ);
    }
}