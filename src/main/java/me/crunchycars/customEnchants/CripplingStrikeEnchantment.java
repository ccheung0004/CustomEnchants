package me.crunchycars.customEnchants;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class CripplingStrikeEnchantment extends CustomEnchantment {

    private final int tier;

    public CripplingStrikeEnchantment(int tier) {
        super("§6Crippling Strike " + tier, tier);
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
                return isDiamondOrAboveAxe(material); // Tier 3 can be applied to diamond axes and above
            default:
                return false;
        }
    }

    private boolean isAnyAxe(Material material) {
        return material.name().endsWith("_AXE");
    }

    private boolean isIronOrAboveAxe(Material material) {
        return material == Material.IRON_AXE || material == Material.DIAMOND_AXE || material == Material.NETHERITE_AXE;
    }

    private boolean isDiamondOrAboveAxe(Material material) {
        return material == Material.DIAMOND_AXE || material == Material.NETHERITE_AXE;
    }

    @Override
    public void applyEffect(Player player, LivingEntity target, ItemStack item) {
        double chance = getSlownessChance(tier);
        if (Math.random() < chance) {
            target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20, 0)); // 1 second of Slowness I
        }
    }

    private double getSlownessChance(int tier) {
        switch (tier) {
            case 1:
                return 0.05; // 10% chance for Tier 1
            case 2:
                return 0.075; // 15% chance for Tier 2
            case 3:
                return 0.1; // 20% chance for Tier 3
            default:
                return 0;
        }
    }
}