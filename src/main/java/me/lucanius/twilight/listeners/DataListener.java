package me.lucanius.twilight.listeners;

import me.lucanius.twilight.Twilight;
import me.lucanius.twilight.service.profile.Profile;
import me.lucanius.twilight.tools.CC;
import me.lucanius.twilight.tools.Scheduler;
import me.lucanius.twilight.tools.events.Events;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

/**
 * @author Lucanius
 * @since May 20, 2022
 */
public class DataListener {

    private final Twilight plugin = Twilight.getInstance();

    public DataListener() {
        Events.subscribe(AsyncPlayerPreLoginEvent.class, event -> plugin.getProfiles().getOrCreate(event.getUniqueId()).load());

        Events.subscribe(PlayerJoinEvent.class, event -> {
            final Player player = event.getPlayer();
            final UUID uniqueId = player.getUniqueId();
            final Profile profile = plugin.getProfiles().get(uniqueId);
            if (profile == null) {
                player.sendMessage(CC.RED + "Contact an administrator immediately or try to re-log!");
                return;
            }

            if (!profile.isLoaded()) {
                Scheduler.runAsync(profile::load);
            }
        });
    }
}
