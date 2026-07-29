package net.rev.stealthandalert.mixin.compat.ironsspellbooks;

import io.redspace.ironsspellbooks.player.ServerPlayerEvents;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.event.entity.living.LivingChangeTargetEvent;
import net.rev.stealthandalert.util.ModTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerPlayerEvents.class)
public class ServerPlayerEventsMixin {
    @Redirect(
            method = "onLivingChangeTarget",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/LivingEntity;hasEffect(Lnet/minecraft/core/Holder;)Z")
    )
    private static boolean stealth_and_alert$ignoreSeekersAtEvent(LivingEntity target, Holder<MobEffect> effect, LivingChangeTargetEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity.getType().is(ModTags.Entities.SEEKERS)) return false;
        return target.hasEffect(effect);
    }
}
