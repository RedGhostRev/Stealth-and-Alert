package net.rev.stealthandalert.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.rev.stealthandalert.StealthAndAlert;
import net.rev.stealthandalert.block.ModBlocks;
import net.rev.stealthandalert.datagen.LangKeys;

import java.util.function.Supplier;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MOD_TAB =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, StealthAndAlert.MOD_ID);

    public static final Supplier<CreativeModeTab> STEALTH_AND_ALERT_ITEM_TAB = CREATIVE_MOD_TAB.register("items_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.PEBBLE.get()))
                    .title(Component.translatable(LangKeys.STEALTH_AND_ALERT_ITEMS_TAB))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModItems.PEBBLE);
                        output.accept(ModItems.CLAMOR_BELL);
                        output.accept(ModItems.SHADOW_CRYSTAL);
                        output.accept(ModItems.SHADOW_CRYSTAL_SHARD);
                        output.accept(ModItems.SHADOW_BERRIES);
                    }).build());

    public static final Supplier<CreativeModeTab> STEALTH_AND_ALERT_BLOCK_TAB = CREATIVE_MOD_TAB.register("blocks_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModBlocks.SHADOW_CRYSTAL_ORE.get()))
                    .withTabsBefore(ResourceLocation.fromNamespaceAndPath(StealthAndAlert.MOD_ID, "items_tab"))
                    .title(Component.translatable(LangKeys.STEALTH_AND_ALERT_BLOCKS_TAB))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModBlocks.PEBBLE_BLOCK);
                        output.accept(ModBlocks.SHADOW_CRYSTAL_ORE);
                        output.accept(ModBlocks.DEEPSLATE_SHADOW_CRYSTAL_ORE);
                    }).build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MOD_TAB.register(eventBus);
    }
}
