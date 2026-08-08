package net.rev.stealthandalert.compat.powerfuldummy;

import net.rev.stealthandalert.compat.SupportedMods;
import net.rev.stealthandalert.datagen.ModEntityTypeTagsProvider;
import net.rev.stealthandalert.util.ModTags;

public class PowerfulDummyCompat {
    public static final SupportedMods mod = SupportedMods.POWERFUL_DUMMY;

    public static void addEntityTags(ModEntityTypeTagsProvider provider) {
        provider.tag(ModTags.Entities.CAN_BE_ASSASSINATED)
                .addOptional(mod.rl("test_dummy"))
                .addOptional(mod.rl("test_dummy_undead"))
                .addOptional(mod.rl("test_dummy_arthropod"))
                .addOptional(mod.rl("test_dummy_water"))
                .addOptional(mod.rl("test_dummy_illager"));
    }
}
