package me.lucanius.prac.service.queue.modules;

import me.lucanius.prac.service.loadout.Loadout;
import me.lucanius.prac.service.queue.abstr.AbstractQueue;
import me.lucanius.prac.service.queue.abstr.AbstractQueueData;
import me.lucanius.prac.service.queue.callback.QueueCallback;
import me.lucanius.prac.service.queue.data.DuoQueueData;
import me.lucanius.prac.tools.Tools;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.Set;

/**
 * @author Lucanius
 * @since May 22, 2022
 */
public class DuoQueue extends AbstractQueue<Set<Player>> {

    @Override
    public void enqueue(Set<Player> element, Loadout loadout) {
        Optional<Player> leader = element.stream().findFirst();
        boolean isPresent = leader.isPresent();

        DuoQueueData data = new DuoQueueData(
                element,
                loadout,
                this,
                isPresent ? Tools.getPing(leader.get()) : 0,
                isPresent ? plugin.getProfiles().get(leader.get().getUniqueId()).getPingRange() : -1
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
