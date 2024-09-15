package me.crunchycars.customEnchants;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Party {
    private UUID leader;
    private final Set<UUID> members = new HashSet<>();

    public Party(UUID leader) {
        this.leader = leader;
        this.members.add(leader);
    }

    public boolean invite(UUID player) {
        return members.add(player);
    }

    public void removeMember(UUID player) {
        members.remove(player);
    }

    public boolean isMember(UUID player) {
        return members.contains(player);
    }

    public boolean isLeader(UUID player) {
        return leader.equals(player);
    }

    public void setLeader(UUID newLeader) {
        this.leader = newLeader;
    }

    public Set<UUID> getMembers() {
        return Collections.unmodifiableSet(members);
    }

    public String getLeaderName() {
        Player leaderPlayer = Bukkit.getPlayer(leader);
        return (leaderPlayer != null) ? leaderPlayer.getName() : "Unknown";
    }
}