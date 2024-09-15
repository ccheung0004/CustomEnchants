package me.crunchycars.customEnchants;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PartyCommand implements CommandExecutor {

    private final PartyManager partyManager;

    public PartyCommand(PartyManager partyManager) {
        this.partyManager = partyManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use party commands.");
            return true;
        }

        Player player = (Player) sender;
        UUID playerUUID = player.getUniqueId();

        if (args.length == 0) {
            player.sendMessage("§c§l(!) §cUnknown command. Usage: /party < create | invite | kick | disband | leave | list | accept | transfer | chat | ping >");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "create":
                if (partyManager.createParty(playerUUID)) {
                    player.sendMessage("§a§l(!) §aParty created!");
                } else {
                    player.sendMessage("§c§l(!) §cYou are already in a party!");
                }
                break;

            case "invite":
                if (args.length < 2) {
                    player.sendMessage("§c§l(!) §cUsage: /party invite <player>");
                    return true;
                }
                Player invitee = player.getServer().getPlayer(args[1]);
                if (invitee == null) {
                    player.sendMessage("§c§l(!) §cPlayer not found.");
                    return true;
                }
                if (partyManager.inviteToParty(playerUUID, invitee.getUniqueId())) {
                    player.sendMessage(ChatColor.GREEN + invitee.getName() + " has been invited to the party.");
                    invitee.sendMessage("§a§l(!) §aYou have been invited to a party by " + player.getName() + ". Use /party accept " + player.getName() + " to join. The invite will expire in 30 seconds.");
                } else {
                    player.sendMessage("§c§l(!) §cCould not invite player. Are they already in a party or is your party full?");
                }
                break;

            case "accept":
                if (args.length < 2) {
                    player.sendMessage("§c§l(!) §cUsage: /party accept <inviter>");
                    return true;
                }
                Player inviter = player.getServer().getPlayer(args[1]);
                if (inviter == null) {
                    player.sendMessage("§c§l(!) §cInviter not found.");
                    return true;
                }
                if (partyManager.acceptInvite(playerUUID, inviter.getUniqueId())) {
                    player.sendMessage("§a§l(!) §aYou have joined the party.");
                } else {
                    player.sendMessage("§c§l(!) §cNo pending invite from " + args[1] + " or you are already in a party, or the party is full.");
                }
                break;

            case "kick":
                if (args.length < 2) {
                    player.sendMessage("§c§l(!) §cUsage: /party kick <member>");
                    return true;
                }
                Player memberToKick = player.getServer().getPlayer(args[1]);
                if (memberToKick == null) {
                    player.sendMessage("§c§l(!) §cMember not found.");
                    return true;
                }
                if (partyManager.kickFromParty(playerUUID, memberToKick.getUniqueId())) {
                    player.sendMessage(ChatColor.RED + memberToKick.getName() + " has been kicked from the party.");
                    memberToKick.sendMessage("§c§l(!) §cYou have been kicked from the party by " + player.getName());
                } else {
                    player.sendMessage("§c§l(!) §cCould not kick member. Are they in your party?");
                }
                break;

            case "disband":
                if (partyManager.disbandParty(playerUUID)) {
                    player.sendMessage("§c§l(!) §cParty disbanded.");
                } else {
                    player.sendMessage("§c§l(!) §cYou do not have a party to disband.");
                }
                break;

            case "leave":
                if (partyManager.leaveParty(playerUUID)) {
                    player.sendMessage("§c§l(!) §cYou left the party.");
                } else {
                    player.sendMessage("§c§l(!) §cYou are not in a party.");
                }
                break;

            case "list":
                Party party = partyManager.getParty(playerUUID);
                if (party == null) {
                    player.sendMessage("§c§l(!) §cYou are not in a party.");
                } else {
                    StringBuilder memberList = new StringBuilder("§2Party members: ");
                    for (UUID memberUUID : party.getMembers()) {
                        Player memberInList = player.getServer().getPlayer(memberUUID);
                        if (memberInList != null) {
                            memberList.append(memberInList.getName()).append(", ");
                        }
                    }
                    if (memberList.length() > 15) {
                        memberList.setLength(memberList.length() - 2);
                    }
                    player.sendMessage(memberList.toString());
                }
                break;

            case "transfer":
                if (args.length < 2) {
                    player.sendMessage("§c§l(!) §cUsage: /party transfer <player>");
                    return true;
                }
                if (!partyManager.isLeader(playerUUID)) {
                    player.sendMessage("§c§l(!) §cOnly the party leader can transfer ownership.");
                    return true;
                }
                Player newLeader = player.getServer().getPlayer(args[1]);
                if (newLeader == null || !partyManager.isInParty(newLeader.getUniqueId())) {
                    player.sendMessage("§c§l(!) §cThat player is not in your party.");
                    return true;
                }
                if (partyManager.transferLeadership(playerUUID, newLeader.getUniqueId())) {
                    player.sendMessage("§a§l(!) §aYou have transferred party leadership to " + newLeader.getName() + ".");
                    newLeader.sendMessage("§a§l(!) §aYou are now the leader of the party.");
                } else {
                    player.sendMessage("§c§l(!) §cFailed to transfer leadership.");
                }
                break;

            case "chat":
                // Toggle party chat for the player
                if (!partyManager.isInParty(playerUUID)) {
                    player.sendMessage("§c§l(!) §cYou are not in a party, so you cannot use party chat.");
                    return true;
                }
                partyManager.togglePartyChat(playerUUID);
                break;

            case "ping":
                Party pingParty = partyManager.getParty(playerUUID);
                if (pingParty == null) {
                    player.sendMessage("§c§l(!) §2You are not in a party.");
                    return true;
                }
                Location loc = player.getLocation();
                String pingMessage = String.format("§2[Party] §a%s pinged: X: %.2f, Y: %.2f, Z: %.2f",
                        player.getName(), loc.getX(), loc.getY(), loc.getZ());
                for (UUID memberUUID : pingParty.getMembers()) {
                    Player memberForPing = Bukkit.getPlayer(memberUUID);
                    if (memberForPing != null && memberForPing.isOnline()) {
                        memberForPing.sendMessage(pingMessage);
                    }
                }
                break;

            default:
                player.sendMessage("§c§l(!) §cUnknown command. Usage: /party < create | invite | kick | disband | leave | list | accept | transfer | chat | ping >");
                break;
        }

        return true;
    }
}