package me.crunchycars.customEnchants;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class EruditionEnchantment extends CustomEnchantment {

    private final int tier;

    public EruditionEnchantment(int tier) {
        super("§6Erudition " + tier, tier);
        this.tier = tier;
    }

    @Override
    public boolean canApplyTo(Material material) {
        switch (tier) {
            case 1:
                return isIronOrAboveArmor(material); // Erudition I can be applied to iron and above
            case 2:
                return isDiamondArmor(material); // Erudition II can be applied to diamond only
            default:
                return false;
        }
    }

    private boolean isIronOrAboveArmor(Material material) {
        return material == Material.IRON_HELMET || material == Material.IRON_LEGGINGS ||
                material == Material.IRON_CHESTPLATE || material == Material.IRON_BOOTS ||
                material == Material.DIAMOND_HELMET || material == Material.DIAMOND_LEGGINGS ||
                material == Material.DIAMOND_CHESTPLATE || material == Material.DIAMOND_BOOTS;
    }

    private boolean isDiamondArmor(Material material) {
        return material == Material.DIAMOND_HELMET || material == Material.DIAMOND_LEGGINGS ||
                material == Material.DIAMOND_CHESTPLATE || material == Material.DIAMOND_BOOTS;
    }

    @Override
    public void applyEffect(Player player, LivingEntity target, ItemStack item) {
        // Check if the player wearing the armor is silenced
        if (SilenceManager.isSilenced(player)) {
            return; // Do nothing if the player is silenced
        }

        // Calculate the chance to trigger based on the tier
        double healChance = tier == 1 ? 0.01 : 0.02; // 1% chance for tier 1, 2% for tier 2
        if (Math.random() < healChance) {
            double healAmount = tier == 1 ? 1.0 : 2.0; // Heal amount (0.5 hearts for tier 1, 1 heart for tier 2)
            double newHealth = Math.min(player.getHealth() + healAmount, player.getAttribute(org.bukkit.attribute.Attribute.GENERIC_MAX_HEALTH).getValue());
            player.setHealth(newHealth);
            player.sendMessage("§d§l(!) §dErudition healed you for " + healAmount / 2 + " hearts!");
        }
    }
}