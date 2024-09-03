package me.crunchycars.customEnchants;

import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

public class VitalityEnchantment extends CustomEnchantment {

    private final int tier;

    public VitalityEnchantment(int tier) {
        super("§6Vitality " + tier);
        this.tier = tier;
    }

    @Override
    public boolean canApplyTo(Material material) {
        switch (tier) {
            case 1:
                return material == Material.LEATHER_HELMET || material == Material.LEATHER_CHESTPLATE ||
                        material == Material.LEATHER_LEGGINGS || material == Material.LEATHER_BOOTS ||
                        material == Material.CHAINMAIL_HELMET || material == Material.CHAINMAIL_CHESTPLATE ||
                        material == Material.CHAINMAIL_LEGGINGS || material == Material.CHAINMAIL_BOOTS ||
                        material == Material.IRON_HELMET || material == Material.IRON_CHESTPLATE ||
                        material == Material.IRON_LEGGINGS || material == Material.IRON_BOOTS ||
                        material == Material.DIAMOND_HELMET || material == Material.DIAMOND_CHESTPLATE ||
                        material == Material.DIAMOND_LEGGINGS || material == Material.DIAMOND_BOOTS;
            case 2:
                return material == Material.IRON_HELMET || material == Material.IRON_CHESTPLATE ||
                        material == Material.IRON_LEGGINGS || material == Material.IRON_BOOTS ||
                        material == Material.DIAMOND_HELMET || material == Material.DIAMOND_CHESTPLATE ||
                        material == Material.DIAMOND_LEGGINGS || material == Material.DIAMOND_BOOTS;
            case 3:
                return material == Material.DIAMOND_HELMET || material == Material.DIAMOND_CHESTPLATE ||
                        material == Material.DIAMOND_LEGGINGS || material == Material.DIAMOND_BOOTS;
            default:
                return false;
        }
    }

    public int getTier() {
        return tier;
    }
}