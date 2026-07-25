package net.rev.stealthandalert.compat.ironsspellbooks;

import io.redspace.ironsspellbooks.capabilities.magic.SummonManager;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.rev.stealthandalert.StealthAndAlert;
import net.rev.stealthandalert.attribute.ModAttributes;
import net.rev.stealthandalert.datagen.ModEntityTypeTagsProvider;
import net.rev.stealthandalert.util.ModTags;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.UUID;

public class IronsSpellbooksCompat {
    public static void init() {
        MobEffectRegistry.TRUE_INVISIBILITY.get().addAttributeModifier(
                ModAttributes.VISIBILITY,
                ResourceLocation.fromNamespaceAndPath(StealthAndAlert.MOD_ID, "true_visibility"),
                -1.0,
                AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
        );
        MobEffectRegistry.GUIDING_BOLT.get().addAttributeModifier(
                ModAttributes.VISIBILITY,
                ResourceLocation.fromNamespaceAndPath(StealthAndAlert.MOD_ID, "guided"),
                1.0,
                AttributeModifier.Operation.ADD_VALUE
        );
    }

    @Nullable
    public static Entity getOwner(Entity entity) {
        return SummonManager.getOwner(entity);
    }

    @SuppressWarnings("unchecked")
    @Nullable
    public static UUID getOwnerUuid(Entity entity) {
        try {
            Field field = SummonManager.class.getDeclaredField("summonToOwner");
            field.setAccessible(true);
            Map<UUID, UUID> summonToOwner = (Map<UUID, UUID>) field.get(SummonManager.INSTANCE);
            if (summonToOwner != null && summonToOwner.containsKey(entity.getUUID())) {
                return summonToOwner.get(entity.getUUID());
            }
        } catch (Exception e) {
            StealthAndAlert.LOGGER.error("Could not get summonToOwner from SummonManager", e);
        }
        return null;
    }

    public static void addEntityTags(ModEntityTypeTagsProvider provider) {
        provider.tag(ModTags.Entities.SEEKERS)
                .addOptional(EntityRegistry.NECROMANCER.getId())
                .addOptional(EntityRegistry.ARCHEVOKER.getId())
                .addOptional(EntityRegistry.ICE_SPIDER.getId())
                .addOptional(EntityRegistry.CATACOMBS_ZOMBIE.getId())
                .addOptional(EntityRegistry.MAGEHUNTER_VINDICATOR.getId())
                .addOptional(EntityRegistry.KEEPER.getId());
        provider.tag(ModTags.Entities.CONDITIONAL_SEEKERS)
                .addOptional(EntityRegistry.PRIEST.getId())
                .addOptional(EntityRegistry.APOTHECARIST.getId())
                .addOptional(EntityRegistry.CRYOMANCER.getId())
                .addOptional(EntityRegistry.PYROMANCER.getId())
                .addOptional(EntityRegistry.SUMMONED_POLAR_BEAR.getId())
                .addOptional(EntityRegistry.SUMMONED_ZOMBIE.getId())
                .addOptional(EntityRegistry.SUMMONED_SKELETON.getId());
        provider.tag(ModTags.Entities.CAN_BE_ASSASSINATED)
                .remove(EntityRegistry.PRIEST.getId());
    }
}
