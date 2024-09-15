package me.crunchycars.customEnchants;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AegisManager {

    private static final Map<UUID, Double> aegisEffects = new HashMap<>();

    public static void applyAegisEffect(Player player, double reductionAmount) {
        aegisEffects.put(player.getUniqueId(), reductionAmount);
    }

    public static double getAegisEffect(Player player) {
        return aegisEffects.getOrDefault(player.getUniqueId(), 0.0);
    }

    public static void clearAegisEffect(Player player) {
        aegisEffects.remove(player.getUniqueId());
    }
}