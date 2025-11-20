package net.rev.stealthandalert.item;

import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.rev.stealthandalert.StealthAndAlert;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(StealthAndAlert.MOD_ID);

    public static final DeferredItem<Item> CLAMOR_BELL = ITEMS.register("clamor_bell",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> PEBBLE = ITEMS.register("pebble",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> SHADOW_CRYSTAL = ITEMS.register("shadow_crystal",
            () -> new Item(new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
