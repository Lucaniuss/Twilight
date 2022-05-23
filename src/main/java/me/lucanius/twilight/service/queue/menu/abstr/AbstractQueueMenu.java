package me.lucanius.twilight.service.queue.menu.abstr;

import lombok.RequiredArgsConstructor;
import me.lucanius.twilight.service.queue.abstr.AbstractQueue;
import me.lucanius.twilight.tools.menu.Menu;
import org.bukkit.entity.Player;

/**
 * @author Lucanius
 * @since May 23, 2022
 */
@RequiredArgsConstructor
public abstract class AbstractQueueMenu extends Menu {

    private final AbstractQueue<?> queue;

    @Override
    public String getTitle(Player player) {
        return queue.getName() + " Queue";
    }
}
