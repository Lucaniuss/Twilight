package me.lucanius.twilight.event.helper;

import com.google.common.base.Preconditions;
import me.lucanius.twilight.event.AbstractEvent;
import me.lucanius.twilight.event.EventListener;
import me.lucanius.twilight.event.TwilightEvent;
import me.lucanius.twilight.event.EventProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Clouke
 * @since 25.05.2022 14:22
 * © Twilight - All Rights Reserved
 */
public class StandardEventProvider implements EventProvider {

    private final List<EventListener> subscribers;
    private final ExecutorService thread;

    public StandardEventProvider() {
        this.subscribers = new ArrayList<>();
        this.thread = Executors.newSingleThreadExecutor();
    }

    @Override
    public void subscribe(EventListener event) {
        Preconditions.checkState(!this.subscribers.contains(event), "Event already subscribed");
        this.subscribers.add(event);
    }

    @Override
    public void subscribe(Class<?> event, EventListener listener) {
        Preconditions.checkState(!this.subscribers.contains(listener), "Event already subscribed");
        this.subscribers.add(listener);
    }

    @Override
    public void unsubscribe(EventListener event) {
        Preconditions.checkState(this.subscribers.remove(event), "Event not subscribed");
        this.subscribers.remove(event);
    }

    @Override
    public void publish(TwilightEvent event) {
        this.thread.execute(() -> this.subscribers.forEach(subscriber -> subscriber.onEvent(event)));
    }

    @Override
    public <T extends TwilightEvent> void publish(Class<T> event) {
        this.thread.execute(() -> this.subscribers.forEach(subscriber -> subscriber.onEvent(event)));
    }
}
