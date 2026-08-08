package net.rev.stealthandalert.compat;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.loading.LoadingModList;
import net.rev.stealthandalert.compat.ironsspellbooks.IronsSpellbooksCompat;

@SuppressWarnings("Convert2MethodRef")
// 若使用方法引用，会导致提前加载类文件，使得 KubeJS 注入失败
public enum SupportedMods {
    JADE,
    IRONS_SPELLBOOKS(() -> IronsSpellbooksCompat.init()),
    DUMMMMMMY,
    GUARDVILLAGERS,
    POWERFUL_DUMMY,
    TWILIGHTFOREST,
    CURIOS;

    private final String id;
    private final boolean isLoaded;
    private final Runnable initTask;

    SupportedMods() {
        this(() -> {});
    }

    SupportedMods(Runnable initTask) {
        this.id = name().toLowerCase();
        this.isLoaded = LoadingModList.get().getModFileById(id) != null;
        this.initTask = initTask;
    }

    public String id() {
        return id;
    }

    public boolean isLoaded() {
        return isLoaded;
    }

    public ResourceLocation rl(String path) {
        return ResourceLocation.fromNamespaceAndPath(id, path);
    }

    public void executeInit() {
        if (isLoaded()) {
            initTask.run();
        }
    }

//    public boolean isLoaded() {
//        return ModList.get().isLoaded(this.id);
//    }
//
//    public boolean isLoading() {
//        return FMLLoader.getLoadingModList().getModFileById(this.id) != null;
//    }
}
