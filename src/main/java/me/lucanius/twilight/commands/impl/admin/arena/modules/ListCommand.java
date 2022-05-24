package me.lucanius.twilight.commands.impl.admin.arena.modules;

import me.lucanius.twilight.commands.abstr.AbstractCommand;
import me.lucanius.twilight.service.arena.Arena;
import me.lucanius.twilight.tools.CC;
import me.lucanius.twilight.tools.Clickable;
import me.lucanius.twilight.tools.command.Command;
import me.lucanius.twilight.tools.command.CommandArgs;
import org.bukkit.entity.Player;

import java.util.Collection;

/**
 * @author Lucanius
 * @since May 24, 2022
 */
public class ListCommand extends AbstractCommand {

    @Command(name = "arena.list", permission = "twilight.admin")
    public void onCommand(CommandArgs cmd) {
        Player player = cmd.getPlayer();
        Collection<Arena> arenas = plugin.getArenas().getAll();
        if (arenas.isEmpty()) {
            player.sendMessage(CC.RED + "There are no arenas...");
            return;
        }

        player.sendMessage(CC.BAR);
        player.sendMessage(CC.MAIN + CC.BOLD + "Current Arenas " + CC.GRAY + "(" + arenas.size() + ")");
        arenas.forEach(arena -> new Clickable(
                CC.ICON + CC.SECOND + arena.getName() + CC.GRAY + " (Click)",
                CC.SECOND + CC.ITALIC + "Click for more info",
                "/arena info " + arena.getName()
        ).send(player));
        player.sendMessage(CC.BAR);
    }
}
