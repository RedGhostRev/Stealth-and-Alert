package net.rev.stealthandalert.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.rev.stealthandalert.StealthAndAlert;
import net.rev.stealthandalert.attribute.ModAttributes;
import net.rev.stealthandalert.client.gui.overlay.SoundWaveOverlay;
import org.jetbrains.annotations.NotNull;

public record S2CSoundPacket(double volume) implements CustomPacketPayload {
    public static final Type<S2CSoundPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(
            "s2c_sound_packet", StealthAndAlert.MOD_ID
    ));

    public static final StreamCodec<FriendlyByteBuf, S2CSoundPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.DOUBLE, S2CSoundPacket::volume,
            S2CSoundPacket::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(S2CSoundPacket payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();
            double multiplier = player.getAttributeValue(ModAttributes.SOUND_MULTIPLIER);
            double volume = payload.volume * multiplier;
            SoundWaveOverlay.receiveRawSound(volume);
        });
    }
}
