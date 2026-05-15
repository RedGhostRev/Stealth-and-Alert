package net.rev.stealthandalert.entity;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.rev.stealthandalert.StealthAndAlert;
import net.rev.stealthandalert.entity.custom.PebbleProjectileEntity;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, StealthAndAlert.MOD_ID);

    public static final DeferredHolder<EntityType<?>, EntityType<PebbleProjectileEntity>> PEBBLE =
            ENTITY_TYPES.register("pebble", () -> EntityType.Builder.<PebbleProjectileEntity>of(PebbleProjectileEntity::new, MobCategory.MISC)
                    .sized(0.25F, 0.25F).build("pebble"));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
