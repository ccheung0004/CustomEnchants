package me.crunchycars.customEnchants;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SwiftnessEnchantment extends CustomEnchantment {

    public SwiftnessEnchantment() {
        super("§6Swiftness I");
    }

    @Override
    public boolean canApplyTo(Material material) {
        return material == Material.DIAMOND_BOOTS;
    }

    @Override
    public void applyToItem(ItemStack item) {
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            List<String> lore = meta.hasLore() ? meta.getLore() : new ArrayList<>();

            if (!lore.contains("§6Swiftness I")) {
                lore.add("§6Swiftness I");
            }

            meta.setLore(lore);
            item.setItemMeta(meta);
        }
    }

    @Override
    public boolean isAppliedTo(ItemStack item) {
        if (item.hasItemMeta()) {
            ItemMeta meta = item.getItemMeta();
            return meta != null && meta.hasLore() && meta.getLore().contains("§6Swiftness I");
        }
        return false;
    }
}