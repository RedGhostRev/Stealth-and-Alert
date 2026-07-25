package net.rev.stealthandalert.event;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingChangeTargetEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.rev.stealthandalert.StealthAndAlert;
import net.rev.stealthandalert.ai.StealthLookAroundGoal;
import net.rev.stealthandalert.attachment.AlertData;
import net.rev.stealthandalert.attachment.CrawlData;
import net.rev.stealthandalert.attachment.InvestigateLkpData;
import net.rev.stealthandalert.attachment.ModAttachments;
import net.rev.stealthandalert.attribute.ModAttributes;
import net.rev.stealthandalert.common.alert.condition.FightBackCondition;
import net.rev.stealthandalert.common.alert.condition.ProtectOthersCondition;
import net.rev.stealthandalert.config.EntityAlertConditionConfigLoader;
import net.rev.stealthandalert.config.EntityAlertConditionSettings;
import net.rev.stealthandalert.network.S2CAlertDataPacket;
import net.rev.stealthandalert.network.S2CCrawlPacket;
import net.rev.stealthandalert.util.AssassinationHandler;
import net.rev.stealthandalert.util.CommonUtils;
import net.rev.stealthandalert.util.ModTags;
import net.rev.stealthandalert.util.StealthUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

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

    // SEEKERS 受击
    @SubscribeEvent
    public static void onSeekersHurt(LivingDamageEvent.Post event) {
        LivingEntity entity = event.getEntity();
        if (entity.level().isClientSide() || !(entity instanceof Mob mob)) return;
        EntityType<?> mobType = mob.getType();
        if (!mobType.is(ModTags.Entities.SEEKERS)) return;
        Entity attacker = event.getSource().getEntity();
        if (!(attacker instanceof Player player)) return;
        if (player.isCreative() || player.isSpectator()) return;
        if (CommonUtils.isPlayerPet(mob, player, true)) return;
        UUID uuid = player.getUUID();
        EntityAlertConditionSettings settings = EntityAlertConditionConfigLoader.get(mobType);
        if (mobType.is(ModTags.Entities.CONDITIONAL_SEEKERS)) {
            if (!settings.alertConditions().containsKey(FightBackCondition.ID)) {
                return;
            } else {
                mob.getData(ModAttachments.EVENT_LISTENER_DATA).updateState(FightBackCondition.ID, uuid, mob.level().getGameTime());
            }
        }
        AlertData data = mob.getData(ModAttachments.ALERT_DATA);
        Map<UUID, Integer> statesMap = new HashMap<>(data.targetStates());
        Map<UUID, Integer> reactionMap = new HashMap<>(data.targetReactionTicks());
        Map<UUID, Integer> memoryMap = new HashMap<>(data.targetMemoryTicks());
        LivingEntity currentTarget = mob.getTarget();
        int currentPState = statesMap.getOrDefault(uuid, AlertData.UNTRACKED);
        // 判定：是否在打别人
        boolean isFightingOthers = (data.state() == AlertData.FIGHTING) && currentTarget != null && currentTarget != player;
        int nextState = data.state();
        Optional<Vec3> nextLKP = data.lastKnownPos();
        boolean dataChanged = false;
        boolean skip = settings.detection().ignoreBaby() && mob.isBaby();
        // 只有当该玩家还没达到 TRACKING 状态时才更新数据
        int stateChangeTicks = 0;
        if (!skip) {
            if (currentPState < AlertData.TRACKING) {
                // progressMap.put(uuid, 100.0F);
                // statesMap.put(uuid, AlertData.TRACKING);
                reactionMap.put(uuid, 0);

                // 根据是否正在打别人，决定全局状态
                if (!isFightingOthers) {
                    nextState = AlertData.SEARCHING;
                    nextLKP = Optional.of(player.position());
                    // nextPrimary = Optional.of(uuid);
                }
                dataChanged = true;
            }
            if (data.willFighting()) {
                stateChangeTicks = data.stateChangeTicks();
            }
        } else {
            memoryMap.put(uuid, settings.getMemoryTicks());
            dataChanged = true;
        }

        if (dataChanged) {
            AlertData newData = mob.setData(ModAttachments.ALERT_DATA, new AlertData(
                    nextState, data.targetAwareness(), data.targetStates(), reactionMap, memoryMap,
                    nextLKP, data.primaryTarget(), stateChangeTicks, data.patienceTicks(), data.canSeeAnyone(), data.willFighting()
            ));
            mob.setData(ModAttachments.INVESTIGATE_LKP_DATA, InvestigateLkpData.DEFAULT);
            PacketDistributor.sendToPlayersTrackingEntity(mob, new S2CAlertDataPacket(mob.getId(), newData));
        }
        mob.getData(ModAttachments.EVENT_LISTENER_DATA).updateState("stealth_and_alert:protect_others", uuid, mob.level().getGameTime());
    }

    @SubscribeEvent
    public static void onProtectedHurt(LivingDamageEvent.Post event) {
        LivingEntity entity = event.getEntity();
        if (!(entity.getType().is(ModTags.Entities.PROTECTED)) || entity.level().isClientSide()) return;
        if (event.getSource().getEntity() instanceof Player player) {
            if (CommonUtils.isPlayerPet(entity, player, true)) return;
            if (player.isCreative() || player.isSpectator()) return;
            AlertData data = entity.getData(ModAttachments.ALERT_DATA);
            Map<UUID, Integer> targetMemoryTicks = new HashMap<>(data.targetMemoryTicks());
            EntityAlertConditionSettings settings = EntityAlertConditionConfigLoader.get(entity.getType());
            targetMemoryTicks.put(player.getUUID(), settings.getMemoryTicks());
            AlertData newData = entity.setData(ModAttachments.ALERT_DATA, new AlertData(
                    data.state(),
                    data.targetAwareness(),
                    data.targetStates(),
                    data.targetReactionTicks(),
                    targetMemoryTicks,
                    data.lastKnownPos(),
                    data.primaryTarget(),
                    data.stateChangeTicks(),
                    data.patienceTicks(),
                    data.canSeeAnyone(),
                    data.willFighting()
            ));
            PacketDistributor.sendToPlayersTrackingEntity(entity, new S2CAlertDataPacket(entity.getId(), newData));
            entity.getData(ModAttachments.EVENT_LISTENER_DATA).updateState(ProtectOthersCondition.ID, player.getUUID(), entity.level().getGameTime());
        }
    }

    // 为 SEEKERS 新增、删除特定 Goal
    @SubscribeEvent
    public static void onEntityJoin(EntityJoinLevelEvent event) {
        if (event.getLevel().isClientSide()) return;
        if (!(event.getEntity() instanceof Mob mob)) return;
        if (!mob.getType().is(ModTags.Entities.SEEKERS)) return;

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
    public static void onPlayerTick(PlayerTickEvent.Pre event) {
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

        double currentVis = StealthUtils.calculateVisibility(player);
        AttributeInstance attribute = player.getAttribute(ModAttributes.VISIBILITY);
        if (attribute != null) {
            attribute.setBaseValue(currentVis);
        }
    }

    // 分步发包？
    @SubscribeEvent
    public static void onEntityTick(EntityTickEvent.Pre event) {
        if (!(event.getEntity() instanceof Mob mob)) return;
        if (mob.level().isClientSide()) return;
        if (!mob.getType().is(ModTags.Entities.SEEKERS) && !mob.getType().is(ModTags.Entities.PROTECTED)) return;
        if (AssassinationHandler.isTargetLocked(mob.level(), mob.getId())) return;
        StealthUtils.processStealthTick(mob);
    }
}

