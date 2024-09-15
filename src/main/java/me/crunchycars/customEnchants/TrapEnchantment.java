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
import java.util.concurrent.atomic.AtomicInteger;

public class TrapEnchantment extends CustomEnchantment {

    private static final Map<UUID, Long> cooldowns = new HashMap<>();
    private final int tier;

    public TrapEnchantment(int tier) {
        super("§6Trap " + tier, tier);
        this.tier = tier;
    }

    @Override
    public boolean canApplyTo(Material material) {
        switch (tier) {
            case 1:
                return isIronOrAboveWeapon(material); // Tier 1 can be applied to iron swords and above
            case 2:
                return isDiamondOrAboveWeapon(material); // Tier 2 can be applied to diamond swords and above
            default:
                return false;
        }
    }

    private boolean isIronOrAboveWeapon(Material material) {
        return material == Material.IRON_SWORD || material == Material.DIAMOND_SWORD || material == Material.NETHERITE_SWORD;
    }

    private boolean isDiamondOrAboveWeapon(Material material) {
        return material == Material.DIAMOND_SWORD || material == Material.NETHERITE_SWORD;
    }

    @Override
    public void applyEffect(Player player, LivingEntity target, ItemStack item) {
        UUID playerUUID = player.getUniqueId();
        long currentTime = System.currentTimeMillis();
        long cooldownTime = getCooldownTime(tier);

        if (cooldowns.containsKey(playerUUID) && (currentTime - cooldowns.get(playerUUID)) < cooldownTime) {
            return;
        }

        double chance = Math.random();
        if (chance < getTrapChance(tier)) {
            AtomicInteger freezeDuration = new AtomicInteger(getFreezeDuration(tier));

            // Check if the target has Houdini enchantment to reduce the duration
            if (target instanceof Player) {
                Player targetPlayer = (Player) target;
                for (ItemStack armorPiece : targetPlayer.getInventory().getArmorContents()) {
                    if (armorPiece != null && armorPiece.hasItemMeta()) {
                        CustomEnchantmentManager.getAllEnchantments().forEach(enchantment -> {
                            if (enchantment instanceof HoudiniEnchantment && enchantment.isAppliedTo(armorPiece)) {
                                freezeDuration.set(((HoudiniEnchantment) enchantment).getReducedDuration(freezeDuration.get()));
                            }
                        });
                    }
                }
            }

            target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, freezeDuration.get() * 20, 255)); // 255 level is effectively a freeze
            player.sendMessage("§b§l(!) §bTrap activated! " + target.getName() + " is frozen for " + freezeDuration.get() + " seconds.");
            if (target instanceof Player) {
                ((Player) target).sendMessage("§c§l(!) §cYou have been trapped by " + player.getName() + " for " + freezeDuration.get() + " seconds!");
            }
            cooldowns.put(playerUUID, currentTime);
        }
    }

    private double getTrapChance(int tier) {
        return 0.05; // 25% chance for both tiers
    }

    private int getFreezeDuration(int tier) {
        switch (tier) {
            case 1:
                return 3; // 3 seconds for Tier 1
            case 2:
                return 4; // 4 seconds for Tier 2
            default:
                return 3; // Default duration
        }
    }

    private long getCooldownTime(int tier) {
        switch (tier) {
            case 1:
                return 50000; // 50 seconds cooldown for Tier 1
            case 2:
                return 45000; // 45 seconds cooldown for Tier 2
            default:
                return 45000; // Default cooldown
        }
    }
}