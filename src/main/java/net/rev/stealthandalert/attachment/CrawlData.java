package net.rev.stealthandalert.attachment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record CrawlData(boolean isCrawling) {
    public static final Codec<CrawlData> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.BOOL.optionalFieldOf("is_crawling", false).forGetter(CrawlData::isCrawling)
            ).apply(instance, CrawlData::new)
    );

    public static final CrawlData DEFAULT = new CrawlData(false);
}
