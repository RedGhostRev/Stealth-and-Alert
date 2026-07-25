package net.rev.stealthandalert.common.animation;

import net.minecraft.resources.ResourceLocation;
import net.rev.stealthandalert.StealthAndAlert;

public class ModAnimations {
    public static final ResourceLocation DAGGER_THROAT_SLIT = getAnimRL("dagger_throat_slit");
    public static final ResourceLocation DAGGER_SLIT_AND_STAB_DUAL = getAnimRL("dagger_slit_and_stab_dual");

    public static final ResourceLocation TRIDENT_IMPALE =  getAnimRL("trident_impale");
    public static final ResourceLocation TRIDENT_IMPALE_DUAL = getAnimRL("trident_impale_dual");

    public static final ResourceLocation MACE_SMASH = getAnimRL("mace_smash");
    public static final ResourceLocation MACE_SMASH_DUAL = getAnimRL("mace_smash_dual");

    public static final ResourceLocation SWORD_SLASH = getAnimRL("sword_slash");
    public static final ResourceLocation SWORD_THRUST = getAnimRL("sword_thrust");
    public static final ResourceLocation SWORD_SLASH_DUAL = getAnimRL("sword_slash_dual");
    public static final ResourceLocation SWORD_THRUST_DUAL = getAnimRL("sword_thrust_dual");
    public static final ResourceLocation EMPTY = getAnimRL("empty");

    public static ResourceLocation getAnimRL(String id) {
        return ResourceLocation.fromNamespaceAndPath(StealthAndAlert.MOD_ID, id);
    }
}
