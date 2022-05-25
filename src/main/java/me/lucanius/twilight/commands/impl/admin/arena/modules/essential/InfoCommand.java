package me.lucanius.twilight.commands.impl.admin.arena.modules.essential;

import me.lucanius.twilight.commands.abstr.AbstractCommand;
import me.lucanius.twilight.service.arena.Arena;
import me.lucanius.twilight.tools.CC;
import me.lucanius.twilight.tools.Clickable;
import me.lucanius.twilight.tools.command.Command;
import me.lucanius.twilight.tools.command.CommandArgs;
import org.bukkit.entity.Player;

/**
 * @author Lucanius
 * @since May 24, 2022
 */
public class InfoCommand extends AbstractCommand {

    @Command(name = "arena.info", permission = "twilight.admin")
    public void onCommand(CommandArgs cmd) {
        Player player = cmd.getPlayer();
        String[] args = cmd.getArgs();

        if (args.length != 1) {
            player.sendMessage(getUsage(cmd.getLabel(), "<arena>"));
            return;
        }

        Arena arena = plugin.getArenas().get(args[0]);
        if (arena == null) {
            player.sendMessage(CC.RED + "That arena could not be found...");
            return;
        }

        player.sendMessage(CC.BAR);
        player.sendMessage(CC.MAIN + CC.BOLD + "Arena Information " + CC.GRAY + "- " + CC.MAIN + arena.getName());
        player.sendMessage(CC.ICON + CC.WHITE + "Min: " + CC.SECOND + arena.getMin().getX() + ", " + arena.getMin().getY() + ", " + arena.getMin().getZ());
        player.sendMessage(CC.ICON + CC.WHITE + "Max: " + CC.SECOND + arena.getMax().getX() + ", " + arena.getMax().getY() + ", " + arena.getMax().getZ());
        player.sendMessage(CC.ICON + CC.WHITE + "A: " + CC.SECOND + arena.getA().getX() + ", " + arena.getA().getY() + ", " + arena.getA().getZ());
        player.sendMessage(CC.ICON + CC.WHITE + "B: " + CC.SECOND + arena.getB().getX() + ", " + arena.getB().getY() + ", " + arena.getB().getZ());
        player.sendMessage(CC.ICON + CC.WHITE + "Build-Height: " + CC.SECOND + arena.getBuildHeight());
        player.sendMessage(CC.ICON + CC.WHITE + "Copies: " + CC.SECOND + (arena.getCopies() != null ? arena.getCopies().size() : CC.RED + "NONE"));
        player.sendMessage(" ");
        new Clickable(
                CC.SECOND + CC.ITALIC + "Click to teleport",
                CC.SECOND + CC.ITALIC + "Click to teleport",
                "/minecraft:tp " + arena.getA().getX() + " " + arena.getA().getY() + " " + arena.getA().getZ()
        ).send(player);
        player.sendMessage(CC.BAR);
    }
}
