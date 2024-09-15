package me.crunchycars.customEnchants;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SwordsmanEnchantment extends CustomEnchantment {

    public SwordsmanEnchantment(int tier) {
        super("§9Swordsman " + tier, tier); // Name and tier of the enchantment
    }

    @Override
    public boolean canApplyTo(Material material) {
        switch (getTier()) {
            case 1:
                return isAnySword(material); // Tier 1 can be applied to any sword
            case 2:
                return isIronOrAboveSword(material); // Tier 2 can be applied to iron and above swords
            case 3:
                return isDiamondOnlySword(material); // Tier 3 can be applied to diamond swords only
            default:
                return false;
        }
    }

    private boolean isAnySword(Material material) {
        return material == Material.WOODEN_SWORD || material == Material.STONE_SWORD ||
                material == Material.IRON_SWORD || material == Material.DIAMOND_SWORD ||
                material == Material.NETHERITE_SWORD || material == Material.GOLDEN_SWORD;
    }

    private boolean isIronOrAboveSword(Material material) {
        return material == Material.IRON_SWORD || material == Material.DIAMOND_SWORD || material == Material.NETHERITE_SWORD;
    }

    private boolean isDiamondOnlySword(Material material) {
        return material == Material.DIAMOND_SWORD || material == Material.NETHERITE_SWORD;
    }

    @Override
    public void applyEffect(Player player, LivingEntity target, ItemStack item) {
        if (target instanceof Player) {
            Player targetPlayer = (Player) target;
            if (isHoldingAxe(targetPlayer)) {
                // Increase the damage to the axe wielder based on the tier
                double damageMultiplier = 1 + (getTier() * 0.02); // Increase damage by 10% per tier
                double damage = targetPlayer.getLastDamage() * damageMultiplier;
                targetPlayer.damage(damage);
            }
        }
    }

    // Helper method to check if the player is holding an axe
    private boolean isHoldingAxe(Player player) {
        ItemStack heldItem = player.getInventory().getItemInMainHand();
        return heldItem != null && heldItem.getType().name().endsWith("_AXE");
    }
}