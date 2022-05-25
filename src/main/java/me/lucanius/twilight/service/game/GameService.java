package me.lucanius.twilight.service.game;

import me.lucanius.twilight.Twilight;

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
}
