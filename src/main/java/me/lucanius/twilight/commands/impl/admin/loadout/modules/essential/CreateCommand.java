package me.lucanius.twilight.commands.impl.admin.loadout.modules.essential;

import me.lucanius.twilight.commands.abstr.AbstractCommand;
import me.lucanius.twilight.tools.CC;
import me.lucanius.twilight.tools.command.Command;
import me.lucanius.twilight.tools.command.CommandArgs;
import org.bukkit.entity.Player;

/**
 * @author Lucanius
 * @since May 25, 2022
 */
public class CreateCommand extends AbstractCommand {

    @Command(name = "loadout.create", permission = "twilight.admin")
    public void onCommand(CommandArgs cmd) {
        Player player = cmd.getPlayer();
        String[] args = cmd.getArgs();

        if (args.length != 1) {
            player.sendMessage(getUsage(cmd.getLabel(), "<loadout>"));
            return;
        }

        if (plugin.getLoadouts().get(args[0]) != null) {
            player.sendMessage(CC.RED + "That loadout already exists...");
            return;
        }

        plugin.getLoadouts().build(args[0]);
        player.sendMessage(CC.SECOND + "Successfully created loadout " + CC.MAIN + args[0] + CC.SECOND + ".");
        player.sendMessage(CC.SECOND + CC.ITALIC + "Make sure you setup all contents using their commands!");
    }
}
