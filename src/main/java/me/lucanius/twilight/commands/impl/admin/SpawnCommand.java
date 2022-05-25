package me.lucanius.twilight.commands.impl.admin;

import me.lucanius.twilight.commands.abstr.AbstractCommand;
import me.lucanius.twilight.service.profile.Profile;
import me.lucanius.twilight.service.profile.ProfileState;
import me.lucanius.twilight.tools.CC;
import me.lucanius.twilight.tools.command.Command;
import me.lucanius.twilight.tools.command.CommandArgs;
import org.bukkit.entity.Player;

/**
 * @author Clouke
 * @since 25.05.2022 12:59
 * © Twilight - All Rights Reserved
 */
public class SpawnCommand extends AbstractCommand {

    @Command(name = "spawn", permission = "twilight.command.spwan")
    public void onCommand(CommandArgs cmd) {
        Player player = cmd.getPlayer();
        Profile profile = plugin.getProfiles().get(player.getUniqueId());

        if (profile.getState() == ProfileState.PLAYING) {
            player.sendMessage(CC.translate("&cYou can't use this command while playing!"));
            return;
        }

        plugin.getLobby().toLobby(player, profile, true);
    }
}
