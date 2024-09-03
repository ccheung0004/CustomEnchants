package me.crunchycars.customEnchants;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SwiftnessEnchantment extends CustomEnchantment {

    public SwiftnessEnchantment() {
        super("§6Swiftness 1", 1);
    }

    @Override
    public boolean canApplyTo(Material material) {
        return material == Material.DIAMOND_BOOTS; // Swiftness can be applied only to diamond boots
    }

    @Override
    public void applyEffect(Player player, LivingEntity target, ItemStack item) {
        if (!player.hasPotionEffect(PotionEffectType.SPEED)) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, true, false));
        }
    }
}