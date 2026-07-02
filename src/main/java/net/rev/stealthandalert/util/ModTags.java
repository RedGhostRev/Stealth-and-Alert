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
        public static final TagKey<Item> CAN_ASSASSINATE_DAGGERS = createTag("can_assassinate/daggers");
        public static final TagKey<Item> CAN_ASSASSINATE_TRIDENTS = createTag("can_assassinate/tridents");
        public static final TagKey<Item> CAN_ASSASSINATE_MACES = createTag("can_assassinate/maces");
        public static final TagKey<Item> CAN_ASSASSINATE_SWORDS = createTag("can_assassinate/swords");
        public static final TagKey<Item> DAGGERS = createTag("daggers");

        private static TagKey<Item> createTag(String name) {
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath(StealthAndAlert.MOD_ID, name));
        }
    }

    public static class Entities {
        public static final TagKey<EntityType<?>> SEEKERS = createTag("seekers");
        public static final TagKey<EntityType<?>> CONDITIONAL_SEEKERS = createTag("conditional_seekers");
        public static final TagKey<EntityType<?>> DETECTABLE = createTag("detectable");
        public static final TagKey<EntityType<?>> CAN_BE_ASSASSINATED = createTag("can_be_assassinated");
    }

    private static TagKey<EntityType<?>> createTag(String name) {
        return TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(StealthAndAlert.MOD_ID, name));
    }

    public enum PriorityCategory {
        DAGGERS(Items.CAN_ASSASSINATE_DAGGERS, 2.0),
        TRIDENTS(Items.CAN_ASSASSINATE_TRIDENTS, 3.5),
        MACES(Items.CAN_ASSASSINATE_MACES, 2.5),
        SWORDS(Items.CAN_ASSASSINATE_SWORDS, 3.0),
        DEFAULT(Items.CAN_ASSASSINATE_SWORDS, 3.5);

        private final TagKey<Item> tag;
        private final double maxDistance;

        PriorityCategory(TagKey<Item> tag, double maxDistance) {
            this.tag = tag;
            this.maxDistance = maxDistance;
        }

        public TagKey<Item> tag() {
            return tag;
        }

        public double maxDistance() {
            return maxDistance;
        }
    }
}
