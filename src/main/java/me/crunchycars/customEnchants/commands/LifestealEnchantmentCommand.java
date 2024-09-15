package me.crunchycars.customEnchants.commands;

import me.crunchycars.customEnchants.LifestealEnchantment;
import me.crunchycars.customEnchants.CustomEnchantmentManager;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class LifestealEnchantmentCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command!");
            return true;
        }

        Player player = (Player) sender;

        if (args.length < 2) {
            player.sendMessage("Usage: /lifesteal <tier> <successRate>");
            return true;
        }

        int tier;
        double successRate;

        try {
            tier = Integer.parseInt(args[0]);
            successRate = Double.parseDouble(args[1]);
        } catch (NumberFormatException e) {
            player.sendMessage("Please enter valid numbers for tier and success rate.");
            return true;
        }

        if (tier < 1 || tier > 3) {
            player.sendMessage("Tier must be between 1 and 3.");
            return true;
        }

        if (successRate < 0 || successRate > 100) {
            player.sendMessage("Success rate must be between 0 and 100.");
            return true;
        }

        // Create an enchanted book with the Lifesteal enchantment
        ItemStack enchantedBook = new ItemStack(Material.ENCHANTED_BOOK);
        EnchantmentStorageMeta meta = (EnchantmentStorageMeta) enchantedBook.getItemMeta();

        LifestealEnchantment lifestealEnchantment = new LifestealEnchantment(tier);
        String enchantmentName = lifestealEnchantment.getName();

        meta.setDisplayName(enchantmentName);
        meta.setLore(List.of("§eSuccess Rate: " + successRate + "%"));

        enchantedBook.setItemMeta(meta);

        // Give the enchanted book to the player
        player.getInventory().addItem(enchantedBook);
        player.sendMessage("You have received a Lifesteal " + tier + " enchanted book with a " + successRate + "% success rate!");

        return true;
    }
}