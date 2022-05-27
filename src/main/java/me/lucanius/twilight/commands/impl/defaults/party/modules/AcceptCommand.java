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
public class AcceptCommand extends AbstractCommand {

    @Command(name = "party.accept", aliases = {"p.accept"})
    public void onCommand(CommandArgs cmd) {
        Player player = cmd.getPlayer();
        UUID uuid = player.getUniqueId();

        PartyService party = plugin.getParties();
        if (party.isParty(uuid)) {
            player.sendMessage(CC.RED + "You are already in a party...");
            return;
        }

        if (plugin.getParties().accept(player) == null) {
            player.sendMessage(CC.RED + "You don't have any party invites...");
        }
    }
}
