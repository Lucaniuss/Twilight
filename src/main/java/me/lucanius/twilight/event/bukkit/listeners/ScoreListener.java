package me.lucanius.twilight.event.bukkit.listeners;

import me.lucanius.twilight.Twilight;
import me.lucanius.twilight.event.bukkit.Events;
import me.lucanius.twilight.event.events.GameEndEvent;
import me.lucanius.twilight.service.game.Game;
import me.lucanius.twilight.service.game.context.GameState;
import me.lucanius.twilight.service.game.task.BridgesTask;
import me.lucanius.twilight.service.game.team.GameTeam;
import me.lucanius.twilight.service.loadout.type.LoadoutType;
import me.lucanius.twilight.service.profile.Profile;
import me.lucanius.twilight.service.profile.ProfileState;
import me.lucanius.twilight.tools.CC;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityPortalEnterEvent;

import java.util.UUID;

/**
 * @author Lucanius
 * @since May 26, 2022
 */
public class ScoreListener {

    private final Twilight plugin = Twilight.getInstance();

    public ScoreListener() {
        Events.subscribe(EntityPortalEnterEvent.class, event -> {
            if (!(event.getEntity() instanceof Player)) {
                return;
            }

            Player player = (Player) event.getEntity();
            UUID uniqueId = player.getUniqueId();
            Profile profile = plugin.getProfiles().get(uniqueId);
            if (profile.getState() != ProfileState.PLAYING) {
                return;
            }

            Game game = plugin.getGames().get(profile);
            if (game == null) {
                return;
            }

            LoadoutType type = game.getLoadout().getType();
            if (type != LoadoutType.BRIDGES) {
                return;
            }

            GameTeam playerTeam = game.getTeam(uniqueId);
            if (playerTeam.getSpawn().distance(event.getLocation()) < 25.0d) {
                if (player.getLocation().distance(playerTeam.getSpawn()) < 4.0d) {
                    return;
                }

                type.getCallable().execute(plugin, player, plugin.getDamages().get(uniqueId), game);
                return;
            }

            if (!playerTeam.isAllowedToScore()) {
                return;
            }

            playerTeam.score();
            game.sendMessage(playerTeam.getColor() + player.getName() + CC.SECOND + " scored a point!");

            if (5 > playerTeam.getPoints()) {
                new BridgesTask(plugin, game).runTaskTimer(plugin, 0L, 20L);
                return;
            }

            player.teleport(playerTeam.getSpawn());
            if (game.getState() == GameState.TERMINATED) {
                return;
            }

            new GameEndEvent(game, playerTeam, game.getOpposingTeam(playerTeam));
        });
    }
}
