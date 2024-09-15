package me.crunchycars.customEnchants;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DesperationEnchantment extends CustomEnchantment {

    private static final Map<UUID, Long> lastHitTimes = new HashMap<>(); // Cooldown to prevent stacking/spamming

    private final int tier;

    public DesperationEnchantment(int tier) {
        super("§eDesperation " + tier, tier);
        this.tier = tier;
    }

    @Override
    public boolean canApplyTo(Material material) {
        switch (tier) {
            case 1:
                return isAnyWeapon(material); // Tier 1 can be applied to any weapon
            case 2:
                return isIronOrAboveWeapon(material); // Tier 2 can be applied to iron weapons and above
            case 3:
                return isDiamondWeapon(material); // Tier 3 can be applied to diamond weapons only
            default:
                return false;
        }
    }

    private boolean isAnyWeapon(Material material) {
        return material.name().endsWith("_SWORD") || material.name().endsWith("_AXE");
    }

    private boolean isIronOrAboveWeapon(Material material) {
        return material == Material.IRON_SWORD || material == Material.IRON_AXE ||
                material == Material.DIAMOND_SWORD || material == Material.DIAMOND_AXE ||
                material == Material.NETHERITE_SWORD || material == Material.NETHERITE_AXE;
    }

    private boolean isDiamondWeapon(Material material) {
        return material == Material.DIAMOND_SWORD || material == Material.DIAMOND_AXE ||
                material == Material.NETHERITE_SWORD || material == Material.NETHERITE_AXE;
    }

    @Override
    public void applyEffect(Player player, LivingEntity target, ItemStack item) {
        UUID playerUUID = player.getUniqueId();

        // Prevent multiple hits registering too quickly (spam protection, 1 hit per second)
        long currentTime = System.currentTimeMillis();
        if (lastHitTimes.containsKey(playerUUID) && (currentTime - lastHitTimes.get(playerUUID)) < 1000) {
            return; // Ignore hits if they are too close in time
        }
        lastHitTimes.put(playerUUID, currentTime); // Update last hit time

        // Calculate damage boost based on player's current health (lower health = more damage)
        double healthPercentage = player.getHealth() / player.getAttribute(org.bukkit.attribute.Attribute.GENERIC_MAX_HEALTH).getValue();
        double damageBoost = getDamageBoost(healthPercentage);

        target.damage(damageBoost, player);

        // Inform the player about the damage boost
    }

    private double getDamageBoost(double healthPercentage) {
        // Different damage scaling based on the tier of the enchantment
        double scalingFactor;
        switch (tier) {
            case 1:
                scalingFactor = 1.0; // Tier 1 has a lower scaling factor
                break;
            case 2:
                scalingFactor = 1.05; // Tier 2 increases the scaling factor
                break;
            case 3:
                scalingFactor = 1.1; // Tier 3 has the highest scaling factor
                break;
            default:
                scalingFactor = 1.0; // Default scaling factor
        }
        // Calculate the damage boost based on health percentage and scaling factor
        return (1 - healthPercentage) * scalingFactor * 1.5; // The lower the health, the more damage
    }
}