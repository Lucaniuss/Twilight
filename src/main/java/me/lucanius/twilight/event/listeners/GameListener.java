package me.lucanius.twilight.event.listeners;

import me.lucanius.twilight.Twilight;
import me.lucanius.twilight.event.bukkit.Events;
import me.lucanius.twilight.event.events.GameEndEvent;
import me.lucanius.twilight.event.events.GameStartEvent;
import me.lucanius.twilight.event.events.AsyncMovementEvent;
import me.lucanius.twilight.event.movement.MovementListener;
import me.lucanius.twilight.service.arena.Arena;
import me.lucanius.twilight.service.game.Game;
import me.lucanius.twilight.service.game.team.GameTeam;
import me.lucanius.twilight.service.game.team.member.TeamMember;
import me.lucanius.twilight.service.profile.Profile;
import me.lucanius.twilight.service.profile.ProfileState;
import me.lucanius.twilight.service.profile.modules.GameProfile;
import me.lucanius.twilight.tools.Scheduler;
import me.lucanius.twilight.tools.Tools;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Lucanius
 * @since May 25, 2022
 */
public class GameListener {

    private final Twilight plugin = Twilight.getInstance();

    public GameListener() {
        Events.subscribe(GameStartEvent.class, event -> {
            Game game = event.getGame();
            if (game.getLoadout().isBuild()) {
                Arena copy = game.getArena().getRandomCopy();
                if (copy == null) {
                    event.setCancelled(true);
                    return;
                }

                game.setArenaCopy(copy);
                if (!plugin.getEvents().subbed(AsyncMovementEvent.class)) {
                    new MovementListener();
                }
            }

            Collection<Player> players = new ArrayList<>();
            Collection<TeamMember> members = game.getMembers();
            members.forEach(member -> {
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
                    team.detectSpawn(game.getArena());
                }
                player.teleport(team.getSpawn());

                players.add(player);
            });

            Scheduler.run(() -> {
                players.forEach(player -> plugin.getOnline().forEach(online -> {
                    online.hidePlayer(player);
                    player.hidePlayer(online);
                }));

                players.forEach(player -> players.forEach(player::showPlayer));
            });

            // start new GameTask
        });

        Events.subscribe(GameEndEvent.class, event -> {
            Game game = event.getGame();
            if (game.getLoadout().isBuild() && plugin.getGames().hasBuild()) {
                if (plugin.getEvents().subbed(AsyncMovementEvent.class)) {
                    plugin.getEvents().unsubscribe(AsyncMovementEvent.class);
                }
            }
        });
    }
}
