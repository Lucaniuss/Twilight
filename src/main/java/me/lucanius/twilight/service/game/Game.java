package me.lucanius.twilight.service.game;

import me.lucanius.twilight.Twilight;
import me.lucanius.twilight.service.arena.Arena;
import me.lucanius.twilight.service.loadout.Loadout;
import me.lucanius.twilight.service.queue.abstr.AbstractQueue;
import org.bukkit.Location;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * @author Lucanius
 * @since May 25, 2022
 */
public class Game {

    private final static Twilight plugin = Twilight.getInstance();

    private final Set<Location> blocks;
    private final UUID uniqueId;
    private final GameType type;
    private final Loadout loadout;
    private final Arena arena;
    private final AbstractQueue<?> queue;
    private final List<GameTeam> teams;

    private GameState state;

    public Game(GameType type, Loadout loadout, Arena arena, AbstractQueue<?> queue, List<GameTeam> teams) {
        this.blocks = new HashSet<>();
        this.uniqueId = UUID.randomUUID();
        this.type = type;
        this.loadout = loadout;
        this.arena = arena;
        this.queue = queue;
        this.teams = teams;

        this.state = GameState.STARTING;
    }
}
