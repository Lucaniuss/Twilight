package me.lucanius.twilight.event;

/**
 * @author Clouke
 * @since 25.05.2022 14:23
 * © Twilight - All Rights Reserved
 */
public interface EventProvider {

    void subscribe(EventListener abstractEvent);

    void subscribe(Class<?> event, EventListener listener);

    void unsubscribe(EventListener abstractEvent);

    void publish(TwilightEvent abstractEvent);

    <T extends TwilightEvent> void publish(Class<T> event);

}
