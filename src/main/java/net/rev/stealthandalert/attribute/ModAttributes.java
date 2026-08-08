package net.rev.stealthandalert.attribute;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.rev.stealthandalert.StealthAndAlert;
import net.rev.stealthandalert.datagen.LangKeys;

public class ModAttributes {
    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(Registries.ATTRIBUTE, StealthAndAlert.MOD_ID);

    public static final DeferredHolder<Attribute, Attribute> VISIBILITY = ATTRIBUTES.register("visibility",
            () -> new VisibilityAttribute(LangKeys.VISIBILITY, 1.0, 0.0, 1.0)
                    .setSyncable(true));

    public static final DeferredHolder<Attribute, Attribute> SOUND_MULTIPLIER = ATTRIBUTES.register("sound",
            () -> new SoundMultiplierAttribute(LangKeys.SOUND_MULTIPLIER, 1.0, 0.0, 64.0)
                    .setSyncable(true));

    public static final DeferredHolder<Attribute, Attribute> ASSASSINATION_DAMAGE = ATTRIBUTES.register("assassination_damage",
            () -> new AssassinationDamageAttribute(LangKeys.ASSASSINATION_DAMAGE, 0.0, 0.0, Integer.MAX_VALUE)
                    .setSyncable(true));

    public static void register(IEventBus eventBus) {
        ATTRIBUTES.register(eventBus);
    }
}
