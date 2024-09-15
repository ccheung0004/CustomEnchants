package me.crunchycars.customEnchants;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class CleaveEnchantment extends CustomEnchantment {

    // To store players currently processing a cleave to prevent re-triggering
    private static final Set<UUID> cleavingPlayers = new HashSet<>();
    private final PartyManager partyManager;

    public CleaveEnchantment(int tier, PartyManager partyManager) {
        super("§eCleave " + tier, tier); // Cleave enchantment with tiers
        this.partyManager = partyManager; // Initialize PartyManager
    }

    @Override
    public boolean canApplyTo(Material material) {
        boolean canApply;
        switch (getTier()) {
            case 1:
                canApply = isAnyAxe(material);
                break;
            case 2:
                canApply = isIronOrAboveAxe(material);
                break;
            case 3:
                canApply = isDiamondOnlyAxe(material);
                break;
            default:
                canApply = false;
        }
        return canApply;
    }

    private boolean isAnyAxe(Material material) {
        return material == Material.WOODEN_AXE || material == Material.STONE_AXE ||
                material == Material.IRON_AXE || material == Material.DIAMOND_AXE ||
                material == Material.NETHERITE_AXE || material == Material.GOLDEN_AXE;
    }

    private boolean isIronOrAboveAxe(Material material) {
        return material == Material.IRON_AXE || material == Material.DIAMOND_AXE || material == Material.NETHERITE_AXE;
    }

    private boolean isDiamondOnlyAxe(Material material) {
        return material == Material.DIAMOND_AXE || material == Material.NETHERITE_AXE;
    }

    @Override
    public void applyEffect(Player player, LivingEntity target, ItemStack item) {
        UUID playerUUID = player.getUniqueId();

        // If the player is already cleaving, don't do it again to prevent multiple triggers
        if (cleavingPlayers.contains(playerUUID)) {
            Bukkit.getLogger().info("Player " + player.getName() + " is already cleaving, skipping.");
            return;
        }

        // Add the player to the set to mark them as currently cleaving
        cleavingPlayers.add(playerUUID);

        // Get the tier of the enchantment
        int tier = getTier();
        double damageMultiplier = getDamageMultiplier(tier);

        // Get the player's location and direction
        Vector direction = player.getLocation().getDirection();
        double range = 4.0; // The range of the cleave effect

        // Loop through nearby entities to apply AOE damage
        List<LivingEntity> nearbyEntities = player.getWorld().getLivingEntities();

        for (LivingEntity entity : nearbyEntities) {
            if (entity == target || entity == player) {
                continue; // Skip the original target and the player
            }
            if (entity.getLocation().distance(player.getLocation()) > range) {
                continue; // Skip entities out of range
            }

            // Check if the entity is a player and if they are a party member
            if (entity instanceof Player) {
                Player nearbyPlayer = (Player) entity;
                if (partyManager.isInParty(playerUUID) &&
                        partyManager.isInParty(nearbyPlayer.getUniqueId()) &&
                        partyManager.getParty(playerUUID).equals(partyManager.getParty(nearbyPlayer.getUniqueId()))) {
                    continue; // Skip party members
                }
            }

            // Check if the entity is in front of the player
            Vector toEntity = entity.getLocation().toVector().subtract(player.getLocation().toVector()).normalize();
            if (direction.dot(toEntity) < 0.5) {
                continue; // Skip entities not in front
            }

            // Apply damage to the entity
            double cleaveDamage = target.getLastDamage() * damageMultiplier;
            entity.damage(cleaveDamage, player); // Damage the entity
        }


        // Remove the player from the set after a short delay to allow for future cleave activations
        Bukkit.getScheduler().runTaskLater(JavaPlugin.getPlugin(CustomEnchants.class), () -> {
            cleavingPlayers.remove(playerUUID);
        }, 1L); // Adjust the delay as needed (1 tick should be sufficient)
    }
    private double getDamageMultiplier(int tier) {
        switch (tier) {
            case 1:
                return 0.1; // 10% of the original damage for Tier 1
            case 2:
                return 0.2; // 20% of the original damage for Tier 2
            case 3:
                return 0.3; // 30% of the original damage for Tier 3
            default:
                return 0.0;
        }
    }
}