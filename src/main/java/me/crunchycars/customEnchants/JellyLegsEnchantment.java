package me.crunchycars.customEnchants;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class JellyLegsEnchantment extends CustomEnchantment {

    public JellyLegsEnchantment(int tier) {
        super("§9Jelly Legs " + tier, tier); // Enchantment name and tier
    }

    @Override
    public boolean canApplyTo(Material material) {
        switch (getTier()) {
            case 1:
                return isAnyPants(material); // Tier 1 can be applied to any pants
            case 2:
                return isIronOrAbovePants(material); // Tier 2 can be applied to iron pants and above
            case 3:
                return isDiamondOnlyPants(material); // Tier 3 can be applied to diamond pants only
            default:
                return false;
        }
    }

    private boolean isAnyPants(Material material) {
        return material == Material.LEATHER_LEGGINGS || material == Material.CHAINMAIL_LEGGINGS ||
                material == Material.IRON_LEGGINGS || material == Material.DIAMOND_LEGGINGS ||
                material == Material.NETHERITE_LEGGINGS || material == Material.GOLDEN_LEGGINGS;
    }

    private boolean isIronOrAbovePants(Material material) {
        return material == Material.IRON_LEGGINGS || material == Material.DIAMOND_LEGGINGS || material == Material.NETHERITE_LEGGINGS;
    }

    private boolean isDiamondOnlyPants(Material material) {
        return material == Material.DIAMOND_LEGGINGS || material == Material.NETHERITE_LEGGINGS;
    }

    @Override
    public void applyEffect(Player player, LivingEntity target, ItemStack item) {
        // This method is not needed for Jelly Legs since it doesn't target other entities
    }

    // Method to handle fall damage negation
    public void handleFallDamage(Player player) {
        double chance = getNegateChance(getTier());
        if (Math.random() <= chance) {
            player.sendMessage("§aYour Jelly Legs negated the fall damage!");
            player.setFallDistance(0); // Negates fall damage
        }
    }

    private double getNegateChance(int tier) {
        switch (tier) {
            case 1:
                return 0.125; // 12.5% for Tier 1
            case 2:
                return 0.15;  // 15% for Tier 2
            case 3:
                return 0.175; // 17.5% for Tier 3
            default:
                return 0.0;
        }
    }
}