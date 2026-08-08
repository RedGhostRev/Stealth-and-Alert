package net.rev.stealthandalert.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.predicates.AllOfCondition;
import net.minecraft.world.level.storage.loot.predicates.AnyOfCondition;
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
        this.add("music_disc_daisy_bell",
                new AddItemModifier(new LootItemCondition[]{
                        AnyOfCondition.anyOf(
                                AllOfCondition.allOf(
                                        new LootTableIdCondition.Builder(ResourceLocation.withDefaultNamespace("chests/simple_dungeon")),
                                        LootItemRandomChanceCondition.randomChance(0.0006F)
                                ),
                                AllOfCondition.allOf(
                                        new LootTableIdCondition.Builder(ResourceLocation.withDefaultNamespace("chests/stronghold_library")),
                                        LootItemRandomChanceCondition.randomChance(0.001F)
                                )
                        ).build()
                }, ModItems.MUSIC_DISC_DAISY_BELL.get(), "", "")
        );

        this.add("claw_seeking_ring",
                new AddItemModifier(new LootItemCondition[]{
                        AnyOfCondition.anyOf(
                                AllOfCondition.allOf(
                                        new LootTableIdCondition.Builder(ResourceLocation.withDefaultNamespace("chests/end_city_treasure")),
                                        LootItemRandomChanceCondition.randomChance(0.02F)
                                ),
                                AllOfCondition.allOf(
                                        new LootTableIdCondition.Builder(ResourceLocation.withDefaultNamespace("chests/ancient_city")),
                                        LootItemRandomChanceCondition.randomChance(0.01F)
                                )
                        ).build()
                }, ModItems.FLAW_SEEKING_RING.get(), "", ""));
    }
}
