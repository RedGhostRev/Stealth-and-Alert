package net.rev.stealthandalert.event;

import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.rev.stealthandalert.StealthAndAlert;
import net.rev.stealthandalert.attribute.ModAttributes;
import net.rev.stealthandalert.damagetype.AssassinationDamageSource;
import net.rev.stealthandalert.damagetype.ModDamageTypes;
import net.rev.stealthandalert.datagen.LangKeys;
import net.rev.stealthandalert.item.ModItems;
import net.rev.stealthandalert.potion.ModPotions;
import net.rev.stealthandalert.util.AssassinationHandler;
import net.rev.stealthandalert.util.CommonUtils;
import net.rev.stealthandalert.util.ModTags;
import net.rev.stealthandalert.util.SpeedHandler;

@EventBusSubscriber(modid = StealthAndAlert.MOD_ID)
public class ModEvents {
    @SubscribeEvent
    public static void onEntityAttributeModification(EntityAttributeModificationEvent event) {
        event.add(EntityType.PLAYER, ModAttributes.VISIBILITY);
        event.add(EntityType.PLAYER, ModAttributes.SOUND_MULTIPLIER);
    }

    @SubscribeEvent
    public static void onRegisterBrewingRecipes(RegisterBrewingRecipesEvent event) {
        PotionBrewing.Builder builder = event.getBuilder();

        builder.addMix(
                Potions.AWKWARD,
                ModItems.SHADOW_BERRIES.get(),
                ModPotions.ETHEREAL_POTION
        );

        builder.addMix(
                ModPotions.ETHEREAL_POTION,
                Items.REDSTONE,
                ModPotions.LONG_ETHEREAL_POTION
        );

        builder.addMix(
                ModPotions.ETHEREAL_POTION,
                Items.GLOWSTONE_DUST,
                ModPotions.STRONG_ETHEREAL_POTION
        );
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        Player player = event.getEntity();
        if (!player.level().isClientSide()) {
            SpeedHandler.clear(player.getUUID());
        }
    }

    @SubscribeEvent
    public static void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        Player player = event.getEntity();
        if (!player.level().isClientSide()) {
            SpeedHandler.clear(player.getUUID());
        }
    }

    @SubscribeEvent
    public static void onLivingDamagePost(LivingDamageEvent.Post event) {
        LivingEntity target = event.getEntity();
        if (!(target.level() instanceof ServerLevel serverLevel)) {
            return;
        }
        DamageSource source = event.getSource();

        if (source.is(ModDamageTypes.ASSASSINATION)
                || target.getTags().contains("stealth_and_alert.processing_assassination")) {
            return;
        }
        if (source.getDirectEntity() instanceof LivingEntity attacker) {
            if (source.is(DamageTypes.PLAYER_ATTACK) || source.is(DamageTypes.MOB_ATTACK)) {
                if (attacker instanceof Player player) {
                    if (player.getAttackStrengthScale(0.5F) < 1F) {
                        return;
                    }
                }

                float extraDamage = CommonUtils.getAssassinationDamage(source.getWeaponItem());
                if (extraDamage > 0 && target.isAlive() && target.getHealth() > 0) {
                    AssassinationDamageSource aSource = AssassinationDamageSource.getSource(attacker, target, LangKeys.ASSASSINATION, AssassinationHandler.AssassinateHand.RIGHT_HAND);
                    int delayTicks = 2;
                    serverLevel.getServer().tell(new TickTask(
                            serverLevel.getServer().getTickCount() + delayTicks,
                            () -> {
                                if (target.isAlive() && target.getHealth() > 0) {
                                    try {
                                        target.addTag("stealth_and_alert.processing_assassination");

                                        target.hurt(aSource, extraDamage);

                                    } finally {
                                        target.removeTag("stealth_and_alert.processing_assassination");
                                    }
                                }
                            }
                    ));
                }
            }
        }
    }

    @SubscribeEvent
    public static void onLivingDamage(LivingDamageEvent.Pre event) {
        if (event.getSource().getDirectEntity() instanceof LivingEntity attacker) {
            if (!attacker.level().isClientSide) {
                ItemStack itemStack = attacker.getMainHandItem();
                if (itemStack.is(ModTags.Items.CAN_BACKSTAB)) {
                    LivingEntity target = event.getEntity();
                    if (isBehind(target, attacker, 60)) {
                        event.setNewDamage(event.getOriginalDamage() * 2F);
                    }
                }
            }
        }
    }

    private static boolean isBehind(LivingEntity target, LivingEntity attacker, double angleDegrees) {
        Vec3 targetLook = target.getLookAngle();
        Vec3 targetLookHorizon = new Vec3(targetLook.x, 0, targetLook.z);
        if (targetLookHorizon.lengthSqr() == 0) {
            return false;
        }
        targetLookHorizon = targetLookHorizon.normalize();

        Vec3 toAttacker = attacker.position().subtract(target.position());
        Vec3 toAttackerHorizon = new Vec3(toAttacker.x, 0, toAttacker.z);
        if (toAttackerHorizon.lengthSqr() == 0) {
            return false;
        }
        toAttackerHorizon = toAttackerHorizon.normalize();

        double dot = targetLookHorizon.dot(toAttackerHorizon);
        double threshold = Math.cos(Math.toRadians(angleDegrees));
        return dot <= -threshold;
    }
}
