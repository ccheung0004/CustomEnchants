package me.crunchycars.customEnchants;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ArsonistEnchantment extends CustomEnchantment {

    public ArsonistEnchantment(int tier) {
        super("§aArsonist " + tier, tier); // Arsonist enchantment with tiers
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
        // Check if the player is on fire
        if (player.getFireTicks() > 0) {
            // Get the tier of the enchantment
            int tier = getTier();
            double damageMultiplier = getDamageMultiplier(tier);

            // Apply the damage multiplier to the player
            double currentDamage = target.getLastDamage();
            double additionalDamage = currentDamage * damageMultiplier;
            target.damage(additionalDamage, player);

            // Notify the player that Arsonist was activated
            player.sendMessage("§cArsonist activated! Dealt " + (int)(damageMultiplier * 100) + "% more damage.");
        }
    }

    private double getDamageMultiplier(int tier) {
        switch (tier) {
            case 1:
                return 0.1; // 10% more damage for Tier 1
            case 2:
                return 0.15; // 15% more damage for Tier 2
            case 3:
                return 0.2; // 20% more damage for Tier 3
            default:
                return 0.0;
        }
    }
}