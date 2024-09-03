package me.crunchycars.customEnchants;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class CustomEnchantment {

    private final String name;
    private final int tier;

    public CustomEnchantment(String name, int tier) {
        this.name = name;
        this.tier = tier;
    }

    public String getName() {
        return name;
    }

    public int getTier() {
        return tier;
    }

    // Abstract methods to be implemented by subclasses
    public abstract boolean canApplyTo(Material material);

    public abstract void applyEffect(Player player, LivingEntity target, ItemStack item);

    public boolean isAppliedTo(ItemStack item) {
        if (item.hasItemMeta() && item.getItemMeta().hasLore()) {
            for (String lore : item.getItemMeta().getLore()) {
                if (lore.equals(name)) {
                    return true;
                }
            }
        }
        return false;
    }
}