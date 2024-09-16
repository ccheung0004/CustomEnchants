package me.crunchycars.customEnchants;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class HeavyEnchantment extends CustomEnchantment {

    public HeavyEnchantment(int tier) {
        super("§9Heavy " + tier, tier);
    }

    @Override
    public boolean canApplyTo(Material material) {
        switch (getTier()) {
            case 1:
                return isAnyChestplate(material); // Tier 1 can be applied to any chestplate
            case 2:
                return isIronOrAboveChestplate(material); // Tier 2 can be applied to iron chestplates and above
            case 3:
                return isDiamondOrAboveChestplate(material); // Tier 3 can be applied to diamond chestplates and above
            default:
                return false;
        }
    }

    private boolean isAnyChestplate(Material material) {
        return material == Material.LEATHER_CHESTPLATE || material == Material.CHAINMAIL_CHESTPLATE ||
                material == Material.IRON_CHESTPLATE || material == Material.DIAMOND_CHESTPLATE ||
                material == Material.NETHERITE_CHESTPLATE || material == Material.GOLDEN_CHESTPLATE;
    }

    private boolean isIronOrAboveChestplate(Material material) {
        return material == Material.IRON_CHESTPLATE || material == Material.DIAMOND_CHESTPLATE || material == Material.NETHERITE_CHESTPLATE;
    }

    private boolean isDiamondOrAboveChestplate(Material material) {
        return material == Material.DIAMOND_CHESTPLATE || material == Material.NETHERITE_CHESTPLATE;
    }

    @Override
    public void applyEffect(Player player, LivingEntity target, ItemStack item) {
        // This method might not be needed unless you want to apply effects during other events
    }

    public void handleBowDamageReduction(Player player, EntityDamageByEntityEvent event) {
        int tier = getTier();
        double reductionPercentage = getDamageReduction(tier);

        // Reduce the damage by the calculated percentage
        double reducedDamage = event.getDamage() * (1 - reductionPercentage);
        event.setDamage(reducedDamage);

        player.sendMessage("§6Heavy enchantment reduced bow damage by " + (int)(reductionPercentage * 100) + "%!");
    }

    private double getDamageReduction(int tier) {
        switch (tier) {
            case 1:
                return 0.02; // 2% damage reduction for Tier 1
            case 2:
                return 0.04; // 4% damage reduction for Tier 2
            case 3:
                return 0.06; // 6% damage reduction for Tier 3
            default:
                return 0.0;
        }
    }
}