package me.crunchycars.customEnchants;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SaturatedEnchantment extends CustomEnchantment {

    public SaturatedEnchantment(int tier) {
        super("§aSaturated " + tier, tier);
    }

    @Override
    public boolean canApplyTo(Material material) {
        switch (getTier()) {
            case 1:
                return material.name().endsWith("_HELMET");
            case 2:
                return material == Material.IRON_HELMET || material == Material.DIAMOND_HELMET || material == Material.NETHERITE_HELMET;
            case 3:
                return material == Material.DIAMOND_HELMET || material == Material.NETHERITE_HELMET;
            default:
                return false;
        }
    }

    @Override
    public void applyEffect(Player player, LivingEntity target, ItemStack item) {
        // This effect is applied continuously while wearing the helmet
    }

    public void applySaturationEffect(Player player) {
        float saturationBoost;
        switch (getTier()) {
            case 1:
                saturationBoost = 1.0f; // Tier 1 gives a small saturation boost
                break;
            case 2:
                saturationBoost = 2.0f; // Tier 2 gives a medium saturation boost
                break;
            case 3:
                saturationBoost = 3.0f; // Tier 3 gives the highest saturation boost
                break;
            default:
                saturationBoost = 0.0f;
        }

        if (player.getFoodLevel() < 20) { // Ensure player isn't already full
            player.setSaturation(player.getSaturation() + saturationBoost); // Increase saturation level
        }
    }
}