package me.lucanius.twilight.event;

import me.lucanius.twilight.Twilight;
import me.lucanius.twilight.event.EventListener;
import me.lucanius.twilight.event.TwilightEvent;

/**
 * @author Lucanius
 * @since May 25, 2022
 */
@FunctionalInterface
public interface TwilightExecutor<E> extends EventListener {

    Twilight plugin = Twilight.getInstance();

    void callEvent(E event);

    @Override
    default void onEvent(TwilightEvent event) {
        try {
            callEvent((E) event);
        } catch (final ClassCastException ignored) {}
    }
}
