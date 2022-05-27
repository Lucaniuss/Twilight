package me.lucanius.twilight.commands.impl.defaults.party.modules;

import me.lucanius.twilight.commands.abstr.AbstractCommand;
import me.lucanius.twilight.service.party.PartyService;
import me.lucanius.twilight.tools.CC;
import me.lucanius.twilight.tools.command.Command;
import me.lucanius.twilight.tools.command.CommandArgs;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * @author Clouke
 * @since 26.05.2022 14:37
 * © Twilight - All Rights Reserved
 */
public class InviteCommand extends AbstractCommand {

    @Command(name = "party.invite", aliases = {"p.invite"})
    public void onCommand(CommandArgs cmd) {
        Player player = cmd.getPlayer();
        UUID uuid = player.getUniqueId();
        String[] args = cmd.getArgs();

        if (args.length != 1) {
            player.sendMessage(getUsage(cmd.getLabel(), "<player>"));
            return;
        }

        PartyService parties = plugin.getParties();
        if (!parties.isParty(uuid)) {
            player.sendMessage(CC.RED + "You are not in a party...");
            return;
        }

        if (!parties.isLeader(uuid)) {
            player.sendMessage(CC.RED + "You are not the leader of your party...");
            return;
        }

        Player target = plugin.getServer().getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(CC.RED + "Player not found...");
            return;
        }

        if (plugin.getParties().invite(target, player) == null) {
            player.sendMessage(CC.RED + "Player is already in a party...");
        }
    }
}
