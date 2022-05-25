package me.lucanius.twilight.event.events;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.lucanius.twilight.event.AbstractEvent;
import me.lucanius.twilight.service.game.Game;
import me.lucanius.twilight.service.game.team.GameTeam;

/**
 * @author Lucanius
 * @since May 25, 2022
 */
@Getter @RequiredArgsConstructor
public class GameEndEvent extends AbstractEvent {

    private final Game game;
    private final GameTeam winner;
    private final GameTeam loser;

}
