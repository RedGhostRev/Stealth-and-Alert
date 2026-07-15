package net.rev.stealthandalert.enchantment;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.enchantment.ConditionalEffect;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.rev.stealthandalert.StealthAndAlert;

import java.util.List;

public class ModEnchantmentEffects {
    public static final DeferredRegister<DataComponentType<?>> ENCHANTMENT_EFFECT_COMPONENTS =
            DeferredRegister.create(Registries.ENCHANTMENT_EFFECT_COMPONENT_TYPE, StealthAndAlert.MOD_ID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<List<ConditionalEffect<LevelBasedValue>>>> ADD_ASSASSINATION_MULTIPLIER =
            ENCHANTMENT_EFFECT_COMPONENTS.register("add_assassination_multiplier",
                    () -> DataComponentType.<List<ConditionalEffect<LevelBasedValue>>>builder()
                            .persistent(ConditionalEffect.codec(LevelBasedValue.CODEC, LootContextParamSets.ENCHANTED_ITEM).listOf())
                            .build());

    public static void register(IEventBus bus) {
        ENCHANTMENT_EFFECT_COMPONENTS.register(bus);
    }
}
