package me.crunchycars.customEnchants;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class ArmoredEnchantment extends CustomEnchantment {

    public ArmoredEnchantment(int tier) {
        super("§eArmored " + tier, tier);
    }

    @Override
    public boolean canApplyTo(Material material) {
        return isIronOrAboveArmor(material); // Can be applied to iron armor and above
    }

    private boolean isIronOrAboveArmor(Material material) {
        return material == Material.IRON_HELMET || material == Material.IRON_CHESTPLATE || material == Material.IRON_LEGGINGS || material == Material.IRON_BOOTS ||
                material == Material.DIAMOND_HELMET || material == Material.DIAMOND_CHESTPLATE || material == Material.DIAMOND_LEGGINGS || material == Material.DIAMOND_BOOTS ||
                material == Material.NETHERITE_HELMET || material == Material.NETHERITE_CHESTPLATE || material == Material.NETHERITE_LEGGINGS || material == Material.NETHERITE_BOOTS;
    }

    @Override
    public void applyEffect(Player damagedPlayer, LivingEntity attacker, ItemStack item) {

        // Only reduce damage if the attacker is using a sword
        if (attacker instanceof Player) {
            Player attackingPlayer = (Player) attacker;
            ItemStack weapon = attackingPlayer.getInventory().getItemInMainHand();

            // Check if the attacker is using a sword
            if (weapon != null && isSword(weapon.getType())) {

                // Calculate total reduction based on all armor pieces
                double totalReduction = getTotalArmorReduction(damagedPlayer);

                // Apply damage reduction
                double newDamage = ((EntityDamageByEntityEvent) damagedPlayer.getLastDamageCause()).getDamage() * (1 - totalReduction);
                ((EntityDamageByEntityEvent) damagedPlayer.getLastDamageCause()).setDamage(newDamage);
            }
        }
    }

    // Check if the item is a sword
    private boolean isSword(Material material) {
        return material == Material.WOODEN_SWORD || material == Material.STONE_SWORD ||
                material == Material.IRON_SWORD || material == Material.DIAMOND_SWORD ||
                material == Material.NETHERITE_SWORD || material == Material.GOLDEN_SWORD;
    }

    // Calculate the total damage reduction from all armor pieces with the Armored enchantment
    private double getTotalArmorReduction(Player player) {
        double totalReduction = 0;

        // Loop through each armor piece
        for (ItemStack armorPiece : player.getInventory().getArmorContents()) {
            if (armorPiece != null && armorPiece.hasItemMeta()) {
                for (CustomEnchantment enchantment : CustomEnchantmentManager.getAllEnchantments()) {
                    if (enchantment instanceof ArmoredEnchantment && enchantment.isAppliedTo(armorPiece)) {
                        // Add the reduction for each piece of armor with the Armored enchantment
                        totalReduction += ((ArmoredEnchantment) enchantment).getDamageReduction();
                    }
                }
            }
        }

        return totalReduction;
    }

    private double getDamageReduction() {
        switch (getTier()) {
            case 1:
                return 0.0125; // 10% damage reduction
            case 2:
                return 0.025; // 15% damage reduction
            default:
                return 0;
        }
    }
}