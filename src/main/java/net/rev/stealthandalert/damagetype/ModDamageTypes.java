package net.rev.stealthandalert.damagetype;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageEffects;
import net.minecraft.world.damagesource.DamageScaling;
import net.minecraft.world.damagesource.DamageType;
import net.rev.stealthandalert.StealthAndAlert;

public class ModDamageTypes {
    public static final ResourceKey<DamageType> ASSASSINATION = ResourceKey.create(
            Registries.DAMAGE_TYPE,
            ResourceLocation.fromNamespaceAndPath(StealthAndAlert.MOD_ID, "assassination")
    );

    public static void bootstrap(BootstrapContext<DamageType> context) {
        context.register(ASSASSINATION, new DamageType(
                "assassination",
                DamageScaling.NEVER,
                0F,
                DamageEffects.HURT
        ));
    }
}
