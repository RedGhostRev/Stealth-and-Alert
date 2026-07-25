package net.rev.stealthandalert.common.assassination;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Pose;
import net.rev.stealthandalert.common.animation.ModAnimations;
import net.rev.stealthandalert.util.ModTags;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AssassinationRegistry {
    private static final Map<ModTags.PriorityCategory, AssassinationDataRegistry.WeaponProfile> REGISTRY =
            new LinkedHashMap<>();

    // TODO 完善刺杀上下文
    public static void init() {
//        AssassinationDataRegistry.AnimationConditions daggerConditions = new AssassinationDataRegistry.AnimationConditions(
//                false,
//                null,
//                null,
//                5.0,
//                null,
//                null
//        );

//        AssassinationDataRegistry.AnimationConditions swordConditions = new AssassinationDataRegistry.AnimationConditions(
//                false,
//                null,
//                null,
//                5.0,
//                null,
//                null
//        );

//        AssassinationDataRegistry.AnimationConditions maceConditions = new AssassinationDataRegistry.AnimationConditions(
//                false,
//                null,
//                null,
//                5.0,
//                null,
//                null
//        );

//        AssassinationDataRegistry.AnimationConditions tridentConditions = new AssassinationDataRegistry.AnimationConditions(
//                false, null, null, null, null, null
//        );

        AssassinationDataRegistry.AnimationConditions commonConditions = makeConditions(
                false,          // requireAir: false 表示必须在地面上
                null,                   // requirePose: 指定姿态，null 表示则站立和潜行都会触发
                null,                   // minSpeed: null 表示不设下限速度
                null,                   // maxSpeed: null 表示不设上限速度
                null,                   // minTargetHeight: null 表示不限制怪物身高下限
                null                    // maxTargetHeight: null 表示不限制怪物身高上限
        );


        AssassinationDataRegistry.AnimationEntry daggerThroatSlitEntry = makeEntry(
                ModAnimations.DAGGER_THROAT_SLIT,
                false,
                commonConditions
        );

        AssassinationDataRegistry.AnimationEntry daggerThroatSlitDualEntry = makeEntry(
                ModAnimations.DAGGER_SLIT_AND_STAB_DUAL,
                true,
                commonConditions
        );
        List<AssassinationDataRegistry.AnimationEntry> daggerClosePool = new ArrayList<>();
        List<AssassinationDataRegistry.AnimationEntry> daggerCloseDualPool = new ArrayList<>();
        daggerClosePool.add(daggerThroatSlitEntry);
        daggerCloseDualPool.add(daggerThroatSlitDualEntry);
        AssassinationDataRegistry.DistanceTier daggerCloseTier = makeTier(
                2.0,
                daggerClosePool
        );
        AssassinationDataRegistry.DistanceTier daggerCloseDualTier = makeTier(
                2.0,
                daggerCloseDualPool
        );
        AssassinationDataRegistry.WeaponProfile daggerProfile = makeProfile(
                List.of(daggerCloseTier),
                List.of(daggerCloseDualTier)
        );


        AssassinationDataRegistry.AnimationEntry swordSlashEntry = makeEntry(
                ModAnimations.SWORD_SLASH,
                false,
                commonConditions
        );
        AssassinationDataRegistry.AnimationEntry swordSlashDualEntry = makeEntry(
                ModAnimations.SWORD_SLASH_DUAL,
                true,
                commonConditions
        );
        AssassinationDataRegistry.AnimationEntry swordThrustEntry = makeEntry(
                ModAnimations.SWORD_THRUST,
                false,
                commonConditions
        );
        AssassinationDataRegistry.AnimationEntry swordThrustDualEntry = makeEntry(
                ModAnimations.SWORD_THRUST_DUAL,
                true,
                commonConditions
        );
        List<AssassinationDataRegistry.AnimationEntry> swordClosePool = new ArrayList<>();
        List<AssassinationDataRegistry.AnimationEntry> swordCloseDualPool = new ArrayList<>();
        swordClosePool.add(swordSlashEntry);
        swordCloseDualPool.add(swordSlashDualEntry);
        swordCloseDualPool.add(swordThrustDualEntry);
        List<AssassinationDataRegistry.AnimationEntry> swordMediumPool = new ArrayList<>();
        List<AssassinationDataRegistry.AnimationEntry> swordMediumDualPool = new ArrayList<>();
        swordMediumPool.add(swordThrustEntry);
        swordMediumDualPool.add(swordSlashDualEntry);
        swordMediumDualPool.add(swordThrustDualEntry);
        AssassinationDataRegistry.DistanceTier swordCloseTier = makeTier(
                1.5,
                swordClosePool
        );
        AssassinationDataRegistry.DistanceTier swordCloseDualTier = makeTier(
                1.5,
                swordCloseDualPool
        );
        AssassinationDataRegistry.DistanceTier swordMediumTier = makeTier(
                3.0,
                swordMediumPool
        );
        AssassinationDataRegistry.DistanceTier swordMediumDualTier = makeTier(
                3.0,
                swordMediumDualPool
        );
        AssassinationDataRegistry.WeaponProfile swordProfile = makeProfile(
                List.of(swordCloseTier, swordMediumTier),
                List.of(swordCloseDualTier, swordMediumDualTier)
        );

        AssassinationDataRegistry.AnimationEntry maceSmashEntry = makeEntry(
                ModAnimations.MACE_SMASH,
                false,
                commonConditions
        );
        AssassinationDataRegistry.AnimationEntry maceSmashDualEntry = makeEntry(
                ModAnimations.MACE_SMASH_DUAL,
                true,
                commonConditions
        );
        List<AssassinationDataRegistry.AnimationEntry> maceClosePool = new ArrayList<>();
        List<AssassinationDataRegistry.AnimationEntry> maceCloseDualPool = new ArrayList<>();
        maceClosePool.add(maceSmashEntry);
        maceCloseDualPool.add(maceSmashDualEntry);
        AssassinationDataRegistry.DistanceTier maceCloseTier = makeTier(
                2.5, maceClosePool
        );
        AssassinationDataRegistry.DistanceTier maceCloseDualTier = makeTier(
                2.5, maceCloseDualPool
        );
        AssassinationDataRegistry.WeaponProfile maceProfile = makeProfile(
                List.of(maceCloseTier),
                List.of(maceCloseDualTier)
        );

        AssassinationDataRegistry.AnimationEntry tridentImpaleEntry = makeEntry(
                ModAnimations.TRIDENT_IMPALE, false, commonConditions
        );
        AssassinationDataRegistry.AnimationEntry tridentImpaleDualEntry = makeEntry(
                ModAnimations.TRIDENT_IMPALE_DUAL, true, commonConditions
        );
        List<AssassinationDataRegistry.AnimationEntry> tridentClosePool = new ArrayList<>();
        List<AssassinationDataRegistry.AnimationEntry> tridentCloseDualPool = new ArrayList<>();
        tridentClosePool.add(tridentImpaleEntry);
        tridentCloseDualPool.add(tridentImpaleDualEntry);
        AssassinationDataRegistry.DistanceTier tridentCloseTier = makeTier(
                3.5, tridentClosePool
        );
        AssassinationDataRegistry.DistanceTier tridentCloseDualTier = makeTier(
                3.5, tridentCloseDualPool
        );
        AssassinationDataRegistry.WeaponProfile tridentProfile = makeProfile(
                List.of(tridentCloseTier),
                List.of(tridentCloseDualTier)
        );

        REGISTRY.put(ModTags.PriorityCategory.DAGGERS, daggerProfile);
        REGISTRY.put(ModTags.PriorityCategory.TRIDENTS, tridentProfile);
        REGISTRY.put(ModTags.PriorityCategory.SWORDS, swordProfile);
        REGISTRY.put(ModTags.PriorityCategory.MACES, maceProfile);
    }

    public static AssassinationDataRegistry.WeaponProfile getProfile(ModTags.PriorityCategory category) {
        if (category == null) return null;
        return REGISTRY.get(category);
    }

    private static AssassinationDataRegistry.WeaponProfile makeProfile(List<AssassinationDataRegistry.DistanceTier> distSingleHandTiers,
                                                                       List<AssassinationDataRegistry.DistanceTier> distDualHandTiers) {
        return new AssassinationDataRegistry.WeaponProfile(distSingleHandTiers, distDualHandTiers);
    }

    private static AssassinationDataRegistry.DistanceTier makeTier(double maxDist, List<AssassinationDataRegistry.AnimationEntry> pool) {
        return new AssassinationDataRegistry.DistanceTier(maxDist, pool);
    }

    private static AssassinationDataRegistry.AnimationEntry makeEntry(ResourceLocation animRL,
                                                                      boolean isTwoHanded,
                                                                      AssassinationDataRegistry.AnimationConditions conditions) {
        return new AssassinationDataRegistry.AnimationEntry(animRL, isTwoHanded, conditions);
    }

    private static AssassinationDataRegistry.AnimationConditions makeConditions(boolean requireAir,
                                                                                Pose requirePose,
                                                                                Double minSpeed,
                                                                                Double maxSpeed,
                                                                                Double minTargetHeight,
                                                                                Double maxTargetHeight) {
        return new AssassinationDataRegistry.AnimationConditions(requireAir, requirePose, minSpeed, maxSpeed, minTargetHeight, maxTargetHeight);
    }
}
