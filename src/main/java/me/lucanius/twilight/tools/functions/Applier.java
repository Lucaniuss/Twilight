package me.lucanius.twilight.tools.functions;

/**
 * @author Clouke
 * @since 25.05.2022 13:24
 * © Twilight - All Rights Reserved
 */
public interface Applier<V> {

    V get();

    V apply(V value);

}
