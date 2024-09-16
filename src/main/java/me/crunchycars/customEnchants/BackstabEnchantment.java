package me.crunchycars.customEnchants;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class BackstabEnchantment extends CustomEnchantment {

    public BackstabEnchantment(int tier) {
        super("§9Backstab " + tier, tier); // Backstab enchantment with tiers
    }

    @Override
    public boolean canApplyTo(Material material) {
        switch (getTier()) {
            case 1:
                return isAnySword(material); // Tier 1 can be applied to any sword
            case 2:
                return isIronOrAboveSword(material); // Tier 2 can be applied to iron swords and above
            case 3:
                return isDiamondOnlySword(material); // Tier 3 can be applied to diamond swords only
            default:
                return false;
        }
    }

    private boolean isAnySword(Material material) {
        return material.name().endsWith("_SWORD");
    }

    private boolean isIronOrAboveSword(Material material) {
        return material == Material.IRON_SWORD || material == Material.DIAMOND_SWORD || material == Material.NETHERITE_SWORD;
    }

    private boolean isDiamondOnlySword(Material material) {
        return material == Material.DIAMOND_SWORD || material == Material.NETHERITE_SWORD;
    }

    @Override
    public void applyEffect(Player player, LivingEntity target, ItemStack item) {
        // Check if the attacker is behind the target
        if (isBehindTarget(player, target)) {
            // Get the tier of the enchantment
            int tier = getTier();
            double damageMultiplier = getDamageMultiplier(tier);

            // Calculate additional damage
            double additionalDamage = target.getLastDamage() * damageMultiplier;
            target.damage(additionalDamage, player); // Apply additional damage

            // Notify the player about the Backstab
            player.sendMessage("§eBackstab activated! Additional damage dealt.");
        }
    }

    private boolean isBehindTarget(Player player, LivingEntity target) {
        // Calculate the vector from the target to the player
        Vector toPlayer = player.getLocation().toVector().subtract(target.getLocation().toVector()).normalize();
        // Calculate the direction the target is facing
        Vector targetDirection = target.getLocation().getDirection().normalize();
        // Check if the player is behind the target (dot product < -0.5 indicates behind)
        return targetDirection.dot(toPlayer) < -0.5;
    }

    private double getDamageMultiplier(int tier) {
        switch (tier) {
            case 1:
                return 0.1; // 20% more damage for Tier 1
            case 2:
                return 0.15; // 40% more damage for Tier 2
            case 3:
                return 0.20; // 60% more damage for Tier 3
            default:
                return 0.0;
        }
    }
}