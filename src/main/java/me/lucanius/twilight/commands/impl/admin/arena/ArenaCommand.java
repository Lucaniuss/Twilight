package me.lucanius.twilight.commands.impl.admin.arena;

import me.lucanius.twilight.commands.abstr.AbstractCommand;
import me.lucanius.twilight.tools.CC;
import me.lucanius.twilight.tools.command.Command;
import me.lucanius.twilight.tools.command.CommandArgs;
import me.lucanius.twilight.tools.command.Completer;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Lucanius
 * @since May 24, 2022
 */
public class ArenaCommand extends AbstractCommand {

    private final List<String> completer;

    public ArenaCommand() {
        completer = plugin.getRegistration()
                .getClassesInPackage("me.lucanius.twilight.commands.impl.admin.arena.modules")
                .stream()
                .map(clazz -> clazz.getSimpleName().replace("Command", "").toLowerCase())
                .collect(Collectors.toList());
    }

    @Command(name = "arena", permission = "twilight.admin")
    public void onCommand(CommandArgs cmd) {
        Player player = cmd.getPlayer();
        player.sendMessage(CC.BAR);
        player.sendMessage(CC.MAIN + CC.BOLD + "Arena Commands " + CC.GRAY + "(" + completer.size() + ")");
        for (String str : completer) {
            player.sendMessage(CC.ICON + CC.SECOND + "/" + cmd.getLabel() + " " + str);
        }
        player.sendMessage(CC.BAR);
    }

    @Completer(name = "arena")
    public List<String> getCompleter(CommandArgs cmd) {
        return completer;
    }
}
