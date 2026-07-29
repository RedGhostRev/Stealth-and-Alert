package net.rev.stealthandalert.mixin.compat.ironsspellbooks;

import io.redspace.ironsspellbooks.effect.TrueInvisibilityEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.rev.stealthandalert.util.ModTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(TrueInvisibilityEffect.class)
public abstract class TrueInvisibilityEffectMixin {
    @Redirect(
            method = "onEffectAdded",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;getNearbyEntities(Ljava/lang/Class;Lnet/minecraft/world/entity/ai/targeting/TargetingConditions;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/phys/AABB;)Ljava/util/List;"
            )
    )
    private List<Mob> stealth_and_alert$ignoreSeekersAtEffect(Level level, Class<Mob> clazz, TargetingConditions conditions, LivingEntity owner, AABB box) {
        List<Mob> nearbyMobs = level.getNearbyEntities(clazz, conditions, owner, box);
        nearbyMobs.removeIf(mob -> mob.getType().is(ModTags.Entities.SEEKERS));
        return nearbyMobs;
    }
}
