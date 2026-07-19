package net.rev.stealthandalert.util;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.enchantment.ConditionalEffect;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.rev.stealthandalert.component.ModDataComponents;
import net.rev.stealthandalert.enchantment.ModEnchantmentEffects;
import org.apache.commons.lang3.mutable.MutableFloat;

import java.util.List;

public class CommonUtils {
    public static float getWeaponBaseDamage(ItemStack stack) {
        ItemAttributeModifiers modifiers = stack.get(DataComponents.ATTRIBUTE_MODIFIERS);
        if (modifiers != null) {
            for (ItemAttributeModifiers.Entry entry : modifiers.modifiers()) {
                if (entry.attribute().equals(Attributes.ATTACK_DAMAGE)) {
                    return (float) entry.modifier().amount();
                }
            }
        }
        return 0.0F;
    }

    public static float getAssassinationTotalMultiplier(ItemStack stack) {
        Float baseMultiplier = stack.getOrDefault(ModDataComponents.ASSASSINATION_BASE_MULTIPLIER, 0F);
        MutableFloat enchantBonus = new MutableFloat(0F);
        EnchantmentHelper.runIterationOnItem(stack, (enchantment, level) -> {
            List<ConditionalEffect<LevelBasedValue>> effectsList = enchantment.value().effects().get(ModEnchantmentEffects.ADD_ASSASSINATION_MULTIPLIER.get());
            if (effectsList != null) {
                for (ConditionalEffect<LevelBasedValue> conditionalEffect : effectsList) {
                    float bonus = conditionalEffect.effect().calculate(level);
                    enchantBonus.add(bonus);
                }
            }
        });
        return baseMultiplier + enchantBonus.getValue();
    }

    public static float getAssassinationDamage(float baseDamage, float multiplier) {
        return baseDamage * multiplier;
    }

    public static float getAssassinationDamage(ItemStack stack) {
        float baseDamage = getWeaponBaseDamage(stack);
        if (baseDamage <= 0F) return 0F;
        float multiplier = getAssassinationTotalMultiplier(stack);
        return getAssassinationDamage(baseDamage, multiplier);
    }

    public static boolean isPlayerPet(Entity entity, Player player, boolean withGolem) {
        if (entity instanceof OwnableEntity ownable) {
            return player.getUUID().equals(ownable.getOwnerUUID());
        }
        if (entity instanceof IronGolem golem) {
            if (withGolem) {
                return golem.isPlayerCreated();
            }
        }
        return false;
    }
}
