package net.rev.stealthandalert.enchantment;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.rev.stealthandalert.StealthAndAlert;

public class ModEnchantments {
    public static ResourceKey<Enchantment> VITAL_PIERCE = ResourceKey.create(Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(StealthAndAlert.MOD_ID, "vital_pierce"));

    public static void bootstrap(BootstrapContext<Enchantment> context) {
        // HolderGetter<Enchantment> enchantments = context.lookup(Registries.ENCHANTMENT);
        HolderGetter<Item> items = context.lookup(Registries.ITEM);
        HolderSet.Named<Item> set = items.getOrThrow(ItemTags.WEAPON_ENCHANTABLE);

        register(context, VITAL_PIERCE, Enchantment.enchantment(Enchantment.definition(
                        set,
                        set,
                        1,
                        5,
                        Enchantment.dynamicCost(10, 11),
                        Enchantment.dynamicCost(25, 11),
                        2,
                        EquipmentSlotGroup.MAINHAND))
                .withEffect(
                        ModEnchantmentEffects.ADD_ASSASSINATION_MULTIPLIER.get(),
                        LevelBasedValue.perLevel(0.2F, 0.2F)));
    }

    private static void register(BootstrapContext<Enchantment> registry, ResourceKey<Enchantment> key,
                                 Enchantment.Builder builder) {
        registry.register(key, builder.build(key.location()));
    }
}
