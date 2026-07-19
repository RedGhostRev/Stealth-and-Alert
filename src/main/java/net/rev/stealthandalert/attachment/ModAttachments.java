package net.rev.stealthandalert.attachment;

import net.minecraft.world.entity.Mob;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.rev.stealthandalert.StealthAndAlert;
import net.rev.stealthandalert.config.EntityAlertConditionConfigLoader;
import net.rev.stealthandalert.config.EntityAlertConditionSettings;

import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public class ModAttachments {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, StealthAndAlert.MOD_ID);

//    public static final Supplier<AttachmentType<AlertData>> ALERT_DATA = ATTACHMENT_TYPES.register("alert_data",
//            () -> AttachmentType.builder(AlertData::createDefault).serialize(AlertData.CODEC).build());

    public static final Supplier<AttachmentType<AlertData>> ALERT_DATA = ATTACHMENT_TYPES.register(
            "alert_data",
            () -> AttachmentType.builder(holder -> {
                if (holder instanceof Mob mob) {
                    EntityAlertConditionSettings settings = EntityAlertConditionConfigLoader.get(mob.getType());
                    return new AlertData(AlertData.IDLE, Map.of(), Map.of(), Map.of(), Map.of(), Optional.empty(),
                            Optional.empty(), 0, settings.getPatienceTicks(), false, false);
                }
                return AlertData.createDefault();
            }).serialize(AlertData.CODEC).build()
    );

    public static final Supplier<AttachmentType<AlertSoundData>> ALERT_SOUND_DATA = ATTACHMENT_TYPES.register("alert_sound_data",
            () -> AttachmentType.builder(() -> AlertSoundData.DEFAULT).serialize(AlertSoundData.CODEC).build());

    public static final Supplier<AttachmentType<InvestigateLkpData>> INVESTIGATE_LKP_DATA = ATTACHMENT_TYPES.register("investigate_lkp_data",
            () -> AttachmentType.builder(() -> InvestigateLkpData.DEFAULT).serialize(InvestigateLkpData.CODEC).build());

    public static final Supplier<AttachmentType<CrawlData>> CRAWL_DATA = ATTACHMENT_TYPES.register("crawl_data",
            () -> AttachmentType.builder(() -> CrawlData.DEFAULT).serialize(CrawlData.CODEC).build());

    public static final Supplier<AttachmentType<AssassinationData>> ASSASSINATION_DATA = ATTACHMENT_TYPES.register("assassinate_data",
            () -> AttachmentType.builder(() -> AssassinationData.DEFAULT).build());

    public static final Supplier<AttachmentType<EventListenerData>> EVENT_LISTENER_DATA = ATTACHMENT_TYPES.register("event_listener_data",
            () -> AttachmentType.builder(EventListenerData::createDefault).build());

    public static void register(IEventBus eventBus) {
        ATTACHMENT_TYPES.register(eventBus);
    }
}
