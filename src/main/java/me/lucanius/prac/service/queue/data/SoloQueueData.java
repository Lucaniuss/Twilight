package me.lucanius.prac.service.queue.data;

import me.lucanius.prac.service.loadout.Loadout;
import me.lucanius.prac.service.queue.abstr.AbstractQueueData;
import me.lucanius.prac.service.queue.modules.SoloQueue;
import me.lucanius.prac.tools.CC;
import org.bukkit.entity.Player;

/**
 * @author Lucanius
 * @since May 22, 2022
 */
public class SoloQueueData extends AbstractQueueData<Player> {

    public SoloQueueData(Player element, Loadout loadout, SoloQueue queue, int playerPing, int pingRange) {
        super(element, loadout, queue, playerPing, pingRange);
    }

    @Override
    public void sendMessage() {
        for (String s : messages) {
            element.sendMessage(CC.translate(s));
        }
    }

    @Override
    public void dequeue() {
        queue.dequeue(this);
    }
}
