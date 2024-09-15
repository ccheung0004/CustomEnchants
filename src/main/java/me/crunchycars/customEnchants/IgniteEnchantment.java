package me.crunchycars.customEnchants;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class IgniteEnchantment extends CustomEnchantment {

    public IgniteEnchantment(int tier) {
        super("§9Ignite " + tier, tier);
    }

    @Override
    public boolean canApplyTo(Material material) {
        switch (getTier()) {
            case 1:
                return isAnySword(material); // Tier 1 can be applied to any sword
            case 2:
                return isIronOrAboveSword(material); // Tier 2 can be applied to iron swords and above
            case 3:
                return isDiamondSword(material); // Tier 3 can be applied to diamond swords and above
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
        // Calculate the chance to ignite the target based on the tier
        double chance = getIgniteChance();
        if (Math.random() < chance) {
            int burnTime = getBurnTime(); // Burn time in ticks (20 ticks = 1 second)

            // Apply the fire effect to the target
            target.setFireTicks(burnTime);

            // Notify the player
            player.sendMessage("§eIgnite activated! You have set " + target.getName() + " on fire for " + burnTime / 20 + " seconds.");
        }
    }

    // Get the chance to ignite based on the enchantment tier
    private double getIgniteChance() {
        switch (getTier()) {
            case 1:
                return 0.01; // 1% chance for Tier 1
            case 2:
                return 0.02; // 2% chance for Tier 2
            case 3:
                return 0.03; // 3% chance for Tier 3
            default:
                return 0.0;
        }
    }

    // Get the burn time in ticks based on the enchantment tier
    private int getBurnTime() {
        switch (getTier()) {
            case 1:
                return 40; // 2 seconds of fire (40 ticks)
            case 2:
                return 60; // 3 seconds of fire (60 ticks)
            case 3:
                return 80; // 4 seconds of fire (80 ticks)
            default:
                return 0;
        }
    }
}