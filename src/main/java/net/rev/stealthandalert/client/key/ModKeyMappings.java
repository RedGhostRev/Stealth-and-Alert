package net.rev.stealthandalert.client.key;

import com.mojang.blaze3d.platform.InputConstants;
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

    public static final KeyMapping ASSASSINATE_KEY = new KeyMapping(
            LangKeys.ASSASSINATE,
            GLFW.GLFW_KEY_F,
            LangKeys.CATEGORY
    );

    public static final KeyMapping MARK_KEY = new KeyMapping(
            LangKeys.MARK,
            InputConstants.Type.MOUSE,
            GLFW.GLFW_MOUSE_BUTTON_LEFT,
            LangKeys.CATEGORY
    );
}
