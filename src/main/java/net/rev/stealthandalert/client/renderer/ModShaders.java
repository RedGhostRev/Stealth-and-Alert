package net.rev.stealthandalert.client.renderer;

import foundry.veil.api.client.render.VeilRenderSystem;
import foundry.veil.api.client.render.shader.program.ShaderProgram;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.rev.stealthandalert.StealthAndAlert;

/**
 * Veil 着色器管理。
 * 着色器文件位于 assets/stealth_and_alert/pinwheel/shaders/program/，
 * 使用 VeilRenderSystem.setShader(ResourceLocation) 标准方式加载。
 */
@OnlyIn(Dist.CLIENT)
public class ModShaders {

    /** frustum 着色器程序 */
    public static ShaderProgram frustumShader;

    private static boolean initialized = false;

    private static final ResourceLocation FRUSTUM_LOC =
            ResourceLocation.fromNamespaceAndPath(StealthAndAlert.MOD_ID, "frustum");

    private ModShaders() {
    }

    /** 惰性初始化：通过 ShaderManager 获取着色器实例（可重复调用） */
    public static void ensureInit() {
        if (initialized) return;
        initialized = true;
        try {
            var manager = VeilRenderSystem.renderer().getShaderManager();
            frustumShader = manager != null ? manager.getShader(FRUSTUM_LOC) : null;
            if (frustumShader == null || !frustumShader.isValid()) {
                StealthAndAlert.LOGGER.error("[ModShaders] Failed to load frustum shader at {}", FRUSTUM_LOC);
                frustumShader = null;
            } else {
                StealthAndAlert.LOGGER.info("[ModShaders] Loaded frustum shader {}", FRUSTUM_LOC);
            }
        } catch (Exception e) {
            StealthAndAlert.LOGGER.error("[ModShaders] Error loading frustum shader", e);
            frustumShader = null;
        }
    }

    /** 资源重载后需要重新获取 */
    public static void reset() {
        initialized = false;
        frustumShader = null;
    }
}
