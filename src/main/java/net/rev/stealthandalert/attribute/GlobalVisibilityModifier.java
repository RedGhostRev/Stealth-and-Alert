package net.rev.stealthandalert.attribute;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.ItemAttributeModifierEvent;
import net.rev.stealthandalert.StealthAndAlert;
import net.rev.stealthandalert.compat.SupportedMods;
import net.rev.stealthandalert.compat.twilightforest.TwilightForestCompat;
import net.rev.stealthandalert.registry.ModDataMaps;
import net.rev.stealthandalert.util.CommonUtils;
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
    public static void onArmorVisibilityAttribute(ItemAttributeModifierEvent event) {
        ItemStack stack = event.getItemStack();
        if (!(stack.getItem() instanceof ArmorItem armorItem)) return;
        Double penalty = stack.getItemHolder().getData(ModDataMaps.ARMOR_VISIBILITY_MODIFIER);
        if (SupportedMods.TWILIGHTFOREST.isLoaded()) {
            boolean shouldInvisible = TwilightForestCompat.hasEmperorsCloth(stack);
            if (shouldInvisible) penalty = 0.0;
        }

        if (penalty == null) {
            penalty = switch (armorItem.getEquipmentSlot()) {
                case HEAD -> 0.05;
                case CHEST -> 0.15;
                case LEGS -> 0.10;
                case FEET -> 0.02;
                default -> 0.0;
            };
        }

        if (penalty >= 0.0) {
            EquipmentSlot slot = armorItem.getEquipmentSlot();
            ResourceLocation modifierId =
                    switch (slot) {
                        case HEAD -> HEAD_VISIBILITY_ID;
                        case CHEST -> CHEST_VISIBILITY_ID;
                        case LEGS -> LEGS_VISIBILITY_ID;
                        case FEET -> FEET_VISIBILITY_ID;
                        default -> null;
                    };
            if (modifierId == null) return;
            if (penalty > 0.0 && stack.isEnchanted()) penalty += 0.1;
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

    public static final ResourceLocation WEAPON_ASSASSINATION_ID = ResourceLocation.fromNamespaceAndPath(
            StealthAndAlert.MOD_ID, "weapon_assassination_damage"
    );

    @SubscribeEvent
    public static void onWeaponAssassinationAttribute(ItemAttributeModifierEvent event) {
        ItemStack stack = event.getItemStack();
        float multiplier = CommonUtils.getAssassinationTotalMultiplier(stack);
        if (multiplier <= 0F) return;
        double baseAttackDamage = 0.0;
        for (ItemAttributeModifiers.Entry entry : event.getModifiers()) {
            if (entry.attribute().equals(Attributes.ATTACK_DAMAGE)) {
                baseAttackDamage = entry.modifier().amount();
                break;
            }
        }
        if (baseAttackDamage <= 0.0) return;
        double finalWeaponAssassinationDamage = baseAttackDamage * multiplier;
        event.addModifier(
                ModAttributes.ASSASSINATION_DAMAGE,
                new AttributeModifier(
                    WEAPON_ASSASSINATION_ID,
                        finalWeaponAssassinationDamage,
                        AttributeModifier.Operation.ADD_VALUE
                ),
                EquipmentSlotGroup.MAINHAND
        );
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
                1000.0,
                AttributeModifier.Operation.ADD_VALUE
        );
    }
}
