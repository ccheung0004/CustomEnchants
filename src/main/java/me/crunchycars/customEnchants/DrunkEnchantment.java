package me.crunchycars.customEnchants;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DrunkEnchantment extends CustomEnchantment {

    private static final int TIER_1_STRENGTH_DURATION = 20; // Strength for 4 seconds (80 ticks)
    private static final int TIER_2_STRENGTH_DURATION = 30; // Strength for 6 seconds (120 ticks)
    private static final int NAUSEA_DURATION = 200; // Nausea for 10 seconds (200 ticks)
    private static final long COOLDOWN = 60500; // 1 minute cooldown in milliseconds

    // Cooldown tracking for each player
    private static final Map<UUID, Long> cooldowns = new HashMap<>();

    public DrunkEnchantment(int tier) {
        super("§6Drunk " + tier, tier);
    }

    @Override
    public boolean canApplyTo(Material material) {
        return getTier() == 1 ? material == Material.IRON_HELMET : material == Material.DIAMOND_HELMET;
    }

    @Override
    public void applyEffect(Player player, LivingEntity target, ItemStack item) {
        UUID playerUUID = player.getUniqueId();
        long currentTime = System.currentTimeMillis();

        // Check if the player is on cooldown
        if (cooldowns.containsKey(playerUUID) && (currentTime - cooldowns.get(playerUUID)) < COOLDOWN) {
            long remainingCooldown = (COOLDOWN - (currentTime - cooldowns.get(playerUUID))) / 1000;
            return;
        }


        // Apply strength and nausea effects
        int strengthDuration = getTier() == 1 ? TIER_1_STRENGTH_DURATION : TIER_2_STRENGTH_DURATION;

        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, strengthDuration, 0, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, NAUSEA_DURATION, 0, false, false));

        player.sendMessage("§e§l(!) §eDrunk activated! You feel stronger but dizzy.");

        // Set the cooldown
        cooldowns.put(playerUUID, currentTime);
    }
}