package me.lucanius.twilight.commands.impl.defaults.party.modules;

import me.lucanius.twilight.commands.abstr.AbstractCommand;
import me.lucanius.twilight.service.party.PartyService;
import me.lucanius.twilight.service.profile.Profile;
import me.lucanius.twilight.tools.CC;
import me.lucanius.twilight.tools.Clickable;
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

    private static final PartyService parties = plugin.getParties();

    @Command(name = "party.invite", aliases = {"p.invite"})
    public void onCommand(CommandArgs cmd) {
        Player player = cmd.getPlayer();
        UUID uuid = player.getUniqueId();
        String[] args = cmd.getArgs();

        if (args.length < 1) {
            player.sendMessage(CC.translate("&cUsage: /party invite <player>"));
            return;
        }

        if (!parties.isParty(uuid)) {
            player.sendMessage(CC.translate("&cYou are not in a party..."));
            return;
        }

        if (!parties.isLeader(uuid)) {
            player.sendMessage(CC.translate("&cYou are not the leader of your party..."));
            return;
        }

        Player target = plugin.getServer().getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(CC.translate("&cPlayer not found..."));
            return;
        }

        Profile profile = plugin.getProfiles().get(target.getUniqueId());
        profile.setPartyLeader(uuid);
        profile.setPartyInvite(parties.getParty(uuid));

        Clickable clickable = new Clickable();
        clickable.add("&aYou've been invited to join &e" + player.getName() + "'s &aparty", "&a&lClick to Join", "/party accept");
        clickable.send(target);
    }
}
