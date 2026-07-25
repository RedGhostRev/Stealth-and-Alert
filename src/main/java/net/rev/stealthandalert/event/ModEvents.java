package net.rev.stealthandalert.event;

import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.gameevent.DynamicGameEventListener;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.EntityLeaveLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.rev.stealthandalert.StealthAndAlert;
import net.rev.stealthandalert.attribute.ModAttributes;
import net.rev.stealthandalert.command.ModCommands;
import net.rev.stealthandalert.common.alert.condition.ActionOnContainerCondition;
import net.rev.stealthandalert.config.EntityAlertConditionConfigLoader;
import net.rev.stealthandalert.damagetype.AssassinationDamageSource;
import net.rev.stealthandalert.damagetype.ModDamageTypes;
import net.rev.stealthandalert.datagen.LangKeys;
import net.rev.stealthandalert.item.ModItems;
import net.rev.stealthandalert.potion.ModPotions;
import net.rev.stealthandalert.util.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@EventBusSubscriber(modid = StealthAndAlert.MOD_ID)
public class ModEvents {
    private static final Map<Mob, DynamicGameEventListener<StealthEventListener>> LISTENERS = new ConcurrentHashMap<>();

    /**
     * 1. 挂载
     */
    @SubscribeEvent
    public static void onEntityJoin(EntityJoinLevelEvent event) {
        if (event.getLevel() instanceof ServerLevel level && event.getEntity() instanceof Mob mob) {

            if (mob.getType().is(ModTags.Entities.CONDITIONAL_SEEKERS) && EntityAlertConditionConfigLoader.get(mob.getType()).alertConditions().containsKey(ActionOnContainerCondition.ID)) {

                StealthEventListener stealthListener = new StealthEventListener(mob);

                // 动态监听器
                DynamicGameEventListener<StealthEventListener> dynamicListener =
                        new DynamicGameEventListener<>(stealthListener);

                // 注册到当前区块
                dynamicListener.add(level);

                LISTENERS.put(mob, dynamicListener);
            }
        }
    }

    /**
     * 2. 卸载
     */
    @SubscribeEvent
    public static void onEntityLeave(EntityLeaveLevelEvent event) {
        if (event.getLevel() instanceof ServerLevel level && event.getEntity() instanceof Mob mob) {
            // 当实体死亡、传送到其他维度、或者区块被卸载时，注销
            DynamicGameEventListener<StealthEventListener> listener = LISTENERS.remove(mob);
            if (listener != null) {
                listener.remove(level);
            }
        }
    }

    /**
     * 3. 动态追踪
     */
    @SubscribeEvent
    public static void onEntityTick(EntityTickEvent.Post event) {
        if (event.getEntity().level() instanceof ServerLevel level && event.getEntity() instanceof Mob mob) {
            DynamicGameEventListener<StealthEventListener> listener = LISTENERS.get(mob);
            if (listener != null) {
                // move() 方法检测实体是否跨越了区块。
                // 自动注销旧区块的注册，并在新区块重新注册。
                listener.move(level);
            }
        }
    }

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        ModCommands.register(event.getDispatcher());
    }

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
                if (Float.isInfinite(extraDamage)) {
                    extraDamage = Integer.MAX_VALUE;
                }
                if (extraDamage > 0F && target.isAlive() && target.getHealth() > 0F) {
                    AssassinationDamageSource aSource = AssassinationDamageSource.getSource(attacker, target, LangKeys.ASSASSINATION, AssassinationHandler.AssassinateHand.RIGHT_HAND);
                    int delayTicks = 2;
                    float finalExtraDamage = extraDamage;
                    serverLevel.getServer().tell(new TickTask(
                            serverLevel.getServer().getTickCount() + delayTicks,
                            () -> {
                                if (target.isAlive() && target.getHealth() > 0) {
                                    try {
                                        target.addTag("stealth_and_alert.processing_assassination");

                                        target.hurt(aSource, finalExtraDamage);

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
}
