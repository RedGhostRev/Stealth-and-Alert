package net.rev.stealthandalert.util;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.Panda;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;
import net.rev.stealthandalert.attachment.*;
import net.rev.stealthandalert.config.CommonConfigs;
import net.rev.stealthandalert.config.EntityAlertConfigLoader;
import net.rev.stealthandalert.config.EntityAlertSettings;
import net.rev.stealthandalert.event.StealthSoundEvent;
import net.rev.stealthandalert.network.S2CAlertDataPacket;

import java.util.*;

public class StealthUtils {
    public static float VISIBILITY_THRESHOLD = CommonConfigs.VISIBILITY_THRESHOLD.get().floatValue();

    private StealthUtils() {
    }

    // 视线检测：如果mob能看到target，则返回true
    public static boolean hasLineOfSight(Mob observer, Entity target) {
        // 距离快速失败
        EntityAlertSettings settings = EntityAlertConfigLoader.get(observer.getType());
        double distanceSqr = observer.distanceToSqr(target);
        double maxDistance = settings.viewRange();
        VisibilityData visData = target.getData(ModAttachments.VISIBILITY_DATA);
        if (visData.isVisible()) {
            maxDistance = maxDistance * visData.visibility();
            if (maxDistance < CommonConfigs.MIN_INVISIBLE_DISTANCE_TO_ENEMY_TRACKING.getAsDouble()) {
                maxDistance = CommonConfigs.MIN_INVISIBLE_DISTANCE_TO_ENEMY_TRACKING.getAsDouble() + 0.5;
            }
        }

        if (distanceSqr > maxDistance * maxDistance) return false;
        // 隐身快速失败
        if (target instanceof Player player) {
            if (player.isInvisible() && isFullyNaked(player)) {
                return false;
            }
        }

        // 获取观察方向与目标向量
        Vec3 eyePos = observer.getEyePosition();
        Vec3 lookVec = observer.getViewVector(1.0F);
        Vec3 targetVec = target.getEyePosition().subtract(eyePos);
        Vec3 targetDir = targetVec.normalize();

        // 水平与垂直FOV判定
        if (!isWithinFOV(observer, lookVec, targetDir)) return false;

        // 多点物理遮挡检查
        return canSeeAnyPart(observer, target, eyePos);
    }

    public static boolean isFullyNaked(Player player) {
        for (ItemStack armor : player.getArmorSlots()) {
            if (!armor.isEmpty()) return false;
        }
        return true;
    }

    // 新感知系统
    public static void processStealthTick(Mob mob) {
        if (mob.level().isClientSide()) return;

        // A: 获取原始数据快照
        AlertData oldData = mob.getData(ModAttachments.ALERT_DATA);

        // B: 门卫检查
        if (mob.level().players().isEmpty() && oldData.targetReactionTicks().isEmpty()) return;

        EntityAlertSettings settings = EntityAlertConfigLoader.get(mob.getType());
        if (settings.ignoreBaby() && mob.isBaby()) return;

        // 继承记忆名单
        Set<UUID> trackedPlayers = new HashSet<>(oldData.targetReactionTicks().keySet());

        // 扫描周围物理范围内的玩家，加入处理名单
        double range = settings.viewRange();
        List<Player> nearbyPlayers = mob.level().getEntitiesOfClass(Player.class,
                mob.getBoundingBox().inflate(range),
                p -> mob.distanceToSqr(p) <= range * range);

        for (Player p : nearbyPlayers) {
            trackedPlayers.add(p.getUUID());
        }

        // C: 一：个体并行计算
        // 临时结果容器
        Map<UUID, StealthEngine.IndividualResult> resultsMap = new HashMap<>();
        boolean anyoneVisible = false;

        for (UUID uuid : trackedPlayers) {
            Player player = mob.level().getPlayerByUUID(uuid);
            if (player == null) continue;
            boolean canSee = shouldArouseAlert(mob, player);
            if (canSee) anyoneVisible = true;

            StealthEngine.IndividualResult res = StealthEngine.updateIndividual(
                    player,
                    mob,
                    mob.getData(ModAttachments.ALERT_DATA).state(),
                    oldData.targetAwareness().getOrDefault(uuid, 0.0F),
                    oldData.targetReactionTicks().getOrDefault(uuid, CommonConfigs.DETECTION_REACTION_TICKS.getAsInt()),
                    oldData.targetStates().getOrDefault(uuid, AlertData.UNTRACKED),
                    oldData.targetMemoryTicks().getOrDefault(uuid, 0),
                    canSee
            );
            resultsMap.put(uuid, res);
        }

        // D: 二：全局协调计算
        StealthEngine.GlobalResult gRes = StealthEngine.updateGlobalContext(
                mob,
                oldData,
                resultsMap,
                anyoneVisible
        );

        // E: 打包
        AlertData newData = assembleData(resultsMap, gRes);

        // 获取调查数据和听觉数据并组合比较
        InvestigateLkpData investData = mob.getData(ModAttachments.INVESTIGATE_LKP_DATA);
        AlertSoundData soundData = mob.getData(ModAttachments.ALERT_SOUND_DATA);
        newData = dealWithVisualAndSound(mob, newData, soundData, investData);
        // F: 写回并同步
        mob.setData(ModAttachments.ALERT_DATA, newData);
        mob.setData(ModAttachments.ALERT_SOUND_DATA, AlertSoundData.DEFAULT);
        PacketDistributor.sendToPlayersTrackingEntity(mob, new S2CAlertDataPacket(mob.getId(), newData));

        // 处理敌人针对主目标的行为
        UUID primaryUuid = newData.primaryTarget().orElse(null);
        boolean canSeePrimary;
        if (primaryUuid != null) {
            Player primaryPlayer = mob.level().getPlayerByUUID(primaryUuid);
            canSeePrimary = shouldArouseAlert(mob, primaryPlayer);
        } else {
            canSeePrimary = false;
        }
        AlertActionHandler.execute(mob, newData, canSeePrimary);
    }

