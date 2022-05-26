package me.lucanius.twilight.event.bukkit.listeners;

import me.lucanius.twilight.Twilight;
import me.lucanius.twilight.event.bukkit.Events;
import me.lucanius.twilight.service.damage.CachedDamage;
import me.lucanius.twilight.service.game.Game;
import me.lucanius.twilight.service.game.context.GameState;
import me.lucanius.twilight.service.loadout.Loadout;
import me.lucanius.twilight.service.loadout.type.LoadoutType;
import me.lucanius.twilight.service.loadout.type.callable.LoadoutTypeCallable;
import me.lucanius.twilight.service.profile.Profile;
import me.lucanius.twilight.service.profile.ProfileState;
import me.lucanius.twilight.service.profile.modules.GameProfile;
import me.lucanius.twilight.tools.CC;
import me.lucanius.twilight.tools.Scheduler;
import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.UUID;

/**
 * @author Lucanius
 * @since May 26, 2022
 */
public class DamageListener {

    private final Twilight plugin = Twilight.getInstance();

    public DamageListener() {
        Events.subscribe(EntityDamageEvent.class, event -> {
            if (!(event.getEntity() instanceof Player)) {
                return;
            }

            Player player = (Player) event.getEntity();
            UUID uniqueId = player.getUniqueId();
            Profile profile = plugin.getProfiles().get(uniqueId);
            switch (profile.getState()) {
                case PLAYING:
                    Game game = plugin.getGames().get(profile);
                    if (game.getState() != GameState.ONGOING) {
                        event.setCancelled(true);
                        return;
                    }

                    switch (event.getCause()) {
                        case VOID:
                            game.getLoadout().getType().getCallable().execute(plugin, player, plugin.getDamages().get(uniqueId), game);
                            break;
                        case FALL:
                            event.setCancelled(game.getLoadout().isNoFall());
                            break;
                    }
                    break;
                case SPECTATING:
                    // Teleport to the game location
                    break;
                default:
                    if (event.getCause() == EntityDamageEvent.DamageCause.VOID) {
                        plugin.getLobby().toLobby(player, profile, true);
                    }

                    event.setCancelled(true);
                    break;
            }
        });

        Events.subscribe(EntityDamageByEntityEvent.class, event -> {
            if (!(event.getEntity() instanceof Player)) {
                return;
            }

            Entity entity = event.getDamager();
            Player victim = (Player) event.getEntity();
            Player damager = (entity instanceof Player
                    ? (Player) entity : entity instanceof Arrow
                    ? (Player) ((Projectile) entity).getShooter() : null
            );
            if (damager == null) {
                return;
            }

            UUID uniqueId = victim.getUniqueId();
            Profile victimProfile = plugin.getProfiles().get(uniqueId);
            Profile damagerProfile = plugin.getProfiles().get(damager.getUniqueId());
            if (victimProfile == null || damagerProfile == null) {
                event.setCancelled(true);
                return;
            }

            ProfileState victimState = victimProfile.getState();
            if (victimState != damagerProfile.getState()) {
                event.setCancelled(true);
                return;
            }

            if (victimState != ProfileState.PLAYING) { // no need to check state of damager because it is already the same
                event.setCancelled(true);
                return;
            }

            boolean hasCached = plugin.getDamages().has(uniqueId);
            CachedDamage cachedDamage = hasCached ? plugin.getDamages().retrieve(uniqueId) : new CachedDamage();
            cachedDamage.setDamager(damager);
            cachedDamage.setTimeStamp(System.currentTimeMillis());
            if (!hasCached) {
                plugin.getDamages().cache(uniqueId, cachedDamage);
            }

            Game game = plugin.getGames().get(victimProfile);
            if (game == null) {
                event.setCancelled(true);
                return;
            }

            if (game.getState() != GameState.ONGOING) {
                event.setCancelled(true);
                return;
            }

            GameProfile victimGameProfile = victimProfile.getGameProfile();
            GameProfile damagerGameProfile = damagerProfile.getGameProfile();
            if (victimGameProfile.getTeam() == damagerGameProfile.getTeam()) {
                event.setCancelled(true);
                return;
            }

            Loadout loadout = game.getLoadout();
            if (loadout.isNoDamage()) {
                event.setDamage(0.0d);
            }

            victimGameProfile.getDamage();
            damagerGameProfile.giveDamage();

            double health = Math.ceil(victim.getHealth() - event.getFinalDamage());
            LoadoutType type = loadout.getType();
            if ((type == LoadoutType.BOXING && damagerGameProfile.getHits() >= 100) || !(health > 0)) {
                event.setDamage(0.0d);
                event.setCancelled(true);

                type.getCallable().execute(plugin, victim, damager, game);
                return;
            }

            if (entity instanceof Arrow) {
                damager.sendMessage(CC.MAIN + victim.getName() + CC.SECOND + " is now on " + CC.DARK_RED + health + "❤" + CC.SECOND + ".");
            }
        });

        Events.subscribe(PlayerDeathEvent.class, event -> {
            event.getDrops().clear();

            Player victim = event.getEntity();
            UUID uniqueId = victim.getUniqueId();
            Profile profile = plugin.getProfiles().get(uniqueId);
            if (profile.getState() != ProfileState.PLAYING) {
                return;
            }

            Game game = plugin.getGames().get(profile);
            if (game == null) {
                return;
            }

            Player killer = victim.getKiller();
            LoadoutType type = game.getLoadout().getType();
            LoadoutTypeCallable callable = type.getCallable();
            if (type != LoadoutType.BRIDGES) {
                callable.execute(plugin, victim, killer, game);
                return;
            }

            Location location = victim.getLocation();
            Scheduler.runLater(() -> { // prevent an error that occurs when the player dies in an unnatural way
                victim.spigot().respawn();
                victim.teleport(location);
                callable.execute(plugin, victim, killer, game);
            }, 1L);
        });
    }
}
