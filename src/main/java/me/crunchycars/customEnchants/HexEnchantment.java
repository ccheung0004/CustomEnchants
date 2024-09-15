package me.crunchycars.customEnchants;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

public class HexEnchantment extends CustomEnchantment {

    private final Random random = new Random();

    public HexEnchantment(int tier) {
        super("§6Hex " + tier, tier); // The enchantment will have 3 tiers
    }

    @Override
    public boolean canApplyTo(Material material) {
        switch (getTier()) {
            case 1:
                return isAnySword(material); // Tier 1 applies to any sword
            case 2:
                return isIronOrAboveSword(material); // Tier 2 applies to iron swords and above
            case 3:
                return isDiamondSword(material); // Tier 3 applies to diamond swords only
            default:
                return false;
        }
    }

    private boolean isAnySword(Material material) {
        return material.name().endsWith("_SWORD");
    }

    private boolean isIronOrAboveSword(Material material) {
        return material == Material.IRON_SWORD || material == Material.DIAMOND_SWORD || material == Material.NETHERITE_SWORD;
    }

    private boolean isDiamondSword(Material material) {
        return material == Material.DIAMOND_SWORD || material == Material.NETHERITE_SWORD;
    }

    @Override
    public void applyEffect(Player damager, LivingEntity target, ItemStack item) {
        double chance = getHexChance(getTier());
        int duration = getWeaknessDuration(getTier());
        int amplifier = getWeaknessAmplifier(getTier());

        if (random.nextDouble() < chance) {
            if (target instanceof Player) {
                Player targetPlayer = (Player) target;
                targetPlayer.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, duration, amplifier));
                damager.sendMessage("§5§l(!) §5Hex enchantment activated! " + targetPlayer.getName() + " is now weakened.");
                targetPlayer.sendMessage("§5§l(!) §5You have been hexed by " + damager.getName() + "!");
            }
        }
    }

    private double getHexChance(int tier) {
        switch (tier) {
            case 1:
                return 0.05; // 5% chance for Tier 1
            case 2:
                return 0.05; // 7.5% chance for Tier 2
            case 3:
                return 0.075; // 10% chance for Tier 3
            default:
                return 0.0;
        }
    }

    private int getWeaknessDuration(int tier) {
        switch (tier) {
            case 1:
                return 40; // Duration in ticks (3 seconds for Tier 1)
            case 2:
                return 60; // 4 seconds for Tier 2
            case 3:
                return 80; // 5 seconds for Tier 3
            default:
                return 0;
        }
    }

    private int getWeaknessAmplifier(int tier) {
        switch (tier) {
            case 1:
                return 0; // Weakness I for Tier 1
            case 2:
                return 0; // Weakness II for Tier 2
            case 3:
                return 0; // Weakness III for Tier 3
            default:
                return 0;
        }
    }
}