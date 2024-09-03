package me.crunchycars.customEnchants;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public abstract class CustomEnchantment {

    private final String name;

    public CustomEnchantment(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    // Abstract method that subclasses must implement
    public abstract boolean canApplyTo(Material material);

    public void applyToItem(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        List<String> lore = meta.hasLore() ? meta.getLore() : new ArrayList<>();
        lore.add(name);
        meta.setLore(lore);
        item.setItemMeta(meta);
    }

    public boolean isAppliedTo(ItemStack item) {
        if (item.hasItemMeta()) {
            ItemMeta meta = item.getItemMeta();
            if (meta.hasLore()) {
                for (String lore : meta.getLore()) {
                    if (lore.equals(name)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}