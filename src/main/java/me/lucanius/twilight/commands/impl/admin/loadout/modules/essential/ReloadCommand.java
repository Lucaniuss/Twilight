package me.lucanius.twilight.commands.impl.admin.loadout.modules.essential;

import me.lucanius.twilight.commands.abstr.AbstractCommand;
import me.lucanius.twilight.service.loadout.Loadout;
import me.lucanius.twilight.tools.CC;
import me.lucanius.twilight.tools.command.Command;
import me.lucanius.twilight.tools.command.CommandArgs;

import java.util.Collection;

/**
 * @author Lucanius
 * @since May 25, 2022
 */
public class ReloadCommand extends AbstractCommand {

    @Command(name = "loadout.reload", permission = "twilight.admin")
    public void onCommand(CommandArgs cmd) {
        Collection<Loadout> loadouts = plugin.getLoadouts().getAll();
        try {
            plugin.getLoadouts().reload();
            cmd.getPlayer().sendMessage(CC.SECOND + "Successfully reloaded loadouts.");
        } catch (final Exception e) {
            for (Loadout loadout : loadouts) {
                plugin.getLoadouts().register(loadout);
            }
            cmd.getPlayer().sendMessage(CC.RED + "An error occurred while reloading loadouts. Restoring...");
        }
    }
}
