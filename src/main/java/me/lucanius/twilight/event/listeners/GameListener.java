package me.lucanius.twilight.event.listeners;

import me.lucanius.twilight.Twilight;
import me.lucanius.twilight.event.bukkit.Events;
import me.lucanius.twilight.event.events.AsyncMovementEvent;
import me.lucanius.twilight.event.events.GameEndEvent;
import me.lucanius.twilight.event.events.GameStartEvent;
import me.lucanius.twilight.event.movement.AsyncMovementListener;
import me.lucanius.twilight.service.arena.Arena;
import me.lucanius.twilight.service.game.Game;
import me.lucanius.twilight.service.game.context.GameState;
import me.lucanius.twilight.service.game.task.GameTask;
import me.lucanius.twilight.service.game.team.GameTeam;
import me.lucanius.twilight.service.loadout.Loadout;
import me.lucanius.twilight.service.loadout.type.LoadoutType;
import me.lucanius.twilight.service.profile.Profile;
import me.lucanius.twilight.service.profile.ProfileState;
import me.lucanius.twilight.service.profile.modules.GameProfile;
import me.lucanius.twilight.tools.Scheduler;
import me.lucanius.twilight.tools.Tools;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Lucanius
 * @since May 25, 2022
 */
public class GameListener {

    private final Twilight plugin = Twilight.getInstance();

    public GameListener() {
        Events.subscribe(GameStartEvent.class, event -> {
            Game game = event.getGame();
            Loadout loadout = game.getLoadout();

            if (loadout.isBuild()) {
                Arena copy = game.getArena().getRandomCopy();
                if (copy == null) {
                    event.setCancelled(true);
                    return;
                }

                game.setArenaCopy(copy);
            }

            // initialize new AsyncMovementListener if it is not already initialized
            if (loadout.needsMovement() && !plugin.getEvents().subbed(AsyncMovementEvent.class)) {
                new AsyncMovementListener();
            }

            List<Player> players = new ArrayList<>();
            game.getTeams().forEach(team -> team.getMembers().forEach(member -> {

                Profile profile = member.getProfile();
                profile.setState(ProfileState.PLAYING);

                GameProfile gameProfile = profile.getGameProfile();

                gameProfile.reset();
                gameProfile.setGameId(game.getUniqueId());
                gameProfile.setTeam(team);

                if (team.getSpawn() == null) {
                    team.detectSpawn(game.getArenaCopy() != null ? game.getArenaCopy() : game.getArena());
                }

                Player player = member.getPlayer();
                player.teleport(team.getSpawn());
                Tools.clearPlayer(player);
                plugin.getGames().giveLoadouts(player, profile, loadout);

                players.add(player);
            }));

            Scheduler.run(() -> {
                // hide players to prevent players from seeing each other
                players.forEach(player -> plugin.getOnline().forEach(online -> {
                    online.hidePlayer(player);
                    player.hidePlayer(online);
                }));

                // show players that are in the game to each other
                players.forEach(player -> players.forEach(player::showPlayer));

                if (loadout.getType() == LoadoutType.BRIDGES) {
                    game.getTeams().forEach(GameTeam::spawnCage);
                }
            });

            game.setTask(new GameTask(plugin, game)).runTaskTimerAsynchronously(plugin, 20L, 20L);
        });

        Events.subscribe(GameEndEvent.class, event -> {
            Game game = event.getGame();

            game.setState(GameState.TERMINATED);
            game.setCountdown(4);

            Loadout loadout = game.getLoadout();

            game.getTeams().forEach(team -> team.getMembers().forEach(member -> {
                Player player = member.getPlayer();
                Tools.clearPlayer(player);

            }));
        });
    }
}
