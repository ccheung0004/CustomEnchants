package me.crunchycars.customEnchants;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

public class ConfusionEnchantment extends CustomEnchantment {

    public ConfusionEnchantment(int tier) {
        super("§aConfusion " + tier, tier);
    }

    @Override
    public boolean canApplyTo(Material material) {
        // Confusion can only be applied to axes and check material type based on tier
        switch (getTier()) {
            case 1:
                // Tier 1 can be applied to any axe
                return material.name().endsWith("_AXE");
            case 2:
                // Tier 2 can only be applied to iron axes and above
                return material == Material.IRON_AXE || material == Material.DIAMOND_AXE || material == Material.NETHERITE_AXE;
            case 3:
                // Tier 3 can only be applied to diamond axes and above
                return material == Material.DIAMOND_AXE || material == Material.NETHERITE_AXE;
            default:
                return false;
        }
    }

    @Override
    public void applyEffect(Player player, LivingEntity target, ItemStack item) {
        Random random = new Random();
        double chance = 0;

        switch (getTier()) {
            case 1:
                chance = 0.05; // 10% chance for tier 1
                break;
            case 2:
                chance = 0.075; // 15% chance for tier 2
                break;
            case 3:
                chance = 0.1; // 20% chance for tier 3
                break;
        }

        if (random.nextDouble() <= chance) {
            // Apply confusion effect to the target
            target.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 100, 0)); // Confusion effect for 5 seconds
            player.sendMessage("§cYou have inflicted confusion on your opponent!");
        }
    }
}