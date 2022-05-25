package me.lucanius.twilight.event.bukkit.listeners;

import me.lucanius.twilight.event.bukkit.Events;
import me.lucanius.twilight.event.events.MovementEvent;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * @author Lucanius
 * @since May 25, 2022
 */
public class PlayerMoveListener {

    /**
     * Running our custom MovementEvent through the Bukkit PlayerMoveEvent
     */
    public PlayerMoveListener() {
        Events.subscribe(PlayerMoveEvent.class, MovementEvent::new);
    }
}
