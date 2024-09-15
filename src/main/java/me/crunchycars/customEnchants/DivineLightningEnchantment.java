package me.crunchycars.customEnchants;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.List;
import java.util.stream.Collectors;

public class DivineLightningEnchantment extends CustomEnchantment {

    private final PartyManager partyManager;

    public DivineLightningEnchantment(PartyManager partyManager) {
        super("§bDivine Lightning", 1); // This enchantment only has one tier
        this.partyManager = partyManager;
    }

    @Override
    public boolean canApplyTo(Material material) {
        return material == Material.DIAMOND_AXE; // Divine Lightning can be applied only to diamond axes
    }

    @Override
    public void applyEffect(Player player, LivingEntity target, ItemStack item) {
        double lightningChance = 0.05; // 5% chance to proc Divine Lightning
        double radius = 5.0; // Radius for AOE lightning strike
        double lightningDamage = 5.0; // Damage dealt by lightning

        if (Math.random() < lightningChance) {
            World world = target.getWorld();
            Location location = target.getLocation();

            // Check if the target is a party member
            if (partyManager.isInParty(player.getUniqueId()) &&
                    partyManager.isInParty(target.getUniqueId()) &&
                    partyManager.getParty(player.getUniqueId()).equals(partyManager.getParty(target.getUniqueId()))) {
                return; // Do not strike lightning on party members
            }

            // Strike lightning at the target's location
            world.strikeLightningEffect(location);

            // Damage the main target
            if (target != player) {
                target.damage(lightningDamage);
            }

            // Find nearby entities in the radius, excluding the player and party members
            List<LivingEntity> nearbyEntities = world.getLivingEntities().stream()
                    .filter(entity -> entity.getLocation().distance(location) <= radius
                            && entity != player
                            && entity != target
                            && (!partyManager.isInParty(player.getUniqueId())
                            || !partyManager.isInParty(entity.getUniqueId())
                            || !partyManager.getParty(player.getUniqueId()).equals(partyManager.getParty(entity.getUniqueId()))))
                    .collect(Collectors.toList());

            // Apply lightning effect and damage to nearby enemies
            for (LivingEntity nearbyEntity : nearbyEntities) {
                world.strikeLightningEffect(nearbyEntity.getLocation());
                nearbyEntity.damage(lightningDamage);
            }

            player.sendMessage("§b§l(!) §bDivine Lightning has struck your target and nearby enemies!");
        }
    }
}