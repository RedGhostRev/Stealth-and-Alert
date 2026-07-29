package net.rev.stealthandalert.compat.dummmmmmy;

import net.rev.stealthandalert.compat.SupportedMods;
import net.rev.stealthandalert.datagen.ModEntityTypeTagsProvider;
import net.rev.stealthandalert.util.ModTags;

public class DummmmmmyCompat {
    private static final SupportedMods mod = SupportedMods.DUMMMMMMY;
    public static void addEntityTags(ModEntityTypeTagsProvider provider) {
        provider.tag(ModTags.Entities.CAN_BE_ASSASSINATED)
                .addOptional(mod.rl("target_dummy"));
    }
}
