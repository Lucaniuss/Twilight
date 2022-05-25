package me.lucanius.twilight.service.queue.abstr;

import lombok.Getter;
import me.lucanius.twilight.Twilight;
import me.lucanius.twilight.service.loadout.Loadout;
import me.lucanius.twilight.service.queue.callback.QueueCallback;
import me.lucanius.twilight.service.queue.menu.abstr.AbstractQueueMenu;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Lucanius
 * @since May 22, 2022
 */
@Getter
public abstract class AbstractQueue<E> {

    protected final static Twilight plugin = Twilight.getInstance();

    private final List<AbstractQueueData<?>> queue;
    private final String name;

    private int index;
    protected AbstractQueueMenu menu;

    public AbstractQueue(String name) {
        this.queue = new ArrayList<>();
        this.name = name;

        this.index = 0;
    }

    public abstract void enqueue(E element, Loadout loadout);

    public abstract void dequeue(AbstractQueueData<?> data, QueueCallback callback);

    public abstract QueueCallback start(AbstractQueueData<?> first, AbstractQueueData<?> second);

    public int increase() {
        return index = index >= 10 ? 0 : index + 1;
    }

    public void add(AbstractQueueData<?> data) {
        queue.add(data);
    }

    public void remove(AbstractQueueData<?> data) {
        queue.remove(data);
    }

    public boolean contains(AbstractQueueData<?> data) {
        return queue.contains(data);
    }
}
