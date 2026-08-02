package net.rev.stealthandalert.event;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.projectile.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.entity.BarrelBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.HitResult;
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
import net.neoforged.neoforge.event.entity.player.PlayerContainerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.level.ExplosionEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.rev.stealthandalert.StealthAndAlert;
import net.rev.stealthandalert.attachment.AlertSoundData;
import net.rev.stealthandalert.network.S2CSoundPacket;

import java.util.HashMap;
import java.util.UUID;

@EventBusSubscriber(modid = StealthAndAlert.MOD_ID)
public class StealthSoundEvents {
    public static IEventBus bus = NeoForge.EVENT_BUS;
    public static final HashMap<UUID, OpenedContainerInfo> activeContainers = new HashMap<>();

    public record OpenedContainerInfo(BlockPos pos, BlockEntity blockEntity) {
    }

    @SubscribeEvent
    public static void onPlayerLooseBow(ArrowLooseEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            if (!event.getBow().is(Items.BOW)) return;
            StealthSoundEvent sEvent = new StealthSoundEvent(StealthSoundEvent.Type.PLAYER_SELF, player.position(), player, 42.0, 6.0, AlertSoundData.MEDIUM);
            bus.post(sEvent);
            PacketDistributor.sendToPlayer(player, new S2CSoundPacket(sEvent.volume));
        }
    }

    @SubscribeEvent
    public static void onPlayerHurtOrAttack(LivingDamageEvent.Post event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            StealthSoundEvent sEvent = new StealthSoundEvent(StealthSoundEvent.Type.PLAYER_SELF, player.position(), player, 43.0, 5.0, AlertSoundData.LOW);
            bus.post(sEvent);
            PacketDistributor.sendToPlayer(player, new S2CSoundPacket(sEvent.volume));
        } else if (!(event.getEntity() instanceof ServerPlayer)) {
            if (event.getSource().getDirectEntity() instanceof ServerPlayer player && event.getSource().is(DamageTypes.PLAYER_ATTACK)) {
                StealthSoundEvent sEvent = new StealthSoundEvent(StealthSoundEvent.Type.PLAYER_SELF, event.getEntity().position(), player, 43.0, 5.0, AlertSoundData.LOW);
                bus.post(sEvent);
                PacketDistributor.sendToPlayer(player, new S2CSoundPacket(sEvent.volume));
            }
        }
    }


    @SubscribeEvent
    public static void onPlayerJump(LivingEvent.LivingJumpEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            StealthSoundEvent sEvent = new StealthSoundEvent(StealthSoundEvent.Type.PLAYER_SELF, player.position(), player, 34.0, 3.0, AlertSoundData.LOW);
            bus.post(sEvent);
            PacketDistributor.sendToPlayer(player, new S2CSoundPacket(sEvent.volume));
        }
    }

    @SubscribeEvent
    public static void onPlayerFall(PlayerTickEvent.Post event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            if (player.onGround()) {
                if (0.9 < player.fallDistance && player.fallDistance < 5) {
                    StealthSoundEvent sEvent = new StealthSoundEvent(StealthSoundEvent.Type.PLAYER_SELF, player.position(), player, 34.0, 3.0, AlertSoundData.LOW);
                    bus.post(sEvent);
                    PacketDistributor.sendToPlayer(player, new S2CSoundPacket(sEvent.volume));
                } else if (player.fallDistance >= 5) {
                    StealthSoundEvent sEvent = new StealthSoundEvent(StealthSoundEvent.Type.PLAYER_SELF, player.position(), player, 45.0, 6.0, AlertSoundData.LOW);
                    bus.post(sEvent);
                    PacketDistributor.sendToPlayer(player, new S2CSoundPacket(sEvent.volume));
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerEatOrDrink(LivingEntityUseItemEvent.Tick event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            if (event.getItem().has(DataComponents.FOOD)) {
                StealthSoundEvent sEvent = new StealthSoundEvent(StealthSoundEvent.Type.PLAYER_SELF, player.position(), player, 41.0, 2.0, AlertSoundData.LOW);
                bus.post(sEvent);
                PacketDistributor.sendToPlayer(player, new S2CSoundPacket(sEvent.volume));
            } else if (event.getItem().is(Items.POTION)) {
                StealthSoundEvent sEvent = new StealthSoundEvent(StealthSoundEvent.Type.PLAYER_SELF, player.position(), player, 30.0, 2.0, AlertSoundData.LOW);
                bus.post(sEvent);
                PacketDistributor.sendToPlayer(player, new S2CSoundPacket(sEvent.volume));
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerFinishBreaking(BlockEvent.BreakEvent event) {
        if (event.getPlayer() instanceof ServerPlayer player) {
            StealthSoundEvent sEvent = new StealthSoundEvent(StealthSoundEvent.Type.PLAYER_SELF, event.getPos().getCenter(), player, 44.0, 5.5, AlertSoundData.LOW);
            bus.post(sEvent);
            PacketDistributor.sendToPlayer(player, new S2CSoundPacket(sEvent.volume));
        }
    }

    @SubscribeEvent
    public static void onPlayerPlace(BlockEvent.EntityPlaceEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            StealthSoundEvent sEvent = new StealthSoundEvent(StealthSoundEvent.Type.PLAYER_SELF, event.getPos().getCenter(), player, 42.0, 4.0, AlertSoundData.LOW);
            bus.post(sEvent);
            PacketDistributor.sendToPlayer(player, new S2CSoundPacket(sEvent.volume));
        }
    }

    @SubscribeEvent
    public static void onPlayerTeleport(EntityTeleportEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            Vec3 prev = event.getPrev();
            Vec3 target = event.getTarget();
            StealthSoundEvent sEventE = new StealthSoundEvent(StealthSoundEvent.Type.ENVIRONMENT, prev, player, 42.0, 3.0, AlertSoundData.LOW);
            bus.post(sEventE);
            StealthSoundEvent sEventP = new StealthSoundEvent(StealthSoundEvent.Type.PLAYER_SELF, target, player, 42.0, 3.0, AlertSoundData.LOW);
            bus.post(sEventP);
            PacketDistributor.sendToPlayer(player, new S2CSoundPacket(sEventP.volume));
        }
    }

    @SubscribeEvent
    public static void onProjectileImpact(ProjectileImpactEvent event) {
        Projectile projectile = event.getProjectile();
        if (projectile.getOwner() instanceof ServerPlayer player && !player.isCreative() && !player.isSpectator()) {
            if (event.getRayTraceResult().getType() == HitResult.Type.ENTITY) return;
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

//    @SubscribeEvent
//    public static void onPlayerUse(LivingEntityUseItemEvent.Finish event) {
//        if (event.getEntity() instanceof ServerPlayer player) {
//            ItemStack item = event.getItem();
//            if (item.is(Items.FIREWORK_ROCKET)) {
//                bus.post(new StealthSoundEvent(StealthSoundEvent.Type.PLAYER_SELF, player.position(), player, 80.0, 24.0, AlertSoundData.MEDIUM));
//                PacketDistributor.sendToPlayer(player, new S2CSoundPacket(80.0));
//            } else if (item.is(Items.FLINT_AND_STEEL)) {
//                bus.post(new StealthSoundEvent(StealthSoundEvent.Type.PLAYER_SELF, player.position(), player, 28.0, 3.0, AlertSoundData.LOW));
//                PacketDistributor.sendToPlayer(player, new S2CSoundPacket(28.0));
//            } else if (item.is(Items.FISHING_ROD)) {
//                bus.post(new StealthSoundEvent(StealthSoundEvent.Type.PLAYER_SELF, player.position(), player, 28.0, 3.0, AlertSoundData.LOW));
//                PacketDistributor.sendToPlayer(player, new S2CSoundPacket(28.0));
//            }
//        }
//    }

    @SubscribeEvent
    public static void onPlayerRightClickItem(PlayerInteractEvent.RightClickItem event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            InteractionHand hand = event.getHand();
            ItemStack item = player.getItemInHand(hand);
            if (item.is(Items.FISHING_ROD)) {
                StealthSoundEvent sEvent = new StealthSoundEvent(StealthSoundEvent.Type.PLAYER_SELF, player.position(), player, 28.0, 3.0, AlertSoundData.LOW);
                bus.post(sEvent);
                PacketDistributor.sendToPlayer(player, new S2CSoundPacket(sEvent.volume));
            } else if (item.is(Items.FIREWORK_ROCKET)) {
                if (player.isFallFlying()) {
                    StealthSoundEvent sEvent = new StealthSoundEvent(StealthSoundEvent.Type.PLAYER_SELF, player.position(), player, 80.0, 24.0, AlertSoundData.MEDIUM);
                    bus.post(sEvent);
                    PacketDistributor.sendToPlayer(player, new S2CSoundPacket(sEvent.volume));
                }
            }
        }
    }

    // 开容器或开关门
    @SubscribeEvent
    public static void onPlayerOpen(PlayerInteractEvent.RightClickBlock event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            ItemStack itemStack = event.getItemStack();
            if (!player.isShiftKeyDown() || event.getItemStack().isEmpty()) {
                Level level = player.level();
                BlockPos pos = event.getPos();
                BlockState state = level.getBlockState(event.getPos());
                if (state.is(Blocks.CHEST) || state.is(Blocks.TRAPPED_CHEST) || state.is(Blocks.ENDER_CHEST)) {
                    if (level.getBlockEntity(pos) instanceof ChestBlockEntity entity) {
                        if (ChestBlock.isChestBlockedAt(level, pos)) return;
                        activeContainers.put(player.getUUID(), new OpenedContainerInfo(pos, entity));
                        StealthSoundEvent sEvent = new StealthSoundEvent(StealthSoundEvent.Type.PLAYER_SELF, pos.getCenter(), player, 36.0, 5.0, AlertSoundData.LOW);
                        bus.post(sEvent);
                        PacketDistributor.sendToPlayer(player, new S2CSoundPacket(sEvent.volume));
                    }
                } else if (state.is(Blocks.BARREL)) {
                    if (level.getBlockEntity(pos) instanceof BarrelBlockEntity entity) {
                        activeContainers.put(player.getUUID(), new OpenedContainerInfo(pos, entity));
                        StealthSoundEvent sEvent = new StealthSoundEvent(StealthSoundEvent.Type.PLAYER_SELF, pos.getCenter(), player, 36.0, 4.0, AlertSoundData.LOW);
                        bus.post(sEvent);
                        PacketDistributor.sendToPlayer(player, new S2CSoundPacket(sEvent.volume));
                    }
                } else if (state.is(BlockTags.WOODEN_DOORS)) {
                    if (state.getValue(DoorBlock.HALF) == DoubleBlockHalf.LOWER) {
                        pos = pos.above();
                    }
                    StealthSoundEvent sEvent = new StealthSoundEvent(StealthSoundEvent.Type.PLAYER_SELF, pos.getCenter(), player, 32.0, 3.0, AlertSoundData.LOW);
                    bus.post(sEvent);
                    PacketDistributor.sendToPlayer(player, new S2CSoundPacket(sEvent.volume));

                } else if (state.is(BlockTags.WOODEN_TRAPDOORS)) {
                    StealthSoundEvent sEvent = new StealthSoundEvent(StealthSoundEvent.Type.PLAYER_SELF, pos.getCenter(), player, 30.0, 2.8, AlertSoundData.LOW);
                    bus.post(sEvent);
                    PacketDistributor.sendToPlayer(player, new S2CSoundPacket(sEvent.volume));
                } else if (state.is(BlockTags.FENCE_GATES)) {
                    StealthSoundEvent sEvent = new StealthSoundEvent(StealthSoundEvent.Type.PLAYER_SELF, pos.getCenter(), player, 29.0, 2.5, AlertSoundData.LOW);
                    bus.post(sEvent);
                    PacketDistributor.sendToPlayer(player, new S2CSoundPacket(sEvent.volume));
                } else if (state.is(Blocks.LEVER)) {
                    StealthSoundEvent sEvent = new StealthSoundEvent(StealthSoundEvent.Type.PLAYER_SELF, pos.getCenter(), player, 28.0, 2.5, AlertSoundData.LOW);
                    bus.post(sEvent);
                    PacketDistributor.sendToPlayer(player, new S2CSoundPacket(sEvent.volume));
                } else if (state.is(BlockTags.BUTTONS)) {
                    StealthSoundEvent sEvent = new StealthSoundEvent(StealthSoundEvent.Type.PLAYER_SELF, pos.getCenter(), player, 28.0, 2.3, AlertSoundData.LOW);
                    bus.post(sEvent);
                    PacketDistributor.sendToPlayer(player, new S2CSoundPacket(sEvent.volume));
                } else if (itemStack.is(Items.FIREWORK_ROCKET)) {
                    StealthSoundEvent sEvent = new StealthSoundEvent(StealthSoundEvent.Type.PLAYER_SELF, player.position(), player, 80.0, 24.0, AlertSoundData.MEDIUM);
                    bus.post(sEvent);
                    PacketDistributor.sendToPlayer(player, new S2CSoundPacket(sEvent.volume));
                }
            } else {
                if (itemStack.is(Items.FIREWORK_ROCKET)) {
                    StealthSoundEvent sEvent = new StealthSoundEvent(StealthSoundEvent.Type.PLAYER_SELF, player.position(), player, 80.0, 24.0, AlertSoundData.MEDIUM);
                    bus.post(sEvent);
                    PacketDistributor.sendToPlayer(player, new S2CSoundPacket(sEvent.volume));
                }
            }
        }
    }

    // 关闭容器
    @SubscribeEvent
    public static void onPlayerClose(PlayerContainerEvent.Close event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            UUID playerUUID = player.getUUID();
            if (activeContainers.containsKey(playerUUID)) {
                OpenedContainerInfo info = activeContainers.remove(playerUUID);
                BlockEntity entity = player.level().getBlockEntity(info.pos());
                BlockEntity currentEntity = player.level().getBlockEntity(info.pos());
                boolean isValid = false;
                if (entity instanceof BarrelBlockEntity && entity == currentEntity) {
                    isValid = true;
                } else if (entity instanceof ChestBlockEntity && currentEntity instanceof ChestBlockEntity) {
                    if (currentEntity == entity) {
                        isValid = true;
                    } else {
                        BlockState state = player.level().getBlockState(info.pos());
                        if (state.is(Blocks.CHEST)) {
                            Container provider = ChestBlock.getContainer((((ChestBlock) state.getBlock())), state, player.level(), info.pos(), true);
                            if (provider != null) {
                                isValid = true;
                            }
                        }
                    }
                }
                if (isValid) {
                    if (entity == info.blockEntity) {
                        if (entity instanceof ChestBlockEntity) {
                            StealthSoundEvent sEvent = new StealthSoundEvent(StealthSoundEvent.Type.PLAYER_SELF, info.pos().getCenter(), player, 34.0, 5.0, AlertSoundData.LOW);
                            bus.post(sEvent);
                            PacketDistributor.sendToPlayer(player, new S2CSoundPacket(sEvent.volume));
                        } else if (entity instanceof BarrelBlockEntity) {
                            StealthSoundEvent sEvent = new StealthSoundEvent(StealthSoundEvent.Type.PLAYER_SELF, info.pos().getCenter(), player, 34.0, 4.0, AlertSoundData.LOW);
                            bus.post(sEvent);
                            PacketDistributor.sendToPlayer(player, new S2CSoundPacket(sEvent.volume));
                        }
                    }
                }
            }
        }
    }
}
