package net.rev.stealthandalert.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredItem;
import net.rev.stealthandalert.StealthAndAlert;
import net.rev.stealthandalert.compat.CompatHandler;
import net.rev.stealthandalert.item.ModItems;
import net.rev.stealthandalert.item.custom.DaggerItem;
import net.rev.stealthandalert.util.ModTags;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class ModItemTagsProvider extends ItemTagsProvider implements IConditionBuilder {
    public ModItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTags, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags, StealthAndAlert.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        for (Consumer<ModItemTagsProvider> consumer : CompatHandler.ITEM_TAGS) {
            consumer.accept(this);
        }

        tag(ModTags.Items.CAN_ASSASSINATE)
                .addTag(ModTags.Items.CAN_ASSASSINATE_DAGGERS)
                .addTag(ModTags.Items.CAN_ASSASSINATE_TRIDENTS)
                .addTag(ModTags.Items.CAN_ASSASSINATE_MACES)
                .addTag(ModTags.Items.CAN_ASSASSINATE_SWORDS);
        tag(ModTags.Items.CAN_ASSASSINATE_DAGGERS)
                .addTag(ModTags.Items.DAGGERS);
        tag(ModTags.Items.CAN_ASSASSINATE_TRIDENTS)
                .add(Items.TRIDENT);
        tag(ModTags.Items.CAN_ASSASSINATE_MACES)
                .add(Items.MACE);
        tag(ModTags.Items.CAN_ASSASSINATE_SWORDS)
                .addTag(ItemTags.SWORDS);

        for (DeferredItem<DaggerItem> dagger : ModItems.DAGGER_LIST) {
            tag(ModTags.Items.DAGGERS).add(dagger.get());
        }

        tag(ItemTags.SHARP_WEAPON_ENCHANTABLE)
                .addTag(ModTags.Items.DAGGERS);
        tag(ItemTags.SWORD_ENCHANTABLE)
                .addTag(ModTags.Items.DAGGERS);
        tag(ItemTags.DURABILITY_ENCHANTABLE)
                .addTag(ModTags.Items.DAGGERS);
    }

    @Override
    public @NotNull IntrinsicTagAppender<Item> tag(@NotNull TagKey<Item> tag) {
        return super.tag(tag);
    }
}
