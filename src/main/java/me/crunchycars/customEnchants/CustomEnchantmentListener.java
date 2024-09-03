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
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CustomEnchantmentListener implements Listener {

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

                CustomEnchantment enchantment = getEnchantmentFromName(enchantmentName);
                if (enchantment != null) {
                    applyEnchantmentToItem(player, currentItem, cursorItem, enchantment);
                    applyEffects(player); // Apply effects after enchantment application
                }
            }
        }
    }

    private CustomEnchantment getEnchantmentFromName(String enchantmentName) {
        if (enchantmentName.startsWith("§eSilence")) {
            int tier = Integer.parseInt(enchantmentName.substring(enchantmentName.length() - 1)); // Get the tier number
            return new SilenceEnchantment(tier);
        } else if (enchantmentName.startsWith("§6Vitality")) {
            int tier = Integer.parseInt(enchantmentName.substring(enchantmentName.length() - 1)); // Get the tier number
            return new VitalityEnchantment(tier);
        } else if (enchantmentName.startsWith("§eBleed")) {
            int tier = Integer.parseInt(enchantmentName.substring(enchantmentName.length() - 1)); // Get the tier number
            return new BleedEnchantment(tier);
        } else if (enchantmentName.startsWith("§6Swiftness")) {
            return new SwiftnessEnchantment();
        } else if (enchantmentName.startsWith("§bDivine Lightning")) {
            return new DivineLightningEnchantment();
        }
        return null;
    }

    private void applyEnchantmentToItem(Player player, ItemStack currentItem, ItemStack cursorItem, CustomEnchantment enchantment) {
        if (!enchantment.canApplyTo(currentItem.getType())) {
            player.sendMessage("§cThis enchantment cannot be applied to this item!");
            return;
        }

        ItemMeta itemMeta = currentItem.getItemMeta();
        List<String> lore = itemMeta != null && itemMeta.hasLore() ? itemMeta.getLore() : new ArrayList<>();

        boolean applied = false;
        for (int i = 0; i < lore.size(); i++) {
            String loreLine = lore.get(i);
            if (loreLine.startsWith(enchantment.getName().split(" ")[0])) {
                int existingTier = getTierFromLoreLine(loreLine);
                if (existingTier >= enchantment.getTier()) {
                    player.sendMessage("§cA higher or equal tier of this enchantment is already applied to this item!");
                    return;
                } else {
                    // Replace with the higher-tier enchantment
                    lore.set(i, enchantment.getName());
                    applied = true;
                    break;
                }
            }
        }

        // Get the success rate from the book's lore
        int successRate = getSuccessRateFromBook(cursorItem);
        if (successRate == -1) {
            player.sendMessage("§cInvalid enchantment book!");
            return;
        }

        // Determine if the enchantment should be applied based on the success rate
        if (attemptEnchantment(successRate)) {
            if (!applied) {
                lore.add(enchantment.getName());
            }

            itemMeta.setLore(lore);
            currentItem.setItemMeta(itemMeta);

            player.sendMessage("§aSuccessfully applied " + enchantment.getName() + "!");
            player.playSound(player.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1.0f, 1.0f);
            player.setItemOnCursor(null); // Consume the book
        } else {
            player.sendMessage("§cThe enchantment failed, and the book was destroyed.");
            player.playSound(player.getLocation(), Sound.ITEM_SHIELD_BREAK, 1.0f, 1.0f);
            player.setItemOnCursor(null); // Consume the book on failure
        }
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

    private int getTierFromLoreLine(String loreLine) {
        // Assuming the tier is the last character in the lore string
        return Integer.parseInt(loreLine.substring(loreLine.length() - 1));
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            ItemStack weapon = player.getInventory().getItemInMainHand();

            for (CustomEnchantment enchantment : CustomEnchantmentManager.getAllEnchantments()) {
                if (enchantment.isAppliedTo(weapon)) {
                    enchantment.applyEffect(player, (LivingEntity) event.getEntity(), weapon);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        applyEffects(event.getPlayer());
    }

    @EventHandler
    public void onPlayerItemHeld(PlayerItemHeldEvent event) {
        applyEffects(event.getPlayer());
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

        // Update the player's health based on Vitality
        AttributeInstance healthAttribute = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (healthAttribute != null) {
            double newMaxHealth = 20.0 + healthBoost;
            healthAttribute.setBaseValue(newMaxHealth);

            if (player.getHealth() > newMaxHealth) {
                player.setHealth(newMaxHealth);
            }
        }

        // Apply or remove speed based on Swiftness
        if (hasSwiftness) {
            if (!player.hasPotionEffect(PotionEffectType.SPEED)) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, true, false));
            }
        } else {
            player.removePotionEffect(PotionEffectType.SPEED);
        }
    }
}