package me.lucanius.twilight.layout.provider;

import me.lucanius.twilight.service.game.Game;
import me.lucanius.twilight.service.game.team.GameTeam;
import me.lucanius.twilight.service.profile.Profile;
import me.lucanius.twilight.service.queue.abstr.AbstractQueueData;
import me.lucanius.twilight.tools.CC;
import me.lucanius.twilight.tools.Tools;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Lucanius
 * @since May 25, 2022
 */
public abstract class LinesProvider extends AbstractProvider {

    private final String signature = CC.GRAY + CC.ITALIC + "lucanius.me";

    public List<String> getLobby() {
        List<String> lines = new ArrayList<>();

        lines.add(CC.SMALL_BAR);
        lines.add(CC.WHITE + "&fOnline: " + CC.SECOND + plugin.getOnline().size());
        lines.add(CC.WHITE + "In Game: " + CC.SECOND + plugin.getGames().getSize());
        lines.add(CC.WHITE + "In Queue: " + CC.SECOND + plugin.getQueues().getSize());
        lines.add(" ");
        lines.add(signature);
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
        lines.add(signature);
        lines.add(CC.SMALL_BAR);

        return lines;
    }

    public List<String> getPlaying(Profile profile, Player player) {
        List<String> lines = new ArrayList<>();
        UUID uniqueId = player.getUniqueId();
        Game game = plugin.getGames().get(profile);
        GameTeam opposingTeam = game.getOpposingTeam(uniqueId);
        Optional<Player> enemy = Optional.ofNullable(opposingTeam.getFirstPlayer());
        boolean isPresent = enemy.isPresent();
        Profile enemyProfile = isPresent ? plugin.getProfiles().get(enemy.get().getUniqueId()) : plugin.getProfiles().getDummy();
        String enemyName = isPresent ? enemy.get().getName() : "...";

        lines.add(CC.SMALL_BAR);
        switch (game.getState()) {
            case STARTING:
                lines.add(CC.WHITE + "Fighting: " + CC.SECOND + enemyName);
                break;
            case ONGOING:
                lines.add(CC.WHITE + "Fighting: " + CC.SECOND + enemyName);
                lines.add(CC.WHITE + "Duration: " + CC.SECOND + game.getTime());
                switch (game.getLoadout().getType()) {
                    case BOXING:
                        lines.addAll(getBoxing(profile.getGameProfile(), enemyProfile.getGameProfile()));
                        break;
                    case BRIDGES:
                        lines.addAll(getBridges(game));
                        break;
                }
                lines.add(" ");
                lines.add(CC.WHITE + "Your Ping: " + CC.SECOND + Tools.getPing(player));
                lines.add(CC.WHITE + "Their Ping: " + CC.SECOND + (isPresent ? Tools.getPing(enemy.get()) : 0));
                break;
            case TERMINATED:
                lines.add(CC.WHITE + "Game ended.");
                break;
        }
        lines.add(" ");
        lines.add(signature);
        lines.add(CC.SMALL_BAR);

        return lines;
    }
}
