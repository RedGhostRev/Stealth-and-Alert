package net.rev.stealthandalert.item;

import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.rev.stealthandalert.StealthAndAlert;
import net.rev.stealthandalert.item.custom.DaggerItem;

import java.util.List;
import java.util.Set;

public class ModItems {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(StealthAndAlert.MOD_ID);

    public static final DeferredItem<Item> PEBBLE = ITEMS.register("pebble",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> CLAMOR_BELL = ITEMS.register("clamor_bell",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> SHADOW_CRYSTAL = ITEMS.register("shadow_crystal",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> SHADOW_CRYSTAL_SHARD = ITEMS.register("shadow_crystal_shard",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> SHADOW_BERRIES = ITEMS.register("shadow_berries",
            () -> new Item(new Item.Properties().food(ModFoodProperties.SHADOW_BERRIES)));

    public static final DeferredItem<DaggerItem> SHADOW_CRYSTAL_DAGGER = ITEMS.register("shadow_crystal_dagger",
            () -> new DaggerItem(ModTiers.SHADOW_CRYSTAL, new Item.Properties().attributes(
                    DaggerItem.createAttributes(ModTiers.SHADOW_CRYSTAL, 2F, -1.2F)
            )));

    public static final List<DeferredItem<DaggerItem>> DAGGER_LIST = List.of(
            SHADOW_CRYSTAL_DAGGER
    );

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
