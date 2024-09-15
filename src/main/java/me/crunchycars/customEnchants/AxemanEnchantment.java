package me.crunchycars.customEnchants;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class AxemanEnchantment extends CustomEnchantment {

    public AxemanEnchantment(int tier) {
        super("§9Axeman " + tier, tier); // Name and tier of the enchantment
    }

    @Override
    public boolean canApplyTo(Material material) {
        switch (getTier()) {
            case 1:
                return isAnyAxe(material); // Tier 1 can be applied to any axe
            case 2:
                return isIronOrAboveAxe(material); // Tier 2 can be applied to iron and above
            case 3:
                return isDiamondOnlyAxe(material); // Tier 3 can be applied to diamond axes only
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

    private boolean isDiamondOnlyAxe(Material material) {
        return material == Material.DIAMOND_AXE || material == Material.NETHERITE_AXE;
    }

    @Override
    public void applyEffect(Player player, LivingEntity target, ItemStack item) {
        if (target instanceof Player) {
            Player targetPlayer = (Player) target;
            if (isHoldingSword(targetPlayer)) {
                // Increase the damage to the sword wielder based on the tier
                double damageMultiplier = 1 + (getTier() * 0.02); // Increase damage by 10% per tier
                double damage = targetPlayer.getLastDamage() * damageMultiplier;
                targetPlayer.damage(damage);
            }
        }
    }

    // Helper method to check if the player is holding a sword
    private boolean isHoldingSword(Player player) {
        ItemStack heldItem = player.getInventory().getItemInMainHand();
        return heldItem != null && heldItem.getType().name().endsWith("_SWORD");
    }
}