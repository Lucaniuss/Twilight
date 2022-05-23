package me.lucanius.twilight.service.queue.menu.buttons;

import me.lucanius.twilight.service.loadout.Loadout;
import me.lucanius.twilight.service.queue.menu.abstr.AbstractQueueButton;
import me.lucanius.twilight.service.queue.modules.DuoQueue;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

/**
 * @author Lucanius
 * @since May 23, 2022
 */
public class DuoQueueButton extends AbstractQueueButton {

    private final DuoQueue queue;

    public DuoQueueButton(DuoQueue queue, Loadout loadout) {
        super(queue, loadout);
        this.queue = queue;
    }

    @Override
    public void onClick(Player player, ClickType clickType) {
        // TODO: Create party system and add the whole party to the queue, only if the party size is 2.
    }
}
