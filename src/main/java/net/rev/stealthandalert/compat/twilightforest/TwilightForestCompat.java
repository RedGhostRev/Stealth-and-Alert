package net.rev.stealthandalert.compat.twilightforest;

import net.minecraft.world.item.ItemStack;
import net.rev.stealthandalert.compat.SupportedMods;
import net.rev.stealthandalert.datagen.ModEntityTypeTagsProvider;
import net.rev.stealthandalert.util.ModTags;
import twilightforest.init.TFDataComponents;

public class TwilightForestCompat {
    public static final SupportedMods mod = SupportedMods.TWILIGHTFOREST;

    public static boolean hasEmperorsCloth(ItemStack stack) {
        return stack.getComponents().has(TFDataComponents.EMPERORS_CLOTH.get());
    }

    public static void addEntityTags(ModEntityTypeTagsProvider provider) {
        provider.tag(ModTags.Entities.SEEKERS)
                .addOptional(mod.rl("snow_guardian"))
                .addOptional(mod.rl("mosquito_swarm"))
                .addOptional(mod.rl("troll"))
                .addOptional(mod.rl("hostile_wolf"))
                .addOptional(mod.rl("goblin_knight_upper"))
                .addOptional(mod.rl("goblin_knight_lower"))
                .addOptional(mod.rl("tower_golem"))
                .addOptional(mod.rl("tower_termite"))
                .addOptional(mod.rl("tower_ghast"))
                .addOptional(mod.rl("mini_ghast"))
                .addOptional(mod.rl("king_spider"))
                .addOptional(mod.rl("skeleton_druid"))
                .addOptional(mod.rl("redcap"))
                .addOptional(mod.rl("redcap_sapper"))
                .addOptional(mod.rl("winter_wolf"))
                .addOptional(mod.rl("swarm_spider"))
                .addOptional(mod.rl("helmet_crab"))
                .addOptional(mod.rl("tower_broodling"))
                .addOptional(mod.rl("pinch_beetle"))
                .addOptional(mod.rl("blockchain_goblin"))
                .addOptional(mod.rl("maze_slime"))
                .addOptional(mod.rl("mist_wolf"))
                .addOptional(mod.rl("slime_beetle"))
                .addOptional(mod.rl("minotaur"))
                .addOptional(mod.rl("fire_beetle"))
                .addOptional(mod.rl("hedge_spider"))
                .addOptional(mod.rl("yeti"));

        provider.tag(ModTags.Entities.CONDITIONAL_SEEKERS)
                        .addOptional(mod.rl("loyal_zombie"));

        provider.tag(ModTags.Entities.ANIMALS)
                .addOptional(mod.rl("penguin"))
                .addOptional(mod.rl("quest_ram"))
                .addOptional(mod.rl("bighorn_sheep"))
                .addOptional(mod.rl("squirrel"))
                .addOptional(mod.rl("raven"))
                .addOptional(mod.rl("bunny"))
                .addOptional(mod.rl("tiny_bird"))
                .addOptional(mod.rl("deer"))
                .addOptional(mod.rl("boar"))
                .addOptional(mod.rl("firefly"));

    }
}
