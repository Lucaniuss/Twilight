package me.lucanius.twilight.commands.impl.admin.loadout.modules.type;

import me.lucanius.twilight.commands.abstr.AbstractCommand;
import me.lucanius.twilight.service.loadout.Loadout;
import me.lucanius.twilight.tools.CC;
import me.lucanius.twilight.tools.command.Command;
import me.lucanius.twilight.tools.command.CommandArgs;
import org.bukkit.entity.Player;

/**
 * @author Lucanius
 * @since May 25, 2022
 */
public class RankedCommand extends AbstractCommand {

    @Command(name = "loadout.ranked", permission = "twilight.admin")
    public void onCommand(CommandArgs cmd) {
        Player player = cmd.getPlayer();
        String[] args = cmd.getArgs();

        if (args.length != 1) {
            player.sendMessage(getUsage(cmd.getLabel(), "<loadout>"));
            return;
        }

        Loadout loadout = plugin.getLoadouts().get(args[0]);
        if (loadout == null) {
            player.sendMessage(CC.RED + "That loadout could not be found...");
            return;
        }

        loadout.setRanked(!loadout.isRanked());
        player.sendMessage(loadout.isRanked()
                ? CC.SECOND + "Successfully enabled ranked for loadout " + CC.MAIN + loadout.getName() + CC.SECOND + "."
                : CC.SECOND + "Successfully disabled ranked for loadout " + CC.MAIN + loadout.getName() + CC.SECOND + "."
        );
    }
}
