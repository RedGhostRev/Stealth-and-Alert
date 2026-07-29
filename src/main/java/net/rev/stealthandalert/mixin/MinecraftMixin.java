package net.rev.stealthandalert.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.rev.stealthandalert.client.renderer.ClientMarkManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Inject(method = "shouldEntityAppearGlowing", at = @At("HEAD"), cancellable = true)
    private void stealth_and_alert$enableGlowing(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if (ClientMarkManager.isMarked(entity.getId())) {
            cir.setReturnValue(true);
        }
    }
}
