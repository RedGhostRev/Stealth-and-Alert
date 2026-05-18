package net.rev.stealthandalert.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.rev.stealthandalert.attachment.AlertData;
import net.rev.stealthandalert.attachment.ModAttachments;
import net.rev.stealthandalert.util.ModTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TargetingConditions.class)
public abstract class TargetConditionsMixin {

    @Inject(method = "test", at = @At("HEAD"), cancellable = true)
    private void stealth_and_alert$onTestTarget(LivingEntity attacker, LivingEntity target, CallbackInfoReturnable<Boolean> cir) {
        if (attacker == null || target == null) return;
        if (attacker.getType().is(ModTags.Entities.SEEKERS) && target.getType().is(ModTags.Entities.DETECTABLE)) {
            AlertData data = attacker.getData(ModAttachments.ALERT_DATA);
            if (data.primaryTarget().isEmpty()) {
                cir.setReturnValue(false);
                return;
            }
            if (!data.primaryTarget().get().equals(target.getUUID())) {
                cir.setReturnValue(false);
                return;
            }
            if (data.state() < AlertData.FIGHTING || data.targetStates().getOrDefault(target.getUUID(), AlertData.UNTRACKED) < AlertData.TRACKING)
                cir.setReturnValue(false);
        }
    }
}
