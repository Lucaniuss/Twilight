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
 * @since 26.05.2022 14:38
 * © Twilight - All Rights Reserved
 */
public class DisbandCommand extends AbstractCommand {

    @Command(name = "party.disband", aliases = {"p.disband"})
    public void onCommand(CommandArgs cmd) {
        final PartyService party = plugin.getParties();
        final Player player = cmd.getPlayer();
        final UUID uuid = player.getUniqueId();

        if (!party.isParty(uuid)) {
            player.sendMessage(CC.translate("&cYou are not in a party..."));
            return;
        }

        if (!party.isLeader(uuid)) {
            player.sendMessage(CC.translate("&cYou are not the leader of your party..."));
            return;
        }

        party.disbandParty(party.getParty(uuid));
    }
}
