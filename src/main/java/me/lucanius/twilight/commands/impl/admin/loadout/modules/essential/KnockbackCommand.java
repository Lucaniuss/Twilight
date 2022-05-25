package me.lucanius.twilight.commands.impl.admin.loadout.modules.essential;

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
public class KnockbackCommand extends AbstractCommand {

    @Command(name = "loadout.knockback", permission = "twilight.admin")
    public void onCommand(CommandArgs cmd) {
        Player player = cmd.getPlayer();
        String[] args = cmd.getArgs();

        if (args.length != 2) {
            player.sendMessage(getUsage(cmd.getLabel(), "<loadout> <knockback>"));
            return;
        }

        Loadout loadout = plugin.getLoadouts().get(args[0]);
        if (loadout == null) {
            player.sendMessage(CC.RED + "That loadout could not be found...");
            return;
        }

        loadout.setKbProfile(args[1]);
        player.sendMessage(CC.SECOND + "Successfully set knockback to " + CC.MAIN + args[1] + CC.SECOND + " for loadout " + CC.MAIN + loadout.getName() + CC.SECOND + ".");
    }
}
