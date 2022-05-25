package me.lucanius.twilight.event;

/**
 * @author Lucanius
 * @since May 25, 2022
 */
@FunctionalInterface
public interface TwilightExecutor<E> extends EventListener {

    void callEvent(E event);

    @Override
    default void onEvent(TwilightEvent event) {
        try {
            callEvent((E) event);
        } catch (final ClassCastException ignored) {}
    }
}
