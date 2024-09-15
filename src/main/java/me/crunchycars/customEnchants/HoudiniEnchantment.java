package me.crunchycars.customEnchants;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class HoudiniEnchantment extends CustomEnchantment {

    private final int tier;

    public HoudiniEnchantment(int tier) {
        super("§9Houdini " + tier, tier); // Houdini enchantment with tiers
        this.tier = tier;
    }

    @Override
    public boolean canApplyTo(Material material) {
        switch (tier) {
            case 1:
                return isIronOrAboveLeggings(material); // Tier 1 can be applied to iron leggings and above
            case 2:
                return isDiamondOrAboveLeggings(material); // Tier 2 can be applied to diamond leggings and above
            default:
                return false;
        }
    }

    private boolean isIronOrAboveLeggings(Material material) {
        return material == Material.IRON_LEGGINGS || material == Material.DIAMOND_LEGGINGS || material == Material.NETHERITE_LEGGINGS;
    }

    private boolean isDiamondOrAboveLeggings(Material material) {
        return material == Material.DIAMOND_LEGGINGS || material == Material.NETHERITE_LEGGINGS;
    }

    // This method is used to reduce the trap effect duration
    public int getReducedDuration(int originalDuration) {
        double reduction = getReductionAmount();
        int reducedDuration = (int) (originalDuration * (1 - reduction));
        return reducedDuration;
    }

    private double getReductionAmount() {
        switch (tier) {
            case 1:
                return 0.25; // Reduce trap duration by 25% for Tier 1
            case 2:
                return 0.50; // Reduce trap duration by 50% for Tier 2
            default:
                return 0.0;
        }
    }

    @Override
    public void applyEffect(Player player, LivingEntity target, ItemStack item) {
        // Houdini doesn't apply a direct effect but modifies trap effects, so this method is not needed.
    }
}