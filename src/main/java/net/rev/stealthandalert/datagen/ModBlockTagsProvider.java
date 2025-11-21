package net.rev.stealthandalert.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.rev.stealthandalert.StealthAndAlert;
import net.rev.stealthandalert.block.ModBlocks;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagsProvider extends BlockTagsProvider {
    public ModBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, StealthAndAlert.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(ModBlocks.PEBBLE_BLOCK.get())
                .add(ModBlocks.SHADOW_CRYSTAL_ORE.get())
                .add(ModBlocks.DEEPSLATE_SHADOW_CRYSTAL_ORE.get());

        tag(BlockTags.NEEDS_IRON_TOOL)
                .add(ModBlocks.SHADOW_CRYSTAL_ORE.get())
                .add(ModBlocks.DEEPSLATE_SHADOW_CRYSTAL_ORE.get());
    }
}
