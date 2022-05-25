package me.lucanius.twilight.service.profile;

import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * @author Clouke
 * @since 25.05.2022 13:13
 * © Twilight - All Rights Reserved
 */
public interface ProfileService {

    Profile getOrCreate(UUID uniqueId);

    Profile get(UUID uniqueId);

    Profile getOffline(UUID uniqueId);

    CompletableFuture<Profile> getAsync(UUID uniqueId);

    Profile getDummy();

    ProfileCache getCache();

    Collection<Profile> getAll();

    void remove(UUID uniqueId);

}
