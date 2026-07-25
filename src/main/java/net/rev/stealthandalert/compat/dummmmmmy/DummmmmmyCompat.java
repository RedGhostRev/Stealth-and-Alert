package net.rev.stealthandalert.compat.dummmmmmy;

import net.mehvahdjukaar.dummmmmmy.Dummmmmmy;
import net.minecraft.resources.ResourceLocation;
import net.rev.stealthandalert.datagen.ModEntityTypeTagsProvider;
import net.rev.stealthandalert.util.ModTags;

public class DummmmmmyCompat {
    public static void addEntityTags(ModEntityTypeTagsProvider provider) {
        provider.tag(ModTags.Entities.CAN_BE_ASSASSINATED)
                .addOptional(ResourceLocation.fromNamespaceAndPath(Dummmmmmy.MOD_ID, Dummmmmmy.TARGET_DUMMY_NAME));
    }
}
