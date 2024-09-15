package me.crunchycars.customEnchants;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class ApprehendEnchantment extends CustomEnchantment {

    private final int tier;

    public ApprehendEnchantment(int tier) {
        super("§9Apprehend " + tier, tier); // Set enchantment name and tier
        this.tier = tier;
    }

    @Override
    public boolean canApplyTo(Material material) {
        switch (tier) {
            case 1:
                return isAnyAxe(material); // Tier 1 can be applied to any axe
            case 2:
                return isIronOrAboveAxe(material); // Tier 2 can be applied to iron axes and above
            case 3:
                return isDiamondAxe(material); // Tier 3 can be applied to diamond axes and above
            default:
                return false;
        }
    }

    private boolean isAnyAxe(Material material) {
        return material == Material.WOODEN_AXE || material == Material.STONE_AXE ||
                material == Material.IRON_AXE || material == Material.DIAMOND_AXE ||
                material == Material.NETHERITE_AXE || material == Material.GOLDEN_AXE;
    }

    private boolean isIronOrAboveAxe(Material material) {
        return material == Material.IRON_AXE || material == Material.DIAMOND_AXE || material == Material.NETHERITE_AXE;
    }

    private boolean isDiamondAxe(Material material) {
        return material == Material.DIAMOND_AXE || material == Material.NETHERITE_AXE;
    }

    @Override
    public void applyEffect(Player damager, LivingEntity target, ItemStack item) {
        // Get the chance to pull based on the tier
        double chance = getPullChance(tier);

        if (Math.random() < chance) {
            // If the chance succeeds, pull the target toward the player
            pullTargetTowardPlayer(damager, target);
            damager.sendMessage("§6§l(!) §6Apprehend has pulled " + target.getName() + " toward you!");
        }
    }

    private double getPullChance(int tier) {
        switch (tier) {
            case 1:
                return 0.05; // 5% for Tier 1
            case 2:
                return 0.075; // 7.5% for Tier 2
            case 3:
                return 0.10; // 10% for Tier 3
            default:
                return 0.0;
        }
    }

    private void pullTargetTowardPlayer(Player damager, LivingEntity target) {
        // Get the vector from the target to the player
        Vector direction = damager.getLocation().toVector().subtract(target.getLocation().toVector()).normalize();

        // Apply the pull force to the target (setY(0.5) for slight lift during pull)
        target.setVelocity(direction.multiply(1.5).setY(0.5));

        // Optional: You can log this for debugging purposes
        Bukkit.getLogger().info("[DEBUG] Apprehend: Pulling " + target.getName() + " toward " + damager.getName());
    }
}