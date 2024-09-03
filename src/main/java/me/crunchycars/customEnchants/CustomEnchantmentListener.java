package me.crunchycars.customEnchants;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class CustomEnchantmentListener implements Listener {

    private final ConcurrentHashMap<UUID, BleedTask> bleedTasks = new ConcurrentHashMap<>();

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        ItemStack currentItem = event.getCurrentItem();
        ItemStack cursorItem = event.getCursor();

        if (currentItem == null || currentItem.getType() == Material.AIR || cursorItem == null || cursorItem.getType() == Material.AIR) {
            return;
        }

        if (cursorItem.getType() == Material.ENCHANTED_BOOK) {
            ItemMeta cursorMeta = cursorItem.getItemMeta();
            if (cursorMeta != null && cursorMeta.hasDisplayName()) {
                String enchantmentName = cursorMeta.getDisplayName();

                if (enchantmentName.equals("§6Swiftness 1")) {
                    handleEnchantment(player, currentItem, cursorItem, enchantmentName, "Swiftness", "§6", 1);
                } else if (enchantmentName.startsWith("§6Vitality")) {
                    int tier = getTier(enchantmentName, "§6Vitality");
                    handleEnchantment(player, currentItem, cursorItem, enchantmentName, "Vitality", "§6", tier);
                } else if (enchantmentName.startsWith("§eBleed")) {
                    int tier = getTier(enchantmentName, "§eBleed");
                    handleEnchantment(player, currentItem, cursorItem, enchantmentName, "Bleed", "§e", tier);
                } else if (enchantmentName.startsWith("§eSilence")) {
                    int tier = getTier(enchantmentName, "§eSilence");
                    handleEnchantment(player, currentItem, cursorItem, enchantmentName, "Silence", "§e", tier);
                }
            }
        }

        if (event.getSlotType() == InventoryType.SlotType.ARMOR || event.getSlotType() == InventoryType.SlotType.QUICKBAR) {
            applyEffects(player);
        }
    }

    private void handleEnchantment(Player player, ItemStack currentItem, ItemStack cursorItem, String enchantmentName, String enchantmentType, String enchantmentPrefix, int tier) {
        if (tier != -1) {
            CustomEnchantment customEnchantment = getCustomEnchantment(enchantmentType, tier);

            if (!customEnchantment.canApplyTo(currentItem.getType())) {
                player.sendMessage("§c" + enchantmentName + " cannot be applied to this item!");
                return;
            }

            // Check for existing enchantments of the same type
            ItemMeta itemMeta = currentItem.getItemMeta();
            List<String> lore = itemMeta != null && itemMeta.hasLore() ? itemMeta.getLore() : new ArrayList<>();

            if (lore != null) {
                for (int i = 0; i < lore.size(); i++) {
                    String loreLine = lore.get(i);
                    int existingTier = getTierFromLoreLine(loreLine, enchantmentPrefix + enchantmentType);
                    if (existingTier != -1) {
                        if (existingTier >= tier) {
                            // Higher or equal tier already applied, do not consume the book
                            player.sendMessage("§cA higher or equal tier of " + enchantmentType + " is already applied to this item!");
                            return; // Exit without consuming the book or applying the enchantment
                        } else {
                            // Replace the lower-tier enchantment with the higher-tier one
                            lore.set(i, enchantmentPrefix + enchantmentType + " " + tier);
                            itemMeta.setLore(lore);
                            currentItem.setItemMeta(itemMeta);
                            player.sendMessage("§aSuccessfully upgraded " + enchantmentPrefix + enchantmentType + " to " + tier + "!");
                            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f);
                            player.setItemOnCursor(null); // Consume the book
                            return;
                        }
                    }
                }
            }

            int successRate = getSuccessRateFromBook(cursorItem);
            if (successRate == -1) {
                player.sendMessage("§cInvalid enchantment book!");
                return;
            }

            if (attemptEnchantment(successRate)) {
                // If no lower tier was found, add the new enchantment
                applyEnchantment(player, currentItem, enchantmentType, enchantmentPrefix, tier);
                player.setItemOnCursor(null); // Consume the book only if the enchantment is applied successfully
            } else {
                player.sendMessage("§cThe enchantment failed, and the book was destroyed.");
                player.playSound(player.getLocation(), Sound.ITEM_SHIELD_BREAK, 1.0f, 1.0f);
                player.setItemOnCursor(null); // Consume the book on failure
            }
        }
    }
    private CustomEnchantment getCustomEnchantment(String enchantmentType, int tier) {
        switch (enchantmentType) {
            case "Swiftness":
                return new SwiftnessEnchantment(); // Swiftness is a single-tier enchantment
            case "Vitality":
                return new VitalityEnchantment(tier); // Create with the correct tier
            case "Bleed":
                return new BleedEnchantment(tier); // Create with the correct tier
            case "Silence":
                return new SilenceEnchantment(tier); // Create with the correct tier
            default:
                throw new IllegalArgumentException("Unknown enchantment type: " + enchantmentType);
        }
    }

    private void applyEnchantment(Player player, ItemStack item, String enchantmentType, String enchantmentPrefix, int tier) {
        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta != null) {
            List<String> lore = itemMeta.hasLore() ? itemMeta.getLore() : new ArrayList<>();

            // Remove any existing enchantment of the same type with a lower or equal tier
            boolean hasLowerTier = false;
            Iterator<String> iterator = lore.iterator();
            while (iterator.hasNext()) {
                String line = iterator.next();
                if (line.startsWith(enchantmentPrefix + enchantmentType)) {
                    int existingTier = getTierFromLoreLine(line, enchantmentPrefix + enchantmentType);
                    if (existingTier <= tier) {
                        iterator.remove(); // Remove the lower or equal tier
                        hasLowerTier = true;
                    } else {
                        // Higher tier exists, do not apply
                        player.sendMessage("§cA higher tier of " + enchantmentType + " is already applied!");
                        return;
                    }
                }
            }

            // Add the new enchantment if it's an upgrade or a new enchantment
            lore.add(enchantmentPrefix + enchantmentType + " " + tier);
            itemMeta.setLore(lore);
            item.setItemMeta(itemMeta);

            player.sendMessage("§aSuccessfully applied " + enchantmentType + " " + tier + "!");
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f);
            player.setItemOnCursor(null);
        } else {
            player.sendMessage("§cFailed to apply enchantment: ItemMeta is null.");
        }
    }

    private int getTier(String enchantmentName, String enchantmentPrefix) {
        if (enchantmentName.startsWith(enchantmentPrefix + " 1")) {
            return 1;
        } else if (enchantmentName.startsWith(enchantmentPrefix + " 2")) {
            return 2;
        } else if (enchantmentName.startsWith(enchantmentPrefix + " 3")) {
            return 3;
        }
        return -1;
    }

    private int getTierFromLoreLine(String loreLine, String enchantmentPrefix) {
        if (loreLine.startsWith(enchantmentPrefix + " 1")) {
            return 1;
        } else if (loreLine.startsWith(enchantmentPrefix + " 2")) {
            return 2;
        } else if (loreLine.startsWith(enchantmentPrefix + " 3")) {
            return 3;
        }
        return -1; // Default if no valid tier is found
    }

    private int getSuccessRateFromBook(ItemStack book) {
        if (book.hasItemMeta()) {
            ItemMeta meta = book.getItemMeta();
            if (meta.hasLore()) {
                List<String> lore = meta.getLore();
                for (String loreLine : lore) {
                    if (loreLine.startsWith("§7Success Rate: §a")) {
                        try {
                            return Integer.parseInt(loreLine.replace("§7Success Rate: §a", "").replace("%", ""));
                        } catch (NumberFormatException e) {
                            return -1;
                        }
                    }
                }
            }
        }
        return -1;
    }

    private boolean attemptEnchantment(int successRate) {
        Random random = new Random();
        return random.nextInt(100) < successRate;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        applyEffects(player);
    }

    private void applyEffects(Player player) {
        double healthBoost = 0;
        boolean hasSwiftness = false;

        ItemStack[] armorContents = player.getInventory().getArmorContents();

        for (ItemStack armorPiece : armorContents) {
            if (armorPiece != null && armorPiece.hasItemMeta()) {
                ItemMeta meta = armorPiece.getItemMeta();
                List<String> lore = meta.getLore();

                if (lore != null) {
                    if (lore.contains("§6Vitality 1")) {
                        healthBoost += 0.5;
                    }
                    if (lore.contains("§6Vitality 2")) {
                        healthBoost += 1.0;
                    }
                    if (lore.contains("§6Vitality 3")) {
                        healthBoost += 2.0;
                    }
                    if (lore.contains("§6Swiftness 1")) {
                        hasSwiftness = true;
                    }
                }
            }
        }

        AttributeInstance healthAttribute = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (healthAttribute != null) {
            double newMaxHealth = 20.0 + healthBoost;
            healthAttribute.setBaseValue(newMaxHealth);

            if (player.getHealth() > newMaxHealth) {
                player.setHealth(newMaxHealth);
            }
        }

        if (hasSwiftness) {
            if (!player.hasPotionEffect(PotionEffectType.SPEED)) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, true, false));
            }
        } else {
            player.removePotionEffect(PotionEffectType.SPEED);
        }
    }

    @EventHandler
    public void onPlayerItemHeld(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        applyEffects(player);
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof LivingEntity) {
            Player player = (Player) event.getDamager();
            LivingEntity target = (LivingEntity) event.getEntity();

            // First, check for and apply Silence effect
            if (applySilenceEffect(player, target)) {
                return; // If Silence is applied, do not activate other custom enchantments
            }

            // If target is not silenced or Silence did not apply, proceed with other enchantments
            if (!SilenceManager.isSilenced(target instanceof Player ? (Player) target : null)) {
                applyBleedEffect(player, target);
            }
        }
    }

    private boolean applySilenceEffect(Player player, LivingEntity target) {
        ItemStack weapon = player.getInventory().getItemInMainHand();
        if (weapon != null && weapon.hasItemMeta()) {
            ItemMeta meta = weapon.getItemMeta();
            List<String> lore = meta.getLore();
            if (lore != null) {
                int silenceTier = getTierFromLoreLine(lore.get(0), "§eSilence");
                if (silenceTier != -1) {
                    double silenceChance = getSilenceChance(silenceTier);
                    if (Math.random() < silenceChance) {
                        SilenceManager.applySilence((Player) target, 1.5); // Apply Silence for 1.5 seconds
                        player.sendMessage("§cYou have silenced " + target.getName() + "!");
                        return true; // Silence was applied
                    }
                }
            }
        }
        return false; // Silence was not applied
    }

    private void applyBleedEffect(Player player, LivingEntity target) {
        ItemStack weapon = player.getInventory().getItemInMainHand();
        if (weapon != null && weapon.hasItemMeta()) {
            ItemMeta meta = weapon.getItemMeta();
            List<String> lore = meta.getLore();
            if (lore != null) {
                int bleedTier = getTierFromLoreLine(lore.get(0), "§eBleed");
                if (bleedTier != -1) {
                    double bleedChance = getBleedChance(bleedTier);
                    if (Math.random() < bleedChance) {
                        int duration = getBleedDuration(bleedTier);
                        startBleedTask(target, duration);

                        player.sendMessage("§eYou have applied Bleed " + bleedTier + " to " + target.getName() + "!");
                        if (target instanceof Player) {
                            Player targetPlayer = (Player) target;
                            targetPlayer.sendMessage("§cYou have been hit with Bleed " + bleedTier + " by " + player.getName() + "!");
                        }
                    }
                }
            }
        }
    }

    private double getSilenceChance(int tier) {
        switch (tier) {
            case 1:
                return 0.10;
            case 2:
                return 0.125;
            case 3:
                return 0.15;
            default:
                return 0.0;
        }
    }

    private double getBleedChance(int tier) {
        switch (tier) {
            case 1: return 0.15;
            case 2: return 0.175;
            case 3: return 0.20;
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
                this.cancel();
                return;
            }

            double damage = target.getHealth() * 0.02;
            target.damage(damage);
            remainingTime -= 20;
        }

        public int getRemainingTime() {
            return remainingTime;
        }
    }
}

