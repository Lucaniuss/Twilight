package me.lucanius.twilight.commands.impl;

import me.lucanius.twilight.commands.abstr.AbstractCommand;
import me.lucanius.twilight.tools.CC;
import me.lucanius.twilight.tools.command.Command;
import me.lucanius.twilight.tools.command.CommandArgs;
import org.bukkit.command.CommandSender;

/**
 * @author Lucanius
 * @since May 20, 2022
 */
public class TwilightCommand extends AbstractCommand {

    @Command(name = "twilight", aliases = {"practice", "lucanius"})
    public void onCommand(CommandArgs cmd) {
        final CommandSender sender = cmd.getSender();
        sender.sendMessage(CC.BAR);
        sender.sendMessage(CC.translate(CC.MAIN + CC.BOLD + "Twilight&r &av" + plugin.getDescription().getVersion() + " &7- &blucA#0999"));
        sender.sendMessage(CC.translate(CC.ICON + CC.SECOND + "https://github.com/Lucaniuss/Twilight"));
        sender.sendMessage(CC.BAR);
    }
}
