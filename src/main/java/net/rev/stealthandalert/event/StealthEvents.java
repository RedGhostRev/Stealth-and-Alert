package net.rev.stealthandalert.event;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingChangeTargetEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.rev.stealthandalert.StealthAndAlert;
import net.rev.stealthandalert.attachment.AlertData;
import net.rev.stealthandalert.attachment.ModAttachments;
import net.rev.stealthandalert.util.ModTags;
import net.rev.stealthandalert.util.StealthUtils;

@EventBusSubscriber(modid = StealthAndAlert.MOD_ID)
public class StealthEvents {

    @SubscribeEvent
    public static void onMobTarget(LivingChangeTargetEvent event) {
        LivingEntity target = event.getNewAboutToBeSetTarget();
        if (target == null) return;

        if (!(event.getEntity() instanceof Mob mob)) return;
        if (!(mob.getType().is(ModTags.Entities.SEEKERS))) return;

        if (!target.getType().is(ModTags.Entities.DETECTABLE)) return;

        if (!StealthUtils.hasLineOfSight(mob, target)) {
            event.setNewAboutToBeSetTarget(null);
        }

        // AlertData data = mob.getData(ModAttachments.ALERT_DATA);
        // if (data.state() < AlertData.DETECTED) {
        //     event.setNewAboutToBeSetTarget(null);
        // }
    }

    @SubscribeEvent
    public static void onEntityTick(EntityTickEvent.Post event) {
        if (!(event.getEntity() instanceof Mob mob)) return;
        if (!(mob.getType().is(ModTags.Entities.SEEKERS))) return;


    }
}
