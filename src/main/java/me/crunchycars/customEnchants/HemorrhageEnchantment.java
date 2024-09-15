package me.crunchycars.customEnchants;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class HemorrhageEnchantment extends CustomEnchantment {

    private final int tier;

    public HemorrhageEnchantment(int tier) {
        super("§6Hemorrhage " + tier, tier);
        this.tier = tier;
    }

    @Override
    public boolean canApplyTo(Material material) {
        switch (tier) {
            case 1:
                return isIronOrAboveWeapon(material); // Tier 1 can be applied to iron weapons and above
            case 2:
                return isDiamondOrAboveWeapon(material); // Tier 2 can be applied to diamond weapons and above
            default:
                return false;
        }
    }

    private boolean isIronOrAboveWeapon(Material material) {
        return material == Material.IRON_SWORD || material == Material.IRON_AXE ||
                material == Material.DIAMOND_SWORD || material == Material.DIAMOND_AXE ||
                material == Material.NETHERITE_SWORD || material == Material.NETHERITE_AXE;
    }

    private boolean isDiamondOrAboveWeapon(Material material) {
        return material == Material.DIAMOND_SWORD || material == Material.DIAMOND_AXE ||
                material == Material.NETHERITE_SWORD || material == Material.NETHERITE_AXE;
    }

    @Override
    public void applyEffect(Player player, LivingEntity target, ItemStack item) {
        // Check if the target is currently affected by the bleed effect
        if (BleedEnchantment.isTargetBleeding(target)) {
            double extraDamage = getExtraDamage(tier);
            // Apply extra damage directly
            target.setHealth(Math.max(0, target.getHealth() - extraDamage));
        }
    }

    private double getExtraDamage(int tier) {
        switch (tier) {
            case 1:
                return 1.0; // 0.5 hearts extra damage for Tier 1
            case 2:
                return 1.5; // 0.75 hearts extra damage for Tier 2
            default:
                return 1.0; // Default extra damage
        }
    }
}