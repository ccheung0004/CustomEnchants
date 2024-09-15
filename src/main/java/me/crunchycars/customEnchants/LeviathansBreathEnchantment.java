package me.crunchycars.customEnchants;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LeviathansBreathEnchantment extends CustomEnchantment {

    private static final int HIT_COUNT_THRESHOLD = 5;
    private static final long COOLDOWN = 10000; // 5 seconds in milliseconds
    private static final int BEAM_RANGE = 10; // Range of the beam in blocks
    private static final Map<UUID, Integer> playerHitCount = new HashMap<>();
    private static final Map<UUID, Long> cooldowns = new HashMap<>();

    private final PartyManager partyManager; // Add PartyManager

    public LeviathansBreathEnchantment(int tier, PartyManager partyManager) {
        super("§bLeviathan's Breath", tier); // Single tier enchantment
        this.partyManager = partyManager; // Initialize PartyManager
    }

    @Override
    public boolean canApplyTo(Material material) {
        return material == Material.DIAMOND_SWORD; // Apply only to diamond swords
    }

    @Override
    public void applyEffect(Player player, LivingEntity target, ItemStack item) {
        UUID playerUUID = player.getUniqueId();

        // Increment hit count
        playerHitCount.put(playerUUID, playerHitCount.getOrDefault(playerUUID, 0) + 1);

        // If hit count reaches threshold, enable the ability
        if (playerHitCount.get(playerUUID) >= HIT_COUNT_THRESHOLD) {
            player.sendMessage("§aLeviathan's Breath is ready! Right-click to use.");
        }
    }

    public void activateAbility(Player player) {
        UUID playerUUID = player.getUniqueId();
        long currentTime = System.currentTimeMillis();

        // Check if the ability is on cooldown
        if (cooldowns.containsKey(playerUUID) && (currentTime - cooldowns.get(playerUUID)) < COOLDOWN) {
            player.sendMessage("§cLeviathan's Breath is on cooldown!");
            return;
        }

        // Check if the player has hit the threshold of hits
        if (playerHitCount.getOrDefault(playerUUID, 0) >= HIT_COUNT_THRESHOLD) {
            // Shoot the guardian beam
            shootBeam(player);

            // Set the cooldown
            cooldowns.put(playerUUID, currentTime);

            // Reset the hit count
            playerHitCount.put(playerUUID, 0);
        } else {
            player.sendMessage("§cYou need to hit a player 5 times to use Leviathan's Breath!");
        }
    }

    private void shootBeam(Player player) {
        Vector direction = player.getLocation().getDirection().normalize();
        Location start = player.getEyeLocation();

        for (int i = 0; i < BEAM_RANGE; i++) {
            start.add(direction);
            player.getWorld().spawnParticle(Particle.DRIP_WATER, start, 5);

            // Check if there's a player in the path
            for (Entity entity : start.getWorld().getNearbyEntities(start, 0.5, 0.5, 0.5)) {
                if (entity instanceof Player && entity != player) {
                    Player target = (Player) entity;

                    // Check if the target is a party member
                    if (partyManager.isInParty(player.getUniqueId()) &&
                            partyManager.isInParty(target.getUniqueId()) &&
                            partyManager.getParty(player.getUniqueId()).equals(partyManager.getParty(target.getUniqueId()))) {
                        continue; // Skip party members
                    }

                    // Apply the guardian effect
                    applyGuardianEffect(player, target);

                    return; // Stop the beam after hitting a player
                }
            }
        }
    }

    private void applyGuardianEffect(Player player, Player target) {
        // Apply damage
        double damage = 6.0; // Set the damage amount
        target.damage(damage, player); // Damage the target
        player.sendMessage("§bLeviathan's Breath hits " + target.getName() + "!");

        // Simulate Guardian effect
        target.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 60, 3)); // Example effect
    }
}