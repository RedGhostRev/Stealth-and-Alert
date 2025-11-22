package net.rev.stealthandalert.event;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.rev.stealthandalert.StealthAndAlert;
import net.rev.stealthandalert.util.ModTags;

@EventBusSubscriber(modid = StealthAndAlert.MOD_ID)
public class ModEvents {
    @SubscribeEvent
    public static void onLivingDamage(LivingDamageEvent.Pre event) {
        if (event.getSource().getDirectEntity() instanceof LivingEntity attacker) {
            if (!attacker.level().isClientSide) {
                ItemStack itemStack = attacker.getMainHandItem();
                if (itemStack.is(ModTags.Items.CAN_BACKSTAB)) {
                    LivingEntity target = event.getEntity();
                    if (isBehind(target, attacker, 60)) {
                        event.setNewDamage(event.getOriginalDamage() * 2F);
                    }
                }
            }
        }
    }

    private static boolean isBehind(LivingEntity target, LivingEntity attacker, double angleDegrees) {
        Vec3 targetLook = target.getLookAngle();
        Vec3 targetLookHorizon = new Vec3(targetLook.x, 0, targetLook.z);
        if (targetLookHorizon.lengthSqr() == 0) {
            return false;
        }
        targetLookHorizon = targetLookHorizon.normalize();

        Vec3 toAttacker = attacker.position().subtract(target.position());
        Vec3 toAttackerHorizon = new Vec3(toAttacker.x, 0, toAttacker.z);
        if (toAttackerHorizon.lengthSqr() == 0) {
            return false;
        }
        toAttackerHorizon = toAttackerHorizon.normalize();

        double dot = targetLookHorizon.dot(toAttackerHorizon);
        double threshold = Math.cos(Math.toRadians(angleDegrees));
        return dot <= -threshold;
    }
}