    private static AlertData dealWithVisualAndSound(Mob mob, AlertData visualData, AlertSoundData soundData, InvestigateLkpData investData) {
        if (soundData.equals(AlertSoundData.DEFAULT)) return visualData;
        if (soundData.pos().isEmpty()) return visualData;
        if (visualData.canSeeAnyone() || soundData.source().isEmpty() || (mob.getType().is(ModTags.Entities.CONDITIONAL_SEEKERS) && !visualData.targetMemoryTicks().containsKey(soundData.source().get())) || visualData.willFighting() || visualData.state() == AlertData.FIGHTING)
            return visualData;
        if (visualData.state() == AlertData.IDLE) {
            if (soundData.volume() < 34.0) {
                return visualData;
            } else {
                if (soundData.threatLevel() <= AlertSoundData.MEDIUM) {
                    if (soundData.volume() <= 42.0) {
                        return visualData.withSound(AlertData.SUSPICIOUS, soundData.pos(), 0, CommonConfigs.PATIENCE_TICKS.getAsInt());
                    } else {
                        mob.setData(ModAttachments.INVESTIGATE_LKP_DATA, InvestigateLkpData.DEFAULT);
                        return visualData.withSound(AlertData.SEARCHING, soundData.pos(), 0, CommonConfigs.PATIENCE_TICKS.getAsInt());
                    }
                } else {
                    mob.setData(ModAttachments.INVESTIGATE_LKP_DATA, InvestigateLkpData.DEFAULT);
                    return visualData.withSound(AlertData.SEARCHING, soundData.pos(), 0, CommonConfigs.PATIENCE_TICKS.getAsInt());
                }
            }
        }
        if (visualData.state() == AlertData.SUSPICIOUS) {
            if (soundData.volume() < 30.0) {
                return visualData;
            } else {
                if (soundData.threatLevel() == AlertSoundData.LOW) {
                    if (soundData.distance() > 7) {
                        return visualData;
                    } else if (soundData.distance() > 6) {
                        return visualData.withSound(AlertData.SUSPICIOUS, soundData.pos(), 0, CommonConfigs.PATIENCE_TICKS.getAsInt());
                    } else {
                        mob.setData(ModAttachments.INVESTIGATE_LKP_DATA, InvestigateLkpData.DEFAULT);
                        return visualData.withSound(AlertData.SEARCHING, soundData.pos(), 0, CommonConfigs.PATIENCE_TICKS.getAsInt());
                    }
                } else if (soundData.threatLevel() == AlertSoundData.MEDIUM) {
                    if (soundData.distance() > 12) {
                        return visualData;
                    } else if (soundData.distance() > 10) {
                        return visualData.withSound(AlertData.SUSPICIOUS, soundData.pos(), 0, CommonConfigs.PATIENCE_TICKS.getAsInt());
                    } else {
                        mob.setData(ModAttachments.INVESTIGATE_LKP_DATA, InvestigateLkpData.DEFAULT);
                        return visualData.withSound(AlertData.SEARCHING, soundData.pos(), 0, CommonConfigs.PATIENCE_TICKS.getAsInt());
                    }
                } else {
                    mob.setData(ModAttachments.INVESTIGATE_LKP_DATA, InvestigateLkpData.DEFAULT);
                    return visualData.withSound(AlertData.SEARCHING, soundData.pos(), 0, CommonConfigs.PATIENCE_TICKS.getAsInt());
                }
            }
        }
        if (visualData.state() == AlertData.SEARCHING) {
            if (soundData.volume() < 28.0) {
                return visualData;
            } else {
                if (soundData.threatLevel() == AlertSoundData.LOW) {
                    if (soundData.distance() > 16) {
                        return visualData;
                    } else if (soundData.distance() > 12) {
                        if (!investData.isSearchingAround() || Math.abs(mob.getY() - soundData.pos().get().y()) > 10) {
                            return visualData;
                        } else {
                            mob.setData(ModAttachments.INVESTIGATE_LKP_DATA, InvestigateLkpData.DEFAULT);
                            return visualData.withSound(AlertData.SEARCHING, soundData.pos(), 0, CommonConfigs.PATIENCE_TICKS.getAsInt());
                        }
                    } else {
                        if (!investData.isSearchingAround() || Math.abs(mob.getY() - soundData.pos().get().y()) > 10) {
                            return visualData;
                        } else {
                            mob.setData(ModAttachments.INVESTIGATE_LKP_DATA, InvestigateLkpData.DEFAULT);
                            return visualData.withSound(AlertData.SEARCHING, soundData.pos(), 0, CommonConfigs.PATIENCE_TICKS.getAsInt());
                        }
                    }
                } else if (soundData.threatLevel() == AlertSoundData.MEDIUM) {
                    if (soundData.distance() > 20) {
                        return visualData;
                    } else if (soundData.distance() > 16) {
                        if (!investData.isSearchingAround() || Math.abs(mob.getY() - soundData.pos().get().y()) > 10) {
                            return visualData;
                        } else {
                            mob.setData(ModAttachments.INVESTIGATE_LKP_DATA, InvestigateLkpData.DEFAULT);
                            return visualData.withSound(AlertData.SEARCHING, soundData.pos(), 0, CommonConfigs.PATIENCE_TICKS.getAsInt());
                        }
                    } else {
                        if (soundData.volume() < 35.0 && (!investData.isSearchingAround() || Math.abs(mob.getY() - soundData.pos().get().y()) > 10)) {
                            return visualData;
                        } else {
                            mob.setData(ModAttachments.INVESTIGATE_LKP_DATA, InvestigateLkpData.DEFAULT);
                            return visualData.withSound(AlertData.SEARCHING, soundData.pos(), 0, CommonConfigs.PATIENCE_TICKS.getAsInt());
                        }
                    }
                } else {
                    if (soundData.distance() > 32) {
                        return visualData;
                    } else {
                        mob.setData(ModAttachments.INVESTIGATE_LKP_DATA, InvestigateLkpData.DEFAULT);
                        return visualData.withSound(AlertData.SEARCHING, soundData.pos(), 0, CommonConfigs.PATIENCE_TICKS.getAsInt());
                    }
                }
            }
        }
        return visualData;
    }

