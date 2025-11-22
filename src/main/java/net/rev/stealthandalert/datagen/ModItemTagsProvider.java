package net.rev.stealthandalert.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredItem;
import net.rev.stealthandalert.StealthAndAlert;
import net.rev.stealthandalert.item.ModItems;
import net.rev.stealthandalert.item.custom.DaggerItem;
import net.rev.stealthandalert.util.ModTags;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModItemTagsProvider extends ItemTagsProvider implements IConditionBuilder {
    public ModItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTags, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags, StealthAndAlert.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(ModTags.Items.CAN_BACKSTAB)
                .addTag(ModTags.Items.DAGGERS);

        for (DeferredItem<DaggerItem> dagger : ModItems.DAGGER_LIST) {
            tag(ModTags.Items.DAGGERS).add(dagger.get());
        }

        tag(ItemTags.WEAPON_ENCHANTABLE)
                .addTag(ModTags.Items.DAGGERS);
        tag(ItemTags.SHARP_WEAPON_ENCHANTABLE)
                .addTag(ModTags.Items.DAGGERS);
        tag(ItemTags.SWORD_ENCHANTABLE)
                .addTag(ModTags.Items.DAGGERS);
        tag(ItemTags.DURABILITY_ENCHANTABLE)
                .addTag(ModTags.Items.DAGGERS);
        tag(ItemTags.FIRE_ASPECT_ENCHANTABLE)
                .addTag(ModTags.Items.DAGGERS);
        tag(ItemTags.VANISHING_ENCHANTABLE)
                .addTag(ModTags.Items.DAGGERS);
    }
}
