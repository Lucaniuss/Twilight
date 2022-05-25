package me.lucanius.twilight.layout.provider;

import me.lucanius.twilight.Twilight;
import me.lucanius.twilight.service.game.Game;
import me.lucanius.twilight.service.game.team.GameTeam;
import me.lucanius.twilight.service.profile.Profile;
import me.lucanius.twilight.service.queue.abstr.AbstractQueueData;
import me.lucanius.twilight.tools.CC;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Lucanius
 * @since May 25, 2022
 */
public abstract class LinesProvider {

    protected final Twilight plugin = Twilight.getInstance();

    public List<String> getLobby() {
        List<String> lines = new ArrayList<>();

        lines.add(CC.SMALL_BAR);
        lines.add(CC.WHITE + "&fOnline: " + CC.SECOND + plugin.getOnline().size());
        lines.add(CC.WHITE + "In Game: " + CC.SECOND + plugin.getGames().getSize());
        lines.add(CC.WHITE + "In Queue: " + CC.SECOND + plugin.getQueues().getSize());
        lines.add(" ");
        lines.add(CC.GRAY + CC.ITALIC + "lucanius.me");
        lines.add(CC.SMALL_BAR);

        return lines;
    }

    public List<String> getQueue(Player player) {
        List<String> lines = new ArrayList<>();
        AbstractQueueData<?> data = plugin.getQueues().getData(player.getUniqueId());

        lines.add(CC.SMALL_BAR);
        lines.add(CC.WHITE + "Online: " + CC.SECOND + plugin.getOnline().size());
        lines.add(CC.WHITE + "In Game: " + CC.SECOND + plugin.getGames().getSize());
        lines.add(CC.WHITE + "In Queue: " + CC.SECOND + plugin.getQueues().getSize());
        lines.add(CC.SMALL_BAR);
        lines.add(CC.MAIN + CC.ITALIC + "Queueing " + data.getQueue().getName());
        lines.add(CC.ICON + CC.WHITE + "Time: " + CC.SECOND + data.getTime());
        lines.add(CC.ICON + CC.WHITE + "Loadout: " + CC.SECOND + data.getLoadout().getName());
        lines.add(" ");
        lines.add(CC.GRAY + CC.ITALIC + "lucanius.me");
        lines.add(CC.SMALL_BAR);

        return lines;
    }

    public List<String> getPlaying(Profile profile, Player player) {
        List<String> lines = new ArrayList<>();
        UUID uniqueId = player.getUniqueId();
        Game game = plugin.getGames().get(profile);
        GameTeam opposingTeam = game.getOpposingTeam(uniqueId);

        lines.add(CC.SMALL_BAR);
        switch (game.getState()) {
            case STARTING:
                lines.add(CC.WHITE + "Fighting: " + CC.SECOND + opposingTeam.getFirstPlayer().getName());
                break;
            case ONGOING:
                lines.add(CC.WHITE + "Fighting: " + CC.SECOND + opposingTeam.getFirstPlayer().getName());
                lines.add(CC.WHITE + "Duration: " + CC.SECOND + game.getTime());
                break;
            case TERMINATED:
                lines.add(CC.WHITE + "Game ended.");
                break;
        }
        lines.add(" ");
        lines.add(CC.GRAY + CC.ITALIC + "lucanius.me");
        lines.add(CC.SMALL_BAR);

        return lines;
    }
}
