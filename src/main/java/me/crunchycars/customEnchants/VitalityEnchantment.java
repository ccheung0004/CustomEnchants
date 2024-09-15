package me.crunchycars.customEnchants;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;

public class VitalityEnchantment extends CustomEnchantment {

    public VitalityEnchantment(int tier) {
        super("§6Vitality " + tier, tier);
    }

    @Override
    public boolean canApplyTo(Material material) {
        if (!isArmor(material)) {
            return false; // Ensure it can only be applied to armor
        }

        switch (getTier()) {
            case 1:
                return true; // Vitality I can be applied to any armor
            case 2:
                return isIronOrAboveArmor(material); // Vitality II can be applied to iron and above
            case 3:
                return isDiamondOrAboveArmor(material); // Vitality III can be applied to diamond and above
            default:
                return false;
        }
    }

    @Override
    public void applyEffect(Player player, LivingEntity target, ItemStack item) {
        double healthBoost = getHealthBoost(getTier());
        AttributeInstance healthAttribute = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (healthAttribute != null) {
            double newMaxHealth = healthAttribute.getBaseValue() + healthBoost;
            healthAttribute.setBaseValue(newMaxHealth);

            if (player.getHealth() > newMaxHealth) {
                player.setHealth(newMaxHealth);
            }
        }
    }

    private boolean isArmor(Material material) {
        return material.name().endsWith("_HELMET") || material.name().endsWith("_CHESTPLATE") ||
                material.name().endsWith("_LEGGINGS") || material.name().endsWith("_BOOTS");
    }

    private boolean isIronOrAboveArmor(Material material) {
        return material == Material.IRON_HELMET || material == Material.IRON_CHESTPLATE || material == Material.IRON_LEGGINGS || material == Material.IRON_BOOTS ||
                material == Material.DIAMOND_HELMET || material == Material.DIAMOND_CHESTPLATE || material == Material.DIAMOND_LEGGINGS || material == Material.DIAMOND_BOOTS ||
                material == Material.NETHERITE_HELMET || material == Material.NETHERITE_CHESTPLATE || material == Material.NETHERITE_LEGGINGS || material == Material.NETHERITE_BOOTS;
    }

    private boolean isDiamondOrAboveArmor(Material material) {
        return material == Material.DIAMOND_HELMET || material == Material.DIAMOND_CHESTPLATE || material == Material.DIAMOND_LEGGINGS || material == Material.DIAMOND_BOOTS ||
                material == Material.NETHERITE_HELMET || material == Material.NETHERITE_CHESTPLATE || material == Material.NETHERITE_LEGGINGS || material == Material.NETHERITE_BOOTS;
    }

    private double getHealthBoost(int tier) {
        switch (tier) {
            case 1: return 0.5;
            case 2: return 1.0;
            case 3: return 2.0;
            default: return 0;
        }
    }
}