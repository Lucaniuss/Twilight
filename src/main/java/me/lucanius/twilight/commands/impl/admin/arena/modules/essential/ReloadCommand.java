package me.lucanius.twilight.commands.impl.admin.arena.modules.essential;

import me.lucanius.twilight.commands.abstr.AbstractCommand;
import me.lucanius.twilight.service.arena.Arena;
import me.lucanius.twilight.tools.CC;
import me.lucanius.twilight.tools.command.Command;
import me.lucanius.twilight.tools.command.CommandArgs;

import java.util.Collection;

/**
 * @author Lucanius
 * @since May 24, 2022
 */
public class ReloadCommand extends AbstractCommand {

    @Command(name = "arena.reload", permission = "twilight.admin")
    public void onCommand(CommandArgs cmd) {
        Collection<Arena> arenas = plugin.getArenas().getAll();
        try {
            plugin.getArenas().reload();
            cmd.getPlayer().sendMessage(CC.SECOND + "Successfully reloaded arenas.");
        } catch (final Exception e) {
            for (Arena arena : arenas) {
                plugin.getArenas().register(arena);
            }
            cmd.getPlayer().sendMessage(CC.RED + "An error occurred while reloading arenas. Restoring...");
            e.printStackTrace();
        }
    }
}
