package me.lucanius.twilight.commands.impl.admin.loadout;

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
 * @since May 25, 2022
 */
public class LoadoutCommand extends AbstractCommand {

    private final List<String> completer;

    public LoadoutCommand() {
        completer = plugin.getRegistration()
                .getClassesInPackage("me.lucanius.twilight.commands.impl.admin.loadout.modules")
                .stream()
                .map(clazz -> clazz.getSimpleName().replace("Command", "").toLowerCase())
                .collect(Collectors.toList());
    }

    @Command(name = "loadout", permission = "twilight.admin")
    public void onCommand(CommandArgs cmd) {
        Player player = cmd.getPlayer();
        player.sendMessage(CC.BAR);
        player.sendMessage(CC.MAIN + CC.BOLD + "Loadout Commands " + CC.GRAY + "(" + completer.size() + ")");
        for (String str : completer) {
            player.sendMessage(CC.ICON + CC.SECOND + "/" + cmd.getLabel() + " " + str);
        }
        player.sendMessage(CC.BAR);
    }

    @Completer(name = "loadout")
    public List<String> getCompleter(CommandArgs cmd) {
        return completer;
    }
}
