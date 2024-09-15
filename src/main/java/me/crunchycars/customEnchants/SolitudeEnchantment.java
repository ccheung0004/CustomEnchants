package me.crunchycars.customEnchants;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SolitudeEnchantment extends CustomEnchantment {

    public SolitudeEnchantment(int tier) {
        super("§6Solitude " + tier, tier); // Legendary enchantment
    }

    @Override
    public boolean canApplyTo(Material material) {
        switch (getTier()) {
            case 1:
                return isAnySword(material); // Solitude I can be applied to any sword
            case 2:
                return isIronOrAboveSword(material); // Solitude II can be applied to iron swords and above
            case 3:
                return isDiamondSword(material); // Solitude III can be applied to diamond swords and above
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
            Player targetPlayer = (Player) target;

            // Check if Silence enchantment is active
            if (SilenceManager.isSilenced(targetPlayer)) {
                // Increase the silence duration based on Solitude tier
                int extraDuration = getSolitudeExtraDuration();
                SilenceManager.extendSilence(targetPlayer, extraDuration); // Extend the silence

                player.sendMessage("§d§l(!) §dSolitude increased the Silence duration on " + targetPlayer.getName() + " by " + extraDuration + " seconds!");
            }
        }
    }

    private int getSolitudeExtraDuration() {
        // Solitude I adds 1 second, II adds 2 seconds, III adds 3 seconds
        return getTier();
    }
}