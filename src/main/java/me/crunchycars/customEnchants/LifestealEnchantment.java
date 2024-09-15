package me.crunchycars.customEnchants;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class LifestealEnchantment extends CustomEnchantment {

    public LifestealEnchantment(int tier) {
        super("§6Lifesteal " + tier, tier);
    }

    @Override
    public boolean canApplyTo(Material material) {
        switch (getTier()) {
            case 1:
                return material == Material.WOODEN_SWORD || material == Material.STONE_SWORD || material == Material.GOLDEN_SWORD;
            case 2:
                return material == Material.IRON_SWORD ||
                        material == Material.DIAMOND_SWORD ||
                        material == Material.NETHERITE_SWORD || material == Material.NETHERITE_AXE;
            case 3:
                return material == Material.DIAMOND_SWORD ||
                        material == Material.NETHERITE_SWORD || material == Material.NETHERITE_AXE;
            default:
                return false;
        }
    }

    @Override
    public void applyEffect(Player damager, LivingEntity target, ItemStack item) {
        // The applyEffect method can call the onEntityDamageByEntity method
        EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(damager, target, EntityDamageByEntityEvent.DamageCause.ENTITY_ATTACK, damager.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).getValue());
        onEntityDamageByEntity(event, damager, target, item);
    }

    @Override
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event, Player damager, LivingEntity target, ItemStack item) {
        double damage = event.getDamage();
        double healAmount;

        switch (getTier()) {
            case 1:
                healAmount = damage * 0.03; // 3% of damage
                break;
            case 2:
                healAmount = damage * 0.04; // 4% of damage
                break;
            case 3:
                healAmount = damage * 0.05; // 5% of damage
                break;
            default:
                healAmount = 0;
        }

        damager.setHealth(Math.min(damager.getHealth() + healAmount, damager.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()));
    }
}