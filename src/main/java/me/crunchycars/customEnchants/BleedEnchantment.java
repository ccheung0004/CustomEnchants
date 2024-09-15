package me.crunchycars.customEnchants;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class BleedEnchantment extends CustomEnchantment {

    private static final ConcurrentHashMap<UUID, BleedTask> bleedTasks = new ConcurrentHashMap<>();

    public BleedEnchantment(int tier) {
        super("§eBleed " + tier, tier);
    }

    @Override
    public boolean canApplyTo(Material material) {
        switch (getTier()) {
            case 1:
                return isAnyAxe(material); // Bleed I can be applied to any sword or axe
            case 2:
                return isIronOrAboveAxe(material); // Bleed II can be applied to iron and above swords/axes
            case 3:
                return isDiamondOrAboveAxe(material); // Bleed III can be applied to diamond and above swords/axes
            default:
                return false;
        }
    }

    @Override
    public void applyEffect(Player player, LivingEntity target, ItemStack item) {
        double bleedChance = getBleedChance(getTier());
        if (Math.random() < bleedChance) {
            int duration = getBleedDuration(getTier());
            startBleedTask(target, duration);

            if (target instanceof Player) {
                Player targetPlayer = (Player) target;
                targetPlayer.sendMessage("§c§l(!) §cYou have been hit with Bleed " + getTier() + " by " + player.getName() + "!");
            }
        }
    }

    public static boolean isTargetBleeding(LivingEntity target) {
        return bleedTasks.containsKey(target.getUniqueId());
    }

    private boolean isAnyAxe(Material material) {
        return  material.name().endsWith("_AXE");
    }

    private boolean isIronOrAboveAxe(Material material) {
        return
                material == Material.IRON_AXE || material == Material.DIAMOND_AXE || material == Material.NETHERITE_AXE;
    }

    private boolean isDiamondOrAboveAxe(Material material) {
        return
                material == Material.DIAMOND_AXE || material == Material.NETHERITE_AXE;
    }

    private double getBleedChance(int tier) {
        switch (tier) {
            case 1: return 0.05;
            case 2: return 0.075;
            case 3: return 0.1;
            default: return 0;
        }
    }

    private int getBleedDuration(int tier) {
        switch (tier) {
            case 1: return 5;
            case 2: return 7;
            case 3: return 10;
            default: return 0;
        }
    }

    private void startBleedTask(LivingEntity target, int duration) {
        UUID targetUUID = target.getUniqueId();
        BleedTask existingTask = bleedTasks.get(targetUUID);

        if (existingTask != null && existingTask.getRemainingTime() >= duration * 20) {
            return; // Don't reset the timer if the current bleed is longer or the same
        }

        if (existingTask != null) {
            existingTask.cancel();
        }

        BleedTask newTask = new BleedTask(target, duration);
        newTask.runTaskTimer(JavaPlugin.getPlugin(CustomEnchants.class), 0, 20);
        bleedTasks.put(targetUUID, newTask);
    }

    private static class BleedTask extends BukkitRunnable {
        private final LivingEntity target;
        private int remainingTime;

        public BleedTask(LivingEntity target, int durationInSeconds) {
            this.target = target;
            this.remainingTime = durationInSeconds * 20;
        }

        @Override
        public void run() {
            if (remainingTime <= 0 || target.isDead()) {
                bleedTasks.remove(target.getUniqueId());
                this.cancel();
                return;
            }

            double damage = Math.max(0.3, target.getHealth() * 0.01);  // Apply incremental damage to prevent spikes
            target.damage(damage);
            remainingTime -= 20;
        }

        public int getRemainingTime() {
            return remainingTime;
        }
    }
}