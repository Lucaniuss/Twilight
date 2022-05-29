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
import me.lucanius.twilight.service.profile.Profile;
import me.lucanius.twilight.service.profile.ProfileState;
import me.lucanius.twilight.service.profile.modules.GameProfile;
import me.lucanius.twilight.tools.Scheduler;
import me.lucanius.twilight.tools.Tools;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

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

            Set<Player> players = new HashSet<>();
            game.getMembers().forEach(member -> {
                Profile profile = member.getProfile();
                profile.setState(ProfileState.PLAYING);
                GameProfile gameProfile = profile.getGameProfile();

                GameTeam team = member.getTeam();

                gameProfile.reset();
                gameProfile.setGameId(game.getUniqueId());
                gameProfile.setTeam(team);

                Player player = member.getPlayer();
                Tools.clearPlayer(player);

                if (team.getSpawn() == null) {
                    team.detectSpawn(game.getArenaCopy() != null ? game.getArenaCopy() : game.getArena());
                }
                player.teleport(team.getSpawn());

                plugin.getGames().giveLoadouts(player, profile, loadout);

                players.add(player);
            });

            Scheduler.run(() -> {
                // hide players to prevent players from seeing each other
                players.forEach(player -> plugin.getOnline().forEach(online -> {
                    online.hidePlayer(player);
                    player.hidePlayer(online);
                }));

                // show players that are in the game to each other
                players.forEach(player -> players.forEach(player::showPlayer));

                game.getTeams().forEach(GameTeam::spawnCage);
            });

            game.setTask(new GameTask(plugin, game)).runTaskTimer(plugin, 20L, 20L);
        });

        Events.subscribe(GameEndEvent.class, event -> {
            Game game = event.getGame();

            game.setState(GameState.TERMINATED);
            game.setCountdown(4);

            Loadout loadout = game.getLoadout();
        });
    }
}
