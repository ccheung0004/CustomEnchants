package me.crunchycars.customEnchants.commands;

import me.crunchycars.customEnchants.DivineLightningEnchantment;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class DivineLightningEnchantmentCommand implements CommandExecutor {

    private final DivineLightningEnchantment enchantment;

    public DivineLightningEnchantmentCommand(DivineLightningEnchantment enchantment) {
        this.enchantment = enchantment;
        Bukkit.getLogger().info("DivineLightningEnchantmentCommand initialized.");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Debug: Command execution start
        Bukkit.getLogger().info("DivineLightningEnchantmentCommand executed.");

        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            Bukkit.getLogger().info("Command sender is not a player.");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("divine.lightning.enchant")) {
            player.sendMessage("§cYou do not have permission to use this command.");
            Bukkit.getLogger().info("Player " + player.getName() + " does not have permission.");
            return true;
        }

        if (args.length != 1) {
            player.sendMessage("§cUsage: /" + label + " <successRate>");
            Bukkit.getLogger().info("Invalid number of arguments provided.");
            return true;
        }

        int successRate;
        try {
            successRate = Integer.parseInt(args[0]);
            if (successRate < 0 || successRate > 100) {
                throw new NumberFormatException();
            }
            Bukkit.getLogger().info("Parsed success rate: " + successRate);
        } catch (NumberFormatException e) {
            player.sendMessage("§cPlease enter a valid percentage (0-100).");
            Bukkit.getLogger().info("Invalid success rate provided: " + args[0]);
            return true;
        }

        ItemStack book = new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta meta = book.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(enchantment.getName());
            meta.setLore(Arrays.asList(
                    "§7Success Rate: §a" + successRate + "%",
                    "§7Has a chance to strike your target with lightning."
            ));
            book.setItemMeta(meta);
            Bukkit.getLogger().info("Created enchantment book with success rate: " + successRate);
        } else {
            Bukkit.getLogger().severe("Failed to create ItemMeta for the enchantment book.");
        }

        player.getInventory().addItem(book);
        player.sendMessage("§aYou have received a " + enchantment.getName() + " enchantment book with " + successRate + "% success rate.");
        Bukkit.getLogger().info("Gave " + player.getName() + " a Divine Lightning enchantment book with " + successRate + "% success rate.");

        return true;
    }
}