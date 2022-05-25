package me.lucanius.twilight.tools.functions;

import com.google.common.cache.Cache;
import com.google.common.collect.ImmutableMap;

import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

/**
 * @author Clouke
 * @since 25.05.2022 12:35
 * © Twilight - All Rights Reserved
 */
public interface Cacheable<K, V> {

    Cacheable<K, V> build(long duration, TimeUnit unit);

    void put(K k, V v);

    @SuppressWarnings("UnstableApiUsage")
    Cache<K, V> get();

    V get(K k, Callable<? extends V> v);

    V getIfPresent(K k);

    ImmutableMap<K, V> getAllPresent();

    ConcurrentMap<K, V> asMap();

    void cleanUp();

    void invalidate(Object o);

    default boolean isCached(K k) {
        return getIfPresent(k) != null;
    }

}
