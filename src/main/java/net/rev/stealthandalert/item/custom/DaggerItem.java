package net.rev.stealthandalert.item.custom;

import net.minecraft.core.Holder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import org.jetbrains.annotations.NotNull;


public class DaggerItem extends SwordItem {


    public DaggerItem(Tier tier, Properties properties) {
        super(tier, properties);
    }

    @Override
    public boolean supportsEnchantment(@NotNull ItemStack stack, Holder<Enchantment> enchantment) {
        if (enchantment.is(Enchantments.SWEEPING_EDGE)) {
            return false;
        }
        return super.supportsEnchantment(stack, enchantment);
    }
}
