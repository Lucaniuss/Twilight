package me.lucanius.twilight.tools.events;

import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;

/**
 * @author Lucanius
 * @since May 20, 2022
 */
@FunctionalInterface
public interface EventsExecutor<E> extends EventExecutor {

    void callEvent(E event);

    @Override
    default void execute(Listener listener, Event event) {
        try {
            callEvent((E) event);
        } catch (final ClassCastException ignored) {}
    }
}
