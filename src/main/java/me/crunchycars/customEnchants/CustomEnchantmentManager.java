package me.crunchycars.customEnchants;

import java.util.ArrayList;
import java.util.List;

public class CustomEnchantmentManager {
    public static final CustomEnchantment SWIFTNESS_ENCHANTMENT = new SwiftnessEnchantment();
    public static final CustomEnchantment VITALITY_TIER_1 = new VitalityEnchantment(1);
    public static final CustomEnchantment VITALITY_TIER_2 = new VitalityEnchantment(2);
    public static final CustomEnchantment VITALITY_TIER_3 = new VitalityEnchantment(3);
    public static final CustomEnchantment BLEED_TIER_1 = new BleedEnchantment(1);
    public static final CustomEnchantment BLEED_TIER_2 = new BleedEnchantment(2);
    public static final CustomEnchantment BLEED_TIER_3 = new BleedEnchantment(3);
    public static final CustomEnchantment SILENCE_TIER_1 = new SilenceEnchantment(1);
    public static final CustomEnchantment SILENCE_TIER_2 = new SilenceEnchantment(2);
    public static final CustomEnchantment SILENCE_TIER_3 = new SilenceEnchantment(3);
    public static final CustomEnchantment DIVINE_LIGHTNING = new DivineLightningEnchantment();

    private static final List<CustomEnchantment> ENCHANTMENTS = new ArrayList<>();

    static {
        ENCHANTMENTS.add(SWIFTNESS_ENCHANTMENT);
        ENCHANTMENTS.add(VITALITY_TIER_1);
        ENCHANTMENTS.add(VITALITY_TIER_2);
        ENCHANTMENTS.add(VITALITY_TIER_3);
        ENCHANTMENTS.add(BLEED_TIER_1);
        ENCHANTMENTS.add(BLEED_TIER_2);
        ENCHANTMENTS.add(BLEED_TIER_3);
        ENCHANTMENTS.add(SILENCE_TIER_1);
        ENCHANTMENTS.add(SILENCE_TIER_2);
        ENCHANTMENTS.add(SILENCE_TIER_3);
        ENCHANTMENTS.add(DIVINE_LIGHTNING);
    }

    public static List<CustomEnchantment> getAllEnchantments() {
        return ENCHANTMENTS;
    }
}