package me.lucanius.twilight.commands.impl.admin.loadout.modules.set;

import me.lucanius.twilight.commands.abstr.AbstractCommand;
import me.lucanius.twilight.service.loadout.Loadout;
import me.lucanius.twilight.tools.CC;
import me.lucanius.twilight.tools.command.Command;
import me.lucanius.twilight.tools.command.CommandArgs;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author Lucanius
 * @since May 25, 2022
 */
public class SetContentsCommand extends AbstractCommand {

    @Command(name = "loadout.setcontents", permission = "twilight.admin")
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

        ItemStack[] contents = player.getInventory().getContents();
        if (contents == null) {
            player.sendMessage(CC.RED + "You do not have any contents equipped...");
            return;
        }

        loadout.setContents(contents);
        player.sendMessage(CC.SECOND + "Successfully set contents for loadout " + CC.MAIN + loadout.getName() + CC.SECOND + ".");
    }
}
