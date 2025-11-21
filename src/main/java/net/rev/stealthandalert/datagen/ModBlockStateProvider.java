package net.rev.stealthandalert.datagen;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.rev.stealthandalert.StealthAndAlert;
import net.rev.stealthandalert.block.ModBlocks;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output , ExistingFileHelper exFileHelper) {
        super(output, StealthAndAlert.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        blockWithItem(ModBlocks.PEBBLE_BLOCK);
        blockWithItem(ModBlocks.SHADOW_CRYSTAL_ORE);
        blockWithItem(ModBlocks.DEEPSLATE_SHADOW_CRYSTAL_ORE);
    }

    private void blockWithItem(DeferredBlock<?> deferredBlock) {
        simpleBlockWithItem(deferredBlock.get(), cubeAll(deferredBlock.get()));
    }
}
