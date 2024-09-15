package me.crunchycars.customEnchants;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LastStandEnchantment extends CustomEnchantment {

    private static final int HEALTH_THRESHOLD = 4; // 2 heart
    private static final double TIER_1_EXTRA_HEALTH = 4; // 2 extra hearts for Tier 1
    private static final double TIER_2_EXTRA_HEALTH = 6; // 3 extra hearts for Tier 2
    private static final int TIER_1_DAMAGE_BOOST_DURATION = 60; // Duration for Tier 1 damage boost (3 seconds)
    private static final int TIER_2_DAMAGE_BOOST_DURATION = 100; // Duration for Tier 2 damage boost (5 seconds)
    private static final int DAMAGE_BOOST_LEVEL = 1; // Extra damage level (Strength I)
    private static final long COOLDOWN = 1800000; // 30 minutes in milliseconds

    // Cooldown tracking for each player
    private static final Map<UUID, Long> cooldowns = new HashMap<>();

    public LastStandEnchantment(int tier) {
        super("§eLast Stand " + tier, tier);
    }

    @Override
    public boolean canApplyTo(Material material) {
        switch (getTier()) {
            case 1:
                return isIronOrAboveChestplate(material); // Tier 1 applies to iron chestplate and above
            case 2:
                return isDiamondChestplate(material); // Tier 2 applies to diamond chestplate only
            default:
                return false;
        }
    }

    private boolean isIronOrAboveChestplate(Material material) {
        return material == Material.IRON_CHESTPLATE || material == Material.DIAMOND_CHESTPLATE || material == Material.NETHERITE_CHESTPLATE;
    }

    private boolean isDiamondChestplate(Material material) {
        return material == Material.DIAMOND_CHESTPLATE || material == Material.NETHERITE_CHESTPLATE;
    }

    @Override
    public void applyEffect(Player player, LivingEntity target, ItemStack item) {
        UUID playerUUID = player.getUniqueId();
        long currentTime = System.currentTimeMillis();

        // Debug message for silence check
        if (SilenceManager.isSilenced(player)) {
            player.sendMessage("§cYou are silenced! Last Stand cannot activate.");
            player.sendMessage("§e[DEBUG] Player " + player.getName() + " is silenced. Last Stand won't trigger.");
            return; // Do not activate Last Stand while silenced
        }

        // Check if the player is on cooldown
        if (cooldowns.containsKey(playerUUID) && (currentTime - cooldowns.get(playerUUID)) < COOLDOWN) {
            long remainingCooldown = (COOLDOWN - (currentTime - cooldowns.get(playerUUID))) / 1000;
            player.sendMessage("§e[DEBUG] Last Stand on cooldown for " + remainingCooldown + " seconds.");
            return; // Exit if still on cooldown
        }

        // Debug message for health check
        player.sendMessage("§e[DEBUG] Player " + player.getName() + " health: " + player.getHealth());

        // Check if player's health is below or equal to 1 heart
        if (player.getHealth() <= HEALTH_THRESHOLD) {
            double extraHealth = getTier() == 2 ? TIER_2_EXTRA_HEALTH : TIER_1_EXTRA_HEALTH;
            int damageBoostDuration = getTier() == 2 ? TIER_2_DAMAGE_BOOST_DURATION : TIER_1_DAMAGE_BOOST_DURATION;

            // Add extra health (2 hearts for Tier 1, 3 hearts for Tier 2)
            AttributeInstance maxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
            double newMaxHealth = maxHealth.getBaseValue() + extraHealth;
            maxHealth.setBaseValue(newMaxHealth);

            // Heal the player by the extra health amount
            player.setHealth(Math.min(player.getHealth() + extraHealth, newMaxHealth));

            // Apply damage boost for the calculated duration (Strength effect)
            player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, damageBoostDuration, DAMAGE_BOOST_LEVEL, false, false));

            // Notify the player
            player.sendMessage("§e§l(!) §eLast Stand activated!");

            // Set the cooldown
            cooldowns.put(playerUUID, currentTime);

            // Debug message for activation
            player.sendMessage("§e[DEBUG] Last Stand activated for " + player.getName() + ". Extra health: " + extraHealth);
        } else {
            // Debug message for health not low enough
            player.sendMessage("§e[DEBUG] Last Stand not activated for " + player.getName() + " due to health above threshold.");
        }
    }
}