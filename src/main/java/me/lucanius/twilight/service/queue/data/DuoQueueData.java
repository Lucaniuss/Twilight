package me.lucanius.twilight.service.queue.data;

import me.lucanius.twilight.service.loadout.Loadout;
import me.lucanius.twilight.service.queue.abstr.AbstractQueueData;
import me.lucanius.twilight.service.queue.modules.DuoQueue;
import me.lucanius.twilight.tools.CC;
import org.bukkit.entity.Player;

import java.util.Set;

/**
 * @author Lucanius
 * @since May 22, 2022
 */
public class DuoQueueData extends AbstractQueueData<Set<Player>> {

    public DuoQueueData(Set<Player> element, Loadout loadout, DuoQueue queue, int playerPing, int pingRange) {
        super(element, loadout, queue, playerPing, pingRange);
    }

    @Override
    public void sendMessage() {
        for (Player player : element) {
            for (String s : messages) {
                player.sendMessage(CC.translate(s));
            }
        }
    }

    @Override
    public void dequeue() {
        queue.dequeue(this);
    }
}
