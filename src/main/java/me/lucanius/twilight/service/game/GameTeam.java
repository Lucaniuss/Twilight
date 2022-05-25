package me.lucanius.twilight.service.game;

import lombok.Getter;
import lombok.Setter;
import me.lucanius.twilight.Twilight;
import me.lucanius.twilight.tools.Tools;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

/**
 * @author Lucanius
 * @since May 25, 2022
 */
@Getter @Setter
public class GameTeam {

    private final static Twilight plugin = Twilight.getInstance();

    private final List<UUID> players;
    private final ChatColor color;
    private final String name;

    public GameTeam(List<UUID> players, ChatColor color) {
        this.players = players;
        this.color = color;
        this.name = Tools.getEnumName(color.name());
    }

    public Player getRandomPlayer() {
        return plugin.getServer().getPlayer(players.get(plugin.getRandom().nextInt()));
    }

    public String getFormattedName() {
        return color + name;
    }
}
