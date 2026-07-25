package net.rev.stealthandalert.common.animation;

import net.minecraft.client.player.LocalPlayer;


public class AssassinationStateHandler {

    // 屏蔽玩家移动、跳跃、潜行等基本操作
    public static void disableInput(LocalPlayer localPlayer) {
        localPlayer.input.forwardImpulse = 0F;
        localPlayer.input.leftImpulse = 0F;
        localPlayer.input.shiftKeyDown = false;
        localPlayer.input.jumping = false;
        localPlayer.input.up = false;
        localPlayer.input.down = false;
    }

//    public static void enforceRotation(LivingEntity target, float yRot) {
//        if (target instanceof Mob mob) {
//            mob.setYRot(yRot);
//            mob.setYBodyRot(yRot);
//            mob.setYHeadRot(yRot);
//            mob.yRotO = yRot;
//            mob.yBodyRotO = yRot;
//            mob.yHeadRotO = yRot;
//            mob.getNavigation().stop();
//        }
//    }
}
