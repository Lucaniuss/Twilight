package me.lucanius.twilight.service.queue.modules;

import me.lucanius.twilight.service.loadout.Loadout;
import me.lucanius.twilight.service.queue.abstr.AbstractQueue;
import me.lucanius.twilight.service.queue.abstr.AbstractQueueData;
import me.lucanius.twilight.service.queue.callback.QueueCallback;
import me.lucanius.twilight.service.queue.data.SoloQueueData;
import me.lucanius.twilight.tools.Tools;
import org.bukkit.entity.Player;

/**
 * @author Lucanius
 * @since May 22, 2022
 */
public class SoloQueue extends AbstractQueue<Player> {

    @Override
    public void enqueue(Player element, Loadout loadout) {
        SoloQueueData data = new SoloQueueData(
                element,
                loadout,
                Tools.getPing(element),
                plugin.getProfiles().get(element.getUniqueId()).getPingRange()
        );

        // TODO: Send messages

        plugin.getQueues().putData(element.getUniqueId(), data);
        add(data);
    }

    @Override
    public void dequeue(AbstractQueueData<?> data) {
        // TODO: Send messages

        plugin.getQueues().removeData(((SoloQueueData) data).getElement().getUniqueId());
        remove(data);
    }

    @Override
    public QueueCallback start(AbstractQueueData<?> first, AbstractQueueData<?> second) {
        // TODO: Implement
        return QueueCallback.ALLOWED;
    }
}
