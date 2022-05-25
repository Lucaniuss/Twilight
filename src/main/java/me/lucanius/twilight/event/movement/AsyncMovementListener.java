package me.lucanius.twilight.event.movement;

import me.lucanius.twilight.Twilight;
import me.lucanius.twilight.event.bukkit.Events;
import me.lucanius.twilight.event.events.AsyncMovementEvent;
import me.lucanius.twilight.service.game.Game;
import me.lucanius.twilight.service.loadout.Loadout;
import me.lucanius.twilight.service.loadout.type.LoadoutType;
import me.lucanius.twilight.service.profile.Profile;
import me.lucanius.twilight.service.profile.ProfileState;
import org.bukkit.entity.Player;

/**
 * @author Lucanius
 * @since May 25, 2022
 */
public class AsyncMovementListener {

    private final Twilight plugin = Twilight.getInstance();

    public AsyncMovementListener() {
        Events.subscribe(AsyncMovementEvent.class, event -> {
            Profile profile = event.getProfile();
            if (profile.getState() != ProfileState.PLAYING) {
                return;
            }

            Game game = plugin.getGames().get(profile);
            Loadout loadout = game.getLoadout();
            if (!loadout.needsMovement()) {
                return;
            }

            Player player = event.getPlayer();
            LoadoutType type = loadout.getType();
            switch (type) {
                case SUMO:
                    if (event.getTo().getBlock().isLiquid()) {
                        type.getCallable().execute(player, plugin.getDamages().get(player.getUniqueId()), game);
                    }
                    break;
                case BRIDGES:
                    break;
            }
        });
    }
}
