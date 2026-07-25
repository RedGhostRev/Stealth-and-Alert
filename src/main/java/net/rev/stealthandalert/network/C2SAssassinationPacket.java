package net.rev.stealthandalert.network;


import io.netty.buffer.ByteBuf;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.rev.stealthandalert.StealthAndAlert;
import net.rev.stealthandalert.attachment.AssassinationData;
import net.rev.stealthandalert.attachment.ModAttachments;
import net.rev.stealthandalert.common.animation.AssassinationManager;
import net.rev.stealthandalert.common.animation.AssassinationScriptFactory;
import net.rev.stealthandalert.common.animation.AssassinationSession;
import net.rev.stealthandalert.common.assassination.AssassinationContext;
import net.rev.stealthandalert.common.assassination.AssassinationDataRegistry;
import net.rev.stealthandalert.common.assassination.AssassinationRegistry;
import net.rev.stealthandalert.config.CommonConfigs;
import net.rev.stealthandalert.config.EntityAlertConditionConfigLoader;
import net.rev.stealthandalert.config.EntityAlertConditionSettings;
import net.rev.stealthandalert.damagetype.AssassinationDamageSource;
import net.rev.stealthandalert.datagen.LangKeys;
import net.rev.stealthandalert.util.AssassinationHandler;
import net.rev.stealthandalert.util.ModTags;
import net.rev.stealthandalert.util.SpeedHandler;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;

public record C2SAssassinationPacket(Optional<UUID> playerUUID, int targetId) implements CustomPacketPayload {
    public static final Type<C2SAssassinationPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(
            StealthAndAlert.MOD_ID, "c2s_assassination_packet"
    ));

    public static final StreamCodec<ByteBuf, C2SAssassinationPacket> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.optional(UUIDUtil.STREAM_CODEC), C2SAssassinationPacket::playerUUID,
                    ByteBufCodecs.INT, C2SAssassinationPacket::targetId,
                    C2SAssassinationPacket::new
            );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(C2SAssassinationPacket payload, IPayloadContext context) {
        ServerPlayer player = ((ServerPlayer) context.player());
        Entity entity = player.level().getEntity(payload.targetId());
        if (!(entity instanceof LivingEntity target)) return;
        ModTags.PriorityCategory category =
                AssassinationHandler.canAssassinate(player, payload.playerUUID(), payload.targetId(), player.getData(ModAttachments.ASSASSINATION_DATA).isAssassinating());
        if (category != null) {
            if (!CommonConfigs.ASSASSINATION.alwaysSuccess.get() && target.getType().is(ModTags.Entities.SEEKERS)) {
                EntityAlertConditionSettings settings = EntityAlertConditionConfigLoader.get(target.getType());
                if (!(settings.detection().ignoreBaby() && target.isBaby())) {
                    double chance = settings.getSuccessChance();
                    if (target.getRandom().nextDouble() >= chance) {
                        AssassinationDamageSource source = AssassinationDamageSource.getSource(player, target, LangKeys.ASSASSINATION, AssassinationHandler.AssassinateHand.RIGHT_HAND);
                        entity.hurt(source, 0F);
                        target.doHurtTarget(player);
                        return;
                    }
                }
            }
            AssassinationHandler.AssassinateHand hand = AssassinationHandler.getHand(player, category);

            double actualDistance = AssassinationHandler.
                    getBoxToBoxDistance(player, target, 1);
            boolean inAir = !player.onGround() && !player.isInWater();
            Pose currentPose = player.getPose();
            double horizontalSpeed = SpeedHandler.getSpeed(player.getUUID());
            double targetHeight = target.getBbHeight();
            AssassinationContext assassinationCtx = new AssassinationContext(
                    actualDistance,
                    inAir,
                    currentPose,
                    horizontalSpeed,
                    targetHeight
            );
            AssassinationDataRegistry.WeaponProfile profile = AssassinationRegistry.getProfile(category);
            if (profile == null) {
                return;
            }
            Optional<AssassinationDataRegistry.DistanceTier> matchedTierOpt = profile.matchTier(actualDistance, hand == AssassinationHandler.AssassinateHand.DUAL_HAND);
            if (matchedTierOpt.isEmpty()) {
                return;
            }
            AssassinationDataRegistry.DistanceTier tier = matchedTierOpt.get();
            Optional<AssassinationDataRegistry.AnimationEntry> animOpt = tier.getRandomValidAnimation(assassinationCtx, player.getRandom());
            if (animOpt.isEmpty()) {
                return;
            }
            AssassinationDataRegistry.AnimationEntry animEntry = animOpt.get();
            ResourceLocation anim = animEntry.animRL();

            AssassinationData data = new AssassinationData(
                    payload.playerUUID(),
                    payload.targetId(),
                    true,
                    context.player().level().getGameTime(),
                    anim,
                    hand
            );
            player.setData(ModAttachments.ASSASSINATION_DATA, data);
            AssassinationSession currentBattle = new AssassinationSession(player,
                    target,
                    AssassinationScriptFactory.createScript(anim, hand));
            AssassinationManager.startSession(currentBattle);
            PacketDistributor.sendToAllPlayers(new S2CAssassinationPacket(data));
            AssassinationHandler.lockTarget(player.level(), payload.targetId(), currentBattle.script.getTotalTicks());
        } else {
            AssassinationHandler.unlockTarget(player.level(), payload.targetId());
        }
    }
}
