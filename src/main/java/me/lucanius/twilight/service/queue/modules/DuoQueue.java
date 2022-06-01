package me.lucanius.twilight.service.queue.modules;

import me.lucanius.twilight.service.loadout.Loadout;
import me.lucanius.twilight.service.queue.abstr.AbstractQueue;
import me.lucanius.twilight.service.queue.abstr.AbstractQueueData;
import me.lucanius.twilight.service.queue.callback.QueueCallback;
import me.lucanius.twilight.service.queue.data.DuoQueueData;
import me.lucanius.twilight.service.queue.menu.menus.DuoQueueMenu;
import me.lucanius.twilight.tools.CC;
import org.bukkit.entity.Player;

import java.util.Set;

/**
 * @author Lucanius
 * @since May 22, 2022
 */
public class DuoQueue extends AbstractQueue<Set<Player>> {

    public DuoQueue() {
        super("Duos");
        menu = new DuoQueueMenu(this);
    }

    @Override
    public void enqueue(Set<Player> element, Loadout loadout) {
        DuoQueueData data = new DuoQueueData(
                element,
                loadout
        );

        element.forEach(player -> {
            player.sendMessage(CC.SECOND + "You have been added to the " + CC.MAIN + getName() + CC.SECOND + " queue.");
            plugin.getQueues().putData(player, player.getUniqueId(), data);
        });
        add(data);
    }

    @Override
    public void dequeue(AbstractQueueData<?> data, QueueCallback callback) {
        String message = callback != QueueCallback.NONE && !callback.getMessage().equals("")
                ? callback.getMessage()
                : CC.SECOND + "You have been removed from the " + CC.MAIN + getName() + CC.SECOND + " queue.";

        ((DuoQueueData) data).getElement().forEach(player -> {
            player.sendMessage(CC.translate(message));
            plugin.getQueues().removeData(player, player.getUniqueId(), callback != QueueCallback.ALLOWED);
        });
        remove(data);
    }

    @Override
    public QueueCallback start(AbstractQueueData<?> first, AbstractQueueData<?> second) {
        // TODO: Implement
        return QueueCallback.ALLOWED;
    }
}
