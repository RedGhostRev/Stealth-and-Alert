package net.rev.stealthandalert.attachment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

public record InvestigateLkpData(
        boolean isSearchingAround,
        int stayTicks,
        Optional<Vec3> targetPos
) {
    public static final Codec<InvestigateLkpData> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.BOOL.optionalFieldOf("is_searching_around", false).forGetter(InvestigateLkpData::isSearchingAround),
                    Codec.INT.optionalFieldOf("stay_ticks", 0).forGetter(InvestigateLkpData::stayTicks),
                    Vec3.CODEC.optionalFieldOf("target_pos").forGetter(InvestigateLkpData::targetPos)
            ).apply(instance, InvestigateLkpData::new)
    );

    public static final InvestigateLkpData DEFAULT = new InvestigateLkpData(false, 0, Optional.empty());
}
