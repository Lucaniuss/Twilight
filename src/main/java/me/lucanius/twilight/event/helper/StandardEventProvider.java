package me.lucanius.twilight.event.helper;

import com.google.common.base.Preconditions;
import me.lucanius.twilight.event.AbstractEvent;
import me.lucanius.twilight.event.EventListener;
import me.lucanius.twilight.event.EventProvider;
import me.lucanius.twilight.event.TwilightEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Clouke
 * @since 25.05.2022 14:22
 * © Twilight - All Rights Reserved
 */
public class StandardEventProvider implements EventProvider {

    private final Map<EventListener, Class<? extends AbstractEvent>> subscribers = new HashMap<>();
    private final ExecutorService thread = Executors.newSingleThreadExecutor();

    @Override
    public void subscribe(Class<? extends AbstractEvent> event, EventListener listener) {
        Preconditions.checkState(!this.subscribers.containsKey(listener), "Event already subscribed");
        this.subscribers.put(listener, event);
    }

    @Override
    public void unsubscribe(EventListener listener) {
        Preconditions.checkState(this.subscribers.containsKey(listener), "Event not subscribed");
        this.subscribers.remove(listener);
    }

    @Override
    public void unsubscribe(Class<? extends AbstractEvent> event) {
        for (Map.Entry<EventListener, Class<? extends AbstractEvent>> entry : this.subscribers.entrySet()) {
            if (entry.getValue().equals(event)) {
                this.subscribers.remove(entry.getKey());
            }
        }
    }

    @Override
    public boolean subbed(EventListener listener) {
        return this.subscribers.containsKey(listener);
    }

    @Override
    public boolean subbed(Class<? extends AbstractEvent> event) {
        return this.subscribers.containsValue(event);
    }

    @Override
    public void publish(TwilightEvent event) {
        this.thread.execute(() -> {
            for (Map.Entry<EventListener, Class<? extends AbstractEvent>> entry : this.subscribers.entrySet()) {
                if (entry.getValue().isInstance(event)) {
                    entry.getKey().onEvent(event);
                }
            }
        });
    }

    @Override
    public <T extends TwilightEvent> void publish(Class<T> event) {
        this.thread.execute(() -> {
            for (Map.Entry<EventListener, Class<? extends AbstractEvent>> entry : this.subscribers.entrySet()) {
                if (entry.getValue().equals(event)) {
                    entry.getKey().onEvent(event);
                }
            }
        });
    }
}
