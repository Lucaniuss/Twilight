package me.lucanius.twilight.service.queue.menu.menus;

import me.lucanius.twilight.service.loadout.Loadout;
import me.lucanius.twilight.service.queue.menu.abstr.AbstractQueueMenu;
import me.lucanius.twilight.service.queue.menu.buttons.RankedQueueButton;
import me.lucanius.twilight.service.queue.modules.RankedQueue;
import me.lucanius.twilight.tools.menu.Button;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Lucanius
 * @since May 26, 2022
 */
public class RankedQueueMenu extends AbstractQueueMenu {

    private final RankedQueue queue;

    public RankedQueueMenu(RankedQueue queue) {
        super(queue);
        this.queue = queue;
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        plugin.getLoadouts().getAll().stream().filter(Loadout::isRanked).forEach(loadout ->
                buttons.put(loadout.getSlot(), new RankedQueueButton(queue, loadout))
        );

        fillEmptyWithGlass(buttons);

        return buttons;
    }
}
