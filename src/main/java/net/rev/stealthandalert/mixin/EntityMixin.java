package net.rev.stealthandalert.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.npc.Npc;
import net.rev.stealthandalert.client.renderer.ClientMarkManager;
import net.rev.stealthandalert.config.ClientConfigs;
import net.rev.stealthandalert.util.CommonUtils;
import net.rev.stealthandalert.util.ModTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMixin {
    @Inject(method = "getTeamColor", at = @At("HEAD"), cancellable = true)
    private void forceRedOutline(CallbackInfoReturnable<Integer> cir) {
        Entity entity = (Entity) (Object) this;
        if (entity.level().isClientSide() && ClientMarkManager.isMarked(entity.getId())) {
            if (CommonUtils.isPlayerPet(entity, Minecraft.getInstance().player, true)) {
                cir.setReturnValue(ClientConfigs.MARK.allyColor.get());
                return;
            }
            if (entity.getType().is(ModTags.Entities.CONDITIONAL_SEEKERS) || entity instanceof NeutralMob) {
                cir.setReturnValue(ClientConfigs.MARK.neutralColor.get());
                return;
            }
            if (entity.getType().is(ModTags.Entities.SEEKERS) || entity instanceof Enemy) {
                cir.setReturnValue(ClientConfigs.MARK.hostileColor.get());
                return;
            }
            if (entity instanceof Npc) {
                cir.setReturnValue(ClientConfigs.MARK.npcColor.get());
                return;
            }
            if (entity.getType().is(ModTags.Entities.ANIMALS) || entity instanceof Animal) {
                cir.setReturnValue(ClientConfigs.MARK.passiveColor.get());
            }
        }
    }
}
