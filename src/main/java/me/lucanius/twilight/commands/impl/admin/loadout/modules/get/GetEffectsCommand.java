package me.lucanius.twilight.commands.impl.admin.loadout.modules.get;

import me.lucanius.twilight.commands.abstr.AbstractCommand;
import me.lucanius.twilight.service.loadout.Loadout;
import me.lucanius.twilight.tools.CC;
import me.lucanius.twilight.tools.command.Command;
import me.lucanius.twilight.tools.command.CommandArgs;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import java.util.Collection;

/**
 * @author Lucanius
 * @since May 25, 2022
 */
public class GetEffectsCommand extends AbstractCommand {

    @Command(name = "loadout.geteffects", permission = "twilight.admin")
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

        Collection<PotionEffect> effects = loadout.getEffects();
        if (effects == null || effects.isEmpty()) {
            player.sendMessage(CC.RED + "That loadout does not contain any effects...");
            return;
        }

        effects.forEach(player::addPotionEffect);
        player.sendMessage(CC.SECOND + "Successfully retrieved effects from loadout " + CC.MAIN + loadout.getName() + CC.SECOND + ".");
    }
}
