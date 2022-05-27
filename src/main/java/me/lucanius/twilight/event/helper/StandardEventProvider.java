package me.lucanius.twilight.event.helper;

import me.lucanius.twilight.event.AbstractEvent;
import me.lucanius.twilight.event.EventListener;
import me.lucanius.twilight.event.EventProvider;
import me.lucanius.twilight.event.TwilightEvent;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Clouke
 * @since 25.05.2022 14:22
 * © Twilight - All Rights Reserved
 */
public class StandardEventProvider implements EventProvider {

    private final Map<EventListener, Class<? extends AbstractEvent>> subscribers = new ConcurrentHashMap<>();
    private final ExecutorService thread = Executors.newSingleThreadExecutor();

    @Override
    public void subscribe(Class<? extends AbstractEvent> event, EventListener listener) {
        if (!subscribers.containsKey(listener)) {
            subscribers.put(listener, event);
        }
    }

    @Override
    public void unsubscribe(EventListener listener) {
        subscribers.remove(listener);
    }

    @Override
    public void unsubscribe(Class<? extends AbstractEvent> event) {
        if (subscribers.containsValue(event)) {
            subscribers.keySet().stream().filter(listener -> subscribers.get(listener).equals(event)).forEach(subscribers::remove);
        }
    }

    @Override
    public boolean subbed(EventListener listener) {
        return subscribers.containsKey(listener);
    }

    @Override
    public boolean subbed(Class<? extends AbstractEvent> event) {
        return subscribers.containsValue(event);
    }

    @Override
    public void publish(TwilightEvent event) {
        this.thread.execute(() -> subscribers.entrySet().stream()
                .filter(entry -> entry.getValue().isInstance(event))
                .forEach(entry -> entry.getKey().onEvent(event))
        );
    }

    @Override
    public <T extends TwilightEvent> void publish(Class<T> event) {
        this.thread.execute(() -> subscribers.entrySet().stream()
                .filter(entry -> entry.getValue().equals(event))
                .forEach(entry -> entry.getKey().onEvent(event))
        );
    }
}
