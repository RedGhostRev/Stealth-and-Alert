package net.rev.stealthandalert.loot;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;
import org.jetbrains.annotations.NotNull;

public class AddItemModifier extends LootModifier {
    public static final MapCodec<AddItemModifier> CODEC = RecordCodecBuilder.mapCodec(inst ->
            LootModifier.codecStart(inst).and(
                            BuiltInRegistries.ITEM.byNameCodec().fieldOf("name").forGetter(e -> e.item))
                    .and(Codec.STRING.optionalFieldOf("target_prefix", "").forGetter(e -> e.targetPrefix))
                    .and(Codec.STRING.optionalFieldOf("excluded_prefix", "").forGetter(e -> e.excludedPrefix))
                    .apply(inst, AddItemModifier::new));

    private final Item item;
    private final String targetPrefix;
    private final String excludedPrefix;

    public AddItemModifier(LootItemCondition[] conditionsIn, Item item, String targetPrefix, String excludedPrefix) {
        super(conditionsIn);
        this.item = item;
        this.targetPrefix = targetPrefix;
        this.excludedPrefix = excludedPrefix;
    }

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(@NotNull ObjectArrayList<ItemStack> generatedLoot, @NotNull LootContext context) {
        ResourceLocation tableId = context.getQueriedLootTableId();
        String path = tableId.getPath();

        if (!this.targetPrefix.isEmpty() && !path.startsWith(this.targetPrefix)) {
            return generatedLoot;
        }

        if (!this.excludedPrefix.isEmpty() && path.startsWith(this.excludedPrefix)) {
            return generatedLoot;
        }

        generatedLoot.add(new ItemStack(this.item));
        return generatedLoot;
    }

    @Override
    public @NotNull MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }
}
