package me.lucanius.twilight.event.bukkit.listeners;

import me.lucanius.twilight.Twilight;
import me.lucanius.twilight.service.profile.Profile;
import me.lucanius.twilight.service.profile.ProfileState;
import me.lucanius.twilight.tools.Scheduler;
import me.lucanius.twilight.event.bukkit.Events;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

/**
 * @author Lucanius
 * @since May 25, 2022
 */
public class MainListener {

    private final Twilight plugin = Twilight.getInstance();

    public MainListener() {
        Events.subscribe(EntityDamageEvent.class, event -> {
            if (!(event.getEntity() instanceof Player)) {
                return;
            }

            Player player = (Player) event.getEntity();
            Profile profile = plugin.getProfiles().get(player.getUniqueId());
            if (profile.getState() != ProfileState.PLAYING) {
                if (event.getCause() == EntityDamageEvent.DamageCause.VOID) {
                    plugin.getLobby().toLobby(player, profile, true);
                }

                event.setCancelled(true);
                return;
            }

            // game damage event
        });

        Events.subscribe(EntityDamageByEntityEvent.class, event -> {

        });

        Events.subscribe(BlockBreakEvent.class, event -> {
            Player player = event.getPlayer();
            Profile profile = plugin.getProfiles().get(player.getUniqueId());
            if (profile.getState() != ProfileState.PLAYING) {
                event.setCancelled(player.getGameMode() != GameMode.CREATIVE);
                return;
            }

            // game block break event
        });

        Events.subscribe(BlockPlaceEvent.class, event -> {
            Player player = event.getPlayer();
            Profile profile = plugin.getProfiles().get(player.getUniqueId());
            if (profile.getState() != ProfileState.PLAYING) {
                event.setCancelled(player.getGameMode() != GameMode.CREATIVE);
                return;
            }

            // game block break event
        });

        Events.subscribe(PlayerDropItemEvent.class, event -> {
            Player player = event.getPlayer();
            Profile profile = plugin.getProfiles().get(player.getUniqueId());
            if (profile.getState() != ProfileState.PLAYING) {
                event.setCancelled(true);
                return;
            }

            // event setcancelled when loadout of game is noDrop
        });

        Events.subscribe(FoodLevelChangeEvent.class, event -> {
            Player player = (Player) event.getEntity();
            Profile profile = plugin.getProfiles().get(player.getUniqueId());
            if (profile.getState() != ProfileState.PLAYING) {
                event.setCancelled(true);
                return;
            }

            // event setcancelled when loadout of game is noHunger
        });

        Events.subscribe(PotionSplashEvent.class, event -> {
            if (!(event.getEntity().getShooter() instanceof Player)) {
                return;
            }

            Player player = (Player) event.getEntity().getShooter();
            Profile profile = plugin.getProfiles().get(player.getUniqueId());
            if (profile.getState() != ProfileState.PLAYING) {
                return;
            }

            profile.getGameProfile().throwPotion(event.getIntensity(player) <= 0.5d);
        });

        Events.subscribe(ItemSpawnEvent.class, event -> Scheduler.runLaterAsync(() -> event.getEntity().remove(), 20L * 10L));
        Events.subscribe(WeatherChangeEvent.class, event -> event.setCancelled(event.toWeatherState()));
        Events.subscribe(BlockIgniteEvent.class, event -> event.setCancelled(true));
        Events.subscribe(CreatureSpawnEvent.class, event -> event.setCancelled(true));
        Events.subscribe(LeavesDecayEvent.class, event -> event.setCancelled(true));
        Events.subscribe(HangingBreakEvent.class, event -> event.setCancelled(true));
        Events.subscribe(BlockBurnEvent.class, event -> event.setCancelled(true));
        Events.subscribe(BlockSpreadEvent.class, event -> event.setCancelled(true));
    }
}
