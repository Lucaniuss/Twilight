package me.lucanius.twilight.commands.impl.admin.arena.modules.essential;

import me.lucanius.twilight.commands.abstr.AbstractCommand;
import me.lucanius.twilight.service.arena.Arena;
import me.lucanius.twilight.service.loadout.Loadout;
import me.lucanius.twilight.tools.CC;
import me.lucanius.twilight.tools.command.Command;
import me.lucanius.twilight.tools.command.CommandArgs;
import org.bukkit.entity.Player;

import java.util.ArrayList;

/**
 * @author Lucanius
 * @since May 25, 2022
 */
public class AddLoadoutCommand extends AbstractCommand {

    @Command(name = "arena.addloadout", permission = "twilight.admin")
    public void onCommand(CommandArgs cmd) {
        Player player = cmd.getPlayer();
        String[] args = cmd.getArgs();

        if (args.length != 2) {
            player.sendMessage(getUsage(cmd.getLabel(), "<arena> <loadout>"));
            return;
        }

        Arena arena = plugin.getArenas().get(args[0]);
        if (arena == null) {
            player.sendMessage(CC.RED + "That arena could not be found...");
            return;
        }

        Loadout loadout = plugin.getLoadouts().get(args[1]);
        if (loadout == null) {
            player.sendMessage(CC.RED + "That loadout could not be found...");
            return;
        }

        if (arena.getLoadouts() == null) {
            arena.setLoadouts(new ArrayList<>());
        }

        arena.getLoadouts().add(loadout.getName());
        player.sendMessage(CC.SECOND + "Added " + CC.MAIN + loadout.getName() + CC.SECOND + " to " + CC.MAIN + arena.getName() + CC.SECOND + ".");
    }
}
