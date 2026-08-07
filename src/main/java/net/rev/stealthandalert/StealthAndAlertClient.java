package net.rev.stealthandalert;

import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.rev.stealthandalert.client.animation.ClientAnimationHandler;
import net.rev.stealthandalert.entity.ModEntities;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL43;
import org.lwjgl.system.MemoryUtil;

// This class will not load on dedicated servers. Accessing client side code from here is safe.
@Mod(value = StealthAndAlert.MOD_ID, dist = Dist.CLIENT)
// You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
@EventBusSubscriber(modid = StealthAndAlert.MOD_ID, value = Dist.CLIENT)
public class StealthAndAlertClient {
    public StealthAndAlertClient(ModContainer container) {
        registerConfigScreen(container);
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            enableOpenGLDebugOutput();
            ClientAnimationHandler.initializePlayerAnimationFactory();
            EntityRenderers.register(ModEntities.PEBBLE.get(), ThrownItemRenderer::new);
        });
    }

    private static void enableOpenGLDebugOutput() {
        System.setProperty("org.lwjgl.util.Debug", "true");
        System.setProperty("veil.debug", "true");

        if (!GL.getCapabilities().OpenGL43) {
            StealthAndAlert.LOGGER.warn("OpenGL debug output is not supported on this platform.");
            return;
        }

        GL11.glEnable(GL43.GL_DEBUG_OUTPUT);
        GL11.glEnable(GL43.GL_DEBUG_OUTPUT_SYNCHRONOUS);
        GL43.glDebugMessageCallback((source, type, id, severity, length, message, userParam) -> {
            String msg = MemoryUtil.memUTF8(message, length);
            StealthAndAlert.LOGGER.error("OpenGL Debug [{}] [{}] [{}] {}", source, type, severity, msg);
        }, 0L);
    }

    private static void registerConfigScreen(ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }
}
