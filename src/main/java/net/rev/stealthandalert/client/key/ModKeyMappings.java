package net.rev.stealthandalert.client.key;

import net.minecraft.client.KeyMapping;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.rev.stealthandalert.datagen.LangKeys;
import org.lwjgl.glfw.GLFW;

@OnlyIn(Dist.CLIENT)
public class ModKeyMappings {
    public static final KeyMapping CRAWL_KEY = new KeyMapping(
            LangKeys.CRAWL,
            GLFW.GLFW_KEY_C,
            LangKeys.CATEGORY
    );
}
