package net.rev.stealthandalert.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.rev.stealthandalert.StealthAndAlert;

public class ModTags {
    public static class Blocks {

        private static TagKey<Block> createTag(String name) {
            return BlockTags.create(ResourceLocation.fromNamespaceAndPath(StealthAndAlert.MOD_ID, name));
        }
    }

    public static class Items {
        public static final TagKey<Item> CAN_BACKSTAB = createTag("can_backstab");

        private static TagKey<Item> createTag(String name) {
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath(StealthAndAlert.MOD_ID, name));
        }
    }
}
