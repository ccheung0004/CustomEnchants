package me.crunchycars.customEnchants;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

public class PoisonEnchantment extends CustomEnchantment {

    private static final Random random = new Random();

    public PoisonEnchantment(int tier) {
        super("§9Poison " + tier, tier); // Poison enchantment with tiers
    }

    @Override
    public boolean canApplyTo(Material material) {
        return material.name().endsWith("_SWORD"); // Only applies to swords
    }

    @Override
    public void applyEffect(Player player, LivingEntity target, ItemStack item) {
        int tier = getTier();
        double chance = getPoisonChance(tier);

        if (random.nextDouble() < chance) {
            int duration = getPoisonDuration(tier);
            target.addPotionEffect(new PotionEffect(PotionEffectType.POISON, duration, 0));
            player.sendMessage("§aPoison enchantment activated! The target is now poisoned.");
        }
    }

    private double getPoisonChance(int tier) {
        switch (tier) {
            case 1:
                return 0.05;
            case 2:
                return 0.075;
            case 3:
                return 0.10;
            default:
                return 0.0;
        }
    }

    private int getPoisonDuration(int tier) {
        switch (tier) {
            case 1:
                return 40; //(20 ticks = 1 second)
            case 2:
                return 60;
            case 3:
                return 80;
            default:
                return 0;
        }
    }
}