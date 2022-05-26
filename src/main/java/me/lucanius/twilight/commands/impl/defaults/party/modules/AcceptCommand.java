package me.lucanius.twilight.commands.impl.defaults.party.modules;

import me.lucanius.twilight.commands.abstr.AbstractCommand;
import me.lucanius.twilight.service.party.PartyService;
import me.lucanius.twilight.service.profile.Profile;
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
            player.sendMessage(CC.translate("&cYou are already in a party!"));
            return;
        }

        Profile profile = plugin.getProfiles().get(uuid);
        if (profile.getPartyInvite() == null) {
            player.sendMessage(CC.translate("&cYou don't have any party invites!"));
            return;
        }

        party.joinParty(player, profile.getPartyLeader());
    }
}
