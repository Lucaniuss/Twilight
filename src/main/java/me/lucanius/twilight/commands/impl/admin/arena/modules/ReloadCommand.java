package me.lucanius.twilight.commands.impl.admin.arena.modules;

import me.lucanius.twilight.commands.abstr.AbstractCommand;
import me.lucanius.twilight.tools.CC;
import me.lucanius.twilight.tools.command.Command;
import me.lucanius.twilight.tools.command.CommandArgs;

/**
 * @author Lucanius
 * @since May 24, 2022
 */
public class ReloadCommand extends AbstractCommand {

    @Command(name = "arena.reload", permission = "twilight.admin")
    public void onCommand(CommandArgs cmd) {
        plugin.getArenas().reload();
        cmd.getPlayer().sendMessage(CC.SECOND + "Successfully reloaded arenas.");
    }
}
