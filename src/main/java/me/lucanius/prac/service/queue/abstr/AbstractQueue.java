package me.lucanius.prac.service.queue.abstr;

import lombok.Getter;
import me.lucanius.prac.Twilight;
import me.lucanius.prac.service.loadout.Loadout;
import me.lucanius.prac.service.queue.callback.QueueCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Lucanius
 * @since May 22, 2022
 */
@Getter
public abstract class AbstractQueue<E> {

    protected final static Twilight plugin = Twilight.getInstance();

    private final List<AbstractQueueData<?>> queue = new ArrayList<>();
    private int index = 0;

    public abstract void enqueue(E element, Loadout loadout);

    public abstract void dequeue(AbstractQueueData<?> data);

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
