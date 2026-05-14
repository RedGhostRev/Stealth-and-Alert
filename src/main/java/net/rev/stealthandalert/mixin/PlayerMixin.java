package net.rev.stealthandalert.mixin;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Abilities;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.rev.stealthandalert.attachment.ModAttachments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity {
    @Shadow
    public abstract Abilities getAbilities();

    public PlayerMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(null, null);
    }

    @Inject(method = "updatePlayerPose", at = @At("HEAD"), cancellable = true)
    private void stealth_and_alert$injectCrawlPose(CallbackInfo ci) {
        boolean crawling = this.getData(ModAttachments.CRAWL_DATA).isCrawling();
        if (crawling && !this.isSleeping() && !this.isPassenger() && !this.isFallFlying() && !this.getAbilities().flying) {
            this.setPose(Pose.SWIMMING);
            ci.cancel();
        }
    }
}
