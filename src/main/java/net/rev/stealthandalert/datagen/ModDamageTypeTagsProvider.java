package net.rev.stealthandalert.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.DamageTypeTagsProvider;
import net.minecraft.tags.DamageTypeTags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.rev.stealthandalert.StealthAndAlert;
import net.rev.stealthandalert.damagetype.ModDamageTypes;

import java.util.concurrent.CompletableFuture;

public class ModDamageTypeTagsProvider extends DamageTypeTagsProvider {
    public ModDamageTypeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, StealthAndAlert.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(DamageTypeTags.BYPASSES_ARMOR).add(ModDamageTypes.ASSASSINATION);
        this.tag(DamageTypeTags.BYPASSES_SHIELD).add(ModDamageTypes.ASSASSINATION);
        this.tag(DamageTypeTags.BYPASSES_INVULNERABILITY).add(ModDamageTypes.ASSASSINATION);
        this.tag(DamageTypeTags.BYPASSES_COOLDOWN).add(ModDamageTypes.ASSASSINATION);
        this.tag(DamageTypeTags.BYPASSES_EFFECTS).add(ModDamageTypes.ASSASSINATION);
        this.tag(DamageTypeTags.BYPASSES_RESISTANCE).add(ModDamageTypes.ASSASSINATION);
        this.tag(DamageTypeTags.BYPASSES_ENCHANTMENTS).add(ModDamageTypes.ASSASSINATION);
        this.tag(DamageTypeTags.NO_KNOCKBACK).add(ModDamageTypes.ASSASSINATION);
    }
}
