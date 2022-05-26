package me.lucanius.twilight.event.bukkit.listeners;

import me.lucanius.twilight.Twilight;
import me.lucanius.twilight.event.bukkit.Events;
import me.lucanius.twilight.service.game.Game;
import me.lucanius.twilight.service.profile.Profile;
import me.lucanius.twilight.service.profile.ProfileState;
import me.lucanius.twilight.tools.Scheduler;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

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
