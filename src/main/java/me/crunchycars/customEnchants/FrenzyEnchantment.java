package me.crunchycars.customEnchants;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FrenzyEnchantment extends CustomEnchantment {

    private static final Map<UUID, Integer> frenzyCounters = new HashMap<>();
    private static final Map<UUID, Long> lastHitTimes = new HashMap<>(); // Tracks the last hit time to prevent rapid spamming
    private static final long FRENZY_DECAY_TIME = 2000; // Frenzy count decays if no hit in 2 seconds
    private static final long MIN_HIT_INTERVAL = 1000; // Minimum 1 second between damage applications

    private final int tier;

    public FrenzyEnchantment(int tier) {
        super("§6Frenzy " + tier, tier);
        this.tier = tier;

        // Schedule a repeating task to check for Frenzy decay
        Bukkit.getScheduler().runTaskTimer(JavaPlugin.getProvidingPlugin(FrenzyEnchantment.class), this::checkFrenzyDecay, 10L, 10L);
    }

    @Override
    public boolean canApplyTo(Material material) {
        switch (tier) {
            case 1:
                return isAnySword(material); // Tier 1 can be applied to any sword
            case 2:
                return isIronOrAboveSword(material); // Tier 2 can be applied to iron swords and above
            case 3:
                return isDiamondSword(material); // Tier 3 can be applied to diamond swords only
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
        UUID playerUUID = player.getUniqueId();
        long currentTime = System.currentTimeMillis();

        // Prevent spamming: Apply damage only if 1 second has passed since the last hit
        if (lastHitTimes.containsKey(playerUUID) && (currentTime - lastHitTimes.get(playerUUID)) < MIN_HIT_INTERVAL) {
            return; // Ignore hit if it is within the cooldown window
        }

        // Update the last hit time
        lastHitTimes.put(playerUUID, currentTime);

        // Increment Frenzy count
        int frenzyCount = frenzyCounters.getOrDefault(playerUUID, 0) + 1;
        frenzyCounters.put(playerUUID, frenzyCount);

        // Calculate and apply damage boost
        double damageBoost = getDamageBoost(tier, frenzyCount);
        target.damage(damageBoost, player); // Apply the damage boost

    }

    private double getDamageBoost(int tier, int frenzyCount) {
        // Adjust damage scaling based on the tier
        switch (tier) {
            case 1:
                return 0.05 * frenzyCount; // 0.1 extra damage per hit for Tier 1
            case 2:
                return 0.075 * frenzyCount; // 0.15 extra damage per hit for Tier 2
            case 3:
                return 0.1 * frenzyCount; // 0.2 extra damage per hit for Tier 3
            default:
                return 0;
        }
    }

    // Reset frenzy when the player gets hit
    public static void resetFrenzy(Player player) {
        UUID playerUUID = player.getUniqueId();
        frenzyCounters.remove(playerUUID);
    }

    // Check for Frenzy decay
    private void checkFrenzyDecay() {
        long currentTime = System.currentTimeMillis();

        for (UUID playerUUID : lastHitTimes.keySet()) {
            long lastHitTime = lastHitTimes.get(playerUUID);

            // Check if Frenzy has decayed (time since last hit > FRENZY_DECAY_TIME)
            if ((currentTime - lastHitTime) > FRENZY_DECAY_TIME) {
                Player player = Bukkit.getPlayer(playerUUID);
                if (player != null && frenzyCounters.containsKey(playerUUID)) {
                    frenzyCounters.remove(playerUUID); // Reset Frenzy count
                    lastHitTimes.remove(playerUUID); // Remove last hit time tracking
                }
            }
        }
    }
}