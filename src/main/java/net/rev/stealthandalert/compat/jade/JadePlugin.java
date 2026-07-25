package net.rev.stealthandalert.compat.jade;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.rev.stealthandalert.StealthAndAlert;
import net.rev.stealthandalert.attachment.AlertData;
import net.rev.stealthandalert.attachment.ModAttachments;
import net.rev.stealthandalert.datagen.LangKeys;
import net.rev.stealthandalert.util.ModTags;
import snownee.jade.api.*;
import snownee.jade.api.config.IPluginConfig;

@WailaPlugin(StealthAndAlert.MOD_ID)
@SuppressWarnings("unused")
public class JadePlugin implements IWailaPlugin {
    public static final ResourceLocation ALERT_ID = ResourceLocation.fromNamespaceAndPath(
            StealthAndAlert.MOD_ID, "alert"
    );

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.markAsClientFeature(ALERT_ID);
        registration.registerEntityComponent(EntityAlertProvider.INSTANCE, LivingEntity.class);
    }

    enum EntityAlertProvider implements IEntityComponentProvider {
        INSTANCE;

        @Override
        public void appendTooltip(ITooltip tooltip, EntityAccessor accessor, IPluginConfig config) {
//            if (!ClientProxy.isShowDetailsPressed()) return;
            Entity entity = accessor.getEntity();
            if (!(entity instanceof LivingEntity target)) return;
            if (!target.getType().is(ModTags.Entities.SEEKERS)) return;
            Player player = accessor.getPlayer();
            if (player == null) return;
            AlertData data = target.getData(ModAttachments.ALERT_DATA);
            String state = switch (data.state()) {
                case 0 -> LangKeys.DEBUG_ALERT_STATE_IDLE;
                case 1 -> LangKeys.DEBUG_ALERT_STATE_SUSPICIOUS;
                case 2 -> LangKeys.DEBUG_ALERT_STATE_SEARCHING;
                case 3 -> LangKeys.DEBUG_ALERT_STATE_FIGHTING;
                default -> LangKeys.DEBUG_UNKNOWN;
            };
            float awareness = data.targetAwareness().getOrDefault(player.getUUID(), 0F);
            String formattedAwareness = String.format("%.2f", awareness);
            tooltip.add(
                    Component.translatable(LangKeys.JADE_ALERT_STATE)
            );
            tooltip.append(Component.translatable(state));
            tooltip.add(Component.translatable(LangKeys.JADE_ALERT_AWARENESS, formattedAwareness));
        }

        @Override
        public ResourceLocation getUid() {
            return ALERT_ID;
        }
    }
}
