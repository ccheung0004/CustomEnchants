package me.crunchycars.customEnchants;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class SilenceEnchantmentCommand implements CommandExecutor {

    private final int tier;

    public SilenceEnchantmentCommand(int tier) {
        this.tier = tier;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used by players.");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("silence.enchant")) {
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
            player.sendMessage("§cInvalid success rate. Please enter a number between 0 and 100.");
            return true;
        }

        ItemStack enchantedBook = new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta meta = enchantedBook.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("§eSilence " + tier);
            meta.setLore(Arrays.asList(
                    "§7Success Rate: §a" + successRate + "%",
                    "§7Has a chance of silencing the target when hitting them."
            ));
            enchantedBook.setItemMeta(meta);

            player.getInventory().addItem(enchantedBook);
            player.sendMessage("§aYou have received a Silence " + tier + " enchantment book with " + successRate + "% success rate.");
        }

        return true;
    }
}