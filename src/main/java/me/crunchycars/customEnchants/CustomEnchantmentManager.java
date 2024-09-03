package me.crunchycars.customEnchants;

import java.util.HashMap;
import java.util.Map;

public class CustomEnchantmentManager {

    private static final Map<String, CustomEnchantment> ENCHANTMENTS = new HashMap<>();

    // Registering Swiftness Enchantment
    public static final SwiftnessEnchantment SWIFTNESS_ENCHANTMENT = new SwiftnessEnchantment();

    // Registering Vitality Enchantments
    public static final VitalityEnchantment VITALITY_TIER_1 = new VitalityEnchantment(1);
    public static final VitalityEnchantment VITALITY_TIER_2 = new VitalityEnchantment(2);
    public static final VitalityEnchantment VITALITY_TIER_3 = new VitalityEnchantment(3);

    public static void registerEnchantments() {
        ENCHANTMENTS.put(SWIFTNESS_ENCHANTMENT.getName(), SWIFTNESS_ENCHANTMENT);
        ENCHANTMENTS.put(VITALITY_TIER_1.getName(), VITALITY_TIER_1);
        ENCHANTMENTS.put(VITALITY_TIER_2.getName(), VITALITY_TIER_2);
        ENCHANTMENTS.put(VITALITY_TIER_3.getName(), VITALITY_TIER_3);
    }

    public static CustomEnchantment getEnchantment(String name) {
        return ENCHANTMENTS.get(name);
    }
}