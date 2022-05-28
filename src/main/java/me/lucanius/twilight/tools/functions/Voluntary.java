package me.lucanius.twilight.tools.functions;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author Lucanius
 * @since May 28, 2022
 */
public class Voluntary<V> {

    private final static Voluntary<?> EMPTY = new Voluntary<>();

    private final V value;

    private Voluntary() {
        this.value = null;
    }

    private Voluntary(V value) {
        this.value = value;
    }

    public static <V> Voluntary<V> of(V value) {
        return new Voluntary<>(value);
    }

    @SuppressWarnings("unchecked")
    public static <V> Voluntary<V> empty() {
        return (Voluntary<V>) EMPTY;
    }

    public V get() {
        return value;
    }

    public V orElse(V other) {
        return value != null ? value : other;
    }

    public boolean isPresent() {
        return value != null;
    }

    public Voluntary<V> ifPresent(Consumer<? super V> consumer) {
        if (isPresent()) {
            consumer.accept(value);
        }

        return this;
    }

    public void orElseDo(Consumer<? super V> consumer) {
        consumer.accept(value);
    }

    public <U> Voluntary<U> map(Function<? super V, ? extends U> mapper) {
        return isPresent() ? Voluntary.of(mapper.apply(value)) : Voluntary.empty();
    }

    public <U> Voluntary<U> flatMap(Function<? super V, ? extends Voluntary<U>> mapper) {
        return isPresent() ? mapper.apply(value) : Voluntary.empty();
    }
}
