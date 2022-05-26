package me.lucanius.twilight.service.queue.data;

import me.lucanius.twilight.service.loadout.Loadout;
import me.lucanius.twilight.service.queue.abstr.AbstractQueueData;
import me.lucanius.twilight.service.queue.callback.QueueCallback;
import org.bukkit.entity.Player;

/**
 * @author Lucanius
 * @since May 26, 2022
 */
public class RankedQueueData extends AbstractQueueData<Player> {

    public RankedQueueData(Player element, Loadout loadout, int playerPing, int pingRange) {
        super(element, loadout, plugin.getQueues().getRanked(), playerPing, pingRange);
    }

    @Override
    public void sendMessage() {
        for (String s : messages) {
            element.sendMessage(s.replace("<loadout>", loadout.getName()));
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
