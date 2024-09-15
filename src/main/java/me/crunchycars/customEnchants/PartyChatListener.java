package me.crunchycars.customEnchants;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.UUID;

public class PartyChatListener implements Listener {

    private final PartyManager partyManager;

    public PartyChatListener(PartyManager partyManager) {
        this.partyManager = partyManager;
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        UUID playerUUID = event.getPlayer().getUniqueId();

        // Check if the player has party chat enabled
        if (partyManager.isPartyChatEnabled(playerUUID)) {
            // Cancel the original chat event
            event.setCancelled(true);

            // Send the message to the player's party members
            String message = event.getMessage();
            partyManager.sendPartyMessage(playerUUID, message);
        }
    }
}