package net.rev.stealthandalert.effect;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.rev.stealthandalert.StealthAndAlert;
import net.rev.stealthandalert.attribute.ModAttributes;

public class ModEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS =
            DeferredRegister.create(Registries.MOB_EFFECT, StealthAndAlert.MOD_ID);

    public static final DeferredHolder<MobEffect, MobEffect> ETHEREAL_EFFECT = MOB_EFFECTS.register("ethereal",
            () -> new EtherealEffect(MobEffectCategory.BENEFICIAL, 0x64199a)
                    .addAttributeModifier(ModAttributes.VISIBILITY,
                            ResourceLocation.fromNamespaceAndPath(StealthAndAlert.MOD_ID, "ethereal"),
                            -0.2,
                            AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                    .addAttributeModifier(ModAttributes.SOUND_MULTIPLIER,
                            ResourceLocation.fromNamespaceAndPath(StealthAndAlert.MOD_ID, "ethereal"),
                            -0.05,
                            AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));

    public static void register(IEventBus eventBus) {
        MOB_EFFECTS.register(eventBus);
    }
}