    // “引起警戒”的检查
    public static boolean shouldArouseAlert(Mob mob, Player player) {
        // 难度检查
        if (mob.level().getDifficulty() == Difficulty.PEACEFUL) {
            return false;
        }
        // 1.基础物理状态检查
        if (player == null || !player.isAlive() || !mob.isAlive()) {
            return false;
        }

        // 2.模式检查
        if (player.isCreative() || player.isSpectator()) {
            return false;
        }

        // 3.属性检查
        if (AlertLogicHandler.isPlayerPet(mob, player)) {
            return false;
        }
        if (mob instanceof Panda panda) {
            if (!panda.isAggressive()) return false;
        }

        // 4.可见度不大于阈值时，最大感知距离检查
        double distanceSqr = mob.distanceToSqr(player);
        if (!player.getData(ModAttachments.VISIBILITY_DATA).isVisible()) {
            double minDistance;
            if (mob.getData(ModAttachments.ALERT_DATA).targetStates().getOrDefault(player.getUUID(), AlertData.UNTRACKED) < AlertData.TRACKING) {
                minDistance = CommonConfigs.MIN_INVISIBLE_DISTANCE.getAsDouble();
            } else {
                minDistance = CommonConfigs.MIN_INVISIBLE_DISTANCE_TO_ENEMY_TRACKING.getAsDouble();
            }
            if (distanceSqr > minDistance * minDistance) return false;
        }

        boolean isTouching = mob.getBoundingBox().intersects(player.getBoundingBox().inflate(0.4));
        // 5.视线与碰撞检查
        if (!isTouching && !StealthUtils.hasLineOfSight(mob, player)) {
            return false;
        }

        // 分流
        if (!mob.getType().is(ModTags.Entities.CONDITIONAL_SEEKERS)) return true;

        // 5.意图检查
        EntityAlertSettings settings = EntityAlertConfigLoader.get(mob.getType());
        if (settings.logicList().isEmpty()) return true;

        for (String logic : settings.logicList()) {
            if (AlertLogicHandler.checkLogic(logic, mob, player, settings)) {
                return true;
            }
        }

        return false;
    }

