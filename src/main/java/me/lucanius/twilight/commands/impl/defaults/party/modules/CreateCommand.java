package me.lucanius.twilight.commands.impl.defaults.party.modules;

import me.lucanius.twilight.commands.abstr.AbstractCommand;
import me.lucanius.twilight.service.party.PartyService;
import me.lucanius.twilight.tools.CC;
import me.lucanius.twilight.tools.command.Command;
import me.lucanius.twilight.tools.command.CommandArgs;
import org.bukkit.entity.Player;

/**
 * @author Clouke
 * @since 26.05.2022 14:38
 * © Twilight - All Rights Reserved
 */
public class CreateCommand extends AbstractCommand {

    @Command(name = "party.create", aliases = {"p.create"})
    public void onCommand(CommandArgs cmd) {
        Player player = cmd.getPlayer();

        PartyService party = plugin.getParties();
        if (party.isParty(player.getUniqueId())) {
            player.sendMessage(CC.RED + "You are already in a party...");
            return;
        }

        plugin.getParties().createParty(player);
    }
}
