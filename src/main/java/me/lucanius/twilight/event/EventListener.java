package me.lucanius.twilight.event;

/**
 * @author Clouke
 * @since 25.05.2022 14:29
 * © Twilight - All Rights Reserved
 */
public interface EventListener {

    default void onEvent(TwilightEvent event) {}

    default <T extends TwilightEvent> void onEvent(Class<T> event) {}

}
