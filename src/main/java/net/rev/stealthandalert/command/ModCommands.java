package net.rev.stealthandalert.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.rev.stealthandalert.config.EntityAlertConditionConfigLoader;
import net.rev.stealthandalert.datagen.LangKeys;

public class ModCommands {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("stealth_and_alert")
                .requires(cs -> cs.hasPermission(2))
                .then(Commands.literal("config")
                        // 1. 重载配置但不生成预设
                        .then(Commands.literal("reload")
                                .executes(context -> executeConfigReload(context, false, false, LangKeys.COMMAND_RELOAD))
                        )
                        // 2. 重载配置且生成缺失预设
                        .then(Commands.literal("regenerate_and_reload")
                                .executes(context -> executeConfigReload(context, true, false, LangKeys.COMMAND_REGENERATE))
                        )
                        // 3. 无视存在强制覆盖并重载
                        .then(Commands.literal("force_regenerate_and_reload")
                                .executes(context -> executeConfigReload(context, true, true, LangKeys.COMMAND_FORCE_REGENERATE))
                        )
                )
        );
    }

    private static int executeConfigReload(CommandContext<CommandSourceStack> context, boolean generate, boolean force, String successMessage) {
        EntityAlertConditionConfigLoader.load(generate, force);

        // 发送成功反馈
        context.getSource().sendSuccess(() ->
                        Component.empty()
                                .append(Component.translatable(LangKeys.COMMAND_MOD_ID).withStyle(ChatFormatting.BOLD, ChatFormatting.GREEN))
                                .append(" ")
                                .append(Component.translatable(successMessage)),
                true);

        return 1;
    }
}
