package me.crunchycars.customEnchants;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SilenceManager {

    private static final Map<UUID, Long> silencedPlayers = new HashMap<>();

    public static void applySilence(Player player, double durationInSeconds) {
        long endTime = System.currentTimeMillis() + (long) (durationInSeconds * 1000);
        silencedPlayers.put(player.getUniqueId(), endTime);
        player.sendMessage("§c§l(!) §cYou have been silenced.");
    }

    public static boolean isSilenced(Player player) {
        Long silenceEndTime = silencedPlayers.get(player.getUniqueId());
        if (silenceEndTime == null || silenceEndTime < System.currentTimeMillis()) {
            silencedPlayers.remove(player.getUniqueId());
            return false;
        }
        return true;
    }

    public static void extendSilence(Player player, int extraSeconds) {
        Long silenceEndTime = silencedPlayers.get(player.getUniqueId());
        if (silenceEndTime != null) {
            long newEndTime = silenceEndTime + (extraSeconds * 1000L); // Extend the silence duration
            silencedPlayers.put(player.getUniqueId(), newEndTime);
            player.sendMessage("§c§l(!) §cYour silence duration has been extended!");
        }
    }
}