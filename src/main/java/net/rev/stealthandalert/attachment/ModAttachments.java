package net.rev.stealthandalert.attachment;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.rev.stealthandalert.StealthAndAlert;

import java.util.function.Supplier;

public class ModAttachments {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, StealthAndAlert.MOD_ID);

    public static final Supplier<AttachmentType<AlertData>> ALERT_DATA = ATTACHMENT_TYPES.register("alert_data",
            () -> AttachmentType.builder(AlertData::createDefault).serialize(AlertData.CODEC).build());

    public static final Supplier<AttachmentType<VisibilityData>> VISIBILITY_DATA = ATTACHMENT_TYPES.register("visibility_data",
            () -> AttachmentType.builder(() -> VisibilityData.DEFAULT).serialize(VisibilityData.CODEC).build());

    public static final Supplier<AttachmentType<CrawlData>> CRAWL_DATA = ATTACHMENT_TYPES.register("crawl_data",
            () -> AttachmentType.builder(() -> CrawlData.DEFAULT).build());

    public static void register(IEventBus eventBus) {
        ATTACHMENT_TYPES.register(eventBus);
    }
}
