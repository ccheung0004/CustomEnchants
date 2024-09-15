package me.crunchycars.customEnchants;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RiposteEnchantment extends CustomEnchantment {

    private static final Map<UUID, Long> riposteCooldowns = new HashMap<>(); // Store cooldowns
    private static final Map<UUID, Boolean> riposteActive = new HashMap<>(); // Store if Riposte is active for blocking

    private final int tier;

    public RiposteEnchantment(int tier) {
        super("§6Riposte " + tier, tier);
        this.tier = tier;
    }

    @Override
    public boolean canApplyTo(Material material) {
        switch (tier) {
            case 1:
                return material.name().endsWith("_SWORD"); // Tier 1 is for any sword
            case 2:
                return material == Material.IRON_SWORD || material == Material.DIAMOND_SWORD || material == Material.NETHERITE_SWORD;
            case 3:
                return material == Material.DIAMOND_SWORD || material == Material.NETHERITE_SWORD;
            default:
                return false;
        }
    }

    @Override
    public void applyEffect(Player player, LivingEntity target, ItemStack item) {
        UUID playerUUID = player.getUniqueId();
        long currentTime = System.currentTimeMillis();
        long cooldownTime = getCooldownTime(tier);

        if (riposteCooldowns.containsKey(playerUUID) && (currentTime - riposteCooldowns.get(playerUUID)) < cooldownTime) {
            return; // On cooldown, do nothing
        }

        // Activate Riposte effect
        double procChance = getProcChance(tier);
        if (Math.random() < procChance) {
            riposteActive.put(playerUUID, true); // Activate the next-block effect
            riposteCooldowns.put(playerUUID, currentTime); // Set cooldown
            player.sendMessage("§b§l(!) §bRiposte activated");
        }
    }

    // Handle blocking effect and notify player
    public static boolean handleRiposte(Player player, EntityDamageByEntityEvent event) {
        UUID playerUUID = player.getUniqueId();
        if (riposteActive.getOrDefault(playerUUID, false)) {
            event.setCancelled(true); // Block the damage
            player.sendMessage("§b§l(!) §bParried");
            riposteActive.put(playerUUID, false); // Deactivate Riposte after blocking
            return true; // Riposte was activated and blocked damage
        }
        return false; // Riposte was not active, no block occurred
    }

    // Get cooldown time based on tier
    private long getCooldownTime(int tier) {
        switch (tier) {
            case 1:
                return 60000; // 1 minute cooldown for tier 1
            case 2:
                return 55000; // 55 seconds cooldown for tier 2
            case 3:
                return 50000; // 50 seconds cooldown for tier 3
            default:
                return 60000; // Default 1 minute
        }
    }

    // Get proc chance based on tier
    private double getProcChance(int tier) {
        switch (tier) {
            case 1:
                return 0.075; // 7.5% for tier 1
            case 2:
                return 0.10; // 10% for tier 2
            case 3:
                return 0.15; // 15% for tier 3
            default:
                return 0;
        }
    }
}