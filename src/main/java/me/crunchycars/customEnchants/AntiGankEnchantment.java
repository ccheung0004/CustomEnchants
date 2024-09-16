package me.crunchycars.customEnchants;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class AntiGankEnchantment extends CustomEnchantment {

    private static final Map<UUID, Long> cooldowns = new HashMap<>();
    private static final Map<UUID, Integer> hitCounters = new HashMap<>();
    private static final Map<UUID, Long> lastHitTimes = new HashMap<>();
    private static final int REQUIRED_PLAYERS_NEARBY = 5; // Minimum number of nearby players
    private static final double NEARBY_RADIUS = 10.0; // Radius within which players need to be

    private final int tier;

    public AntiGankEnchantment(int tier) {
        super("§9Anti Gank " + tier, tier);
        this.tier = tier;
    }

    @Override
    public boolean canApplyTo(Material material) {
        switch (tier) {
            case 1:
                return isAnyPants(material); // Tier 1 can be applied to any pants
            case 2:
                return isIronOrAbovePants(material); // Tier 2 can be applied to iron pants and above
            case 3:
                return isDiamondOrAbovePants(material); // Tier 3 can be applied to diamond pants and above
            default:
                return false;
        }
    }

    private boolean isAnyPants(Material material) {
        return material == Material.LEATHER_LEGGINGS || material == Material.CHAINMAIL_LEGGINGS ||
                material == Material.IRON_LEGGINGS || material == Material.DIAMOND_LEGGINGS ||
                material == Material.NETHERITE_LEGGINGS;
    }

    private boolean isIronOrAbovePants(Material material) {
        return material == Material.IRON_LEGGINGS || material == Material.DIAMOND_LEGGINGS || material == Material.NETHERITE_LEGGINGS;
    }

    private boolean isDiamondOrAbovePants(Material material) {
        return material == Material.DIAMOND_LEGGINGS || material == Material.NETHERITE_LEGGINGS;
    }

    @Override
    public void applyEffect(Player player, LivingEntity target, ItemStack item) {
        // Check if the player is silenced
        if (SilenceManager.isSilenced(player)) {
            return; // Do nothing if the player is silenced
        }

        UUID playerId = player.getUniqueId();
        long currentTime = System.currentTimeMillis();
        long cooldownTime = getCooldownTime(tier);

        if (cooldowns.containsKey(playerId) && (currentTime - cooldowns.get(playerId)) < cooldownTime) {
            return; // Cooldown is still active, do nothing
        }

        // Check if there are enough nearby players
        int nearbyPlayersCount = (int) player.getNearbyEntities(NEARBY_RADIUS, NEARBY_RADIUS, NEARBY_RADIUS)
                .stream()
                .filter(entity -> entity instanceof Player && entity != player)
                .count();

        if (nearbyPlayersCount < REQUIRED_PLAYERS_NEARBY) {
            return;
        }

        // Track hits and timeframe
        long lastHitTime = lastHitTimes.getOrDefault(playerId, 0L);
        if (currentTime - lastHitTime <= 500) { // 500 milliseconds = 0.5 seconds
            hitCounters.put(playerId, hitCounters.getOrDefault(playerId, 0) + 1);
        } else {
            hitCounters.put(playerId, 1); // Reset hit counter if outside the 0.5-second window
        }
        lastHitTimes.put(playerId, currentTime);

        // If the player has been hit 9 times within the 0.5-second timeframe, apply the effect
        if (hitCounters.get(playerId) >= 8) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 100, getDamageBoostLevel(tier) - 1)); // Apply damage boost based on tier
            player.sendMessage("§cAnti Gank has activated, granting you extra damage!");
            cooldowns.put(playerId, currentTime); // Set cooldown
            hitCounters.remove(playerId); // Reset hit counter after activation
        }
    }

    private long getCooldownTime(int tier) {
        switch (tier) {
            case 1:
                return 90000; // 1 minute 30 seconds for Tier 1
            case 2:
                return 75000; // 1 minute 15 seconds for Tier 2
            case 3:
                return 60000; // 1 minute for Tier 3
            default:
                return 60000; // Default 1 minute
        }
    }

    private int getDamageBoostLevel(int tier) {
        switch (tier) {
            case 1:
                return 1; // 0.5 hearts extra damage for Tier 1
            case 2:
                return 2; // 0.75 hearts extra damage for Tier 2
            case 3:
                return 2; // 1 heart extra damage for Tier 3
            default:
                return 1; // Default level
        }
    }
}