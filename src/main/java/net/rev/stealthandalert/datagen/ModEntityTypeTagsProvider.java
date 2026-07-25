package net.rev.stealthandalert.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.rev.stealthandalert.StealthAndAlert;
import net.rev.stealthandalert.compat.CompatHandler;
import net.rev.stealthandalert.compat.dummmmmmy.DummmmmmyCompat;
import net.rev.stealthandalert.compat.ironsspellbooks.IronsSpellbooksCompat;
import net.rev.stealthandalert.util.ModTags;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModEntityTypeTagsProvider extends EntityTypeTagsProvider {
    public ModEntityTypeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, provider, StealthAndAlert.MOD_ID, existingFileHelper);
    }

    @Override
    public @NotNull IntrinsicTagAppender<EntityType<?>> tag(@NotNull TagKey<EntityType<?>> tag) {
        return super.tag(tag);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        tag(ModTags.Entities.SEEKERS)
                .addTag(ModTags.Entities.CONDITIONAL_SEEKERS)
                .addTag(EntityTypeTags.ZOMBIES)
                .remove(EntityType.ZOMBIE_HORSE)
                .addTag(EntityTypeTags.SKELETONS)
                .remove(EntityType.SKELETON_HORSE)
                .addTag(EntityTypeTags.ILLAGER)
                .add(EntityType.RAVAGER)
                .add(EntityType.CREEPER)
                .add(EntityType.SLIME)
                .add(EntityType.MAGMA_CUBE)
                .add(EntityType.WITCH)
                .add(EntityType.SILVERFISH)
                .add(EntityType.PIGLIN_BRUTE)
                .add(EntityType.HOGLIN)
                .add(EntityType.BLAZE);
        // 带有条件的生物，触发特定条件后，才会执行警戒
        tag(ModTags.Entities.CONDITIONAL_SEEKERS)
                .add(EntityType.SPIDER)
                .add(EntityType.CAVE_SPIDER)
                .add(EntityType.PIGLIN)
                .add(EntityType.ENDERMAN)
                .add(EntityType.ZOMBIFIED_PIGLIN)
                .add(EntityType.DOLPHIN)
                .add(EntityType.WOLF)
                .add(EntityType.POLAR_BEAR)
                .add(EntityType.IRON_GOLEM);

        if (CompatHandler.HAS_IRONS_SPELLBOOKS) {
            IronsSpellbooksCompat.addEntityTags(this);
        }
        if (CompatHandler.HAS_DUMMMMMMY) {
            DummmmmmyCompat.addEntityTags(this);
        }

        // 受保护的生物，受击后产生仇恨记忆
        tag(ModTags.Entities.PROTECTED)
                .add(EntityType.VILLAGER);

        tag(ModTags.Entities.DETECTABLE)
                .add(EntityType.PLAYER);

        for (EntityType<?> entityType : BuiltInRegistries.ENTITY_TYPE) {
            ResourceLocation registryName = BuiltInRegistries.ENTITY_TYPE.getKey(entityType);
            if (!registryName.getNamespace().equals("minecraft")) continue;
            if (entityType == EntityType.WANDERING_TRADER) continue;
            MobCategory category = entityType.getCategory();
            if (category == MobCategory.CREATURE || category == MobCategory.WATER_CREATURE
                    || category == MobCategory.UNDERGROUND_WATER_CREATURE || category == MobCategory.AMBIENT
                    || category == MobCategory.WATER_AMBIENT || category == MobCategory.AXOLOTLS) {
                tag(ModTags.Entities.ANIMALS).add(entityType);
            }
        }

        // 能被刺杀的生物
        // 将铁傀儡排除在外
        tag(ModTags.Entities.CAN_BE_ASSASSINATED)
                .addTag(ModTags.Entities.SEEKERS)
                .remove(EntityType.IRON_GOLEM)
                .addTag(ModTags.Entities.ANIMALS)
                .add(EntityType.VILLAGER)
                .add(EntityType.WANDERING_TRADER)
                .add(EntityType.PLAYER);
    }
}
