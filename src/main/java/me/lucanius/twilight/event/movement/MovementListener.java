package me.lucanius.twilight.event.movement;

import me.lucanius.twilight.event.bukkit.Events;
import me.lucanius.twilight.event.events.AsyncMovementEvent;
import me.lucanius.twilight.tools.Tools;

/**
 * @author Lucanius
 * @since May 25, 2022
 */
public class MovementListener {

    public MovementListener() { // TESTING
        Events.subscribe(AsyncMovementEvent.class, event -> {
            Tools.log(event.getPlayer().getName() + " triggered a movement event");
        });
    }
}
