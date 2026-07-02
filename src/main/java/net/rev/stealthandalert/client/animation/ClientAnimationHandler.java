package net.rev.stealthandalert.client.animation;

import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.api.layered.modifier.MirrorModifier;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationFactory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.HumanoidArm;
import net.rev.stealthandalert.StealthAndAlert;
import net.rev.stealthandalert.attachment.ModAttachments;
import net.rev.stealthandalert.util.AssassinationHandler;

public class ClientAnimationHandler {
    public static final ResourceLocation ANIM_LAYER = ResourceLocation.fromNamespaceAndPath(StealthAndAlert.MOD_ID, "assassinate_layer");

    public static void initializePlayerAnimationFactory() {
        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(
                ANIM_LAYER,  // 图层标签
                1000,           // 优先级
                (player) -> {
                    // 为每个生成的玩家实体自动创建
                    ModifierLayer<IAnimation> layer = new ModifierLayer<>();
                    layer.addModifier(new HandleItemFixModifier(), 0); // 添加修正手持物品位置的修饰器
                    layer.addModifierLast(new MirrorModifier() {
                        @Override
                        public boolean isEnabled() {
                            AssassinationHandler.AssassinateHand hand = player.getData(ModAttachments.ASSASSINATION_DATA).hand();
                            boolean isLeftHand = hand == AssassinationHandler.AssassinateHand.LEFT_HAND;
                            boolean isMainHandLeft = player.getMainArm() == HumanoidArm.LEFT;
                            return isLeftHand ^ isMainHandLeft;
                        }
                    });
                    return layer;
                }
        );
    }
}
