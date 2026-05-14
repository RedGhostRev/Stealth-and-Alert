package net.rev.stealthandalert.event;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.Panda;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingChangeTargetEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.rev.stealthandalert.StealthAndAlert;
import net.rev.stealthandalert.ai.InvestigateLkpGoal;
import net.rev.stealthandalert.ai.StealthLookAroundGoal;
import net.rev.stealthandalert.attachment.AlertData;
import net.rev.stealthandalert.attachment.CrawlData;
import net.rev.stealthandalert.attachment.ModAttachments;
import net.rev.stealthandalert.attachment.VisibilityData;
import net.rev.stealthandalert.config.CommonConfigs;
import net.rev.stealthandalert.config.EntityAlertConfigLoader;
import net.rev.stealthandalert.config.EntityAlertSettings;
import net.rev.stealthandalert.network.S2CAlertDataPacket;
import net.rev.stealthandalert.network.S2CCrawlPacket;
import net.rev.stealthandalert.network.S2CVisibilityDataPacket;
import net.rev.stealthandalert.util.AlertLogicHandler;
import net.rev.stealthandalert.util.ModTags;
import net.rev.stealthandalert.util.StealthUtils;

import java.util.*;

@EventBusSubscriber(modid = StealthAndAlert.MOD_ID)
public class StealthEvents {

