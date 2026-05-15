package net.rev.stealthandalert;

import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.rev.stealthandalert.attachment.ModAttachments;
import net.rev.stealthandalert.block.ModBlocks;
import net.rev.stealthandalert.config.ClientConfigs;
import net.rev.stealthandalert.config.CommonConfigs;
import net.rev.stealthandalert.config.EntityAlertConfigLoader;
import net.rev.stealthandalert.entity.ModEntities;
import net.rev.stealthandalert.event.StealthSoundEventHandler;
import net.rev.stealthandalert.item.ModCreativeModeTabs;
import net.rev.stealthandalert.item.ModItems;
import net.rev.stealthandalert.sound.ModSounds;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(StealthAndAlert.MOD_ID)
public class StealthAndAlert {
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "stealth_and_alert";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public StealthAndAlert(IEventBus modEventBus, ModContainer modContainer) {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register ourselves for server and other game events we are interested in.
        // Note that this is necessary if and only if we want *this* class (ExampleMod) to respond directly to events.
        // Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like onServerStarting() below.
        NeoForge.EVENT_BUS.register(this);
        NeoForge.EVENT_BUS.register(StealthSoundEventHandler.class);

        ModCreativeModeTabs.register(modEventBus);

        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModEntities.register(modEventBus);
        ModSounds.register(modEventBus);

        ModAttachments.register(modEventBus);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, CommonConfigs.SPEC);
        modContainer.registerConfig(ModConfig.Type.CLIENT, ClientConfigs.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(EntityAlertConfigLoader::load);
    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
    }
}
