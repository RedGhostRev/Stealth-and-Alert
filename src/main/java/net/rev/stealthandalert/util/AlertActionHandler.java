package net.rev.stealthandalert.util;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import net.rev.stealthandalert.attachment.AlertData;
import net.rev.stealthandalert.attachment.InvestigateLkpData;
import net.rev.stealthandalert.attachment.ModAttachments;

import java.util.Optional;


public class AlertActionHandler {
    public static void execute(Mob mob, AlertData data, boolean canSeePrimary) {
        // 若为闲逛
        if (data.state() == AlertData.IDLE) return;

        // 获取当前主目标
        Player primary = data.primaryTarget()
                .map(uuid -> mob.level().getPlayerByUUID(uuid))
                .orElse(null);
        if (canSeePrimary && primary != null) {
            // 如果看见了主目标
            int pState = data.targetStates().getOrDefault(primary.getUUID(), AlertData.UNTRACKED);

            if (pState >= AlertData.AWARE) {
                mob.getLookControl().setLookAt(primary, 30.0F, 30.0F);

                if (data.state() == AlertData.FIGHTING && pState == AlertData.TRACKING) {
                    if (mob instanceof Piglin piglin) {
                        piglin.getBrain().eraseMemory(MemoryModuleType.PACIFIED);
                        piglin.getBrain().setMemory(MemoryModuleType.ANGRY_AT, primary.getUUID());
                    }
                    mob.setTarget(primary);
                } else {
                    if (mob.getTarget() == primary) mob.setTarget(null);
                    if (!mob.getNavigation().isDone()) mob.getNavigation().stop();
                }
                mob.setData(ModAttachments.INVESTIGATE_LKP_DATA, InvestigateLkpData.DEFAULT);
            }


        } else {
            // 如果没看见主目标
            if (mob.getTarget() != null && mob.getTarget().equals(primary)) {
                // 如果仍处于 FIGHTING 状态，继续保持锁定，直到降级为 SEARCHING 状态
                // 因此，必须确保 FIGHTING 状态，在丢失视野后，只能持续很短的一段时间
                // 附加条件：主目标必须处于 TRACKING 状态
                // 因此 TRACKING 状态也应在丢失视野后只能持续很短的一段时间
                // 由于没有为观测状态设定计时器，所以用别的办法来实现

                // 但是，若目标处于非生存模式，则无视这些条件
                if (primary instanceof Player player) {
                    if ((player.isInvisible() && StealthUtils.isFullyNaked(player)) || player.isCreative() || player.isSpectator()) {
                        mob.setTarget(null);
                    }
                } else if (data.state() < AlertData.FIGHTING && data.targetStates().getOrDefault(primary.getUUID(), AlertData.UNTRACKED) < AlertData.TRACKING) {
                    mob.setTarget(null);
                } else {
                    mob.setTarget(primary);
                }
            }
            Vec3 lkp = mob.getData(ModAttachments.ALERT_DATA).lastKnownPos().orElse(null);
            if (lkp == null) return;

            // 接下来，执行 LKP 调查逻辑
            if (data.state() == AlertData.SUSPICIOUS) {
                mob.setData(ModAttachments.INVESTIGATE_LKP_DATA, InvestigateLkpData.DEFAULT);
                mob.getLookControl().setLookAt(lkp.x, lkp.y + mob.getEyeHeight(), lkp.z, 30.0F, 30.0F);
                mob.getNavigation().stop();
            } else if (data.state() == AlertData.SEARCHING) {
                investigateLkp(mob, lkp);
            } else if (data.state() == AlertData.FIGHTING && !data.canSeeAnyone()) {
                investigateLkp(mob, lkp);
            }
        }
    }

