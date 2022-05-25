package me.lucanius.twilight.event.helper;

import com.google.common.base.Preconditions;
import me.lucanius.twilight.event.EventListener;
import me.lucanius.twilight.event.TwilightEvent;
import me.lucanius.twilight.event.EventProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Clouke
 * @since 25.05.2022 14:22
 * © Twilight - All Rights Reserved
 */
public class StandardEventProvider implements EventProvider {

    private final List<EventListener> subscribers;

    public StandardEventProvider() {
        this.subscribers = new ArrayList<>();
    }

    @Override
    public void subscribe(EventListener event) {
        Preconditions.checkState(!this.subscribers.contains(event), "Event already subscribed");
        this.subscribers.add(event);
    }

    @Override
    public void unsubscribe(EventListener event) {
        Preconditions.checkState(this.subscribers.remove(event), "Event not subscribed");
        this.subscribers.remove(event);
    }

    @Override
    public void publish(TwilightEvent event) {
        this.subscribers.forEach(subscriber -> subscriber.onEvent(event));
    }

    @Override
    public <T extends TwilightEvent> void publish(Class<T> event) {
        this.subscribers.forEach(subscriber -> subscriber.onEvent(event));
    }
}
