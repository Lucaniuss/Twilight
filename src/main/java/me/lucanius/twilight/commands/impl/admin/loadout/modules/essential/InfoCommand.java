package me.lucanius.twilight.commands.impl.admin.loadout.modules.essential;

import me.lucanius.twilight.commands.abstr.AbstractCommand;
import me.lucanius.twilight.service.loadout.Loadout;
import me.lucanius.twilight.tools.CC;
import me.lucanius.twilight.tools.Clickable;
import me.lucanius.twilight.tools.command.Command;
import me.lucanius.twilight.tools.command.CommandArgs;
import org.bukkit.entity.Player;

/**
 * @author Lucanius
 * @since May 25, 2022
 */
public class InfoCommand extends AbstractCommand {

    @Command(name = "loadout.info", permission = "twilight.admin")
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

        player.sendMessage(CC.BAR);
        player.sendMessage(CC.MAIN + CC.BOLD + "Loadout Information " + CC.GRAY + "- " + CC.MAIN + loadout.getName());
        player.sendMessage(CC.ICON + CC.WHITE + "Slot: " + loadout.getSlot());
        player.sendMessage(CC.ICON + CC.WHITE + "Icon: " + loadout.getIcon().getType().name());
        player.sendMessage(CC.ICON + CC.WHITE + "Type: " + loadout.getType().name());
        player.sendMessage(CC.ICON + CC.WHITE + "Ranked: " + (loadout.isRanked() ? CC.GREEN + "✓" : CC.RED + "✗"));
        player.sendMessage(CC.ICON + CC.WHITE + "NoDamage: " + (loadout.isNoDamage() ? CC.GREEN + "✓" : CC.RED + "✗"));
        player.sendMessage(CC.ICON + CC.WHITE + "NoDrop: " + (loadout.isNoDrop() ? CC.GREEN + "✓" : CC.RED + "✗"));
        player.sendMessage(CC.ICON + CC.WHITE + "NoFall: " + (loadout.isNoFall() ? CC.GREEN + "✓" : CC.RED + "✗"));
        player.sendMessage(CC.ICON + CC.WHITE + "NoHunger: " + (loadout.isNoHunger() ? CC.GREEN + "✓" : CC.RED + "✗"));
        player.sendMessage(CC.ICON + CC.WHITE + "NoRegen: " + (loadout.isNoRegen() ? CC.GREEN + "✓" : CC.RED + "✗"));
        player.sendMessage(CC.ICON + CC.WHITE + "HealthShow: " + (loadout.isHealthShow() ? CC.GREEN + "✓" : CC.RED + "✗"));
        player.sendMessage(" ");
        new Clickable(
                CC.SECOND + CC.ITALIC + "Click to get contents",
                CC.SECOND + CC.ITALIC + "Click to get contents",
                "/loadout getcontents " + loadout.getName() // TODO: Make this command
        ).send(player);
        player.sendMessage(CC.BAR);
    }
}
