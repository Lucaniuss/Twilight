package me.lucanius.twilight.commands.impl.admin.arena.modules;

import me.lucanius.twilight.commands.abstr.AbstractCommand;
import me.lucanius.twilight.tools.CC;
import me.lucanius.twilight.tools.command.Command;
import me.lucanius.twilight.tools.command.CommandArgs;
import org.bukkit.entity.Player;

/**
 * @author Lucanius
 * @since May 24, 2022
 */
public class CreateCommand extends AbstractCommand {

    @Command(name = "arena.create", permission = "twilight.admin")
    public void onCommand(CommandArgs cmd) {
        Player player = cmd.getPlayer();
        String[] args = cmd.getArgs();

        if (args.length != 1) {
            player.sendMessage(getUsage(cmd.getLabel(), "<arena>"));
            return;
        }

        if (plugin.getArenas().get(args[0]) != null) {
            player.sendMessage(CC.RED + "That arena already exists...");
            return;
        }

        plugin.getArenas().build(args[0]);
        player.sendMessage(CC.SECOND + "Successfully deleted arena " + CC.MAIN + args[0] + CC.SECOND + ".");
        player.sendMessage(CC.SECOND + CC.ITALIC + "Make sure you setup all locations using their commands!");
    }
}
