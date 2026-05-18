package net.rev.stealthandalert.util;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.rev.stealthandalert.StealthAndAlert;

public class ModTags {
    public static class Blocks {
        public static final TagKey<Block> NEEDS_SHADOW_CRYSTAL_TOOL = createTag("needs_shadow_crystal_tool");
        public static final TagKey<Block> INCORRECT_FOR_SHADOW_CRYSTAL_TOOL = createTag("incorrect_for_shadow_crystal_tool");
        public static final TagKey<Block> CAN_COVER = createTag("can_cover");

        private static TagKey<Block> createTag(String name) {
            return BlockTags.create(ResourceLocation.fromNamespaceAndPath(StealthAndAlert.MOD_ID, name));
        }
    }

    public static class Items {
        public static final TagKey<Item> CAN_BACKSTAB = createTag("can_backstab");
        public static final TagKey<Item> CAN_ASSASSINATE = createTag("can_assassinate");
        public static final TagKey<Item> DAGGERS = createTag("daggers");

        private static TagKey<Item> createTag(String name) {
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath(StealthAndAlert.MOD_ID, name));
        }
    }

    public static class Entities {
        public static final TagKey<EntityType<?>> SEEKERS = createTag("seekers");
        public static final TagKey<EntityType<?>> CONDITIONAL_SEEKERS = createTag("conditional_seekers");
        public static final TagKey<EntityType<?>> DETECTABLE = createTag("detectable");
    }

    private static TagKey<EntityType<?>> createTag(String name) {
        return TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(StealthAndAlert.MOD_ID, name));
    }
}
