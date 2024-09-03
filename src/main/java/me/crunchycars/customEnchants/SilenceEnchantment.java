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
        return material.name().endsWith("_SWORD"); // Silence can be applied only to swords
    }

    @Override
    public void applyEffect(Player player, LivingEntity target, ItemStack item) {
        if (target instanceof Player) {
            double silenceChance = getSilenceChance(getTier());
            if (Math.random() < silenceChance) {
                SilenceManager.applySilence((Player) target, getTier()); // Apply Silence effect based on tier
                player.sendMessage("§cYou have silenced " + target.getName() + "!");
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