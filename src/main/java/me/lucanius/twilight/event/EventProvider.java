package me.lucanius.twilight.event;

/**
 * @author Clouke
 * @since 25.05.2022 14:23
 * © Twilight - All Rights Reserved
 */
public interface EventProvider {

    void subscribe(Class<? extends AbstractEvent> event, EventListener listener);

    void unsubscribe(EventListener abstractEvent);

    void unsubscribe(Class<? extends AbstractEvent> event);

    boolean subbed(EventListener listener);

    boolean subbed(Class<? extends AbstractEvent> event);

    void publish(TwilightEvent abstractEvent);

    <T extends TwilightEvent> void publish(Class<T> event);

    boolean execute(Runnable runnable);

}
