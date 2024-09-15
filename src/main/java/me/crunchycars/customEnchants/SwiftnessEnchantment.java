package me.crunchycars.customEnchants;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SwiftnessEnchantment extends CustomEnchantment {

    // Default Minecraft player walk speed is 0.2
    private static final float BASE_WALK_SPEED = 0.2f;
    private static final float SWIFTNESS_SPEED_BOOST = 0.1f; // Increase the speed boost per tier

    public SwiftnessEnchantment() {
        super("§6Swiftness 1", 1);
    }

    @Override
    public boolean canApplyTo(Material material) {
        return material == Material.DIAMOND_BOOTS; // Swiftness can be applied only to diamond boots
    }

    @Override
    public void applyEffect(Player player, LivingEntity target, ItemStack item) {
        // Calculate the new walk speed based on the enchantment's tier
        applyWalkSpeed(player);
    }

    // Apply walk speed based on Swiftness enchantment
    public static void applyWalkSpeed(Player player) {
        float newWalkSpeed = BASE_WALK_SPEED + SWIFTNESS_SPEED_BOOST;
        if (player.getWalkSpeed() != newWalkSpeed) {
            player.setWalkSpeed(newWalkSpeed);
        }
    }

    // Reset walk speed to default when removing boots
    public static void resetWalkSpeed(Player player) {
        player.setWalkSpeed(BASE_WALK_SPEED);
    }
}