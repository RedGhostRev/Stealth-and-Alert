package net.rev.stealthandalert.event;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.Panda;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingChangeTargetEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.rev.stealthandalert.StealthAndAlert;
import net.rev.stealthandalert.attachment.AlertData;
import net.rev.stealthandalert.attachment.ModAttachments;
import net.rev.stealthandalert.config.CommonConfigs;
import net.rev.stealthandalert.config.EntityAlertConfigLoader;
import net.rev.stealthandalert.config.EntityAlertSettings;
import net.rev.stealthandalert.network.S2CAlertDataPacket;
import net.rev.stealthandalert.util.AlertLogicHandler;
import net.rev.stealthandalert.util.ModTags;
import net.rev.stealthandalert.util.StealthUtils;

import java.util.*;

@EventBusSubscriber(modid = StealthAndAlert.MOD_ID)
public class StealthEvents {

    // 拦截原版setTarget事件
    @SubscribeEvent
    public static void onMobTarget(LivingChangeTargetEvent event) {
        LivingEntity target = event.getNewAboutToBeSetTarget();
        if (target == null) return;
        if (target instanceof Player player && (player.isCreative() || player.isSpectator())) return;

        if (!(event.getEntity() instanceof Mob mob)) return;
        if (!(mob.getType().is(ModTags.Entities.SEEKERS))) return;

        if (!target.getType().is(ModTags.Entities.DETECTABLE)) return;

        AlertData data = mob.getData(ModAttachments.ALERT_DATA);
        if (data.state() < AlertData.FIGHTING) {
            event.setNewAboutToBeSetTarget(null);
            return;
        }

        int pState = data.targetStates().getOrDefault(target.getUUID(), AlertData.UNTRACKED);
        if (pState < AlertData.TRACKING) {
            event.setNewAboutToBeSetTarget(null);
            return;
        }

        UUID primaryUUID = data.primaryTarget().orElse(null);
        if (primaryUUID != null && !primaryUUID.equals(target.getUUID())) {
            event.setNewAboutToBeSetTarget(null);
        }
    }

