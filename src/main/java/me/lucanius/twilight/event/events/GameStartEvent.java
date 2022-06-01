package me.lucanius.twilight.event.events;

import lombok.Getter;
import me.lucanius.twilight.event.AbstractEvent;
import me.lucanius.twilight.service.game.Game;

/**
 * @author Clouke
 * @since 25.05.2022 14:28
 * © Twilight - All Rights Reserved
 */

@Getter
public class GameStartEvent extends AbstractEvent {

    private final Game game;

    public GameStartEvent(Game game) {
        this.game = game;

        plugin.getEvents().publish(this);
    }
}
