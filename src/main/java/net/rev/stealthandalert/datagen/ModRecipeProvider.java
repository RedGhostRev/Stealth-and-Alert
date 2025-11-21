package net.rev.stealthandalert.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.ItemLike;
import net.rev.stealthandalert.StealthAndAlert;
import net.rev.stealthandalert.block.ModBlocks;
import net.rev.stealthandalert.item.ModItems;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider {
    public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    private static final List<ItemLike> SHADOW_CRYSTAL_ORES = List.of(
            ModBlocks.SHADOW_CRYSTAL_ORE,
            ModBlocks.DEEPSLATE_SHADOW_CRYSTAL_ORE
    );

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.PEBBLE, 9)
                .requires(ModBlocks.PEBBLE_BLOCK)
                .unlockedBy("has_pebble_block", has(ModBlocks.PEBBLE_BLOCK))
                .save(recipeOutput);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.SHADOW_CRYSTAL_SHARD, 4)
                .requires(ModItems.SHADOW_CRYSTAL)
                .unlockedBy("has_shadow_crystal", has(ModItems.SHADOW_CRYSTAL))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.PEBBLE_BLOCK)
                .pattern("PPP")
                .pattern("PPP")
                .pattern("PPP")
                .define('P', ModItems.PEBBLE)
                .unlockedBy("has_pebble", has(ModItems.PEBBLE))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.CLAMOR_BELL)
                .pattern(" S ")
                .pattern("DBD")
                .pattern(" D ")
                .define('S', Items.STICK)
                .define('D', Items.DRIED_KELP)
                .define('B', Items.BELL)
                .unlockedBy("has_bell", has(Items.BELL))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.SHADOW_BERRIES)
                .pattern(" S ")
                .pattern("SBS")
                .pattern(" S ")
                .define('S', ModItems.SHADOW_CRYSTAL_SHARD)
                .define('B', Items.SWEET_BERRIES)
                .unlockedBy("has_shadow_crystal_shard", has(ModItems.SHADOW_CRYSTAL_SHARD))
                .save(recipeOutput);

        oreSmelting(recipeOutput, SHADOW_CRYSTAL_ORES, RecipeCategory.MISC, ModItems.SHADOW_CRYSTAL, 1.0F, 200, "shadow_crystal");
        oreBlasting(recipeOutput, SHADOW_CRYSTAL_ORES, RecipeCategory.MISC, ModItems.SHADOW_CRYSTAL, 1.0F, 100, "shadow_crystal");
    }

    protected static void oreSmelting(
            RecipeOutput recipeOutput, List<ItemLike> ingredients, RecipeCategory category, ItemLike result, float experience, int cookingTime, String group
    ) {
        oreCooking(
                recipeOutput,
                RecipeSerializer.SMELTING_RECIPE,
                SmeltingRecipe::new,
                ingredients,
                category,
                result,
                experience,
                cookingTime,
                group,
                "_from_smelting"
        );
    }

    protected static void oreBlasting(
            RecipeOutput recipeOutput, List<ItemLike> ingredients, RecipeCategory category, ItemLike result, float experience, int cookingTime, String group
    ) {
        oreCooking(
                recipeOutput,
                RecipeSerializer.BLASTING_RECIPE,
                BlastingRecipe::new,
                ingredients,
                category,
                result,
                experience,
                cookingTime,
                group,
                "_from_blasting"
        );
    }

    protected static <T extends AbstractCookingRecipe> void oreCooking(
            RecipeOutput recipeOutput,
            RecipeSerializer<T> serializer,
            AbstractCookingRecipe.Factory<T> recipeFactory,
            List<ItemLike> ingredients,
            RecipeCategory category,
            ItemLike result,
            float experience,
            int cookingTime,
            String group,
            String suffix
    ) {
        for (ItemLike itemlike : ingredients) {
            SimpleCookingRecipeBuilder.generic(Ingredient.of(itemlike), category, result, experience, cookingTime, serializer, recipeFactory)
                    .group(group)
                    .unlockedBy(getHasName(itemlike), has(itemlike))
                    .save(recipeOutput, StealthAndAlert.MOD_ID + ":" + getItemName(result) + suffix + "_" + getItemName(itemlike));
        }
    }
}
