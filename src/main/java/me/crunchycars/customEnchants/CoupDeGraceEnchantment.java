package me.crunchycars.customEnchants;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CoupDeGraceEnchantment extends CustomEnchantment {

    private static final Map<UUID, Long> lastHitTimes = new HashMap<>(); // Cooldown to prevent rapid spamming
    private static final long COUP_COOLDOWN_TIME = 1000; // 1 second cooldown between hits

    private final int tier;

    public CoupDeGraceEnchantment(int tier) {
        super("§eCoup de Grace " + tier, tier);
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
        long currentTime = System.currentTimeMillis();

        // Check cooldown to prevent spam
        if (lastHitTimes.containsKey(playerUUID) && (currentTime - lastHitTimes.get(playerUUID)) < COUP_COOLDOWN_TIME) {
            return; // Ignore the hit if within the cooldown period
        }
        lastHitTimes.put(playerUUID, currentTime); // Update last hit time

        // Calculate the opponent's current health percentage
        double healthPercentage = target.getHealth() / target.getAttribute(org.bukkit.attribute.Attribute.GENERIC_MAX_HEALTH).getValue();

        // Calculate the damage boost based on opponent's remaining health
        double damageBoost = getDamageBoost(healthPercentage);

        // Apply the additional damage to the target
        target.damage(damageBoost, player);
    }

    private double getDamageBoost(double healthPercentage) {
        // The lower the opponent's health, the higher the damage boost
        // Adjust the scaling by tier: higher tier = more damage boost
        switch (tier) {
            case 1:
                return (1 - healthPercentage) * 1.3; // Tier 1: small boost
            case 2:
                return (1 - healthPercentage) * 1.4; // Tier 2: medium boost
            case 3:
                return (1 - healthPercentage) * 1.5; // Tier 3: high boost
            default:
                return 0;
        }
    }
}