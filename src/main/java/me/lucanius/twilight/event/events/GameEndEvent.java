package me.lucanius.twilight.event.events;

import lombok.Getter;
import me.lucanius.twilight.event.AbstractEvent;
import me.lucanius.twilight.service.game.Game;
import me.lucanius.twilight.service.game.team.GameTeam;

/**
 * @author Lucanius
 * @since May 25, 2022
 */
@Getter
public class GameEndEvent extends AbstractEvent {

    private final Game game;
    private final GameTeam winner;
    private final GameTeam loser;

    public GameEndEvent(Game game, GameTeam winner, GameTeam loser) {
        this.game = game;
        this.winner = winner;
        this.loser = loser;

        plugin.getEvents().publish(this);
    }
}
