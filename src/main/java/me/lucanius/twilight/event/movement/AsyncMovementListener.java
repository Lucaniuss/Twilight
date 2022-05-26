package me.lucanius.twilight.event.movement;

import me.lucanius.twilight.Twilight;
import me.lucanius.twilight.event.bukkit.Events;
import me.lucanius.twilight.event.events.AsyncMovementEvent;
import me.lucanius.twilight.service.game.Game;
import me.lucanius.twilight.service.game.context.GameState;
import me.lucanius.twilight.service.loadout.Loadout;
import me.lucanius.twilight.service.loadout.type.LoadoutType;
import me.lucanius.twilight.service.profile.Profile;
import me.lucanius.twilight.service.profile.ProfileState;
import me.lucanius.twilight.tools.Tools;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * @author Lucanius
 * @since May 25, 2022
 */
public class AsyncMovementListener {

    private final Twilight plugin = Twilight.getInstance();

    public AsyncMovementListener() {
        Tools.log("Initializing AsyncMovementListener...");
        Events.subscribe(AsyncMovementEvent.class, event -> {
            Profile profile = event.getProfile();
            if (profile == null) {
                return;
            }

            if (profile.getState() != ProfileState.PLAYING) {
                return;
            }

            Game game = plugin.getGames().get(profile);
            Loadout loadout = game.getLoadout();
            if (!loadout.needsMovement()) {
                return;
            }

            GameState state = game.getState();
            LoadoutType type = loadout.getType();
            Location to = event.getTo();
            Location from = event.getFrom();
            Player player = event.getPlayer();
            boolean sumo = type == LoadoutType.SUMO;
            if (state == GameState.STARTING && sumo && (to.getX() != from.getX() || to.getZ() != from.getZ())) {
                player.teleport(from);
            }

            boolean bridges = type == LoadoutType.BRIDGES;
            if ((sumo && to.getBlock().isLiquid()) || (bridges && (profile.getGameProfile().getTeam().getSpawn().getY() - 30) > to.getY())) {
                type.getCallable().execute(player, plugin.getDamages().get(player.getUniqueId()), game);
            }
        });
    }
}