    // 打包数据
    private static AlertData assembleData(Map<UUID, StealthEngine.IndividualResult> res, StealthEngine.GlobalResult gRes) {
        Map<UUID, Float> progress = new HashMap<>();
        Map<UUID, Integer> states = new HashMap<>();
        Map<UUID, Integer> reactions = new HashMap<>();
        Map<UUID, Integer> lastDamageTicks = new HashMap<>();

        // lres意思为Lambda表达式中的参数，代表每个玩家的个体结果
        res.forEach((uuid, lres) -> {
            boolean isDeadData = lres.level() <= 0.0F &&
                    lres.pState() == AlertData.UNTRACKED &&
                    lres.reaction() >= CommonConfigs.DETECTION_REACTION_TICKS.getAsInt() &&
                    lres.memory() <= 0;
            if (!isDeadData) {
                progress.put(uuid, lres.level());
                states.put(uuid, lres.pState());
                reactions.put(uuid, lres.reaction());
                lastDamageTicks.put(uuid, lres.memory());
            } else {
                progress.remove(uuid);
                states.remove(uuid);
                reactions.remove(uuid);
                lastDamageTicks.remove(uuid);
            }
        });

        return new AlertData(
                gRes.state(),
                progress,
                states,
                reactions,
                lastDamageTicks,
                gRes.lkp(),
                gRes.primaryTarget(),
                gRes.stateTicks(),
                gRes.patienceTicks(),
                gRes.isSeeingAnyone(),
                gRes.willFighting()
        );
    }

    // 三点检查
    private static boolean canSeeAnyPart(Mob observer, Entity target, Vec3 start) {
        Vec3[] checkPoints = {
                // 头
                target.getEyePosition(),
                // 胸
                target.position().add(0, target.getBbHeight() / 2.0, 0),
                // 脚
                target.position().add(0, 0.1, 0)
        };

        for (Vec3 end : checkPoints) {
            if (isLineClear(observer, start, end)) return true;
        }
        return false;
    }

    // 视线射线检测
    private static boolean isLineClear(Mob observer, Vec3 start, Vec3 end) {
        // TODO 能透过玻璃
        return observer.level().clip(new ClipContext(
                start, end,
                ClipContext.Block.COLLIDER,
                ClipContext.Fluid.NONE,
                observer
        )).getType() == HitResult.Type.MISS;
    }

    // FOV判定
    private static boolean isWithinFOV(Mob observer, Vec3 lookVec, Vec3 targetDir) {
        boolean isVerticalLooking = Math.abs(lookVec.x) < 0.0001 && Math.abs(lookVec.z) < 0.0001;

        EntityAlertSettings settings = EntityAlertConfigLoader.get(observer.getType());
        // 水平角度判定
        if (!isVerticalLooking) {
            Vec3 lookHorizontal = new Vec3(lookVec.x, 0, lookVec.z).normalize();
            Vec3 targetHorizontal = new Vec3(targetDir.x, 0, targetDir.z).normalize();

            double horizontalDot = lookHorizontal.dot(targetHorizontal);
            double horizontalThreshold = Math.cos(Math.toRadians(settings.horizontalFov() / 2.0));

            if (horizontalDot < horizontalThreshold) return false;
        }

        // 垂直角度判定
        double pitchToTargetDegrees = Math.toDegrees(Math.asin(targetDir.y));

        double maxUpPitch = settings.maxUpPitch();
        double maxDownPitch = -settings.maxDownPitch();

        return pitchToTargetDegrees >= maxDownPitch && pitchToTargetDegrees <= maxUpPitch;
    }

