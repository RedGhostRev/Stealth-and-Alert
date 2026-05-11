package net.rev.stealthandalert.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.rev.stealthandalert.StealthAndAlert;
import net.rev.stealthandalert.util.ModTags;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModEntityTypeTagsProvider extends EntityTypeTagsProvider {
    public ModEntityTypeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, provider, StealthAndAlert.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(ModTags.Entities.SEEKERS)
                .addTag(ModTags.Entities.CONDITIONAL_SEEKERS)
                .addTag(EntityTypeTags.ZOMBIES)
                .remove(EntityType.ZOMBIE_HORSE)
                .addTag(EntityTypeTags.SKELETONS)
                .remove(EntityType.SKELETON_HORSE)
                .addTag(EntityTypeTags.ILLAGER)
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
                .add(EntityType.PANDA)
                .add(EntityType.IRON_GOLEM);

        tag(ModTags.Entities.DETECTABLE)
                .add(EntityType.PLAYER);
    }
}
