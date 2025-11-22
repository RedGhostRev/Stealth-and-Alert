package net.rev.stealthandalert.item;

import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.SimpleTier;
import net.rev.stealthandalert.util.ModTags;

public class ModTiers {
    public static final Tier SHADOW_CRYSTAL = new SimpleTier(
            ModTags.Blocks.INCORRECT_FOR_SHADOW_CRYSTAL_TOOL,
            800, 9.0F, 2.5F, 24, () -> Ingredient.of(ModItems.SHADOW_CRYSTAL));
}
