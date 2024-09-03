package me.crunchycars.customEnchants;

import org.bukkit.Material;
import org.bukkit.entity.Player;

public class SilenceEnchantment extends CustomEnchantment {

    private final int tier;

    public SilenceEnchantment(int tier) {
        super("§eSilence " + tier);
        this.tier = tier;
    }

    @Override
    public boolean canApplyTo(Material material) {
        switch (tier) {
            case 1:
                return isAnySword(material);
            case 2:
                return isIronOrAboveSword(material);
            case 3:
                return isDiamondOrNetheriteSword(material);
            default:
                return false;
        }
    }

    public int getTier() {
        return tier;
    }

    public double getActivationChance() {
        switch (tier) {
            case 1:
                return 0.10;
            case 2:
                return 0.125;
            case 3:
                return 0.15;
            default:
                return 0;
        }
    }

    private boolean isAnySword(Material material) {
        return material.name().endsWith("_SWORD");
    }

    private boolean isIronOrAboveSword(Material material) {
        return material == Material.IRON_SWORD ||
                material == Material.DIAMOND_SWORD || material == Material.NETHERITE_SWORD;
    }

    private boolean isDiamondOrNetheriteSword(Material material) {
        return material == Material.DIAMOND_SWORD || material == Material.NETHERITE_SWORD;
    }

    public void applySilence(Player target) {
        SilenceManager.applySilence(target, 1.5);
    }
}