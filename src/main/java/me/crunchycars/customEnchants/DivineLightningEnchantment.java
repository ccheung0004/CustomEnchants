package me.crunchycars.customEnchants;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Location;
import org.bukkit.World;

public class DivineLightningEnchantment extends CustomEnchantment {

    public DivineLightningEnchantment() {
        super("§bDivine Lightning", 1); // This enchantment only has one tier
    }

    @Override
    public boolean canApplyTo(Material material) {
        return material == Material.DIAMOND_SWORD; // Divine Lightning can be applied only to diamond swords
    }

    @Override
    public void applyEffect(Player player, LivingEntity target, ItemStack item) {
        double lightningChance = 0.05; // 7.5% chance to proc Divine Lightning
        if (Math.random() < lightningChance) {
            World world = target.getWorld();
            Location location = target.getLocation();

            // Create the lightning effect at the target's location
            world.strikeLightningEffect(location);

            // Apply damage to the target (excluding the player)
            if (target != player) {
                target.damage(5.0); // Damage dealt by Divine Lightning
            }

            player.sendMessage("§bDivine Lightning has struck your target!");
        }
    }
}