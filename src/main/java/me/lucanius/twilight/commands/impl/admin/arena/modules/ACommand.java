package me.lucanius.twilight.commands.impl.admin.arena.modules;

import me.lucanius.twilight.commands.abstr.AbstractCommand;
import me.lucanius.twilight.service.arena.Arena;
import me.lucanius.twilight.tools.CC;
import me.lucanius.twilight.tools.command.Command;
import me.lucanius.twilight.tools.command.CommandArgs;
import me.lucanius.twilight.tools.location.SerializableLocation;
import org.bukkit.entity.Player;

/**
 * @author Lucanius
 * @since May 24, 2022
 */
public class ACommand extends AbstractCommand {

    @Command(name = "arena.a", permission = "twilight.admin")
    public void onCommand(CommandArgs cmd) {
        Player player = cmd.getPlayer();
        String[] args = cmd.getArgs();

        if (args.length != 1) {
            player.sendMessage(getUsage(cmd.getLabel(), "<arena>"));
            return;
        }

        Arena arena = plugin.getArenas().get(args[0]);
        if (arena == null) {
            player.sendMessage(CC.RED + "That arena could not be found...");
            return;
        }

        arena.setA(new SerializableLocation(player.getLocation()));
        player.sendMessage(CC.SECOND + "Successfully set location A for " + CC.MAIN + arena.getName() + CC.SECOND + ".");
    }
}
