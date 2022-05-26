package me.lucanius.twilight.commands.impl.defaults.party;

import me.lucanius.twilight.commands.abstr.AbstractCommand;
import me.lucanius.twilight.tools.CC;
import me.lucanius.twilight.tools.command.Command;
import me.lucanius.twilight.tools.command.CommandArgs;
import me.lucanius.twilight.tools.command.Completer;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Clouke
 * @since 26.05.2022 14:36
 * © Twilight - All Rights Reserved
 */
public class PartyCommand extends AbstractCommand {

    private final List<String> completer;

    public PartyCommand() {
        completer = plugin.getRegistration()
                .getClassesInPackage("me.lucanius.twilight.commands.impl.defaults.party.modules")
                .stream()
                .map(clazz -> clazz.getSimpleName().replace("Command", "").toLowerCase())
                .collect(Collectors.toList());
    }

    @Command(name = "party", aliases = {"p"})
    public void onCommand(CommandArgs cmd) {
        Player player = cmd.getPlayer();
        player.sendMessage(CC.BAR);
        player.sendMessage(CC.MAIN + CC.BOLD + "Party Commands " + CC.GRAY + "(" + completer.size() + ")");
        for (String str : completer) {
            player.sendMessage(CC.ICON + CC.SECOND + "/" + cmd.getLabel() + " " + str);
        }
        player.sendMessage(CC.BAR);
    }

    @Completer(name = "party", aliases = {"p"})
    public List<String> getCompleter(CommandArgs cmd) {
        return completer;
    }
}
