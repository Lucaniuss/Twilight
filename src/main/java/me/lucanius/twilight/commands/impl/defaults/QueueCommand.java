package me.lucanius.twilight.commands.impl.defaults;

import me.lucanius.twilight.commands.abstr.AbstractCommand;
import me.lucanius.twilight.tools.command.Command;
import me.lucanius.twilight.tools.command.CommandArgs;
import org.bukkit.entity.Player;

/**
 * @author Clouke
 * @since 25.05.2022 12:58
 * © Twilight - All Rights Reserved
 */
public class QueueCommand extends AbstractCommand {

    @Command(name = "queue", aliases = {"q"})
    public void onCommand(CommandArgs cmd) {
        Player player = cmd.getPlayer();

        // not implemented yet
    }
}
