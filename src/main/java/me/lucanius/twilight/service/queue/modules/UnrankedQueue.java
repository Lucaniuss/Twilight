package me.lucanius.twilight.service.queue.modules;

import me.lucanius.twilight.service.arena.Arena;
import me.lucanius.twilight.service.game.Game;
import me.lucanius.twilight.service.game.context.GameContext;
import me.lucanius.twilight.service.game.team.GameTeam;
import me.lucanius.twilight.service.loadout.Loadout;
import me.lucanius.twilight.service.queue.abstr.AbstractQueue;
import me.lucanius.twilight.service.queue.abstr.AbstractQueueData;
import me.lucanius.twilight.service.queue.callback.QueueCallback;
import me.lucanius.twilight.service.queue.data.UnrankedQueueData;
import me.lucanius.twilight.service.queue.menu.menus.UnrankedQueueMenu;
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
public class UnrankedQueue extends AbstractQueue<Player> {

    public UnrankedQueue() {
        super("Unranked");
        menu = new UnrankedQueueMenu(this);
    }

    @Override
    public void enqueue(Player element, Loadout loadout) {
        UnrankedQueueData data = new UnrankedQueueData(
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
        UnrankedQueueData soloData = (UnrankedQueueData) data;
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
                    new GameTeam(Collections.singletonList(((UnrankedQueueData) first).getElement().getUniqueId()), ChatColor.BLUE),
                    new GameTeam(Collections.singletonList(((UnrankedQueueData) second).getElement().getUniqueId()), ChatColor.RED)
            );

            Loadout loadout = first.getLoadout();
            if (loadout == null) {
                loadout = second.getLoadout();
            }
            if (loadout == null) {
                return QueueCallback.NO_LOADOUT;
            }

            Arena arena = plugin.getArenas().getRandom(loadout);
            if (arena == null) {
                return QueueCallback.NO_ARENA;
            }

            Game game = new Game(GameContext.NORMAL, loadout, arena, this, teams);

            return plugin.getGames().startGame(game) ? QueueCallback.ALLOWED : QueueCallback.DENIED;
        } catch (final Exception e) {
            e.printStackTrace();
            return QueueCallback.DENIED;
        }
    }
}
