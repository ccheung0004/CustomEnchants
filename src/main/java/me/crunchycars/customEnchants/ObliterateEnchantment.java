package me.crunchycars.customEnchants;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class ObliterateEnchantment extends CustomEnchantment {

    private final int tier;

    public ObliterateEnchantment(int tier) {
        super("§9Obliterate " + tier, tier);
        this.tier = tier;
    }

    @Override
    public boolean canApplyTo(Material material) {
        switch (tier) {
            case 1:
                return isAnySword(material); // Tier 1 can be applied to any sword
            case 2:
                return isIronOrAboveSword(material); // Tier 2 can be applied to iron swords and above
            case 3:
                return isDiamondOrAboveSword(material); // Tier 3 can be applied to diamond swords and above
            default:
                return false;
        }
    }

    private boolean isAnySword(Material material) {
        return material == Material.WOODEN_SWORD || material == Material.STONE_SWORD ||
                material == Material.IRON_SWORD || material == Material.DIAMOND_SWORD ||
                material == Material.NETHERITE_SWORD;
    }

    private boolean isIronOrAboveSword(Material material) {
        return material == Material.IRON_SWORD || material == Material.DIAMOND_SWORD || material == Material.NETHERITE_SWORD;
    }

    private boolean isDiamondOrAboveSword(Material material) {
        return material == Material.DIAMOND_SWORD || material == Material.NETHERITE_SWORD;
    }

    @Override
    public void applyEffect(Player player, LivingEntity target, ItemStack item) {
        // Calculate the knockback strength based on the tier
        double knockbackStrength = getKnockbackStrength(tier);

        // Apply knockback to the target
        Vector knockback = player.getLocation().getDirection().normalize().multiply(knockbackStrength);
        target.setVelocity(target.getVelocity().add(knockback));
    }

    private double getKnockbackStrength(int tier) {
        switch (tier) {
            case 1:
                return 1.0; // Knockback strength for Tier 1
            case 2:
                return 1.5; // Knockback strength for Tier 2
            case 3:
                return 2.0; // Knockback strength for Tier 3
            default:
                return 1.0; // Default knockback strength
        }
    }
}