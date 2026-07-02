package net.rev.stealthandalert.client.animation;

import dev.kosmx.playerAnim.api.TransformType;
import dev.kosmx.playerAnim.api.layered.modifier.AbstractModifier;
import dev.kosmx.playerAnim.core.util.Vec3f;
import org.jetbrains.annotations.NotNull;

/**
 * 用于解决游戏中与 Blockbench 中动画播放时手持物品旋转情况不一的问题
 */
public class HandleItemFixModifier extends AbstractModifier {

    @Override
    public @NotNull Vec3f get3DTransform(@NotNull String modelName, @NotNull TransformType type, float tickDelta, @NotNull Vec3f value0) {
        Vec3f rawRot = super.get3DTransform(modelName, type, tickDelta, value0);

        if (type == TransformType.ROTATION && ("rightItem".equals(modelName) || "leftItem".equals(modelName))) {
            float finalX = -rawRot.getX();
            float finalY = -rawRot.getZ();
            float finalZ = -rawRot.getY();
            return new Vec3f(finalX, finalY, finalZ);
        }

        if (type == TransformType.POSITION && ("rightItem".equals(modelName) || "leftItem".equals(modelName))) {
            Vec3f rawPos = super.get3DTransform(modelName, type, tickDelta, value0);
            return new Vec3f(-rawPos.getX(), -rawPos.getZ(), -rawPos.getY());
        }

        return rawRot;
    }
}
