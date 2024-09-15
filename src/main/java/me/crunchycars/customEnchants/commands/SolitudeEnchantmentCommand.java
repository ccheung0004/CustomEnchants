package me.crunchycars.customEnchants.commands;

import me.crunchycars.customEnchants.SolitudeEnchantment;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class SolitudeEnchantmentCommand implements CommandExecutor {

    private final SolitudeEnchantment enchantment;

    public SolitudeEnchantmentCommand(SolitudeEnchantment enchantment) {
        this.enchantment = enchantment;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("solitude.enchant")) {
            player.sendMessage("§cYou do not have permission to use this command.");
            return true;
        }

        if (args.length != 1) {
            player.sendMessage("§cUsage: /" + label + " <successRate>");
            return true;
        }

        int successRate;
        try {
            successRate = Integer.parseInt(args[0]);
            if (successRate < 0 || successRate > 100) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            player.sendMessage("§cPlease enter a valid success rate (0-100).");
            return true;
        }

        int tier;
        switch (label.toLowerCase()) {
            case "solitude1":
                tier = 1;
                break;
            case "solitude2":
                tier = 2;
                break;
            case "solitude3":
                tier = 3;
                break;
            default:
                player.sendMessage("§cInvalid command.");
                return true;
        }

        ItemStack book = new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta meta = book.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("§6Solitude " + tier);
            meta.setLore(Arrays.asList(
                    "§7Success Rate: §a" + successRate + "%",
                    "§7Increases your opponent's silence duration."
            ));
            book.setItemMeta(meta);
        }

        player.getInventory().addItem(book);
        player.sendMessage("§aYou have received a Solitude " + tier + " enchantment book with a " + successRate + "% success rate.");

        return true;
    }
}