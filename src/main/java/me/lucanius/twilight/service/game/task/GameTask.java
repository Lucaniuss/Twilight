package me.lucanius.twilight.service.game.task;

import lombok.RequiredArgsConstructor;
import me.lucanius.twilight.Twilight;
import me.lucanius.twilight.event.events.AsyncMovementEvent;
import me.lucanius.twilight.service.game.Game;
import me.lucanius.twilight.service.game.context.GameState;
import me.lucanius.twilight.tools.CC;
import me.lucanius.twilight.tools.Tools;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author Lucanius
 * @since May 26, 2022
 */
@RequiredArgsConstructor
public class GameTask extends BukkitRunnable {

    private final Twilight plugin;
    private final Game game;

    @Override
    public void run() {
        if (game == null) {
            cancel();
            return;
        }

        switch (game.getState()) {
            case STARTING:
                if (game.decrement() == 0) {
                    game.setTimeStamp(System.currentTimeMillis());
                    game.setState(GameState.ONGOING);
                    game.sendMessageWithSound(CC.SECOND + "The game has been started!", Sound.FIREWORK_TWINKLE);
                } else {
                    game.sendMessageWithSound(CC.SECOND + "Starting in " + CC.MAIN + game.getCountdown() + CC.SECOND + "s...", Sound.NOTE_STICKS);
                }
                break;
            case TERMINATED:
                if (game.decrement() == 0) {
                    game.clearArena();

                    game.getAlive().forEach(member -> plugin.getLobby().toLobby(member, true));
                    game.forEachSpectator(game::removeSpectator);

                    plugin.getGames().removeGame(game);

                    // we remove the movement listener for optimization purposes
                    if (game.getLoadout().needsMovement() && !plugin.getGames().needsMovement() && plugin.getEvents().subbed(AsyncMovementEvent.class)) {
                        plugin.getEvents().unsubscribe(AsyncMovementEvent.class);
                        Tools.log("Unsubscribed from AsyncMovementEvent");
                    }

                    cancel();
                }
                break;
        }
    }
}
