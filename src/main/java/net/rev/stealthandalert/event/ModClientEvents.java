package net.rev.stealthandalert.event;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderNameTagEvent;
import net.neoforged.neoforge.common.util.TriState;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import net.rev.stealthandalert.StealthAndAlert;
import net.rev.stealthandalert.attachment.AlertData;
import net.rev.stealthandalert.attachment.ModAttachments;
import net.rev.stealthandalert.config.ClientConfigs;
import net.rev.stealthandalert.config.CommonConfigs;
import net.rev.stealthandalert.datagen.LangKeys;
import net.rev.stealthandalert.util.ModTags;

import java.util.List;
import java.util.UUID;

@EventBusSubscriber(modid = StealthAndAlert.MOD_ID, value = Dist.CLIENT)
public class ModClientEvents {
    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        ItemStack itemStack = event.getItemStack();
        if (itemStack.isEmpty()) {
            return;
        }
        if (itemStack.is(ModTags.Items.CAN_BACKSTAB)) {
            List<Component> lines = event.getToolTip();
            int index = -1;

            MutableComponent tooltip = Component.translatable(LangKeys.TOOLTIP_CAN_STAB);
            MutableComponent mainHandComponent = Component.translatable("item.modifiers.mainhand");
            for (int i = 0; i < lines.size(); i++) {
                if (lines.get(i).contains(mainHandComponent)) {
                    index = i + 1;
                    break;
                }
            }

            if (index < 0) {
                int enchantmentCount = itemStack.getTagEnchantments().size();
                for (int i = 0; i < lines.size(); i++) {
                    if (lines.get(i).getContents() instanceof TranslatableContents contents) {
                        if (contents.getKey().startsWith("enchantment.")) {
                            index = i + enchantmentCount;
                            break;
                        }
                    }
                }
            }
            if (index < 0) {
                index = 1;
            }

            if (index == lines.size()) {
                lines.add(tooltip);
            } else {
                lines.add(index, tooltip);
            }
        }
    }

    // DEBUG内容
    @SubscribeEvent
    public static void onRenderNameTag(RenderNameTagEvent event) {
        if (!(event.getEntity() instanceof Mob mob) || !mob.getType().is(ModTags.Entities.SEEKERS)) return;
        if (!ClientConfigs.DEBUG_MODE.get()) return;

        Player self = Minecraft.getInstance().player;
        if (self == null) return;

        AlertData data = mob.getData(ModAttachments.ALERT_DATA);
        UUID myUUID = self.getUUID();

        MutableComponent stateText = switch (data.state()) {
            case AlertData.IDLE -> Component.translatable(LangKeys.DEBUG_ALERT_STATE_IDLE);
            case AlertData.SUSPICIOUS -> Component.translatable(LangKeys.DEBUG_ALERT_STATE_SUSPICIOUS);
            case AlertData.SEARCHING -> Component.translatable(LangKeys.DEBUG_ALERT_STATE_SEARCHING);
            case AlertData.FIGHTING -> Component.translatable(LangKeys.DEBUG_ALERT_STATE_FIGHTING);
            default -> Component.translatable(LangKeys.DEBUG_UNKNOWN);
        };
        String primaryName = Component.translatable(LangKeys.DEBUG_PRIMARY_TARGET_NULL).getString();
        if (data.primaryTarget().isPresent()) {
            Player p = mob.level().getPlayerByUUID(data.primaryTarget().get());
            if (p != null) primaryName = p.getName().getString();
        }
        int stateTicks = data.stateTicks();
        int patienceTicks = data.patienceTicks();

        float myLevel = data.targetProgress().getOrDefault(myUUID, 0.0F);
        int myPState = data.targetStates().getOrDefault(myUUID, AlertData.UNTRACKED);
        Component pStateText = switch (myPState) {
            case AlertData.UNTRACKED -> Component.translatable(LangKeys.DEBUG_TARGET_ALERT_STATE_UNTRACKED);
            case AlertData.AWARE -> Component.translatable(LangKeys.DEBUG_TARGET_ALERT_STATE_AWARE);
            case AlertData.TRACKING -> Component.translatable(LangKeys.DEBUG_TARGET_ALERT_STATE_TRACKING);
            default -> Component.translatable(LangKeys.DEBUG_UNKNOWN);
        };
        MutableComponent debugHeader = stateText
                .append(" ")
                .append(pStateText)
                .append(" ")
                .append(primaryName)
                .append(" ")
                .append(Component.translatable(LangKeys.DEBUG_TARGET_ALERT_LEVEL, myLevel))
                .append(" ")
                .append(Component.translatable(LangKeys.DEBUG_ALERT_STATE_TICKS, stateTicks))
                .append(" ")
                .append(Component.translatable(LangKeys.DEBUG_PATIENCE_TICKS, patienceTicks));

        event.setContent(debugHeader);
        event.setCanRender(TriState.TRUE);
    }
}
