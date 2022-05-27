package me.lucanius.twilight.commands.impl.admin;

import me.lucanius.twilight.commands.abstr.AbstractCommand;
import me.lucanius.twilight.tools.CC;
import me.lucanius.twilight.tools.command.Command;
import me.lucanius.twilight.tools.command.CommandArgs;
import me.lucanius.twilight.tools.location.SerializableLocation;
import org.bukkit.entity.Player;

/**
 * @author Lucanius
 * @since May 27, 2022
 */
public class SetSpawnCommand extends AbstractCommand {

    @Command(name = "setspawn", permission = "twilight.admin")
    public void onCommand(CommandArgs cmd) {
        Player player = cmd.getPlayer();
        plugin.getLobby().setLobbyLocation(new SerializableLocation(player.getLocation()));
        player.sendMessage(CC.SECOND + "Successfully set the new lobby location!");
    }
}
