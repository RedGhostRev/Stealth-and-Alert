package net.rev.stealthandalert.common.animation;

import net.rev.stealthandalert.StealthAndAlert;

public class AnimationProxySelector {
    private static IAnimationVisuals instance;

    public static IAnimationVisuals get() {
        if (instance == null) {
            instance = createProxy();
        }
        return instance;
    }

    private static IAnimationVisuals createProxy() {
        try {
            Class.forName("net.minecraft.client.Minecraft");
            return (IAnimationVisuals) Class.forName("net.rev.stealthandalert.client.animation.ClientAnimationVisuals")
                    .getDeclaredConstructor()
                    .newInstance();
        } catch (ClassNotFoundException e) {
            return new ServerAnimationVisuals();
        } catch (Exception e) {
            StealthAndAlert.LOGGER.error("Failed to create AnimationVisuals proxy instance", e);
            throw new RuntimeException("Failed to create AnimationVisuals proxy instance", e);
        }
    }
}