    private static void investigateLkp(Mob mob, Vec3 lkp) {
        InvestigateLkpData data = mob.getData(ModAttachments.INVESTIGATE_LKP_DATA);
        boolean isSearchingAround = data.isSearchingAround();
        boolean isMoving = data.isMoving();
        int stayTicks = data.stayTicks();
        Vec3 targetPos = null;
        if (data.targetPos().isPresent()) {
            targetPos = data.targetPos().get();
        }
        if (!isSearchingAround) {
            targetPos = lkp;
        }

        if (targetPos == null) {
            mob.setData(ModAttachments.INVESTIGATE_LKP_DATA, InvestigateLkpData.DEFAULT);
            return;
        }

        targetPos = getDownBlockPos(mob, targetPos);
        double distSqr = mob.distanceToSqr(targetPos);
        if (distSqr > 2.25 && !isSearchingAround) {
            mob.getLookControl().setLookAt(targetPos.x, targetPos.y + mob.getEyeHeight(), targetPos.z);
            mob.getNavigation().moveTo(targetPos.x, targetPos.y, targetPos.z, 1.0);

            if (mob instanceof EnderMan) {
                if ((distSqr > 64.0 || mob.getNavigation().isStuck()) && mob.getRandom().nextInt(100) == 0) {
                    tryTeleportNear(mob, targetPos);
                }
            }
            if (mob.getData(ModAttachments.ALERT_DATA).patienceTicks() <= 0) {
                isSearchingAround = true;
                isMoving = false;
            }
        } else {
            if (!isSearchingAround) {
                isSearchingAround = true;
                isMoving = false;
            }
            if (!isMoving) {
                mob.getNavigation().stop();
                if (stayTicks <= 0) {
                    stayTicks = 100 + mob.getRandom().nextInt(100); // 5 ~ 10s
                }
                if (--stayTicks <= 0) {
                    isMoving = true;
                    targetPos = pickNextSearchPoint(mob, lkp);
                }
            } else {
                mob.getNavigation().moveTo(targetPos.x, targetPos.y, targetPos.z, 1.0);
                if (mob.getNavigation().isDone()) {
                    isMoving = false;
                }
            }
//            if (!isMoving) {
//                stayTicks = 100 + mob.getRandom().nextInt(100); // 5 ~ 10s
//                isSearchingAround = true;
//                isMoving = false;
//            } else {
//                mob.getNavigation().stop();
//                stayTicks--;
//                if (stayTicks <= 0) {
//                    targetPos = pickNextSearchPoint(mob, lkp);
//                    mob.getNavigation().moveTo(targetPos.x, targetPos.y, targetPos.z, 1.0);
//                    isMoving = true;
//                }
//            }
        }
        mob.setData(ModAttachments.INVESTIGATE_LKP_DATA, new InvestigateLkpData(isSearchingAround, isMoving, stayTicks, Optional.of(targetPos)));
    }

    private static Vec3 pickNextSearchPoint(Mob mob, Vec3 lkp) {
        double radius = 4.0 + mob.getRandom().nextDouble() * 6.0;
        double angle = mob.getRandom().nextDouble() * Math.PI * 2;
        double newX = lkp.x + Math.cos(angle) * radius;
        double newZ = lkp.z + Math.sin(angle) * radius;
        return new Vec3(newX, lkp.y, newZ);
    }

    // 尝试传送
    private static boolean tryTeleportNear(Mob mob, Vec3 targetPos) {
        for (int i = 0; i < 10; i++) {
            double dx = targetPos.x + (mob.getRandom().nextDouble() - 0.5) * 8.0;
            double dy = targetPos.y + (mob.getRandom().nextInt(7) - 3);
            double dz = targetPos.z + (mob.getRandom().nextDouble() - 0.5) * 8.0;
            Vec3 pos = mob.position();
            if (mob.randomTeleport(dx, dy, dz, true)) {
                mob.level().gameEvent(GameEvent.TELEPORT, pos, GameEvent.Context.of(mob));
                if (!mob.isSilent()) {
                    mob.level().playSound(null, pos.x(), pos.y(), pos.z(), SoundEvents.ENDERMAN_TELEPORT, mob.getSoundSource(), 1.0F, 1.0F);
                    mob.playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F);
                }
                mob.getNavigation().stop();
                return true;
            }
        }
        return false;
    }

    private static Vec3 getDownBlockPos(Mob mob, Vec3 pos) {
        // 计算目标点与生物的高度差
        double heightDifference = pos.y - mob.getY();

        // 高度差足够小时，取目标点为等高点
        if (heightDifference > 0 && heightDifference <= 1.75) {
            return new Vec3(pos.x, mob.getY(), pos.z);
        }

        // 高度差足够大时，向下扫描
        BlockPos.MutableBlockPos mutable = BlockPos.containing(pos).mutable();
        int minHeight = mob.level().getMinBuildHeight();

        int scanDepth = 0;
        boolean foundFloorNearby = false;

        while (mutable.getY() >= minHeight && scanDepth < 4) {
            BlockState currentState = mob.level().getBlockState(mutable);

            // 现代原版判定：方块是否属于固体
            if (!currentState.isAir() && !currentState.getCollisionShape(mob.level(), mutable).isEmpty()) {
                foundFloorNearby = true;
                break;
            }

            mutable.move(0, -1, 0);
            scanDepth++;

            if (!mob.level().hasChunk(mutable.getX() >> 4, mutable.getZ() >> 4)) break;
        }

        if (foundFloorNearby) {
            // 如果往下几格内找到了方块，抬头
            return Vec3.atBottomCenterOf(mutable.above());
        } else {
            // 如果往下 4 格全是空气，平视
            return new Vec3(pos.x, mob.getY(), pos.z);
        }
    }
}
