package net.rev.stealthandalert.datagen;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.rev.stealthandalert.StealthAndAlert;
import net.rev.stealthandalert.item.ModItems;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, StealthAndAlert.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicItem(ModItems.PEBBLE.get());
        basicItem(ModItems.CLAMOR_BELL.get());
        basicItem(ModItems.SHADOW_CRYSTAL.get());
        basicItem(ModItems.SHADOW_CRYSTAL_SHARD.get());
        basicItem(ModItems.SHADOW_BERRIES.get());
    }
}
