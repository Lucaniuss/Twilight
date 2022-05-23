package me.lucanius.twilight.service.queue.menu.menus;

import me.lucanius.twilight.service.queue.menu.abstr.AbstractQueueMenu;
import me.lucanius.twilight.service.queue.menu.buttons.SoloQueueButton;
import me.lucanius.twilight.service.queue.modules.SoloQueue;
import me.lucanius.twilight.tools.menu.Button;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Lucanius
 * @since May 23, 2022
 */
public class SoloQueueMenu extends AbstractQueueMenu {

    private final SoloQueue queue;

    public SoloQueueMenu(SoloQueue queue) {
        super(queue);
        this.queue = queue;
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        plugin.getLoadouts().getAll().forEach(loadout ->
                buttons.put(loadout.getSlot(), new SoloQueueButton(queue, loadout))
        );

        fillEmptyWithGlass(buttons);

        return buttons;
    }
}