    // 可见度计算
    public static float calculateVisibility(Player player) {
        if (player.isInvisible() && isFullyNaked(player)) return 0.0F;
        if (isFullyHiddenByEnvironment(player)) return 0.0F;

        int skyDarken = player.level().getSkyDarken();
        int ambientLight = player.level().getRawBrightness(player.blockPosition(), skyDarken);
        int emittedLight = getPlayerEmittedLight(player);
        int finalLight = Math.max(ambientLight, emittedLight);

        int effectiveThreshold = 2;
//        if (player.isCrouching()) {
//            effectiveThreshold = 4;
//        } else if (player.isVisuallyCrawling() || player.isVisuallySwimming()) {
//            effectiveThreshold = 5;
//        }

        float visibility = 1.0F;

        // 计算
        // 光照修正
        float adjustedLight = (float) (finalLight - effectiveThreshold) / (15 - effectiveThreshold);
        float lightMultiplier = 0.2F + adjustedLight * 0.8F;
        visibility *= lightMultiplier;

        // 环境修正
        if (isInTallGrass(player.level(), player.blockPosition())) {
            visibility *= 0.5F;
        }

        // 姿态修正
        if (player.isVisuallyCrawling() || player.isVisuallySwimming()) {
            visibility *= 0.4F;
        } else if (player.isCrouching()) {
            visibility *= 0.7F;
        }

        if (player.isSprinting()) {
            visibility *= 1.25F;
        }

        return Math.clamp(visibility, 0.0F, 1.0F);
    }

    private static boolean isFullyHiddenByEnvironment(Player player) {
        Level level = player.level();
        BlockPos center = player.blockPosition();

        if (isInLargeTallGrassPatch(level, center)) return true;
        if (player.isVisuallyCrawling() && isInLargeShortGrassPatch(level, center)) return true;

        BlockState feetState = level.getBlockState(center);
        BlockState headState = level.getBlockState(center.above());

        if (feetState.is(BlockTags.LEAVES) && headState.is(BlockTags.LEAVES)) return true;

        return false;
    }

    private static boolean isInTallGrass(Level level, BlockPos pos) {
        BlockState feet = level.getBlockState(pos);
        BlockState head = level.getBlockState(pos.above());
        boolean feetIsCover = feet.is(ModTags.Blocks.CAN_COVER);
        boolean headIsCover = head.is(ModTags.Blocks.CAN_COVER);
        return feetIsCover && headIsCover;
    }

/*    private static boolean inInShortGrass(Level level, BlockPos pos) {
        BlockState feet = level.getBlockState(pos);
        return feet.is(Blocks.SHORT_GRASS) || feet.is(Blocks.FERN) || feet.is(BlockTags.SMALL_FLOWERS);
    }*/

