package me.lucanius.twilight.event.bukkit.listeners;

import me.lucanius.twilight.Twilight;
import me.lucanius.twilight.event.bukkit.Events;
import me.lucanius.twilight.service.arena.Arena;
import me.lucanius.twilight.service.game.Game;
import me.lucanius.twilight.service.game.context.callback.DestroyCallback;
import me.lucanius.twilight.service.profile.Profile;
import me.lucanius.twilight.service.profile.ProfileState;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

/**
 * @author Lucanius
 * @since May 26, 2022
 */
public class WorldListener {

    private final Twilight plugin = Twilight.getInstance();

    public WorldListener() {
        Events.subscribe(BlockBreakEvent.class, event -> {
            Player player = event.getPlayer();
            Profile profile = plugin.getProfiles().get(player.getUniqueId());
            if (profile.getState() != ProfileState.PLAYING) {
                event.setCancelled(player.getGameMode() != GameMode.CREATIVE);
                return;
            }

            Game game = plugin.getGames().get(profile);
            if (game == null || !game.getLoadout().isBuild()) {
                event.setCancelled(true);
                return;
            }

            Block block = event.getBlock();
            DestroyCallback callback = game.isBreakable(block);
            switch (callback) {
                case NOT_INSIDE:
                case INVALID:
                case ABOVE_LIMIT:
                case INVALID_TYPE:
                case INVALID_BLOCK:
                    event.setCancelled(true);
                    break;
                case PLACED:
                    game.getPlacedBlocks().remove(block.getLocation());
                    break;
            }

            if (callback != DestroyCallback.VALID) {
                return;
            }

            game.getBrokenBlocks().put(block.getLocation(), block.getState());
        });

        Events.subscribe(BlockPlaceEvent.class, event -> {
            Player player = event.getPlayer();
            Profile profile = plugin.getProfiles().get(player.getUniqueId());
            if (profile.getState() != ProfileState.PLAYING) {
                event.setCancelled(player.getGameMode() != GameMode.CREATIVE);
                return;
            }

            Game game = plugin.getGames().get(profile);
            if (game == null || !game.getLoadout().isBuild()) {
                event.setCancelled(true);
                return;
            }

            Block block = event.getBlock();
            Location location = block.getLocation();
            Arena arena = game.getArenaCopy();
            if (!arena.isInside(location)) {
                event.setCancelled(true);
                return;
            }

            if (location.getY() > arena.getBuildHeight()) {
                event.setCancelled(true);
                return;
            }

            game.getPlacedBlocks().add(location);
        });
    }
}
