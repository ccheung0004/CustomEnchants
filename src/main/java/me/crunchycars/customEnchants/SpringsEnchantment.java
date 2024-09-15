package me.crunchycars.customEnchants;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SpringsEnchantment extends CustomEnchantment {

    public SpringsEnchantment() {
        super("§9Springs", 1); // Only one tier for Springs
    }

    @Override
    public boolean canApplyTo(Material material) {
        return material.name().endsWith("_BOOTS"); // Springs can be applied to boots only
    }

    public void applyJumpBoost(Player player) {
        // Apply jump boost effect
        player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 0, false, false));
    }

    public void removeJumpBoost(Player player) {
        // Remove the jump boost effect
        player.removePotionEffect(PotionEffectType.JUMP);
    }

    @Override
    public void applyEffect(Player player, LivingEntity target, ItemStack item) {
        // No direct interaction effect needed for Springs, handled in applyEffects method
    }
}