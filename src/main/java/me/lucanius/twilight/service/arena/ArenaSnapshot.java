package me.lucanius.twilight.service.arena;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.ImmutableMap;
import me.lucanius.twilight.tools.functions.Cacheable;
import org.bukkit.ChunkSnapshot;

import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author Clouke
 * @since 25.05.2022 13:10
 * © Twilight - All Rights Reserved
 */

@SuppressWarnings("UnstableApiUsage")
public class ArenaSnapshot implements Cacheable<Integer, ChunkSnapshot> {

    private Cache<Integer, ChunkSnapshot> cache;

    @Override
    public Cacheable<Integer, ChunkSnapshot> build(long duration, TimeUnit unit) {
        this.cache = CacheBuilder.newBuilder().expireAfterWrite(duration, TimeUnit.MINUTES).build();
        return this;
    }

    @Override
    public void put(Integer integer, ChunkSnapshot chunkSnapshot) {
        this.cache.put(integer, chunkSnapshot);
    }

    @Override
    public Cache<Integer, ChunkSnapshot> get() {
        return this.cache;
    }

    @Override
    public ChunkSnapshot get(Integer integer, Callable<? extends ChunkSnapshot> v) {
        try {
            return this.cache.get(integer, v);
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public ChunkSnapshot getIfPresent(Integer integer) {
        return this.cache.getIfPresent(integer);
    }

    @Override
    public ImmutableMap<Integer, ChunkSnapshot> getAllPresent() {
        return null;
    }

    @Override
    public ConcurrentMap<Integer, ChunkSnapshot> asMap() {
        return this.cache.asMap();
    }

    @Override
    public void cleanUp() {
        this.cache.cleanUp();
    }

    @Override
    public void invalidate(Object o) {
        this.cache.invalidate(o);
    }
}
