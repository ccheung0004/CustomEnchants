package me.crunchycars.customEnchants;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class PartyManager {

    private final CustomEnchants plugin; // Store a reference to the plugin
    private static final int MAX_PARTY_SIZE = 4;

    private final Map<UUID, Party> playerPartyMap = new HashMap<>();
    private final Map<UUID, Party> parties = new HashMap<>();
    private final Map<UUID, List<UUID>> pendingInvites = new HashMap<>();  // Track multiple invites
    private final Set<UUID> partyChatEnabled = new HashSet<>(); // Track players using party chat

    public PartyManager(CustomEnchants plugin) {
        this.plugin = plugin;
    }

    public boolean createParty(UUID leader) {
        if (playerPartyMap.containsKey(leader)) {
            return false; // Already in a party
        }
        Party party = new Party(leader);
        parties.put(leader, party);
        playerPartyMap.put(leader, party);
        return true;
    }

    public boolean inviteToParty(UUID leader, UUID invitee) {
        plugin.getLogger().info("inviteToParty called for leader: " + leader + " and invitee: " + invitee);

        Party party = parties.get(leader);
        if (party == null || playerPartyMap.containsKey(invitee)) {
            return false; // No party or invitee is already in a party
        }

        if (party.getMembers().size() >= MAX_PARTY_SIZE) {
            Player leaderPlayer = Bukkit.getPlayer(leader);
            if (leaderPlayer != null) {
                leaderPlayer.sendMessage("§c§l(!) §cYour party is full! The maximum number of members is " + MAX_PARTY_SIZE + ".");
            }
            return false; // Party is full
        }

        pendingInvites.computeIfAbsent(invitee, k -> new ArrayList<>()).add(leader);  // Add the invite to pendingInvites

        // Schedule a task to remove the invite after 30 seconds
        new BukkitRunnable() {
            @Override
            public void run() {
                List<UUID> inviters = pendingInvites.get(invitee);
                if (inviters != null) {
                    inviters.remove(leader);
                    if (inviters.isEmpty()) {
                        pendingInvites.remove(invitee);
                    }
                    Player inviteePlayer = Bukkit.getPlayer(invitee);
                    if (inviteePlayer != null && inviteePlayer.isOnline()) {
                        inviteePlayer.sendMessage("§c§l(!) §cThe party invite from " + Bukkit.getPlayer(leader).getName() + " has expired.");
                    }
                }
            }
        }.runTaskLater(plugin, 600L);  // Use the plugin reference stored in the constructor

        plugin.getLogger().info("Task scheduled to remove the invite after 30 seconds for invitee: " + invitee);
        return true;
    }

    public boolean acceptInvite(UUID invitee, UUID inviterUUID) {
        List<UUID> inviters = pendingInvites.get(invitee);
        if (inviters == null || !inviters.contains(inviterUUID)) {
            return false; // No invite from this inviter or already in a party
        }
        Party party = parties.get(inviterUUID);
        if (party == null) {
            return false; // No party to join
        }

        if (party.getMembers().size() >= MAX_PARTY_SIZE) {
            Player inviteePlayer = Bukkit.getPlayer(invitee);
            if (inviteePlayer != null) {
                inviteePlayer.sendMessage("§c§l(!) §cThe party is full and you cannot join.");
            }
            return false; // Party is full
        }

        party.invite(invitee);
        playerPartyMap.put(invitee, party);

        // Announce to the party that the new player has joined
        Player inviteePlayer = Bukkit.getPlayer(invitee);
        if (inviteePlayer != null) {
            String joinMessage = ChatColor.GREEN + inviteePlayer.getName() + " has joined the party!";
            for (UUID memberUUID : party.getMembers()) {
                Player member = Bukkit.getPlayer(memberUUID);
                if (member != null && member.isOnline()) {
                    member.sendMessage(joinMessage);
                }
            }
        }

        // Remove the invite from the list of pending invites
        inviters.remove(inviterUUID);
        if (inviters.isEmpty()) {
            pendingInvites.remove(invitee);
        }

        return true;
    }

    public boolean kickFromParty(UUID leader, UUID member) {
        Party party = parties.get(leader);
        if (party == null || !party.isMember(member)) {
            return false; // No party or member is not in the party
        }
        party.removeMember(member);
        playerPartyMap.remove(member);
        return true;
    }

    public boolean disbandParty(UUID leader) {
        Party party = parties.remove(leader);
        if (party == null) {
            return false; // No party to disband
        }

        // Notify all members that the party has been disbanded
        String disbandMessage = "§c§l(!) §cThe party has been disbanded.";
        for (UUID memberUUID : party.getMembers()) {
            Player member = Bukkit.getPlayer(memberUUID);
            if (member != null && member.isOnline()) {
                member.sendMessage(disbandMessage);
            }
            playerPartyMap.remove(memberUUID);
        }

        return true;
    }

    public boolean leaveParty(UUID player) {
        Party party = playerPartyMap.remove(player);
        if (party == null) {
            return false; // Not in a party
        }
        if (party.isLeader(player)) {
            disbandParty(player); // Leader leaves, disband the party
        } else {
            party.removeMember(player);
            // Notify remaining members that a player has left the party
            Player leavingPlayer = Bukkit.getPlayer(player);
            if (leavingPlayer != null) {
                String leaveMessage = ChatColor.RED + leavingPlayer.getName() + " has left the party.";
                for (UUID memberUUID : party.getMembers()) {
                    Player member = Bukkit.getPlayer(memberUUID);
                    if (member != null && member.isOnline()) {
                        member.sendMessage(leaveMessage);
                    }
                }
            }
        }
        return true;
    }

    public boolean isInParty(UUID player) {
        return playerPartyMap.containsKey(player);
    }

    public Party getParty(UUID player) {
        return playerPartyMap.get(player);
    }

    public boolean transferLeadership(UUID currentLeaderUUID, UUID newLeaderUUID) {
        Party party = parties.get(currentLeaderUUID);
        if (party == null || !party.isMember(newLeaderUUID)) {
            return false; // No party or new leader not in the party
        }
        party.setLeader(newLeaderUUID);
        parties.remove(currentLeaderUUID);
        parties.put(newLeaderUUID, party);
        playerPartyMap.put(newLeaderUUID, party);
        return true;
    }

    public boolean isLeader(UUID playerUUID) {
        Party party = playerPartyMap.get(playerUUID);
        return party != null && party.isLeader(playerUUID);
    }

    public List<Player> getOnlinePartyMembers(UUID playerUUID) {
        Party party = playerPartyMap.get(playerUUID);
        if (party != null) {
            List<Player> members = new ArrayList<>();
            for (UUID memberUUID : party.getMembers()) {
                Player member = Bukkit.getPlayer(memberUUID);
                if (member != null && member.isOnline()) {
                    members.add(member);
                }
            }
            return members;
        }
        return Collections.emptyList();
    }

    // Toggle party chat for a player
    public void togglePartyChat(UUID playerUUID) {
        if (partyChatEnabled.contains(playerUUID)) {
            partyChatEnabled.remove(playerUUID);
            Player player = Bukkit.getPlayer(playerUUID);
            if (player != null) {
                player.sendMessage(ChatColor.YELLOW + "Party chat disabled.");
            }
        } else {
            partyChatEnabled.add(playerUUID);
            Player player = Bukkit.getPlayer(playerUUID);
            if (player != null) {
                player.sendMessage(ChatColor.GREEN + "Party chat enabled. You will now only send messages to your party.");
            }
        }
    }

    // Check if a player has party chat enabled
    public boolean isPartyChatEnabled(UUID playerUUID) {
        return partyChatEnabled.contains(playerUUID);
    }

    // Send a message to all party members
    public void sendPartyMessage(UUID senderUUID, String message) {
        Party party = playerPartyMap.get(senderUUID);
        if (party != null) {
            String formattedMessage = ChatColor.GREEN + "[Party] " + Bukkit.getPlayer(senderUUID).getName() + ": " + ChatColor.WHITE + message;
            for (UUID memberUUID : party.getMembers()) {
                Player member = Bukkit.getPlayer(memberUUID);
                if (member != null && member.isOnline()) {
                    member.sendMessage(formattedMessage);
                }
            }
        }
    }
}