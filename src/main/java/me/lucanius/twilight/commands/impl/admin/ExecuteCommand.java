package me.lucanius.twilight.commands.impl.admin;

import me.lucanius.twilight.commands.abstr.AbstractCommand;
import me.lucanius.twilight.service.loadout.type.LoadoutType;
import me.lucanius.twilight.service.profile.Profile;
import me.lucanius.twilight.service.profile.ProfileState;
import me.lucanius.twilight.tools.CC;
import me.lucanius.twilight.tools.command.Command;
import me.lucanius.twilight.tools.command.CommandArgs;
import org.bukkit.entity.Player;

/**
 * @author Lucanius
 * @since May 27, 2022
 */
public class ExecuteCommand extends AbstractCommand {

    @Command(name = "execute", permission = "twilight.admin")
    public void onCommand(CommandArgs cmd) {
        Player player = cmd.getPlayer();
        String[] args = cmd.getArgs();
        if (args.length != 1) {
            player.sendMessage(getUsage(cmd.getLabel(), "<player>"));
            return;
        }

        Player target = plugin.getServer().getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(CC.RED + "Player not found...");
            return;
        }

        Profile profile = plugin.getProfiles().get(target.getUniqueId());
        if (profile.getState() != ProfileState.PLAYING) {
            player.sendMessage(CC.RED + "That player is not playing...");
            return;
        }

        LoadoutType.NONE.getCallable().execute(plugin, target, plugin.getDamages().get(target.getUniqueId()), plugin.getGames().get(profile));
    }
}
