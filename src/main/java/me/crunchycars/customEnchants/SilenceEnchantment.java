package me.crunchycars.customEnchants;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SilenceEnchantment extends CustomEnchantment {

    public SilenceEnchantment(int tier) {
        super("§eSilence " + tier, tier);
    }

    @Override
    public boolean canApplyTo(Material material) {
        switch (getTier()) {
            case 1:
                return isAnySword(material); // Silence I can be applied to any sword
            case 2:
                return isIronOrAboveSword(material); // Silence II can be applied to iron swords and above
            case 3:
                return isDiamondSword(material); // Silence III can be applied to diamond swords and above
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

    private boolean isDiamondSword(Material material) {
        return material == Material.DIAMOND_SWORD || material == Material.NETHERITE_SWORD;
    }
    @Override
    public void applyEffect(Player player, LivingEntity target, ItemStack item) {
        if (target instanceof Player) {
            double silenceChance = getSilenceChance(getTier());
            if (Math.random() < silenceChance) {
                SilenceManager.applySilence((Player) target, getTier()); // Apply Silence effect based on tier
                player.sendMessage("§c§l(!) §cYou have silenced " + target.getName() + "!");
            }
        }
    }

    private double getSilenceChance(int tier) {
        switch (tier) {
            case 1:
                return 0.025; // 7.5% chance for Silence I
            case 2:
                return 0.05;  // 10% chance for Silence II
            case 3:
                return 0.075; // 12.5% chance for Silence III
            default:
                return 0.0;
        }
    }
}