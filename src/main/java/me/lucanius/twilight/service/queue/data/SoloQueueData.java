package me.lucanius.twilight.service.queue.data;

import me.lucanius.twilight.service.loadout.Loadout;
import me.lucanius.twilight.service.queue.abstr.AbstractQueueData;
import me.lucanius.twilight.service.queue.modules.SoloQueue;
import me.lucanius.twilight.tools.CC;
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
