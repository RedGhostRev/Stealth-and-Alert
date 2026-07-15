package net.rev.stealthandalert.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.rev.stealthandalert.config.ClientConfigs;
import net.rev.stealthandalert.datagen.LangKeys;

import java.util.List;

public class DebugWandItem extends Item {
    public DebugWandItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);

        if (!level.isClientSide) {
            boolean currentDebug = ClientConfigs.DEBUG_MODE.turnOn.get();
            boolean newDebug = !currentDebug;

            ClientConfigs.DEBUG_MODE.turnOn.set(newDebug);
            ClientConfigs.DEBUG_MODE.turnOn.save();

            String status = newDebug ? LangKeys.DEBUG_MODE_ON : LangKeys.DEBUG_MODE_OFF;
            player.displayClientMessage(Component.translatable(status), true);
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(
                Component.translatable(LangKeys.TOOLTIP_DEBUG_WAND)
                        .withStyle(ChatFormatting.GRAY)
        );
        tooltipComponents.add(
                Component.translatable(LangKeys.TOOLTIP_DEBUG_WAND_DEBUG_MODE_DESC)
                        .withStyle(ChatFormatting.YELLOW, ChatFormatting.ITALIC)
        );

        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }
}
