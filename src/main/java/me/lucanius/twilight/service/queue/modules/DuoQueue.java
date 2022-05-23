package me.lucanius.twilight.service.queue.modules;

import me.lucanius.twilight.service.loadout.Loadout;
import me.lucanius.twilight.service.queue.abstr.AbstractQueue;
import me.lucanius.twilight.service.queue.abstr.AbstractQueueData;
import me.lucanius.twilight.service.queue.callback.QueueCallback;
import me.lucanius.twilight.service.queue.data.DuoQueueData;
import me.lucanius.twilight.service.queue.menu.menus.DuoQueueMenu;
import me.lucanius.twilight.service.queue.menu.menus.SoloQueueMenu;
import org.bukkit.entity.Player;

import java.util.Set;

/**
 * @author Lucanius
 * @since May 22, 2022
 */
public class DuoQueue extends AbstractQueue<Set<Player>> {

    public DuoQueue() {
        super("Duo");
        menu = new DuoQueueMenu(this);
    }

    @Override
    public void enqueue(Set<Player> element, Loadout loadout) {
        DuoQueueData data = new DuoQueueData(
                element,
                loadout
        );

        // TODO: Send messages

        element.forEach(player -> plugin.getQueues().putData(player.getUniqueId(), data));
        add(data);
    }

    @Override
    public void dequeue(AbstractQueueData<?> data) {
        // TODO: Send messages

        ((DuoQueueData) data).getElement().forEach(player -> plugin.getQueues().removeData(player.getUniqueId()));
        remove(data);
    }

    @Override
    public QueueCallback start(AbstractQueueData<?> first, AbstractQueueData<?> second) {
        // TODO: Implement
        return QueueCallback.ALLOWED;
    }
}
