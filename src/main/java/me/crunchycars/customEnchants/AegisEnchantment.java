package me.crunchycars.customEnchants;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AegisEnchantment extends CustomEnchantment {

    private static final Map<UUID, Long> aegisActiveTimes = new HashMap<>();
    private final int tier;

    public AegisEnchantment(int tier) {
        super("§6Aegis " + tier, tier);
        this.tier = tier;
    }

    @Override
    public boolean canApplyTo(Material material) {
        switch (tier) {
            case 1:
                return isAnyChestplate(material); // Tier 1 can be applied to any chestplate
            case 2:
                return isIronOrAboveChestplate(material); // Tier 2 can be applied to iron and above chestplates
            case 3:
                return isDiamondOnlyChestplate(material); // Tier 3 can be applied to diamond chestplates only
            default:
                return false;
        }
    }

    private boolean isAnyChestplate(Material material) {
        return material == Material.LEATHER_CHESTPLATE || material == Material.CHAINMAIL_CHESTPLATE ||
                material == Material.IRON_CHESTPLATE || material == Material.DIAMOND_CHESTPLATE ||
                material == Material.NETHERITE_CHESTPLATE;
    }

    private boolean isIronOrAboveChestplate(Material material) {
        return material == Material.IRON_CHESTPLATE || material == Material.DIAMOND_CHESTPLATE ||
                material == Material.NETHERITE_CHESTPLATE;
    }

    private boolean isDiamondOnlyChestplate(Material material) {
        return material == Material.DIAMOND_CHESTPLATE || material == Material.NETHERITE_CHESTPLATE;
    }

    // Check if the chestplate is Aegis and activate
    @Override
    public void applyEffect(Player player, LivingEntity target, ItemStack item) {
        UUID targetUUID = target.getUniqueId();
        aegisActiveTimes.put(targetUUID, System.currentTimeMillis());

        // Schedule a task to remove the effect after 3 seconds
        new BukkitRunnable() {
            @Override
            public void run() {
                Long activeTime = aegisActiveTimes.get(targetUUID);
                if (activeTime != null && (System.currentTimeMillis() - activeTime) >= 3000) {
                    aegisActiveTimes.remove(targetUUID);
                }
            }
        }.runTaskLater(JavaPlugin.getPlugin(CustomEnchants.class), 60L); // 60 ticks = 3 seconds
    }

    public static boolean isAegisActive(LivingEntity entity) {
        return aegisActiveTimes.containsKey(entity.getUniqueId());
    }

    // Method to get the reduction multiplier based on the Aegis level
    public static double getAegisReductionMultiplier(Player player) {
        for (ItemStack armorPiece : player.getInventory().getArmorContents()) {
            if (armorPiece != null && armorPiece.hasItemMeta()) {
                for (CustomEnchantment enchantment : CustomEnchantmentManager.getAllEnchantments()) {
                    if (enchantment instanceof AegisEnchantment && enchantment.isAppliedTo(armorPiece)) {
                        // Reduction multiplier based on the Aegis tier
                        int tier = ((AegisEnchantment) enchantment).getTier();
                        switch (tier) {
                            case 1:
                                return 0.9; // 10% reduction
                            case 2:
                                return 0.75; // 25% reduction
                            case 3:
                                return 0.6; // 40% reduction
                        }
                    }
                }
            }
        }
        return 1.0; // Default to no reduction if Aegis isn't applied
    }
}