package me.lucanius.twilight.event.bukkit.listeners;

import me.lucanius.twilight.Twilight;
import me.lucanius.twilight.event.bukkit.Events;
import me.lucanius.twilight.service.cooldown.Cooldown;
import me.lucanius.twilight.service.game.Game;
import me.lucanius.twilight.service.loadout.type.LoadoutType;
import me.lucanius.twilight.service.profile.Profile;
import me.lucanius.twilight.service.profile.ProfileState;
import me.lucanius.twilight.tools.CC;
import me.lucanius.twilight.tools.Scheduler;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

/**
 * @author Lucanius
 * @since May 25, 2022
 */
public class MainListener {

    private final Twilight plugin = Twilight.getInstance();

    public MainListener() {
        Events.subscribe(PlayerDropItemEvent.class, event -> {
            Player player = event.getPlayer();
            Profile profile = plugin.getProfiles().get(player.getUniqueId());
            if (profile.getEditorProfile().isEditing()) {
                event.getItemDrop().remove();
                return;
            }

            if (profile.getState() != ProfileState.PLAYING) {
                event.setCancelled(true);
                return;
            }

            Game game = plugin.getGames().get(profile);
            Material drop = event.getItemDrop().getItemStack().getType();
            boolean droppable = drop.name().contains("_SWORD") || drop.name().contains("_AXE") || drop.name().contains("_SPADE") || drop.name().contains("_PICKAXE") || drop == Material.BOW || drop == Material.ENCHANTED_BOOK || drop == Material.MUSHROOM_SOUP;
            if (game == null || game.getLoadout().isNoDrop() || droppable) {
                event.setCancelled(true);
                return;
            }

            if (drop.equals(Material.GLASS_BOTTLE) || drop.equals(Material.BOWL)) {
                event.getItemDrop().remove();
                return;
            }

            game.getDroppedItems().add(event.getItemDrop());
        });

        Events.subscribe(PlayerPickupItemEvent.class, event -> {
            Player player = event.getPlayer();
            Profile profile = plugin.getProfiles().get(player.getUniqueId());
            if (profile.getState() != ProfileState.PLAYING) {
                event.setCancelled(true);
                return;
            }

            Game game = plugin.getGames().get(profile);
            Item item = event.getItem();
            if (game == null || !game.getDroppedItems().contains(item)) {
                event.setCancelled(true);
                return;
            }

            game.getDroppedItems().remove(item);
        });

        Events.subscribe(FoodLevelChangeEvent.class, event -> {
            Player player = (Player) event.getEntity();
            Profile profile = plugin.getProfiles().get(player.getUniqueId());
            if (profile.getState() != ProfileState.PLAYING) {
                event.setCancelled(true);
                return;
            }

            Game game = plugin.getGames().get(profile);
            event.setCancelled(game != null && game.getLoadout().isNoHunger());
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

        Events.subscribe(ProjectileLaunchEvent.class, event -> {
            Projectile projectile = event.getEntity();
            if (!(projectile.getShooter() instanceof Player)) {
                return;
            }

            Player player = (Player) projectile.getShooter();
            UUID uniqueId = player.getUniqueId();
            Profile profile = plugin.getProfiles().get(uniqueId);
            if (profile.getState() != ProfileState.PLAYING) {
                return;
            }

            Game game = plugin.getGames().get(profile);
            if (game == null) {
                return;
            }

            game.getDroppedItems().add(projectile);
            if (game.getLoadout().getType() != LoadoutType.BRIDGES || !(projectile instanceof Arrow)) {
                return;
            }

            Cooldown cooldown = plugin.getCooldowns().get(uniqueId, "BRIDGES");
            if (cooldown == null) {
                cooldown = new Cooldown(4 * 1000L, () -> {
                    if (!player.getInventory().contains(Material.ARROW)) {
                        player.getInventory().addItem(new ItemStack(Material.ARROW));
                        player.sendMessage(CC.GREEN + "You can now use your bow again!");
                        player.playSound(player.getLocation(), Sound.CHICKEN_EGG_POP, 10f, 1f);
                    }
                });
                plugin.getCooldowns().add(uniqueId, "BRIDGES", cooldown);
            }

            if (cooldown.active()) {
                event.setCancelled(true);
                player.updateInventory();
                return;
            }

            cooldown.reset();
        });

        Events.subscribe(ProjectileHitEvent.class, event -> {
            Projectile projectile = event.getEntity();
            if (!(projectile.getShooter() instanceof Player)) {
                return;
            }

            Player player = (Player) projectile.getShooter();
            UUID uniqueId = player.getUniqueId();
            Profile profile = plugin.getProfiles().get(uniqueId);
            if (profile.getState() != ProfileState.PLAYING) {
                return;
            }

            Game game = plugin.getGames().get(profile);
            if (game == null) {
                return;
            }

            game.getDroppedItems().remove(projectile);
            if (event.getEntityType() == EntityType.ARROW) {
                projectile.remove();
            }
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