    // 拦截原版setTarget事件
    // 当且仅当生物的待设定攻击目标有 DETECTABLE 标签时，才根据条件拦截
    // 如果生物有主目标，且生物为 FIGHTING 状态，主目标为 TRACKING 状态，则拦截生物对其他任何生物的目标设定，强制将待设定目标设置为主目标
    @SubscribeEvent
    public static void onMobTarget(LivingChangeTargetEvent event) {
        LivingEntity target = event.getNewAboutToBeSetTarget();
        if (target == null) return;
        if (target instanceof Player player && (player.isCreative() || player.isSpectator())) return;

        if (!(event.getEntity() instanceof Mob mob)) return;
        if (!(mob.getType().is(ModTags.Entities.SEEKERS))) return;

        AlertData data = mob.getData(ModAttachments.ALERT_DATA);

        // 若生物无主目标且待设目标也不是 DETECTABLE，则任由原版 AI 处理
        if (data.primaryTarget().isEmpty()) {
            if (!target.getType().is(ModTags.Entities.DETECTABLE)) {
                return;
            } else {
                // 接下来，生物待设目标是 DETECTABLE
                // 生物无主目标时，无论如何也不应锁定当前待设目标，直到主目标设立
                event.setNewAboutToBeSetTarget(null);
                return;
            }
        }

        // 接下来，生物有主目标

        // 如果待设目标与主目标不一致，
        if (!target.getUUID().equals(data.primaryTarget().get())) {
            // 此时，若生物处于FIGHTING状态且主目标处于TRACKING状态
            // 那么，强制将主目标设置为待设目标
            if ((data.state() == AlertData.FIGHTING && data.targetStates().getOrDefault(data.primaryTarget().get(), AlertData.UNTRACKED) == AlertData.TRACKING)) {
                ServerLevel level = ((ServerLevel) mob.level());
                Entity entity = level.getEntity(data.primaryTarget().get());
                if (entity instanceof LivingEntity livingEntity) {
                    event.setNewAboutToBeSetTarget(livingEntity);
                    return;
                }
            } else {
                if (!target.getType().is(ModTags.Entities.DETECTABLE)) {
                    // 如若不然，如果待设目标不是 DETECTABLE，则归于原版
                    return;
                } else {
                    // 如果待设目标是 DETECTABLE，由于待设目标不是主目标，无论如何也不应被锁定
                    event.setNewAboutToBeSetTarget(null);
                    return;
                }
            }
        }

        // 如果待设目标与主目标一致，此时待设目标必为 DETECTABLE
        // 为可读性，保留 DETECTABLE 判断
        if (!target.getType().is(ModTags.Entities.DETECTABLE)) return;

        // 锁定要求：生物处于 FIGHTING 状态且主目标处于 TRACKING 状态
        if (data.state() < AlertData.FIGHTING) {
            event.setNewAboutToBeSetTarget(null);
            return;
        }

        int pState = data.targetStates().getOrDefault(target.getUUID(), AlertData.UNTRACKED);
        if (pState < AlertData.TRACKING) {
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


            Map<UUID, Float> progressMap = new HashMap<>(data.targetAwareness());
            Map<UUID, Integer> statesMap = new HashMap<>(data.targetStates());
            Map<UUID, Integer> reactionsMap = new HashMap<>(data.targetReactionTicks());
            Map<UUID, Integer> lastDamageTicksMap = new HashMap<>(data.targetMemoryTicks());
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
                        data.lastKnownPos(),
                        data.primaryTarget(),
                        data.stateChangeTicks(),
                        data.patienceTicks(),
                        data.canSeeAnyone(),
                        data.willFighting()
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
            Optional<Vec3> nextLKP = data.lastKnownPos();
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
                        data.stateChangeTicks(),
                        data.patienceTicks(),
                        data.canSeeAnyone(),
                        data.willFighting()
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
                    Map<UUID, Integer> memoryMap = new HashMap<>(data.targetMemoryTicks());
                    memoryMap.put(player.getUUID(), 1200);

                    AlertData newData = new AlertData(
                            data.state(), data.targetAwareness(), data.targetStates(), data.targetReactionTicks(),
                            memoryMap, data.lastKnownPos(), data.primaryTarget(), data.stateChangeTicks(), data.patienceTicks(), data.canSeeAnyone(), data.willFighting()
                    );

                    golem.setData(ModAttachments.ALERT_DATA, newData);

                    PacketDistributor.sendToPlayersTrackingEntity(golem, new S2CAlertDataPacket(golem.getId(), newData));
                }
            }
        }
    }

    // 为 SEEKERS 新增、删除特定 Goal
    @SubscribeEvent
    public static void onEntityJoin(EntityJoinLevelEvent event) {
        if (event.getLevel().isClientSide()) return;
        if (!(event.getEntity() instanceof Mob mob)) return;
        if (!mob.getType().is(ModTags.Entities.SEEKERS)) return;

        boolean hasInvestigateGoal = mob.goalSelector.getAvailableGoals().stream().anyMatch(wrappedGoal ->
                wrappedGoal.getGoal() instanceof InvestigateLkpGoal);
        if (!hasInvestigateGoal) {
            mob.goalSelector.addGoal(3, new InvestigateLkpGoal(mob, 0.9));
        }

        mob.goalSelector.getAvailableGoals().removeIf(wrappedGoal ->
                wrappedGoal.getGoal() instanceof LookAtPlayerGoal);

        int originalPriority = -1;
        Goal goalToRemove = null;

        for (WrappedGoal wrappedGoal : mob.goalSelector.getAvailableGoals()) {
            Goal innerGoal = wrappedGoal.getGoal();
            if (innerGoal instanceof RandomLookAroundGoal && !(innerGoal instanceof StealthLookAroundGoal)) {
                originalPriority = wrappedGoal.getPriority();
                goalToRemove = innerGoal;
                break;
            }
        }

        if (goalToRemove != null && originalPriority != -1) {
            mob.goalSelector.removeGoal(goalToRemove);
            mob.goalSelector.addGoal(originalPriority, new StealthLookAroundGoal(mob));
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        if (player.level().isClientSide()) return;
        boolean crawling = player.getData(ModAttachments.CRAWL_DATA).isCrawling();
        if (crawling) {
            if (player.isSleeping() || player.isPassenger() || player.isFallFlying() || player.getAbilities().flying) {
                crawling = false;
                player.setData(ModAttachments.CRAWL_DATA, new CrawlData(crawling));
            }
        }
        PacketDistributor.sendToPlayer((ServerPlayer) player, new S2CCrawlPacket(crawling));

        float currentVis = StealthUtils.calculateVisibility(player);
        boolean isVisible;
        isVisible = !(currentVis <= StealthUtils.VISIBILITY_THRESHOLD);

        player.setData(ModAttachments.VISIBILITY_DATA, new VisibilityData(currentVis, isVisible));

        if (player.tickCount % 2 == 0) {
            PacketDistributor.sendToPlayer((ServerPlayer) player, new S2CVisibilityDataPacket(currentVis, isVisible));
        }
    }

    // FIXME 可能有潜在的性能问题
    // 分步发包？
    @SubscribeEvent
    public static void onEntityTick(EntityTickEvent.Pre event) {
        if (!(event.getEntity() instanceof Mob mob)) return;
        if (mob.level().isClientSide()) return;
        if (!(mob.getType().is(ModTags.Entities.SEEKERS))) return;

        StealthUtils.processStealthTick(mob);
    }
}

