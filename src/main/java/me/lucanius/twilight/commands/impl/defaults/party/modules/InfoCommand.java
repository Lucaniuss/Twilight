package me.lucanius.twilight.commands.impl.defaults.party.modules;

import me.lucanius.twilight.commands.abstr.AbstractCommand;
import me.lucanius.twilight.service.party.Party;
import me.lucanius.twilight.tools.CC;
import me.lucanius.twilight.tools.command.Command;
import me.lucanius.twilight.tools.command.CommandArgs;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * @author Lucanius
 * @since May 27, 2022
 */
public class InfoCommand extends AbstractCommand {

    @Command(name = "party.info", aliases = {"p.info"})
    public void onCommand(CommandArgs cmd) {
        Player player = cmd.getPlayer();
        UUID uuid = player.getUniqueId();

        Party party = plugin.getParties().getParty(uuid);
        if (party == null) {
            player.sendMessage(CC.RED + "You are not in a party...");
            return;
        }

        player.sendMessage(CC.BAR);
        player.sendMessage(CC.MAIN + party.getLeaderPlayer().getName() + "'s " + CC.SECOND + "Party");
        player.sendMessage(CC.MAIN + "Members " + CC.GRAY + "(" + party.getMembers().size() + ")");
        party.toPlayers().forEach(member -> player.sendMessage(CC.ICON + CC.SECOND + member.getName()));
        player.sendMessage(CC.BAR);
    }
}
