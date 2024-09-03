package me.crunchycars.customEnchants;

import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

public class BleedEnchantment extends CustomEnchantment {

    private final int tier;

    public BleedEnchantment(int tier) {
        super("§eBleed " + tier);
        this.tier = tier;
    }

    @Override
    public boolean canApplyTo(Material material) {
        switch (tier) {
            case 1:
                return isSwordOrAxe(material);
            case 2:
                return isIronOrAboveSwordOrAxe(material);
            case 3:
                return isDiamondOrNetheriteSwordOrAxe(material);
            default:
                return false;
        }
    }

    private boolean isSwordOrAxe(Material material) {
        return material == Material.WOODEN_SWORD || material == Material.STONE_SWORD ||
                material == Material.IRON_SWORD || material == Material.GOLDEN_SWORD ||
                material == Material.DIAMOND_SWORD || material == Material.NETHERITE_SWORD ||
                material == Material.WOODEN_AXE || material == Material.STONE_AXE ||
                material == Material.IRON_AXE || material == Material.GOLDEN_AXE ||
                material == Material.DIAMOND_AXE || material == Material.NETHERITE_AXE;
    }

    private boolean isIronOrAboveSwordOrAxe(Material material) {
        return material == Material.IRON_SWORD ||
                material == Material.DIAMOND_SWORD || material == Material.NETHERITE_SWORD ||
                material == Material.IRON_AXE ||
                material == Material.DIAMOND_AXE || material == Material.NETHERITE_AXE;
    }

    private boolean isDiamondOrNetheriteSwordOrAxe(Material material) {
        return material == Material.DIAMOND_SWORD || material == Material.NETHERITE_SWORD ||
                material == Material.DIAMOND_AXE || material == Material.NETHERITE_AXE;
    }

}