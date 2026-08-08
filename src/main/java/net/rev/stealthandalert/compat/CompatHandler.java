package net.rev.stealthandalert.compat;

import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.rev.stealthandalert.compat.curios.CuriosCompat;
import net.rev.stealthandalert.compat.curios.CuriosProvider;
import net.rev.stealthandalert.compat.dummmmmmy.DummmmmmyCompat;
import net.rev.stealthandalert.compat.guardvillagers.GuardVillagersCompat;
import net.rev.stealthandalert.compat.ironsspellbooks.IronsSpellbooksCompat;
import net.rev.stealthandalert.compat.powerfuldummy.PowerfulDummyCompat;
import net.rev.stealthandalert.compat.twilightforest.TwilightForestCompat;
import net.rev.stealthandalert.datagen.ModEntityTypeTagsProvider;
import net.rev.stealthandalert.datagen.ModItemTagsProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class CompatHandler {
    public static final List<Consumer<ModEntityTypeTagsProvider>> ENTITY_TAGS = new ArrayList<>();
    public static final List<Consumer<ModItemTagsProvider>> ITEM_TAGS = new ArrayList<>();

    static {
        ENTITY_TAGS.add(IronsSpellbooksCompat::addEntityTags);
        ENTITY_TAGS.add(DummmmmmyCompat::addEntityTags);
        ENTITY_TAGS.add(GuardVillagersCompat::addEntityTags);
        ENTITY_TAGS.add(PowerfulDummyCompat::addEntityTags);
        ENTITY_TAGS.add(TwilightForestCompat::addEntityTags);
    }

    static {
        if (SupportedMods.CURIOS.isLoaded()) {
            ITEM_TAGS.add(CuriosProvider::addItemTags);
        }
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        if (SupportedMods.CURIOS.isLoaded()) {
            CuriosCompat.registerCurios(event);
        }
    }

    public static void init() {
        for (SupportedMods mod : SupportedMods.values()) {
            mod.executeInit();
        }
    }
}
