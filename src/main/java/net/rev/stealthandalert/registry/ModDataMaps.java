package net.rev.stealthandalert.registry;

import com.mojang.serialization.Codec;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.datamaps.DataMapType;
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent;
import net.rev.stealthandalert.StealthAndAlert;

@EventBusSubscriber(modid = StealthAndAlert.MOD_ID)
public class ModDataMaps {
    public static final DataMapType<Item, Double> ARMOR_VISIBILITY_MODIFIER = DataMapType.builder(
            ResourceLocation.fromNamespaceAndPath(StealthAndAlert.MOD_ID, "armor_visibility_modifier"),
            Registries.ITEM,
            Codec.DOUBLE
    ).synced(Codec.DOUBLE, true).build();

    public static final DataMapType<Item, Double> CURIOS_ASSASSINATION_MODIFIER = DataMapType.builder(
            ResourceLocation.fromNamespaceAndPath(StealthAndAlert.MOD_ID, "curios_assassination_modifier"),
            Registries.ITEM,
            Codec.DOUBLE
    ).synced(Codec.DOUBLE, true).build();

    @SubscribeEvent
    public static void registerDataMapTypes(RegisterDataMapTypesEvent event) {
        event.register(ARMOR_VISIBILITY_MODIFIER);
        event.register(CURIOS_ASSASSINATION_MODIFIER);
    }
}
