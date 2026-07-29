package net.rev.stealthandalert.compat;

import net.rev.stealthandalert.compat.dummmmmmy.DummmmmmyCompat;
import net.rev.stealthandalert.compat.guardvillagers.GuardVillagersCompat;
import net.rev.stealthandalert.compat.ironsspellbooks.IronsSpellbooksCompat;
import net.rev.stealthandalert.datagen.ModEntityTypeTagsProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class CompatHandler {
    public static final List<Consumer<ModEntityTypeTagsProvider>> ENTITY_TAGS = new ArrayList<>();

    static {
        ENTITY_TAGS.add(IronsSpellbooksCompat::addEntityTags);
        ENTITY_TAGS.add(DummmmmmyCompat::addEntityTags);
        ENTITY_TAGS.add(GuardVillagersCompat::addEntityTags);
    }

    public static void init() {
        for (SupportedMods mod : SupportedMods.values()) {
            mod.executeInit();
        }
    }
}
