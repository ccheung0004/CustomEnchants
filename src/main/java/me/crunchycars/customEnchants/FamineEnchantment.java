package me.crunchycars.customEnchants;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import java.util.Random;

public class FamineEnchantment extends CustomEnchantment {

    private final Random random = new Random();

    public FamineEnchantment(int tier) {
        super("§9Famine " + tier, tier); // The enchantment will have 3 tiers
    }

    @Override
    public boolean canApplyTo(Material material) {
        switch (getTier()) {
            case 1:
                return isAnySword(material); // Tier 1 applies to any sword
            case 2:
                return isIronOrAboveSword(material); // Tier 2 applies to iron swords and above
            case 3:
                return isDiamondOrAboveSword(material); // Tier 3 applies to diamond swords and above
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

    private boolean isDiamondOrAboveSword(Material material) {
        return material == Material.DIAMOND_SWORD || material == Material.NETHERITE_SWORD;
    }

    @Override
    public void applyEffect(Player damager, LivingEntity target, ItemStack item) {
        if (target instanceof Player) {
            Player targetPlayer = (Player) target;
            double chance = getFamineChance(getTier());

            if (random.nextDouble() < chance) {
                int hungerToRemove = getHungerToRemove(getTier());
                targetPlayer.setFoodLevel(Math.max(0, targetPlayer.getFoodLevel() - hungerToRemove));
                damager.sendMessage("§c§l(!) §cFamine enchantment activated! You have drained hunger from " + targetPlayer.getName() + "!");
                targetPlayer.sendMessage("§c§l(!) §cYou feel weakened as your hunger is drained by " + damager.getName() + "!");
            }
        }
    }

    private double getFamineChance(int tier) {
        switch (tier) {
            case 1:
                return 0.05; // 5% chance for Tier 1
            case 2:
                return 0.075; // 7.5% chance for Tier 2
            case 3:
                return 0.10; // 10% chance for Tier 3
            default:
                return 0.0;
        }
    }

    private int getHungerToRemove(int tier) {
        switch (tier) {
            case 1:
                return 2; // Removes 2 hunger points for Tier 1
            case 2:
                return 3; // Removes 3 hunger points for Tier 2
            case 3:
                return 4; // Removes 4 hunger points for Tier 3
            default:
                return 0;
        }
    }
}