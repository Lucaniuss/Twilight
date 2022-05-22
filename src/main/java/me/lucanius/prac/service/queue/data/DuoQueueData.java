package me.lucanius.prac.service.queue.data;

import me.lucanius.prac.service.loadout.Loadout;
import me.lucanius.prac.service.queue.abstr.AbstractQueueData;
import me.lucanius.prac.service.queue.modules.DuoQueue;
import me.lucanius.prac.tools.CC;
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
