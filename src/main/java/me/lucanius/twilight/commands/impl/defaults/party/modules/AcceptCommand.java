package me.lucanius.twilight.commands.impl.defaults.party.modules;

import me.lucanius.twilight.commands.abstr.AbstractCommand;
import me.lucanius.twilight.service.party.PartyService;
import me.lucanius.twilight.service.profile.Profile;
import me.lucanius.twilight.tools.CC;
import me.lucanius.twilight.tools.command.Command;
import me.lucanius.twilight.tools.command.CommandArgs;
import org.bukkit.entity.Player;

/**
 * @author Clouke
 * @since 26.05.2022 14:37
 * © Twilight - All Rights Reserved
 */
public class AcceptCommand extends AbstractCommand {

    @Command(name = "party.accept", aliases = {"p.accept"})
    public void onCommand(CommandArgs cmd) {
        final PartyService party = plugin.getParties();
        final Player player = cmd.getPlayer();

        if (party.isParty(player.getUniqueId())) {
            player.sendMessage(CC.translate("&cYou are already in a party!"));
            return;
        }

        Profile profile = plugin.getProfiles().get(player.getUniqueId());
        if (profile.getPartyInvite() == null) {
            player.sendMessage(CC.translate("&cYou don't have any party invites!"));
            return;
        }

        party.joinParty(player, profile.getPartyLeader());
    }
}