    // 生物受击
    @SubscribeEvent
    public static void onMobHurt(LivingIncomingDamageEvent event) {
        if (!(event.getEntity() instanceof Mob mob) || mob.level().isClientSide()) return;
        if (!mob.getType().is(ModTags.Entities.SEEKERS)) return;

        if (event.getSource().getEntity() instanceof Player player) {
            if (player.isCreative() || player.isSpectator()) return;
            if (AlertLogicHandler.isPlayerPet(mob, player)) return;
            if (mob.getType().is(ModTags.Entities.CONDITIONAL_SEEKERS)) {
                EntityAlertSettings settings = EntityAlertConfigLoader.get(mob.getType());
                if (!settings.logicList().isEmpty() && !settings.logicList().contains(StealthAndAlert.MOD_ID + ":provocation")) {
                    return;
                }
            }
            AlertData data = mob.getData(ModAttachments.ALERT_DATA);
            UUID uuid = player.getUUID();


            Map<UUID, Float> progressMap = new HashMap<>(data.targetProgress());
            Map<UUID, Integer> statesMap = new HashMap<>(data.targetStates());
            Map<UUID, Integer> reactionsMap = new HashMap<>(data.targetReactions());
            Map<UUID, Integer> lastDamageTicksMap = new HashMap<>(data.lastDamageTicks());
            boolean canPerceive = StealthUtils.hasLineOfSight(mob, player) || (StealthUtils.shouldArouseAlert(mob, player) && reactionsMap.getOrDefault(uuid, CommonConfigs.DETECTION_REACTION_TICKS.get()) <= 0);
            boolean dataChanged = false;
            if (canPerceive) {
                lastDamageTicksMap.put(uuid, 1200);
                dataChanged = true;
            }
            if ((EntityAlertConfigLoader.get(mob.getType()).ignoreBaby() && mob.isBaby()) ||
                    (mob instanceof Panda panda && !panda.isAggressive())) {
                // 写回
                AlertData newData = new AlertData(
                        data.state(),
                        progressMap,
                        statesMap,
                        reactionsMap,
                        lastDamageTicksMap,
                        data.lastSeenPos(),
                        data.primaryTarget(),
                        data.stateTicks(),
                        data.patienceTicks()
                );

                mob.setData(ModAttachments.ALERT_DATA, newData);
                // 确保客户端UI响应
                PacketDistributor.sendToPlayersTrackingEntity(mob, new S2CAlertDataPacket(mob.getId(), newData));
                return;
            }

            LivingEntity currentTarget = mob.getTarget();
            int currentPState = statesMap.getOrDefault(uuid, AlertData.UNTRACKED);

            // 判定：是否在打别人
            boolean isFightingOthers = (data.state() == AlertData.FIGHTING) && currentTarget != null && currentTarget != player;
            int nextState = data.state();
            Optional<Vec3> nextLKP = data.lastSeenPos();
            Optional<UUID> nextPrimary = data.primaryTarget();
            // 只有当该玩家还没达到 TRACKING 状态时才更新数据
            if (currentPState < AlertData.TRACKING) {
                progressMap.put(uuid, 100.0F);
                statesMap.put(uuid, AlertData.TRACKING);
                reactionsMap.put(uuid, 0);

                // 根据是否正在打别人，决定全局状态
                if (!isFightingOthers) {
                    nextState = AlertData.SEARCHING;
                    nextLKP = Optional.of(player.position());
                    nextPrimary = Optional.of(uuid);
                }
                dataChanged = true;
            }

            if (dataChanged) {
                // 写回
                AlertData newData = new AlertData(
                        nextState,
                        progressMap,
                        statesMap,
                        reactionsMap,
                        lastDamageTicksMap,
                        nextLKP,
                        nextPrimary,
                        data.stateTicks(),
                        data.patienceTicks()
                );

                mob.setData(ModAttachments.ALERT_DATA, newData);
                // 确保客户端UI响应
                PacketDistributor.sendToPlayersTrackingEntity(mob, new S2CAlertDataPacket(mob.getId(), newData));
            }
        }
    }

    @SubscribeEvent
    // 适用于铁傀儡仇恨逻辑
    public static void onVillagerHurt(LivingIncomingDamageEvent event) {
        if (!(event.getEntity() instanceof Villager villager) || villager.level().isClientSide()) return;
        if (event.getSource().getEntity() instanceof Player player) {
            if (player.isCreative() || player.isSpectator()) return;

            double range = EntityAlertConfigLoader.get(EntityType.IRON_GOLEM).viewRange();
            List<IronGolem> nearbyGolems = villager.level().getEntitiesOfClass(IronGolem.class,
                    villager.getBoundingBox().inflate(range));

            for (IronGolem golem : nearbyGolems) {
                if (StealthUtils.hasLineOfSight(golem, villager)) {
                    AlertData data = golem.getData(ModAttachments.ALERT_DATA);
                    Map<UUID, Integer> memoryMap = new HashMap<>(data.lastDamageTicks());
                    memoryMap.put(player.getUUID(), 1200);

                    AlertData newData = new AlertData(
                            data.state(), data.targetProgress(), data.targetStates(), data.targetReactions(),
                            memoryMap, data.lastSeenPos(), data.primaryTarget(), data.stateTicks(), data.patienceTicks()
                    );

                    golem.setData(ModAttachments.ALERT_DATA, newData);

                    PacketDistributor.sendToPlayersTrackingEntity(golem, new S2CAlertDataPacket(golem.getId(), newData));
                }
            }
        }
    }

    // FIXME 可能有潜在的性能问题
    @SubscribeEvent
    public static void onEntityTick(EntityTickEvent.Pre event) {
        if (!(event.getEntity() instanceof Mob mob)) return;
        if (mob.level().isClientSide()) return;
        if (!(mob.getType().is(ModTags.Entities.SEEKERS))) return;

        StealthUtils.processStealthTick(mob);
    }
}

