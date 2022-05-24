package me.lucanius.twilight.commands.impl.admin.arena.modules;

import me.lucanius.twilight.commands.abstr.AbstractCommand;
import me.lucanius.twilight.service.arena.Arena;
import me.lucanius.twilight.tools.CC;
import me.lucanius.twilight.tools.command.Command;
import me.lucanius.twilight.tools.command.CommandArgs;
import org.bukkit.entity.Player;

/**
 * @author Lucanius
 * @since May 24, 2022
 */
public class DeleteCommand extends AbstractCommand {

    @Command(name = "arena.delete", permission = "twilight.admin")
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

        plugin.getArenas().destroy(arena);
        player.sendMessage(CC.SECOND + "Successfully deleted arena " + CC.MAIN + arena.getName() + CC.SECOND + ".");
    }
}
