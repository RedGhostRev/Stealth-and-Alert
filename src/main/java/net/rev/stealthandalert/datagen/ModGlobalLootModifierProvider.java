package net.rev.stealthandalert.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider;
import net.neoforged.neoforge.common.loot.LootTableIdCondition;
import net.rev.stealthandalert.StealthAndAlert;
import net.rev.stealthandalert.item.ModItems;
import net.rev.stealthandalert.loot.AddItemModifier;

import java.util.concurrent.CompletableFuture;

public class ModGlobalLootModifierProvider extends GlobalLootModifierProvider {
    public ModGlobalLootModifierProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, StealthAndAlert.MOD_ID);
    }

    @Override
    protected void start() {
        this.add("music_disc_daisy_bell_from_simple_dungeon",
                new AddItemModifier(new LootItemCondition[]{
                        new LootTableIdCondition.Builder(ResourceLocation.withDefaultNamespace("chests/simple_dungeon")).build(),
                        LootItemRandomChanceCondition.randomChance(0.0006F).build()
                }, ModItems.MUSIC_DISC_DAISY_BELL.get(), "", "")
        );

        this.add("music_disc_daisy_bell_from_stronghold_library",
                new AddItemModifier(new LootItemCondition[]{
                        new LootTableIdCondition.Builder(ResourceLocation.withDefaultNamespace("chests/stronghold_library")).build(),
                        LootItemRandomChanceCondition.randomChance(0.001F).build()
                }, ModItems.MUSIC_DISC_DAISY_BELL.get(), "", ""));
    }
}
