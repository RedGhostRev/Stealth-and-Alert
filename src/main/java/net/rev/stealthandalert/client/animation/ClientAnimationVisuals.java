package net.rev.stealthandalert.client.animation;

import dev.kosmx.playerAnim.api.firstPerson.FirstPersonConfiguration;
import dev.kosmx.playerAnim.api.firstPerson.FirstPersonMode;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.api.layered.modifier.AbstractFadeModifier;
import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import dev.kosmx.playerAnim.core.util.Ease;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.rev.stealthandalert.common.animation.IAnimationVisuals;

public class ClientAnimationVisuals implements IAnimationVisuals {

    @SuppressWarnings("unchecked")
    @Override
    public void playPlayerAnimation(Player player, ResourceLocation animRL, long elapsedTicks) {
        if (!(player instanceof AbstractClientPlayer clientPlayer)) return;

        var animationStack = PlayerAnimationAccess.getPlayerAssociatedData(clientPlayer);
        if (animationStack == null) return;

        ModifierLayer<IAnimation> layer = (ModifierLayer<IAnimation>) animationStack.get(ClientAnimationHandler.ANIM_LAYER);
        if (layer == null) return;
        var rawAnim = PlayerAnimationRegistry.getAnimation(animRL);
        if (rawAnim instanceof KeyframeAnimation keyframeAnimation) {
            KeyframeAnimationPlayer animationPlayer = new KeyframeAnimationPlayer(keyframeAnimation);
            for (long i = 0; i < elapsedTicks; i++) {
                animationPlayer.tick();
            }

            animationPlayer.setFirstPersonMode(FirstPersonMode.THIRD_PERSON_MODEL);
            animationPlayer.setFirstPersonConfiguration(new FirstPersonConfiguration(true, true, true, true));

            layer.replaceAnimationWithFade(
                    AbstractFadeModifier.standardFadeIn(2, Ease.INOUTSINE),
                    animationPlayer,
                    true
            );
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void stopPlayerAnimation(Player player, ResourceLocation animRL) {
        if (!(player instanceof AbstractClientPlayer clientPlayer)) return;
        var animationStack = PlayerAnimationAccess.getPlayerAssociatedData(clientPlayer);
        if (animationStack == null) return;

        ModifierLayer<IAnimation> layer = (ModifierLayer<IAnimation>) animationStack.get(ClientAnimationHandler.ANIM_LAYER);
        if (layer == null) return;
        layer.replaceAnimationWithFade(AbstractFadeModifier.standardFadeIn(4, Ease.INOUTSINE), null, false);
    }
}
