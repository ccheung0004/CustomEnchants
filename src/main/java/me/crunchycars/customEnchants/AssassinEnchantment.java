package me.crunchycars.customEnchants;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class AssassinEnchantment extends CustomEnchantment {

    public AssassinEnchantment(int tier) {
        super("§aAssassin " + tier, tier); // Enchantment name with tier
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
        return material == Material.LEATHER_LEGGINGS ||
                material == Material.CHAINMAIL_LEGGINGS ||
                material == Material.IRON_LEGGINGS ||
                material == Material.GOLDEN_LEGGINGS ||
                material == Material.DIAMOND_LEGGINGS ||
                material == Material.NETHERITE_LEGGINGS;
    }

    private boolean isIronOrAbovePants(Material material) {
        return material == Material.IRON_LEGGINGS ||
                material == Material.DIAMOND_LEGGINGS ||
                material == Material.NETHERITE_LEGGINGS;
    }

    private boolean isDiamondOnlyPants(Material material) {
        return material == Material.DIAMOND_LEGGINGS ||
                material == Material.NETHERITE_LEGGINGS;
    }

    @Override
    public void applyEffect(Player player, LivingEntity target, ItemStack item) {
        // Check if the player is sneaking
        if (player.isSneaking()) {
            // Get the tier of the enchantment
            int tier = getTier();
            double damageMultiplier = getDamageMultiplier(tier);

            // Calculate the additional damage based on the current damage
            double additionalDamage = target.getLastDamage() * damageMultiplier;
            target.damage(additionalDamage, player);

            // Notify the player that Assassin activated
            player.sendMessage("§6Assassin activated! Extra damage dealt to " + target.getName() + ".");
        }
    }

    private double getDamageMultiplier(int tier) {
        switch (tier) {
            case 1:
                return 0.10; // 10% more damage
            case 2:
                return 0.15; // 15% more damage
            case 3:
                return 0.20; // 20% more damage
            default:
                return 0.0;
        }
    }
}