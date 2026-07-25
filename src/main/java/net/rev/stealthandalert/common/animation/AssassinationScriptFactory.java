package net.rev.stealthandalert.common.animation;

import net.minecraft.resources.ResourceLocation;
import net.rev.stealthandalert.common.animation.script.dagger.DaggerSlitAndStabDualScript;
import net.rev.stealthandalert.common.animation.script.dagger.DaggerThroatSlitScript;
import net.rev.stealthandalert.common.animation.script.mace.MaceSmashDualScript;
import net.rev.stealthandalert.common.animation.script.mace.MaceSmashScript;
import net.rev.stealthandalert.common.animation.script.sword.SwordSlashDualScript;
import net.rev.stealthandalert.common.animation.script.sword.SwordSlashScript;
import net.rev.stealthandalert.common.animation.script.sword.SwordThrustDualScript;
import net.rev.stealthandalert.common.animation.script.sword.SwordThrustScript;
import net.rev.stealthandalert.common.animation.script.trident.TridentImpaleDualScript;
import net.rev.stealthandalert.common.animation.script.trident.TridentImpaleScript;
import net.rev.stealthandalert.util.AssassinationHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class AssassinationScriptFactory {
    private static final Map<ResourceLocation, Function<AssassinationHandler.AssassinateHand, AssassinationScript>> ANIM_SCRIPTS = new HashMap<>();

    static {
        ANIM_SCRIPTS.put(ModAnimations.EMPTY, hand -> null);
        ANIM_SCRIPTS.put(ModAnimations.DAGGER_THROAT_SLIT, DaggerThroatSlitScript::new);
        ANIM_SCRIPTS.put(ModAnimations.DAGGER_SLIT_AND_STAB_DUAL, DaggerSlitAndStabDualScript::new);
        ANIM_SCRIPTS.put(ModAnimations.SWORD_SLASH, SwordSlashScript::new);
        ANIM_SCRIPTS.put(ModAnimations.SWORD_SLASH_DUAL, SwordSlashDualScript::new);
        ANIM_SCRIPTS.put(ModAnimations.SWORD_THRUST, SwordThrustScript::new);
        ANIM_SCRIPTS.put(ModAnimations.SWORD_THRUST_DUAL, SwordThrustDualScript::new);
        ANIM_SCRIPTS.put(ModAnimations.MACE_SMASH, MaceSmashScript::new);
        ANIM_SCRIPTS.put(ModAnimations.MACE_SMASH_DUAL, MaceSmashDualScript::new);
        ANIM_SCRIPTS.put(ModAnimations.TRIDENT_IMPALE, TridentImpaleScript::new);
        ANIM_SCRIPTS.put(ModAnimations.TRIDENT_IMPALE_DUAL, TridentImpaleDualScript::new);
    }

    public static AssassinationScript createScript(ResourceLocation animRL, AssassinationHandler.AssassinateHand hand) {
        return ANIM_SCRIPTS.get(animRL).apply(hand);
    }
}
