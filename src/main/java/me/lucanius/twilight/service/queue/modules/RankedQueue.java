package me.lucanius.twilight.service.queue.modules;

import me.lucanius.twilight.service.arena.Arena;
import me.lucanius.twilight.service.game.Game;
import me.lucanius.twilight.service.game.context.GameContext;
import me.lucanius.twilight.service.game.team.GameTeam;
import me.lucanius.twilight.service.loadout.Loadout;
import me.lucanius.twilight.service.queue.abstr.AbstractQueue;
import me.lucanius.twilight.service.queue.abstr.AbstractQueueData;
import me.lucanius.twilight.service.queue.callback.QueueCallback;
import me.lucanius.twilight.service.queue.data.RankedQueueData;
import me.lucanius.twilight.service.queue.menu.menus.RankedQueueMenu;
import me.lucanius.twilight.tools.CC;
import me.lucanius.twilight.tools.Tools;
import me.lucanius.twilight.tools.functions.Voluntary;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Lucanius
 * @since May 26, 2022
 */
public class RankedQueue extends AbstractQueue<Player> {

    public RankedQueue() {
        super("Ranked");
        menu = new RankedQueueMenu(this);
    }

    @Override
    public void enqueue(Player element, Loadout loadout) {
        RankedQueueData data = new RankedQueueData(
                element,
                loadout,
                Tools.getPing(element), // check for elo
                plugin.getProfiles().get(element.getUniqueId()).getPingRange() // check for elo
        );

        element.sendMessage(CC.SECOND + "You have been added to the " + CC.MAIN + getName() + CC.SECOND + " queue.");

        plugin.getQueues().putData(element, element.getUniqueId(), data);
        add(data);
    }

    @Override
    public void dequeue(AbstractQueueData<?> data, QueueCallback callback) {
        RankedQueueData rankedData = (RankedQueueData) data;
        String message = callback != QueueCallback.NONE && !callback.getMessage().equals("")
                ? callback.getMessage()
                : CC.SECOND + "You have been removed from the " + CC.MAIN + getName() + CC.SECOND + " queue.";

        rankedData.getElement().sendMessage(CC.translate(message));
        plugin.getQueues().removeData(rankedData.getElement(), rankedData.getElement().getUniqueId(), callback != QueueCallback.ALLOWED);
        remove(data);
    }

    @Override
    public QueueCallback start(AbstractQueueData<?> first, AbstractQueueData<?> second) {
        try {
            Loadout loadout = Voluntary.ofNull(first.getLoadout()).orElse(second.getLoadout());
            if (loadout == null) {
                return QueueCallback.NO_LOADOUT;
            }

            Arena arena = plugin.getArenas().getRandom(loadout);
            if (arena == null) {
                return QueueCallback.NO_ARENA;
            }

            List<GameTeam> teams = new ArrayList<>(Arrays.asList(
                    new GameTeam(Collections.singletonList(((RankedQueueData) first).getElement().getUniqueId()), ChatColor.BLUE),
                    new GameTeam(Collections.singletonList(((RankedQueueData) second).getElement().getUniqueId()), ChatColor.RED)
            ));

            return plugin.getGames().startGame(new Game(GameContext.NORMAL, loadout, arena, this, teams))
                    ? QueueCallback.ALLOWED
                    : QueueCallback.DENIED;
        } catch (final Exception e) {
            e.printStackTrace();
            return QueueCallback.DENIED;
        }
    }
}
