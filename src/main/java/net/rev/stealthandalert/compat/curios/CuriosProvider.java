package net.rev.stealthandalert.compat.curios;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.rev.stealthandalert.StealthAndAlert;
import net.rev.stealthandalert.datagen.ModItemTagsProvider;
import net.rev.stealthandalert.item.ModItems;
import top.theillusivec4.curios.api.CuriosDataProvider;
import top.theillusivec4.curios.api.CuriosTags;

import java.util.concurrent.CompletableFuture;

public class CuriosProvider extends CuriosDataProvider {
    public CuriosProvider(PackOutput output, ExistingFileHelper fileHelper, CompletableFuture<HolderLookup.Provider> registries) {
        super(StealthAndAlert.MOD_ID, output, fileHelper, registries);
    }

    public static void addItemTags(ModItemTagsProvider provider) {
        provider.tag(CuriosTags.RING)
                .add(ModItems.FLAW_SEEKING_RING.get());
    }

    @Override
    public void generate(HolderLookup.Provider registries, ExistingFileHelper fileHelper) {
        this.createEntities("slot_entities")
                .addPlayer()
                .addSlots("ring");
    }


}
