package me.crunchycars.customEnchants;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SupermanEnchantment extends CustomEnchantment {

    private static final long COOLDOWN = 1800000; // 30 minutes in milliseconds
    private static final double ACTIVATION_HEALTH_THRESHOLD = 8; // 6 hearts or less
    private static final Map<UUID, Long> cooldowns = new HashMap<>();

    public SupermanEnchantment(int tier) {
        super("§bSuperman " + tier, tier); // Superman has 1 tier
    }

    @Override
    public boolean canApplyTo(Material material) {
        return material == Material.DIAMOND_CHESTPLATE; // Superman applies only to diamond chestplate
    }

    @Override
    public void applyEffect(Player player, LivingEntity target, ItemStack item) {
        UUID playerUUID = player.getUniqueId();
        long currentTime = System.currentTimeMillis();

        // Check if the player is silenced
        if (SilenceManager.isSilenced(player)) {
            player.sendMessage("§cYou are silenced! Superman cannot activate.");
            return;
        }

        // Check if the player is on cooldown
        if (cooldowns.containsKey(playerUUID) && (currentTime - cooldowns.get(playerUUID)) < COOLDOWN) {
            long remainingCooldown = (COOLDOWN - (currentTime - cooldowns.get(playerUUID))) / 1000;
            player.sendMessage("§cSuperman is on cooldown for " + remainingCooldown + " seconds.");
            return; // Exit if on cooldown
        }

        // Check if player's health is below or equal to the activation threshold
        if (player.getHealth() <= ACTIVATION_HEALTH_THRESHOLD) {
            // Activate Superman only when the health threshold is met
            activateSuperman(player);
            cooldowns.put(playerUUID, currentTime); // Set cooldown
        }
    }

    private void activateSuperman(Player player) {
        // DEBUG message to indicate Superman activation
        Bukkit.getLogger().info("[DEBUG] Activating Superman for player: " + player.getName());

        // Disable flight mode in case it's active
        player.setAllowFlight(false);
        player.setFlying(false);

        // Get the direction the player is facing and add forward velocity
        Vector direction = player.getLocation().getDirection().normalize();
        Vector launchVelocity = direction.multiply(4).setY(7); // Forward with a factor of 2 and upward with Y-velocity 10

        // Launch the player super high into the air and forward
        player.setVelocity(launchVelocity);
        player.sendMessage("§b§l(!) §bSuperman activated! You are invulnerable!");

        // Forcefully apply the velocity multiple times to avoid interruptions
        new BukkitRunnable() {
            int attempts = 0;

            @Override
            public void run() {
                if (attempts < 3) { // Apply the velocity 3 times for safety
                    player.setVelocity(launchVelocity);
                    attempts++;
                } else {
                    this.cancel(); // Stop reapplying after 3 attempts
                }
            }
        }.runTaskTimer(JavaPlugin.getPlugin(CustomEnchants.class), 0L, 5L); // Reapply every 5 ticks

        // Set invulnerability
        player.setInvulnerable(true);

        // Schedule disabling invulnerability after 10 seconds
        Bukkit.getScheduler().runTaskLater(JavaPlugin.getPlugin(CustomEnchants.class), () -> {
            player.setInvulnerable(false); // Disable invulnerability
            player.sendMessage("§c§l(!) §cSuperman effect has ended.");
        }, 125L); // 200 ticks = 10 seconds

        // Apply fast descent after 2 seconds
        Bukkit.getScheduler().runTaskLater(JavaPlugin.getPlugin(CustomEnchants.class), () -> {
            if (!player.isOnGround()) {
                player.setVelocity(new Vector(0, -2, 0)); // Quick downward force
            }
        }, 40L); // 2 seconds delay
    }
}