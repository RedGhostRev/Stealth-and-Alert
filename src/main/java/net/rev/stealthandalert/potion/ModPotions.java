package net.rev.stealthandalert.potion;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.rev.stealthandalert.StealthAndAlert;
import net.rev.stealthandalert.effect.ModEffects;

public class ModPotions {
    public static final DeferredRegister<Potion> POTIONS =
            DeferredRegister.create(BuiltInRegistries.POTION, StealthAndAlert.MOD_ID);

    public static final DeferredHolder<Potion, Potion> ETHEREAL_POTION = POTIONS.register(StealthAndAlert.MOD_ID + ".ethereal",
            () -> new Potion(StealthAndAlert.MOD_ID + ".ethereal", new MobEffectInstance(ModEffects.ETHEREAL_EFFECT, 3600, 0, true, true)));

    public static final DeferredHolder<Potion, Potion> LONG_ETHEREAL_POTION = POTIONS.register(StealthAndAlert.MOD_ID + ".long_ethereal",
            () -> new Potion(StealthAndAlert.MOD_ID + ".ethereal", new MobEffectInstance(ModEffects.ETHEREAL_EFFECT, 9600, 0, true, true)));

    public static final DeferredHolder<Potion, Potion> STRONG_ETHEREAL_POTION = POTIONS.register(StealthAndAlert.MOD_ID + ".strong_ethereal",
            () -> new Potion(StealthAndAlert.MOD_ID + ".ethereal", new MobEffectInstance(ModEffects.ETHEREAL_EFFECT, 1800, 1, true, true)));

    public static void register(IEventBus eventBus) {
        POTIONS.register(eventBus);
    }
}
