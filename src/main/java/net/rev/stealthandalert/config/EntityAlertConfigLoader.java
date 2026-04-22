package net.rev.stealthandalert.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.neoforged.fml.loading.FMLPaths;
import net.rev.stealthandalert.StealthAndAlert;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class EntityAlertConfigLoader {
    private static final Map<EntityType<?>, EntityAlertSettings> CONFIG_MAP = new HashMap<>();
    private static EntityAlertSettings cachedDefault;

    public static EntityAlertSettings get(EntityType<?> type) {
        return CONFIG_MAP.getOrDefault(type, cachedDefault);
    }

    public static void load() {
        Path configDir = FMLPaths.CONFIGDIR.get().resolve("stealth_and_alert/entities");

        if (!Files.exists(configDir)) {
            try {
                Files.createDirectories(configDir);
            } catch (IOException e) {
                return;
            }
        }

        generateDefaultPresets(configDir);

        try (Stream<Path> paths = Files.walk(configDir)) {
            paths.filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".json"))
                    .forEach(path -> {
                        String entityName = path.getFileName().toString().replace(".json", "");
                        String namespace = path.getParent().getFileName().toString();

                        ResourceLocation location = ResourceLocation.fromNamespaceAndPath(namespace, entityName);
                        BuiltInRegistries.ENTITY_TYPE.getOptional(location).ifPresent(type -> {
                            EntityAlertSettings settings = readFile(path);
                            CONFIG_MAP.put(type, settings);
                        });
                    });
        } catch (IOException e) {

        }

        cachedDefault = EntityAlertSettings.fromConfig();
    }

    private static EntityAlertSettings readFile(Path path) {
        EntityAlertSettings global = EntityAlertSettings.fromConfig();

        try (Reader reader = Files.newBufferedReader(path)) {
            JsonElement json = JsonParser.parseReader(reader);
            EntityAlertSettings parsed = EntityAlertSettings.CODEC.parse(JsonOps.INSTANCE, json).result().orElse(global);

            return new EntityAlertSettings(
                    parsed.viewRange() == -1.0 ? global.viewRange() : parsed.viewRange(),
                    parsed.horizontalFov() == -1.0 ? global.horizontalFov() : parsed.horizontalFov(),
                    parsed.maxUpPitch() == -1.0 ? global.maxUpPitch() : parsed.maxUpPitch(),
                    parsed.maxDownPitch() == -1.0 ? global.maxDownPitch() : parsed.maxDownPitch(),
                    parsed.ignoreBaby(),
                    parsed.logicList(),
                    parsed.params()
            );
        } catch (IOException e) {
            return global;
        }
    }

    private static void generateDefaultPresets(Path configDir) {
        List<String> presets = List.of(
                "minecraft/spider.json",
                "minecraft/cave_spider.json",
                "minecraft/piglin.json",
                "minecraft/enderman.json",
                "minecraft/zombified_piglin.json",
                "minecraft/dolphin.json",
                "minecraft/wolf.json",
                "minecraft/polar_bear.json",
                "minecraft/panda.json",
                "minecraft/iron_golem.json"
        );

        for (String relativePath : presets) {
            Path targetPath = configDir.resolve(relativePath);
            if (!Files.exists(targetPath)) {
                try {
                    Files.createDirectories(targetPath.getParent());
                    String resourceLocation = "/data/" + StealthAndAlert.MOD_ID + "/presets/" + relativePath;
                    try (InputStream is = EntityAlertConfigLoader.class.getResourceAsStream(resourceLocation)) {
                        if (is != null) {
                            Files.copy(is, targetPath, StandardCopyOption.REPLACE_EXISTING);
                        }
                    }
                } catch (IOException e) {

                }
            }
        }
    }
}
