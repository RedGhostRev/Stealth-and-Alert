package net.rev.stealthandalert.ai;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.rev.stealthandalert.attachment.AlertData;
import net.rev.stealthandalert.attachment.ModAttachments;

import java.util.EnumSet;

public class StealthLookAroundGoal extends RandomLookAroundGoal {
    private final Mob mob;

    public StealthLookAroundGoal(Mob mob) {
        super(mob);
        this.mob = mob;
        this.setFlags(EnumSet.of(Flag.LOOK, Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        AlertData data = mob.getData(ModAttachments.ALERT_DATA);
        if (data.state() > AlertData.IDLE && !mob.getData(ModAttachments.INVESTIGATE_LKP_DATA).isSearchingAround()) {
            return false;
        }
        return super.canUse();
    }

    @Override
    public boolean canContinueToUse() {
        AlertData data = mob.getData(ModAttachments.ALERT_DATA);
        if (data.state() > AlertData.IDLE && !mob.getData(ModAttachments.INVESTIGATE_LKP_DATA).isSearchingAround()) {
            return false;
        }
        return super.canContinueToUse();
    }

    @Override
    public void start() {
        super.start();
        this.mob.getNavigation().stop();
    }

    @Override
    public void tick() {
        super.tick();
        this.mob.getNavigation().stop();
    }

    @Override
    public void stop() {
        super.stop();
        this.mob.getNavigation().stop();
    }
}
