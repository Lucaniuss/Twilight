package me.lucanius.twilight.event.bukkit.listeners;

import me.lucanius.twilight.event.bukkit.Events;
import me.lucanius.twilight.event.events.AsyncMovementEvent;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * @author Lucanius
 * @since May 25, 2022
 */
public class PlayerMoveListener {

    /**
     * Running our custom AsyncMovementEvent through the Bukkit PlayerMoveEvent
     */
    public PlayerMoveListener() {
        Events.subscribe(PlayerMoveEvent.class, AsyncMovementEvent::new);
    }
}
