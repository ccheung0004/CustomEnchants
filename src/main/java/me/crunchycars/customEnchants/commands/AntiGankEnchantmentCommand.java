package me.crunchycars.customEnchants.commands;

import me.crunchycars.customEnchants.AntiGankEnchantment;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class AntiGankEnchantmentCommand implements CommandExecutor {

    private final AntiGankEnchantment enchantment;

    public AntiGankEnchantmentCommand(AntiGankEnchantment enchantment) {
        this.enchantment = enchantment;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("antigank.enchant")) {
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
            player.sendMessage("§cPlease enter a valid percentage (0-100).");
            return true;
        }

        ItemStack book = new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta meta = book.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(enchantment.getName());
            meta.setLore(Arrays.asList(
                    "§7Success Rate: §a" + successRate + "%",
                    "§7Gives Resistance when more than 5 players are hitting you",
                    "§7Cooldown: " + getCooldownDescription(enchantment.getTier())
            ));
            book.setItemMeta(meta);
        }

        player.getInventory().addItem(book);
        player.sendMessage("§aYou have received a " + enchantment.getName() + " enchantment book with " + successRate + "% success rate.");

        return true;
    }

    private String getCooldownDescription(int tier) {
        switch (tier) {
            case 1:
                return "1 minute 30 seconds";
            case 2:
                return "1 minute 15 seconds";
            case 3:
                return "1 minute";
            default:
                return "1 minute";
        }
    }
}