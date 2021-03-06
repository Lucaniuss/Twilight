package me.lucanius.twilight.event.events;

import lombok.Getter;
import me.lucanius.twilight.event.AbstractEvent;
import me.lucanius.twilight.service.profile.Profile;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * @author Lucanius
 * @since May 25, 2022
 */
@Getter
public class AsyncMovementEvent extends AbstractEvent {

    private final PlayerMoveEvent originalEvent;
    private final Player player;
    private final Profile profile;
    private final Location to;
    private final Location from;

    public AsyncMovementEvent(PlayerMoveEvent originalEvent) {
        this.originalEvent = originalEvent;
        this.player = originalEvent.getPlayer();
        this.profile = plugin.getProfiles().get(player.getUniqueId());
        this.to = originalEvent.getTo();
        this.from = originalEvent.getFrom();

        plugin.getEvents().publish(this);
    }
}
