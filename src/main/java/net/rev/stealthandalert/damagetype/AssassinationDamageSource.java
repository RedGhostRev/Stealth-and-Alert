package net.rev.stealthandalert.damagetype;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.rev.stealthandalert.util.AssassinationHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AssassinationDamageSource extends DamageSource {
    private final String rootMessage;
    private final AssassinationHandler.AssassinateHand hand;

    public AssassinationDamageSource(Holder<DamageType> type, @Nullable Entity entity, String rootMessage, AssassinationHandler.AssassinateHand hand) {
        super(type, entity);
        this.rootMessage = rootMessage;
        this.hand = hand;
    }

    @Override
    public @NotNull Component getLocalizedDeathMessage(@NotNull LivingEntity victim) {
        LivingEntity attacker = victim.getKillCredit();
        Component vicName = victim.getDisplayName();
        if (attacker == null) {
            return Component.translatable(rootMessage, vicName);
        } else {
            Component attName = attacker.getDisplayName();
            MutableComponent defaultMes = Component.translatable(rootMessage + ".player", vicName, attName);
            if (hand == AssassinationHandler.AssassinateHand.RIGHT_HAND) {
                ItemStack item = attacker.getMainHandItem();
                return !item.isEmpty() && item.has(DataComponents.CUSTOM_NAME) ?
                        Component.translatable(rootMessage + ".item", vicName, attName, item.getDisplayName())
                        : defaultMes;
            } else if (hand == AssassinationHandler.AssassinateHand.LEFT_HAND) {
                ItemStack item = attacker.getOffhandItem();
                return !item.isEmpty() && item.has(DataComponents.CUSTOM_NAME) ?
                        Component.translatable(rootMessage + ".item", vicName, attName, item.getDisplayName())
                        : defaultMes;
            } else {
                ItemStack rightItem = attacker.getMainHandItem();
                ItemStack leftItem = attacker.getOffhandItem();
                if (!rightItem.isEmpty() && leftItem.isEmpty() && rightItem.has(DataComponents.CUSTOM_NAME))
                    return Component.translatable(rootMessage + ".item", vicName, attName, rightItem.getDisplayName());
                if (rightItem.isEmpty() && !leftItem.isEmpty() && leftItem.has(DataComponents.CUSTOM_NAME))
                    return Component.translatable(rootMessage + ".item", vicName, attName, leftItem.getDisplayName());
                if (!rightItem.isEmpty() && !leftItem.isEmpty() && (rightItem.has(DataComponents.CUSTOM_NAME) || leftItem.has(DataComponents.CUSTOM_NAME))) {
                    return Component.translatable(rootMessage + ".item.dual", vicName, attName, rightItem.getDisplayName(), leftItem.getDisplayName());
                }
                return defaultMes;
            }
        }
    }

    public static AssassinationDamageSource getSource(Player player, LivingEntity target,
                                                      String rootMessage, AssassinationHandler.AssassinateHand hand) {
        return new AssassinationDamageSource(
                target.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE)
                        .getHolderOrThrow(ModDamageTypes.ASSASSINATION),
                player,
                rootMessage,
                hand
        );
    }
}
