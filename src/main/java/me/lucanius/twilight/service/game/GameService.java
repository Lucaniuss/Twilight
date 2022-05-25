package me.lucanius.twilight.service.game;

import me.lucanius.twilight.Twilight;
import me.lucanius.twilight.event.AbstractEvent;
import me.lucanius.twilight.event.events.GameStartEvent;
import me.lucanius.twilight.service.loadout.Loadout;
import me.lucanius.twilight.service.profile.Profile;
import me.lucanius.twilight.service.queue.abstr.AbstractQueue;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Lucanius
 * @since May 25, 2022
 */
public class GameService {

    private final Twilight plugin;
    private final Map<UUID, Game> games;

    public GameService(Twilight plugin) {
        this.plugin = plugin;
        this.games = new HashMap<>();
    }

    public void startGame(Game game) {
        AbstractEvent event = new GameStartEvent(game);
        if (event.isCancelled()) {
            return;
        }

        games.put(game.getUniqueId(), game);
    }

    public Game get(UUID uniqueId) {
        return games.get(uniqueId);
    }

    public Game get(Profile profile) {
        return games.get(profile.getGameProfile().getGameId());
    }

    public Game get(Player player) {
        return games.get(plugin.getProfiles().get(player.getUniqueId()).getGameProfile().getGameId());
    }

    public int getSize() {
        return games.values()
                .stream()
                .map(Game::getEveryone)
                .map(Collection::size)
                .reduce(0, Integer::sum);
    }

    public int getSize(Loadout loadout, AbstractQueue<?> queue) {
        return games.values()
                .stream()
                .filter(game -> game.getLoadout().equals(loadout) && game.getQueue().equals(queue))
                .map(Game::getEveryone)
                .map(Collection::size)
                .reduce(0, Integer::sum);
    }
}
