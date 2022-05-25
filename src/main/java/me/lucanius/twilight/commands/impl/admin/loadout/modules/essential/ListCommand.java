package me.lucanius.twilight.commands.impl.admin.loadout.modules.essential;

import me.lucanius.twilight.commands.abstr.AbstractCommand;
import me.lucanius.twilight.service.loadout.Loadout;
import me.lucanius.twilight.tools.CC;
import me.lucanius.twilight.tools.Clickable;
import me.lucanius.twilight.tools.command.Command;
import me.lucanius.twilight.tools.command.CommandArgs;
import org.bukkit.entity.Player;

import java.util.Collection;

/**
 * @author Lucanius
 * @since May 25, 2022
 */
public class ListCommand extends AbstractCommand {

    @Command(name = "loadout.list", permission = "twilight.admin")
    public void onCommand(CommandArgs cmd) {
        Player player = cmd.getPlayer();
        Collection<Loadout> loadouts = plugin.getLoadouts().getAll();
        if (loadouts.isEmpty()) {
            player.sendMessage(CC.RED + "There are no loadouts...");
            return;
        }

        player.sendMessage(CC.BAR);
        player.sendMessage(CC.MAIN + CC.BOLD + "Current Loadouts " + CC.GRAY + "(" + loadouts.size() + ")");
        loadouts.forEach(loadout -> new Clickable(
                CC.ICON + CC.SECOND + loadout.getName() + CC.GRAY + " (Click)",
                CC.SECOND + CC.ITALIC + "Click for more info",
                "/loadout info " + loadout.getName()
        ).send(player));
        player.sendMessage(CC.BAR);
    }
}