    private static boolean isInLargeTallGrassPatch(Level level, BlockPos center) {
        BlockPos[] origins = {
                center,
                center.north(),
                center.west(),
                center.north().west()
        };

        for (BlockPos origin : origins) {
            if (is2x2TallGrassBlock(level, origin)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isInLargeShortGrassPatch(Level level, BlockPos center) {
        BlockPos[] origins = {
                center,
                center.north(),
                center.west(),
                center.north().west()
        };

        for (BlockPos origin : origins) {
            if (is2x2ShortGrassBlock(level, origin)) {
                return true;
            }
        }
        return false;
    }

    private static boolean is2x2TallGrassBlock(Level level, BlockPos origin) {
        BlockPos[] feetPositions = {
                origin, origin.east(), origin.south(), origin.east().south()
        };
        BlockPos[] headPositions = {
                origin.above(), origin.east().above(), origin.south().above(), origin.east().south().above()
        };

        for (int i = 0; i < 4; i++) {
            BlockState feet = level.getBlockState(feetPositions[i]);
            BlockState head = level.getBlockState(headPositions[i]);
            boolean feetIsCover = feet.is(ModTags.Blocks.CAN_COVER);
            boolean headIsCover = head.is(ModTags.Blocks.CAN_COVER);
            if (!(feetIsCover && headIsCover)) return false;
        }

        return true;
    }

    private static boolean is2x2ShortGrassBlock(Level level, BlockPos origin) {
        BlockPos[] feetPositions = {
                origin, origin.east(), origin.south(), origin.east().south()
        };

        for (int i = 0; i < 4; i++) {
            BlockState feet = level.getBlockState(feetPositions[i]);
            boolean feetIsCover = feet.is(Blocks.SHORT_GRASS) || feet.is(Blocks.FERN) || feet.is(BlockTags.SMALL_FLOWERS);
            if (!feetIsCover) return false;
        }

        return true;
    }

    private static int getPlayerEmittedLight(Player player) {
        if (player.hasEffect(MobEffects.GLOWING)) return 15;

        int glintLevel = 0;

        for (ItemStack armor : player.getArmorSlots()) {
            if (armor.isEnchanted()) {
                glintLevel += 3;
            }
        }

        if (player.getMainHandItem().isEnchanted()) glintLevel += 2;
        if (player.getOffhandItem().isEnchanted()) glintLevel += 2;

        return Math.min(glintLevel, 15);
    }

    public static void reactToSound(StealthSoundEvent event) {
        if (event.volume <= 0F || event.radius <= 0) return;
        if (!(event.soundSource instanceof Player)) return;
        Level level = event.soundSource.level();
        Vec3 pos = event.soundPos;
        double radius = event.radius;

        // 框选声音半径内的所有 SEEKERS 怪物
        List<Mob> mobs = level.getEntitiesOfClass(Mob.class, new AABB(pos.x - radius, pos.y - radius, pos.z - radius,
                pos.x + radius, pos.y + radius, pos.z + radius), mob -> mob.isAlive() && mob.getType().is(ModTags.Entities.SEEKERS));

        for (Mob mob : mobs) {
            Vec3 eyePos = mob.getEyePosition();
            double distance = pos.distanceTo(eyePos);

            if (distance > radius) {
                continue;
            }

            double currentVolume = event.volume;

            // 计算中间隔着的固体厚度与层数
            double solidBlockThickness = 0.0;
            int solidBlockCount = 0;
            Vec3 direction = eyePos.subtract(pos);
            Vec3 normalizedDir = direction.normalize();

            BlockPos originBlockPos = BlockPos.containing(pos.x, pos.y, pos.z);

            double stepSize = 0.5;
            int totalSteps = (int) (distance / stepSize);

            BlockPos.MutableBlockPos mutablePos = BlockPos.containing(pos).mutable();
            BlockState lastState = null;

            for (int i = 0; i < totalSteps; i++) {
                Vec3 currentCheckPos = pos.add(normalizedDir.scale(i * stepSize));
                mutablePos.set(currentCheckPos.x, currentCheckPos.y, currentCheckPos.z);
                BlockState currentState = level.getBlockState(mutablePos);

                if (mutablePos.equals(originBlockPos)) continue;

                // 如果在固体方块内部
                if (!currentState.isAir() && currentState.isCollisionShapeFullBlock(level, mutablePos)) {
                    solidBlockThickness += stepSize; // 固体厚度累加当前的步长

                    // 如果材质，层数 +1
                    if (lastState == null || !currentState.getBlock().equals(lastState.getBlock())) {
                        solidBlockCount++;
                    }
                }
                lastState = currentState;
            }

            // 计算音量损耗
            double thicknessPenalty = solidBlockThickness * 8.0; // 每 1 米厚度扣 8 点
            double layerPenalty = solidBlockCount * 4.0;          // 每跨一层界面额外重罚 4 点
            double totalOcclusionDamping = thicknessPenalty + layerPenalty;

            // 计算最终音量
            double finalReceivedVolume = currentVolume - totalOcclusionDamping;
            finalReceivedVolume = Math.max(0.0, finalReceivedVolume);
            boolean canHear = finalReceivedVolume > 0;

            // 比较
            if (canHear) {
                AlertSoundData data = mob.getData(ModAttachments.ALERT_SOUND_DATA);
                double currentScore = getScore(finalReceivedVolume, distance, event.threatLevel);
                if (currentScore > data.score()) {
                    mob.setData(ModAttachments.ALERT_SOUND_DATA, new AlertSoundData(Optional.of(event.soundSource.getUUID()), Optional.of(event.soundPos), finalReceivedVolume, distance, event.threatLevel, currentScore));
                }
            }
        }
    }

    private static double getScore(double volume, double distance, int threatLevel) {
        double wVolume = 1.0;
        double wDistance = 0.5;
        double wThreat = 20.0;
        return (volume * wVolume) + (threatLevel * wThreat) - (distance * wDistance);
    }
}
