package net.rev.stealthandalert.compat.curios;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.rev.stealthandalert.attribute.ModAttributes;
import net.rev.stealthandalert.compat.SupportedMods;
import net.rev.stealthandalert.registry.ModDataMaps;
import top.theillusivec4.curios.api.CuriosCapability;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;

public class CuriosCompat {
    public static final SupportedMods mod = SupportedMods.CURIOS;

    public static void registerCurios(RegisterCapabilitiesEvent event) {
        for (Item item : BuiltInRegistries.ITEM) {
            event.registerItem(
                    CuriosCapability.ITEM,
                    (stack, context) -> new ICurio() {
                        @Override
                        public ItemStack getStack() {
                            return stack;
                        }

                        @Override
                        public void curioTick(SlotContext slotContext) {
                            ICurio.super.curioTick(slotContext);
                        }

                        @Override
                        public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext slotContext, ResourceLocation id) {
                            HashMultimap<Holder<Attribute>, AttributeModifier> modifiers = HashMultimap.create(
                                    ICurio.super.getAttributeModifiers(slotContext, id)
                            );

                            Double modifier = stack.getItemHolder().getData(ModDataMaps.CURIOS_ASSASSINATION_MODIFIER);
                            if (modifier != null && modifier != 0.0) {
                                modifiers.put(
                                        ModAttributes.ASSASSINATION_DAMAGE,
                                        new AttributeModifier(
                                                id,
                                                modifier,
                                                AttributeModifier.Operation.ADD_MULTIPLIED_BASE
                                        )
                                );
                            }
                            return modifiers;
                        }
                    },
                    item
            );
        }
    }

}
