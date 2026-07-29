package net.rev.stealthandalert.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforgespi.locating.IModFile;
import net.rev.stealthandalert.StealthAndAlert;
import org.slf4j.Logger;

import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class EntityAlertConditionConfigLoader {
    private static final Logger LOGGER = StealthAndAlert.LOGGER;
    private static final Map<EntityType<?>, EntityAlertConditionSettings> CONFIG_MAP = new HashMap<>();

    public static final EntityAlertConditionSettings DEFAULT_SETTINGS = new EntityAlertConditionSettings(
            new EntityAlertConditionSettings.DetectionSettings(
                    false, -1, -1, -1, -1, -1, -1, -1, -1),
            new EntityAlertConditionSettings.AssassinationSettings(-1.0),
            Map.of()
    );

    public static EntityAlertConditionSettings get(EntityType<?> type) {
        return CONFIG_MAP.getOrDefault(type, DEFAULT_SETTINGS);
    }

    public static void load(boolean generatePresets, boolean forceOverride) {
        CONFIG_MAP.clear();
        Path rootDir = FMLPaths.CONFIGDIR.get().resolve("stealth_and_alert/entities");
        if (!Files.exists(rootDir)) {
            try {
                Files.createDirectories(rootDir);
            } catch (Exception e) {
                LOGGER.error("无法创建配置目录", e);
                return;
            }
        }

        // 根据参数决定是否调用预设生成逻辑
        if (generatePresets) {
            generateDefaultPresets(rootDir, forceOverride);
        }

        // 遍历加载
        try (Stream<Path> paths = Files.walk(rootDir)) {
            paths.filter(p -> p.toString().endsWith(".json"))
                    .forEach(EntityAlertConditionConfigLoader::loadSingleFile);
        } catch (Exception e) {
            LOGGER.error("扫描配置文件目录失败", e);
        }
    }

    private static void generateDefaultPresets(Path configDir, boolean forceOverride) {
        for (var modFileInfo : ModList.get().getModFiles()) {
            IModFile modFile = modFileInfo.getFile();
            Path presetsRoot = modFile.findResource("data", "stealth_and_alert", "presets", "entities");

            if (!Files.exists(presetsRoot)) continue;

            try (Stream<Path> paths = Files.walk(presetsRoot)) {
                paths.filter(p -> p.toString().endsWith(".json")).forEach(sourcePath -> {
                    try {
                        Path relativePath = presetsRoot.relativize(sourcePath);
                        Path targetPath = configDir.resolve(relativePath.toString());
                        String namespace = relativePath.getParent().toString();
                        if (!ModList.get().isLoaded(namespace)) return;

                        // 如果是强制覆盖，或者目标文件不存在，则执行复制
                        if (forceOverride || !Files.exists(targetPath)) {
                            Files.createDirectories(targetPath.getParent());
                            // StandardCopyOption.REPLACE_EXISTING 允许覆盖
                            Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
                            LOGGER.info("已从模组 {} 释放/覆盖默认预设: {}", modFileInfo.getMods().getFirst().getModId(), relativePath);
                        }
                    } catch (Exception e) {
                        LOGGER.error("释放预设文件异常: {}", sourcePath, e);
                    }
                });
            } catch (Exception e) {
                LOGGER.error("扫描模组预设目录失败", e);
            }
        }
    }

    private static void loadSingleFile(Path path) {
        try {
            // 获取相对路径
            Path relativePath = FMLPaths.CONFIGDIR.get().resolve("stealth_and_alert/entities").relativize(path);
            // 路经检验
            if (relativePath.getNameCount() < 2) {
                LOGGER.warn("跳过无效配置文件（缺少命名空间文件夹）：{}", relativePath);
                return;
            }
            // 提取命名空间和 ID
            String namespace = relativePath.getParent().toString();
            String entityId = relativePath.getFileName().toString().replace(".json", "");

            ResourceLocation rl = ResourceLocation.fromNamespaceAndPath(namespace, entityId);
            if (!BuiltInRegistries.ENTITY_TYPE.containsKey(rl)) {
                LOGGER.warn("配置文件指定了不存在的实体类型：{}", rl);
                return;
            }
            EntityType<?> type = BuiltInRegistries.ENTITY_TYPE.get(rl);
            try (Reader reader = Files.newBufferedReader(path)) {
                JsonElement je = JsonParser.parseReader(reader);
                EntityAlertConditionSettings.CODEC.parse(JsonOps.INSTANCE, je)
                        .resultOrPartial(err -> StealthAndAlert.LOGGER.error("解析JSON失败 [{}]: {}", rl, err))
                        .ifPresent(settings -> {
                            CONFIG_MAP.put(type, settings);
                            LOGGER.debug("加载 {} 成功", relativePath);
                        });
            }
        } catch (Exception e) {
            LOGGER.error("处理配置文件异常：{}", path, e);
        }
    }
}
