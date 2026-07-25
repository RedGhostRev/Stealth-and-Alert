package net.rev.stealthandalert.compat;

import net.neoforged.fml.ModList;
import net.rev.stealthandalert.compat.ironsspellbooks.IronsSpellbooksCompat;

public class CompatHandler {
    public static final boolean HAS_JADE = ModList.get().isLoaded("jade");
    public static final boolean HAS_IRONS_SPELLBOOKS = ModList.get().isLoaded("irons_spellbooks");
    public static final boolean HAS_DUMMMMMMY = ModList.get().isLoaded("dummmmmmy");

    public static void init() {
        if (HAS_IRONS_SPELLBOOKS) {
            IronsSpellbooksCompat.init();
        }
    }
}
