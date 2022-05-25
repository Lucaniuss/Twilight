package me.lucanius.twilight.commands.impl.admin.loadout.modules.cosmetic;

import me.lucanius.twilight.commands.abstr.AbstractCommand;
import me.lucanius.twilight.service.loadout.Loadout;
import me.lucanius.twilight.tools.CC;
import me.lucanius.twilight.tools.command.Command;
import me.lucanius.twilight.tools.command.CommandArgs;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author Lucanius
 * @since May 25, 2022
 */
public class IconCommand extends AbstractCommand {

    @Command(name = "loadout.icon", permission = "twilight.admin")
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

        ItemStack item = player.getItemInHand();
        if (item == null || item.getType() == Material.AIR) {
            player.sendMessage(CC.RED + "You must be holding an item to set it as the loadout icon.");
            return;
        }

        loadout.setIcon(item);
        player.sendMessage(CC.SECOND + "Successfully set icon to " + CC.MAIN + item.getType().name() + CC.SECOND + " for loadout " + CC.MAIN + loadout.getName() + CC.SECOND + ".");
    }
}
