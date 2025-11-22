package net.rev.stealthandalert.event;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import net.rev.stealthandalert.StealthAndAlert;
import net.rev.stealthandalert.datagen.LangKeys;
import net.rev.stealthandalert.util.ModTags;

import java.util.List;

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
}
