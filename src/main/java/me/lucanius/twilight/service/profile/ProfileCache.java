package me.lucanius.twilight.service.profile;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.ImmutableMap;
import me.lucanius.twilight.tools.functions.Cacheable;

import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author Clouke
 * @since 25.05.2022 12:34
 * © Twilight - All Rights Reserved
 */

@SuppressWarnings("UnstableApiUsage")
public class ProfileCache implements Cacheable<UUID, Profile> {

    private Cache<UUID, Profile> profiles;

    @Override
    public Cacheable<UUID, Profile> build(long duration, TimeUnit unit) {
        this.profiles = CacheBuilder.newBuilder().expireAfterWrite(duration, TimeUnit.MINUTES).build();
        return this;
    }

    @Override
    public void put(UUID uuid, Profile profile) {
        this.profiles.put(uuid, profile);
    }

    @Override
    public Cache<UUID, Profile> get() {
        return this.profiles;
    }

    @Override
    public Profile get(UUID uuid, Callable<? extends Profile> v) {
        try {
            return this.profiles.get(uuid, v);
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Profile getIfPresent(UUID uuid) {
        return this.profiles.getIfPresent(uuid);
    }

    @Override
    public ImmutableMap<UUID, Profile> getAllPresent() {
        return null;
    }

    @Override
    public ConcurrentMap<UUID, Profile> asMap() {
        return this.profiles.asMap();
    }

    @Override
    public void cleanUp() {
        this.profiles.cleanUp();
    }

    @Override
    public void invalidate(Object o) {
        this.profiles.invalidate(o);
    }
}
