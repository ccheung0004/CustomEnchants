package me.crunchycars.customEnchants;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;



import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

public class CustomEnchantmentListener implements Listener {

    private final PartyManager partyManager;
    public CustomEnchantmentListener(PartyManager partyManager) {
        this.partyManager = partyManager; // Initialize the field
    }


    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        ItemStack currentItem = event.getCurrentItem();
        ItemStack cursorItem = event.getCursor();




        // Check if cursor item is an enchanted book
        if (cursorItem.getType() == Material.ENCHANTED_BOOK) {

            ItemMeta cursorMeta = cursorItem.getItemMeta();
            if (cursorMeta != null && cursorMeta.hasDisplayName()) {
                String enchantmentName = cursorMeta.getDisplayName();

                // Pass the player to the getEnchantmentFromName method
                CustomEnchantment enchantment = getEnchantmentFromName(enchantmentName, player);
                if (enchantment != null) {

                    // Check if the item (currentItem) can have the enchantment applied
                    if (!enchantment.canApplyTo(currentItem.getType())) {
                        player.sendMessage("§cThis enchantment cannot be applied to this item!");
                        return;
                    }

                    // Check for conflicting enchantments (if applicable)
                    if (enchantment instanceof ArmoredEnchantment || enchantment instanceof TankEnchantment) {
                        if (hasConflictingEnchantment(currentItem, enchantment)) {
                            player.sendMessage("§cYou cannot apply both Armored and Tank enchantments to the same item!");
                            return;
                        }
                    }

                    int successRate = getSuccessRateFromLore(cursorItem);

                    // Roll for success and apply enchantment if successful
                    if (rollSuccess(successRate)) {
                        applyEnchantmentToItem(player, currentItem, cursorItem, enchantment);
                        applyEffects(player); // Apply effects after enchantment application
                    } else {
                        player.sendMessage("§cThe enchantment application failed!");
                        removeBookFromCursor(player); // Consume the book on failure
                    }
                }
            }
        }
    }


    private int getSuccessRateFromLore(ItemStack item) {
        if (item.hasItemMeta() && item.getItemMeta().hasLore()) {
            List<String> lore = item.getItemMeta().getLore();
            if (lore != null && !lore.isEmpty()) {
                // Look for the "Success Rate: <value>%" line
                for (String line : lore) {
                    if (line.contains("Success Rate:")) {
                        try {
                            // Strip color codes from the line
                            String strippedLine = ChatColor.stripColor(line);

                            // Extract the success rate from the stripped string (e.g., "Success Rate: 100%")
                            String[] parts = strippedLine.split(": ");
                            if (parts.length == 2) {
                                return Integer.parseInt(parts[1].replace("%", "").trim()); // Extract the percentage number
                            }
                        } catch (NumberFormatException e) {
                            Bukkit.getLogger().warning("Could not parse success rate from: " + line);
                        }
                    }
                }
            }
        }
        return 100; // Default to 100% success if no success rate found
    }

    private CustomEnchantment getEnchantmentFromName(String enchantmentName, Player player) {
        for (CustomEnchantment enchantment : CustomEnchantmentManager.getAllEnchantments()) {
            if (enchantmentName.startsWith(enchantment.getName())) {
                return enchantment;
            }
        }
        return null;
    }

    private void applyEnchantmentToItem(Player player, ItemStack currentItem, ItemStack cursorItem, CustomEnchantment enchantment) {
        // Check if the item can have this enchantment applied
        if (!enchantment.canApplyTo(currentItem.getType())) {
            player.sendMessage("§cThis enchantment cannot be applied to this item!");
            removeBookFromCursor(player); // Remove the book even if enchantment cannot be applied
            return;
        }

        // Check if the item already has an equal or higher-tier enchantment
        ItemMeta itemMeta = currentItem.getItemMeta();
        List<String> lore = itemMeta != null && itemMeta.hasLore() ? itemMeta.getLore() : new ArrayList<>();

        for (String loreLine : lore) {
            if (loreLine.startsWith(enchantment.getName().split(" ")[0])) {
                int existingTier = getTierFromLoreLine(loreLine);
                if (existingTier >= enchantment.getTier()) {
                    player.sendMessage("§cA higher or equal tier of this enchantment is already applied to this item!");
                    removeBookFromCursor(player); // Remove the book even if enchantment fails due to existing tier
                    return;
                }
            }
        }

        // Now check for the success rate
        int successRate = getSuccessRateFromLore(cursorItem);
        if (Math.random() * 100 > successRate) {
            // Enchantment application failed
            player.sendMessage("§cThe enchantment application failed!");
            removeBookFromCursor(player); // Consume the book on failure
            return;
        }

        // Success: Apply the enchantment
        boolean applied = false;

        // Loop through existing lore to replace or apply the enchantment
        for (int i = 0; i < lore.size(); i++) {
            String loreLine = lore.get(i);
            if (loreLine.startsWith(enchantment.getName().split(" ")[0])) {
                // Replace with the higher-tier enchantment
                lore.set(i, enchantment.getName());
                applied = true;
                break;
            }
        }

        if (!applied) {
            lore.add(enchantment.getName());
        }

        itemMeta.setLore(lore);
        currentItem.setItemMeta(itemMeta);

        player.sendMessage("§aSuccessfully applied " + enchantment.getName() + "!");
        player.playSound(player.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1.0f, 1.0f);

        // Book is consumed whether or not the enchantment succeeds or fails
        removeBookFromCursor(player);
    }


    private void removeBookFromCursor(Player player) {
        // Remove the enchanted book from the player's cursor (consume the book)
        player.setItemOnCursor(null);


    }




    private int getTierFromLoreLine(String loreLine) {
        // Assuming the tier is the last character in the lore string
        return Integer.parseInt(loreLine.substring(loreLine.length() - 1));
    }

    // Method to check for conflicting enchantments
    private boolean hasConflictingEnchantment(ItemStack item, CustomEnchantment newEnchantment) {
        if (item.hasItemMeta() && item.getItemMeta().hasLore()) {
            List<String> lore = item.getItemMeta().getLore();
            for (String loreLine : lore) {
                if ((newEnchantment instanceof ArmoredEnchantment && loreLine.contains("§6Tank")) ||
                        (newEnchantment instanceof TankEnchantment && loreLine.contains("§6Armored"))) {
                    return true; // Conflict detected
                }
            }
        }
        return false;
    }



    // Roll for success based on a given success rate
    private boolean rollSuccess(int successRate) {
        Random random = new Random();
        int roll = random.nextInt(100) + 1; // Roll between 1 and 100
        return roll <= successRate; // Return true if the roll is within the success rate
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        // Debug: Damage event triggered
        Bukkit.getLogger().info("[DEBUG] onEntityDamageByEntity triggered.");

        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            Player damagedPlayer = (Player) event.getEntity();
            Player damagerPlayer = (Player) event.getDamager();

            if (partyManager.isInParty(damagerPlayer.getUniqueId()) && partyManager.isInParty(damagedPlayer.getUniqueId())) {
                Party damagerParty = partyManager.getParty(damagerPlayer.getUniqueId());
                Party targetParty = partyManager.getParty(damagedPlayer.getUniqueId());

                if (damagerParty != null && damagerParty.equals(targetParty)) {
                    event.setCancelled(true); // Cancel the event, preventing any enchantment activation
                    return;
                }
            }

            // Handle Riposte blocking if active
            if (RiposteEnchantment.handleRiposte(damagedPlayer, event)) {
                return; // Riposte activated and blocked the damage, stop further processing
            }

            // Reset Frenzy if the damaged player was on a streak
            FrenzyEnchantment.resetFrenzy(damagedPlayer);

            // Apply effects for armor-based enchantments on the damaged player
            for (ItemStack armorPiece : damagedPlayer.getInventory().getArmorContents()) {
                if (armorPiece != null && armorPiece.hasItemMeta()) {
                    CustomEnchantmentManager.getAllEnchantments().forEach(enchantment -> {
                        if (enchantment.isAppliedTo(armorPiece)) {

                            // Handle SupermanEnchantment
                            if (enchantment instanceof SupermanEnchantment) {
                                // Superman should only activate if health is <= 4
                                if (damagedPlayer.getHealth() <= 8) {
                                    enchantment.applyEffect(damagedPlayer, null, armorPiece); // Apply Superman
                                }
                            }
                            // Handle Last Stand Enchantment or any other
                            else if (enchantment instanceof LastStandEnchantment) {
                                double currentHealth = damagedPlayer.getHealth();
                                if (currentHealth <= 4) {
                                    ((LastStandEnchantment) enchantment).applyEffect(damagedPlayer, damagerPlayer, armorPiece); // Apply Last Stand
                                }
                            } else {
                                // Apply other armor-based enchantment effects
                                enchantment.applyEffect(damagedPlayer, damagerPlayer, armorPiece);
                            }
                        }
                    });
                }
            }

            // Apply effects for weapon-based enchantments on the damager player
            ItemStack weapon = damagerPlayer.getInventory().getItemInMainHand();
            if (weapon != null && weapon.hasItemMeta()) {
                double aegisMultiplier = AegisEnchantment.getAegisReductionMultiplier(damagedPlayer); // Get Aegis reduction multiplier

                CustomEnchantmentManager.getAllEnchantments().forEach(enchantment -> {
                    if (enchantment.isAppliedTo(weapon)) {
                        // Debug: Enchantment found on weapon

                        // Check if the Swordsman enchantment applies and the damaged player is holding an axe
                        if (enchantment instanceof SwordsmanEnchantment && isHoldingAxe(damagedPlayer)) {
                            ((SwordsmanEnchantment) enchantment).applyEffect(damagerPlayer, damagedPlayer, weapon);
                        }
                        // Check if the Axeman enchantment applies and the damaged player is holding a sword
                        else if (enchantment instanceof AxemanEnchantment && isHoldingSword(damagedPlayer)) {
                            ((AxemanEnchantment) enchantment).applyEffect(damagerPlayer, damagedPlayer, weapon);
                        } else {
                            // Apply weapon enchantment effects with a reduced chance if Aegis is active
                            if (Math.random() <= aegisMultiplier) {
                                enchantment.applyEffect(damagerPlayer, damagedPlayer, weapon);
                            }
                        }
                    }
                });
            }
        }
    }

    // Helper method to check if the player is holding an axe
    private boolean isHoldingAxe(Player player) {
        ItemStack heldItem = player.getInventory().getItemInMainHand();
        return heldItem != null && heldItem.getType().name().endsWith("_AXE");
    }

    // Helper method to check if the player is holding a sword
    private boolean isHoldingSword(Player player) {
        ItemStack heldItem = player.getInventory().getItemInMainHand();
        return heldItem != null && heldItem.getType().name().endsWith("_SWORD");
    }


    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        // Check if the interaction is a right-click and player is holding an item
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Bukkit.getLogger().info("Right-click detected by player: " + player.getName()); // Debug message

            // Get the enchantment from the item
            CustomEnchantment enchantment = getEnchantmentFromItem(item);

            // Check if the item has the Leviathan's Breath enchantment
            if (enchantment instanceof LeviathansBreathEnchantment) {
                Bukkit.getLogger().info("Leviathan's Breath enchantment detected on item."); // Debug message

                // Cast the enchantment to LeviathansBreathEnchantment
                LeviathansBreathEnchantment leviathanEnchantment = (LeviathansBreathEnchantment) enchantment;

                // Activate the ability
                leviathanEnchantment.activateAbility(player);
                event.setCancelled(true); // Cancel any other interaction with the right-click
            }
        }
    }

