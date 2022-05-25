package me.lucanius.twilight.service.queue.modules;

import me.lucanius.twilight.service.game.Game;
import me.lucanius.twilight.service.game.context.GameContext;
import me.lucanius.twilight.service.game.team.GameTeam;
import me.lucanius.twilight.service.loadout.Loadout;
import me.lucanius.twilight.service.queue.abstr.AbstractQueue;
import me.lucanius.twilight.service.queue.abstr.AbstractQueueData;
import me.lucanius.twilight.service.queue.callback.QueueCallback;
import me.lucanius.twilight.service.queue.data.SoloQueueData;
import me.lucanius.twilight.service.queue.menu.menus.SoloQueueMenu;
import me.lucanius.twilight.tools.Tools;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Lucanius
 * @since May 22, 2022
 */
public class SoloQueue extends AbstractQueue<Player> {

    public SoloQueue() {
        super("Solo");
        menu = new SoloQueueMenu(this);
    }

    @Override
    public void enqueue(Player element, Loadout loadout) {
        SoloQueueData data = new SoloQueueData(
                element,
                loadout,
                Tools.getPing(element),
                plugin.getProfiles().get(element.getUniqueId()).getPingRange()
        );

        // TODO: Send messages

        plugin.getQueues().putData(element, element.getUniqueId(), data);
        add(data);
    }

    @Override
    public void dequeue(AbstractQueueData<?> data, QueueCallback callback) {
        SoloQueueData soloData = (SoloQueueData) data;
        if (callback != QueueCallback.NONE && !callback.getMessage().equals("")) {
            soloData.getElement().sendMessage(callback.getMessage());
        }

        plugin.getQueues().removeData(soloData.getElement(), soloData.getElement().getUniqueId());
        remove(data);
    }

    @Override
    public QueueCallback start(AbstractQueueData<?> first, AbstractQueueData<?> second) {
        try {
            List<GameTeam> teams = Arrays.asList(
                    new GameTeam(Collections.singletonList(((SoloQueueData) first).getElement().getUniqueId()), ChatColor.BLUE),
                    new GameTeam(Collections.singletonList(((SoloQueueData) second).getElement().getUniqueId()), ChatColor.RED)
            );
            Loadout loadout = first.getLoadout();
            Game game = new Game(
                    GameContext.NORMAL,
                    loadout,
                    plugin.getArenas().getRandom(loadout),
                    this,
                    teams
            );

            return plugin.getGames().startGame(game) ? QueueCallback.ALLOWED : QueueCallback.DENIED;
        } catch (final Exception e) {
            e.printStackTrace();
            return QueueCallback.DENIED;
        }
    }
}
