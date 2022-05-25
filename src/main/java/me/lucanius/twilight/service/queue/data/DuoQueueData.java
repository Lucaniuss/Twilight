package me.lucanius.twilight.service.queue.data;

import me.lucanius.twilight.service.loadout.Loadout;
import me.lucanius.twilight.service.queue.abstr.AbstractQueueData;
import me.lucanius.twilight.service.queue.callback.QueueCallback;
import org.bukkit.entity.Player;

import java.util.Set;

/**
 * @author Lucanius
 * @since May 22, 2022
 */
public class DuoQueueData extends AbstractQueueData<Set<Player>> {

    public DuoQueueData(Set<Player> element, Loadout loadout) {
        super(element, loadout, plugin.getQueues().getDuoQueue(), 0, -1);
    }

    @Override
    public void sendMessage() {
        for (Player player : element) {
            for (String s : messages) {
                player.sendMessage(s.replace("<loadout>", loadout.getName()));
            }
        }
    }

    @Override
    public void dequeue() {
        queue.dequeue(this, QueueCallback.NONE);
    }

    @Override
    public void dequeue(QueueCallback callback) {
        queue.dequeue(this, callback);
    }
}