// Helper method to get enchantment from item
    private CustomEnchantment getEnchantmentFromItem(ItemStack item) {
        if (item.hasItemMeta() && item.getItemMeta().hasLore()) {
            List<String> lore = item.getItemMeta().getLore();
            Bukkit.getLogger().info("Item has lore: " + lore); // Debug message to see the lore on the item
            for (String loreLine : lore) {
                String strippedLoreLine = ChatColor.stripColor(loreLine);
                for (CustomEnchantment enchantment : CustomEnchantmentManager.getAllEnchantments()) {
                    String enchantmentName = enchantment.getName();
                    String strippedEnchantmentName = ChatColor.stripColor(enchantmentName);
                    // Use exact match instead of contains
                    if (strippedLoreLine.equalsIgnoreCase(strippedEnchantmentName)) {
                        return enchantment;
                    }
                }
            }
        }
        return null;
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
        boolean hasSprings = false;

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
                    if (lore.contains("§9Springs")) {
                        hasSprings = true;
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
            SwiftnessEnchantment.applyWalkSpeed(player);
        } else {
            SwiftnessEnchantment.resetWalkSpeed(player);
        }

        // Apply or remove jump boost based on Springs
        if (hasSprings) {
            new SpringsEnchantment().applyJumpBoost(player);
        } else {
            new SpringsEnchantment().removeJumpBoost(player);
        }
    }
}