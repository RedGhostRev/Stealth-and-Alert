package net.rev.stealthandalert.attachment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record VisibilityData(float visibility, boolean isVisible) {

    public static final Codec<VisibilityData> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.FLOAT.optionalFieldOf("visibility", 1F).forGetter(VisibilityData::visibility),
                    Codec.BOOL.optionalFieldOf("is_visible", true).forGetter(VisibilityData::isVisible)
            ).apply(instance, VisibilityData::new)
    );

    public static final VisibilityData DEFAULT = new VisibilityData(1.0F, true);
}
