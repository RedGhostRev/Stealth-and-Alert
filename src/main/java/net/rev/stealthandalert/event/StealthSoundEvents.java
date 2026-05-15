package net.rev.stealthandalert.event;

import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.projectile.*;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.EntityTeleportEvent;
import net.neoforged.neoforge.event.entity.ProjectileImpactEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import net.neoforged.neoforge.event.entity.player.ArrowLooseEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.level.ExplosionEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.rev.stealthandalert.StealthAndAlert;
import net.rev.stealthandalert.attachment.AlertSoundData;
import net.rev.stealthandalert.network.S2CSoundPacket;

@EventBusSubscriber(modid = StealthAndAlert.MOD_ID)
public class StealthSoundEvents {
    public static IEventBus bus = NeoForge.EVENT_BUS;

    @SubscribeEvent
    public static void onPlayerLooseBow(ArrowLooseEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            if (!event.getBow().is(Items.BOW)) return;
            bus.post(new StealthSoundEvent(StealthSoundEvent.Type.PLAYER_SELF, player.position(), player, 42.0, 6.0, AlertSoundData.MEDIUM));
            PacketDistributor.sendToPlayer(player, new S2CSoundPacket(42.0));
        }
    }

    @SubscribeEvent
    public static void onPlayerHurtOrAttack(LivingDamageEvent.Post event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            bus.post(new StealthSoundEvent(StealthSoundEvent.Type.PLAYER_SELF, player.position(), player, 43.0, 5.0, AlertSoundData.LOW));
            PacketDistributor.sendToPlayer(player, new S2CSoundPacket(43.0));
        } else if (!(event.getEntity() instanceof ServerPlayer)) {
            if (event.getSource().getDirectEntity() instanceof ServerPlayer player && event.getSource().is(DamageTypes.PLAYER_ATTACK)) {
                bus.post(new StealthSoundEvent(StealthSoundEvent.Type.PLAYER_SELF, event.getEntity().position(), player, 43.0, 5.0, AlertSoundData.LOW));
                PacketDistributor.sendToPlayer(player, new S2CSoundPacket(43.0));
            }
        }
    }


    @SubscribeEvent
    public static void onPlayerJump(LivingEvent.LivingJumpEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            bus.post(new StealthSoundEvent(StealthSoundEvent.Type.PLAYER_SELF, player.position(), player, 34.0, 3.0, AlertSoundData.LOW));
            PacketDistributor.sendToPlayer(player, new S2CSoundPacket(34.0));
        }
    }

    @SubscribeEvent
    public static void onPlayerFall(PlayerTickEvent.Post event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            if (player.onGround()) {
                if (0.5 < player.fallDistance && player.fallDistance < 5) {
                    bus.post(new StealthSoundEvent(StealthSoundEvent.Type.PLAYER_SELF, player.position(), player, 34.0, 3.0, AlertSoundData.LOW));
                    PacketDistributor.sendToPlayer(player, new S2CSoundPacket(34.0));
                } else if (player.fallDistance >= 5) {
                    bus.post(new StealthSoundEvent(StealthSoundEvent.Type.PLAYER_SELF, player.position(), player, 45.0, 6.0, AlertSoundData.LOW));
                    PacketDistributor.sendToPlayer(player, new S2CSoundPacket(45.0));
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerEat(LivingEntityUseItemEvent.Tick event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            if (event.getItem().has(DataComponents.FOOD)) {
                bus.post(new StealthSoundEvent(StealthSoundEvent.Type.PLAYER_SELF, player.position(), player, 41.0, 2.0, AlertSoundData.LOW));
                PacketDistributor.sendToPlayer(player, new S2CSoundPacket(41.0));
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerFinishBreaking(BlockEvent.BreakEvent event) {
        if (event.getPlayer() instanceof ServerPlayer player) {
            bus.post(new StealthSoundEvent(StealthSoundEvent.Type.PLAYER_SELF, event.getPos().getCenter(), player, 44.0, 5.5, AlertSoundData.LOW));
            PacketDistributor.sendToPlayer(player, new S2CSoundPacket(44.0));
        }
    }

    @SubscribeEvent
    public static void onPlayerPlace(BlockEvent.EntityPlaceEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            bus.post(new StealthSoundEvent(StealthSoundEvent.Type.PLAYER_SELF, event.getPos().getCenter(), player, 42.0, 4.0, AlertSoundData.LOW));
            PacketDistributor.sendToPlayer(player, new S2CSoundPacket(42.0));
        }
    }

    @SubscribeEvent
    public static void onPlayerTeleport(EntityTeleportEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            Vec3 prev = event.getPrev();
            Vec3 target = event.getTarget();
            bus.post(new StealthSoundEvent(StealthSoundEvent.Type.ENVIRONMENT, prev, player, 42.0, 3.0, AlertSoundData.LOW));
            bus.post(new StealthSoundEvent(StealthSoundEvent.Type.PLAYER_SELF, target, player, 42.0, 3.0, AlertSoundData.LOW));
            PacketDistributor.sendToPlayer(player, new S2CSoundPacket(42.0));
        }
    }

    @SubscribeEvent
    public static void onProjectileImpact(ProjectileImpactEvent event) {
        Projectile projectile = event.getProjectile();
        if (projectile.getOwner() instanceof ServerPlayer player && !player.isCreative() && !player.isSpectator()) {
            if (projectile instanceof Snowball) {
                bus.post(new StealthSoundEvent(StealthSoundEvent.Type.ENVIRONMENT, projectile.position(), player, 42.0, 5.0, AlertSoundData.LOW));
            } else if (projectile instanceof ThrownEgg) {
                bus.post(new StealthSoundEvent(StealthSoundEvent.Type.ENVIRONMENT, projectile.position(), player, 42.0, 5.0, AlertSoundData.LOW));
            } else if (projectile instanceof Arrow) {
                bus.post(new StealthSoundEvent(StealthSoundEvent.Type.ENVIRONMENT, projectile.position(), player, 50.0, 7.0, AlertSoundData.MEDIUM));
            } else if (projectile instanceof SpectralArrow) {
                bus.post(new StealthSoundEvent(StealthSoundEvent.Type.ENVIRONMENT, projectile.position(), player, 50.0, 7.0, AlertSoundData.MEDIUM));
            } else if (projectile instanceof ThrownTrident) {
                bus.post(new StealthSoundEvent(StealthSoundEvent.Type.ENVIRONMENT, projectile.position(), player, 60.0, 10.0, AlertSoundData.HIGH));
            } else if (projectile instanceof ThrownPotion) {
                bus.post(new StealthSoundEvent(StealthSoundEvent.Type.ENVIRONMENT, projectile.position(), player, 50.0, 7.0, AlertSoundData.LOW));
            }
        }
    }

    @SubscribeEvent
    public static void onExplode(ExplosionEvent.Detonate event) {
        Explosion explosion = event.getExplosion();
        if (explosion.getIndirectSourceEntity() instanceof ServerPlayer player && !player.isCreative() && !player.isSpectator()) {
            bus.post(new StealthSoundEvent(StealthSoundEvent.Type.ENVIRONMENT, explosion.center(), player, 150.0, 32.0, AlertSoundData.HIGH));
        } else if (explosion.getDirectSourceEntity() instanceof Creeper creeper) {
            if (creeper.getTarget() instanceof ServerPlayer player && !player.isCreative() && !player.isSpectator()) {
                if (!creeper.isPowered()) {
                    bus.post(new StealthSoundEvent(StealthSoundEvent.Type.ENVIRONMENT, explosion.center(), player, 150.0, 32.0, AlertSoundData.HIGH));
                } else {
                    bus.post(new StealthSoundEvent(StealthSoundEvent.Type.ENVIRONMENT, explosion.center(), player, 180.0, 36.0, AlertSoundData.HIGH));
                }
            }
        }
    }
}
