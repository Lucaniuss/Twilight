package me.lucanius.twilight.event.listeners;

import me.lucanius.twilight.Twilight;
import me.lucanius.twilight.event.EventListener;
import me.lucanius.twilight.event.TwilightEvent;
import me.lucanius.twilight.event.events.GameStartEvent;
import me.lucanius.twilight.service.game.Game;
import me.lucanius.twilight.service.game.team.GameTeam;
import me.lucanius.twilight.service.game.team.member.TeamMember;
import me.lucanius.twilight.service.profile.Profile;
import me.lucanius.twilight.service.profile.ProfileState;
import me.lucanius.twilight.service.profile.modules.GameProfile;
import me.lucanius.twilight.tools.Scheduler;
import me.lucanius.twilight.tools.Tools;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * @author Lucanius
 * @since May 25, 2022
 */
public class GameListener implements EventListener {

    private final Twilight plugin = Twilight.getInstance();

    @Override
    public void onEvent(TwilightEvent twilightEvent) {
        if (!(twilightEvent instanceof GameStartEvent)) {
            return;
        }

        GameStartEvent event = (GameStartEvent) twilightEvent;
        Game game = event.getGame();
        // TODO: Check if game is buildable, get arena copy and set it to game, cancel event when arena copy is null

        Collection<TeamMember> members = game.getMembers();
        members.forEach(member -> {
            Profile profile = member.getProfile();
            profile.setState(ProfileState.PLAYING);
            GameProfile gameProfile = profile.getGameProfile();

            gameProfile.reset();
            gameProfile.setGameId(game.getUniqueId());

            GameTeam team = member.getTeam();
            gameProfile.setTeam(team);

            Player player = member.getPlayer();
            Tools.clearPlayer(player);

            if (team.getSpawn() == null) {
                team.setSpawn(team.getColor() == ChatColor.BLUE ? game.getArena().getA().getBukkitLocation() : game.getArena().getB().getBukkitLocation());
            }
            player.teleport(team.getSpawn());
        });

        Collection<Player> players = members.stream().map(TeamMember::getPlayer).collect(Collectors.toList());
        Scheduler.run(() -> {
            players.forEach(player -> plugin.getOnline().forEach(online -> {
                online.hidePlayer(player);
                player.hidePlayer(online);
            }));

            players.forEach(player -> players.forEach(player::showPlayer));
        });

        // start new GameTask
    }
}
