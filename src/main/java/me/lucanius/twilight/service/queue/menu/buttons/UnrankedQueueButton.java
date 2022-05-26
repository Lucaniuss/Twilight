package me.lucanius.twilight.service.queue.menu.buttons;

import me.lucanius.twilight.service.loadout.Loadout;
import me.lucanius.twilight.service.queue.menu.abstr.AbstractQueueButton;
import me.lucanius.twilight.service.queue.modules.UnrankedQueue;
import me.lucanius.twilight.tools.Scheduler;
import me.lucanius.twilight.tools.Tools;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

/**
 * @author Lucanius
 * @since May 23, 2022
 */
public class UnrankedQueueButton extends AbstractQueueButton {

    private final UnrankedQueue queue;

    public UnrankedQueueButton(UnrankedQueue queue, Loadout loadout) {
        super(queue, loadout);
        this.queue = queue;
    }

    @Override
    public void onClick(Player player, ClickType clickType) {
        queue.enqueue(player, loadout);
        Tools.playSuccess(player);

        Scheduler.runLater(player::closeInventory, 1L);
    }
}
