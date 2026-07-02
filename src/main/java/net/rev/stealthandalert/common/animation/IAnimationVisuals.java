package net.rev.stealthandalert.common.animation;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public interface IAnimationVisuals {
    IAnimationVisuals INSTANCE = AnimationProxySelector.get();

    void playPlayerAnimation(Player player, ResourceLocation animRL, long elapsedTicks);

    void stopPlayerAnimation(Player player, ResourceLocation animRL);

//    void spawnLocalParticle(ResourceLocation particleRL, double x, double y, double z,
//                            double xSpeed, double ySpeed, double zSpeed);
}
