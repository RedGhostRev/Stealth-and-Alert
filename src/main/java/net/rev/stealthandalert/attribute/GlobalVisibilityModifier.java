package net.rev.stealthandalert.attribute;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.ItemAttributeModifierEvent;
import net.rev.stealthandalert.StealthAndAlert;
import net.rev.stealthandalert.util.StealthUtils;

@EventBusSubscriber(modid = StealthAndAlert.MOD_ID)
public class GlobalVisibilityModifier {
    private static final ResourceLocation HEAD_VISIBILITY_ID =
            ResourceLocation.fromNamespaceAndPath(StealthAndAlert.MOD_ID, "armor_visibility_head");
    private static final ResourceLocation CHEST_VISIBILITY_ID =
            ResourceLocation.fromNamespaceAndPath(StealthAndAlert.MOD_ID, "armor_visibility_chest");
    private static final ResourceLocation LEGS_VISIBILITY_ID =
            ResourceLocation.fromNamespaceAndPath(StealthAndAlert.MOD_ID, "armor_visibility_legs");
    private static final ResourceLocation FEET_VISIBILITY_ID =
            ResourceLocation.fromNamespaceAndPath(StealthAndAlert.MOD_ID, "armor_visibility_feet");

    @SubscribeEvent
    public static void onArmorAttribute(ItemAttributeModifierEvent event) {
        ItemStack stack = event.getItemStack();
        if (stack.getItem() instanceof ArmorItem armorItem) {
            EquipmentSlot slot = armorItem.getEquipmentSlot();
            double penalty;
            ResourceLocation modifierId;
            switch (slot) {
                case HEAD -> {
                    penalty = 0.05;
                    modifierId = HEAD_VISIBILITY_ID;
                }
                case CHEST -> {
                    penalty = 0.15;
                    modifierId = CHEST_VISIBILITY_ID;
                }
                case LEGS -> {
                    penalty = 0.10;
                    modifierId = LEGS_VISIBILITY_ID;
                }
                case FEET -> {
                    penalty = 0.05;
                    modifierId = FEET_VISIBILITY_ID;
                }
                default -> {
                    return;
                }
            }
            if (stack.isEnchanted()) penalty += 0.1;

            event.addModifier(
                    ModAttributes.VISIBILITY,
                    new AttributeModifier(
                            modifierId,
                            penalty,
                            AttributeModifier.Operation.ADD_MULTIPLIED_BASE
                    ),
                    EquipmentSlotGroup.bySlot(slot)
            );
        }
    }

    public static void init() {
        MobEffects.INVISIBILITY.value().addAttributeModifier(
                ModAttributes.VISIBILITY,
                ResourceLocation.fromNamespaceAndPath(StealthAndAlert.MOD_ID, "invisibility"),
                StealthUtils.VISIBILITY_THRESHOLD / 2.0 - 1.0,
                AttributeModifier.Operation.ADD_MULTIPLIED_BASE
        );
        MobEffects.GLOWING.value().addAttributeModifier(
                ModAttributes.VISIBILITY,
                ResourceLocation.fromNamespaceAndPath(StealthAndAlert.MOD_ID, "glowing"),
                1.0,
                AttributeModifier.Operation.ADD_VALUE
        );
    }
}
