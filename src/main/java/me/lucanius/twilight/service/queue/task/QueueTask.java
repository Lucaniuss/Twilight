package me.lucanius.twilight.service.queue.task;

import me.lucanius.twilight.Twilight;
import me.lucanius.twilight.service.queue.abstr.AbstractQueueData;
import me.lucanius.twilight.service.queue.callback.QueueCallback;
import me.lucanius.twilight.service.queue.menu.abstr.AbstractQueueMenu;
import me.lucanius.twilight.tools.functions.Voluntary;
import me.lucanius.twilight.tools.menu.handlers.MenuSaver;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.Iterator;

/**
 * @author Lucanius
 * @since May 22, 2022
 */
public class QueueTask extends BukkitRunnable {

    private final Twilight plugin = Twilight.getInstance();

    @Override
    public void run() {
        if (!MenuSaver.isEmpty()) {
            MenuSaver.getCached().forEach((key, value) -> {
                if (value instanceof AbstractQueueMenu) {
                    Voluntary.ofNull(plugin.getServer().getPlayer(key)).ifPresent(value::update);
                }
            });
        }

        plugin.getQueues().getAll().forEach(queue -> {

            Iterator<AbstractQueueData<?>> iterator = queue.getQueue().iterator();
            while (iterator.hasNext()) {
                AbstractQueueData<?> first = iterator.next();
                if (first == null) {
                    return;
                }

                int index = queue.increase();
                if (index == 5 && first.hasRange()) {
                    first.increaseRange();
                }
                if (index == 0) {
                    first.sendMessage();
                }

                if (!iterator.hasNext()) {
                    return;
                }

                AbstractQueueData<?> second = iterator.next();
                if (second == null) {
                    return;
                }

                if (first.getLoadout() != second.getLoadout()) {
                    return;
                }
                if (first.getQueue() != second.getQueue()) {
                    return;
                }

                QueueCallback callback = first.hasRange() && second.hasRange() ? inRange(first, second) ? queue.start(first, second) : QueueCallback.DENIED : queue.start(first, second);
                if (callback != QueueCallback.NONE) {
                    Arrays.asList(first, second).forEach(data -> data.dequeue(callback));
                    return;
                }
            }
        });
    }

    private boolean inRange(AbstractQueueData<?> first, AbstractQueueData<?> second) {
        return (first.getPlayerPing() >= second.getMinPing() && first.getPlayerPing() <= second.getMaxPing())
                || (second.getPlayerPing() >= first.getMinPing() && second.getPlayerPing() <= first.getMaxPing());
    }
}
