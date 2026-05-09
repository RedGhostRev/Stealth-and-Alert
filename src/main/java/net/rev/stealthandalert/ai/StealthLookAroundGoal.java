package net.rev.stealthandalert.ai;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.rev.stealthandalert.attachment.AlertData;
import net.rev.stealthandalert.attachment.ModAttachments;

public class StealthLookAroundGoal extends RandomLookAroundGoal {
    private final Mob mob;

    public StealthLookAroundGoal(Mob mob) {
        super(mob);
        this.mob = mob;
    }

    @Override
    public boolean canUse() {
        AlertData data = mob.getData(ModAttachments.ALERT_DATA);
        if (data.state() > AlertData.IDLE) {
            return false;
        }

        return super.canUse();
    }

    @Override
    public boolean canContinueToUse() {
        AlertData data = mob.getData(ModAttachments.ALERT_DATA);
        if (data.state() > AlertData.IDLE) {
            return false;
        }
        return super.canContinueToUse();
    }
}
