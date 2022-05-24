package me.lucanius.twilight.commands.impl.admin.arena.modules;

import me.lucanius.twilight.commands.abstr.AbstractCommand;
import me.lucanius.twilight.service.arena.Arena;
import me.lucanius.twilight.tools.CC;
import me.lucanius.twilight.tools.Tools;
import me.lucanius.twilight.tools.command.Command;
import me.lucanius.twilight.tools.command.CommandArgs;
import org.bukkit.entity.Player;

/**
 * @author Lucanius
 * @since May 24, 2022
 */
public class CopyCommand extends AbstractCommand {

    @Command(name = "arena.copy", permission = "twilight.admin")
    public void onCommand(CommandArgs cmd) {
        Player player = cmd.getPlayer();
        String[] args = cmd.getArgs();

        if (args.length != 2) {
            player.sendMessage(getUsage(cmd.getLabel(), "<arena> <amount>"));
            return;
        }

        Arena arena = plugin.getArenas().get(args[0]);
        if (arena == null) {
            player.sendMessage(CC.RED + "That arena could not be found...");
            return;
        }

        if (!Tools.isInt(args[1])) {
            player.sendMessage(CC.RED + "That is not a valid number...");
            return;
        }

        int amount = Integer.parseInt(args[1]);
        arena.generate(amount);
        player.sendMessage(CC.SECOND + "Generating " + CC.MAIN + amount + CC.SECOND + " arenas for " + CC.MAIN + arena.getName() + CC.SECOND + ".");
    }
}
