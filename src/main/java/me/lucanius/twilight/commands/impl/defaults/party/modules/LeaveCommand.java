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
public class LeaveCommand extends AbstractCommand {

    @Command(name = "party.leave", aliases = {"p.leave"})
    public void onCommand(CommandArgs cmd) {
        Player player = cmd.getPlayer();
        UUID uuid = player.getUniqueId();

        PartyService parties = plugin.getParties();
        if (!parties.isParty(uuid)) {
            player.sendMessage(CC.translate("&cYou are not in a party..."));
            return;
        }

        if (parties.isLeader(uuid)) {
            parties.disbandParty(parties.getParty(uuid));
            return;
        }

        parties.leaveParty(player);
    }
}
