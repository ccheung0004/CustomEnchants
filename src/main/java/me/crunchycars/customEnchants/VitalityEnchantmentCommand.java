package me.crunchycars.customEnchants;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class VitalityEnchantmentCommand implements CommandExecutor {

    private final int tier;

    public VitalityEnchantmentCommand(int tier) {
        this.tier = tier;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be run by a player.");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("vitality.enchant")) {
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
            meta.setDisplayName("§6Vitality " + tier);
            meta.setLore(Arrays.asList(
                    "§7Success Rate: §a" + successRate + "%",
                    "§7Grants additional health when applied to armor"
            ));
            book.setItemMeta(meta);
        }

        player.getInventory().addItem(book);
        player.sendMessage("§aYou have received a Vitality " + tier + " enchantment book with " + successRate + "% success rate.");

        return true;
    }
}