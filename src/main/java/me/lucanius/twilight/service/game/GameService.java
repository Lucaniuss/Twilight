package me.lucanius.twilight.service.game;

import me.lucanius.twilight.Twilight;
import me.lucanius.twilight.event.events.GameStartEvent;
import me.lucanius.twilight.service.loadout.Loadout;
import me.lucanius.twilight.service.loadout.personal.PersonalLoadout;
import me.lucanius.twilight.service.loadout.type.LoadoutType;
import me.lucanius.twilight.service.lobby.hotbar.context.HotbarContext;
import me.lucanius.twilight.service.profile.Profile;
import me.lucanius.twilight.service.queue.abstr.AbstractQueue;
import me.lucanius.twilight.tools.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Lucanius
 * @since May 25, 2022
 */
public class GameService {

    private final Twilight plugin;
    private final Map<UUID, Game> games;

    public GameService(Twilight plugin) {
        this.plugin = plugin;
        this.games = new HashMap<>();
    }

    public boolean startGame(Game game) {
        GameStartEvent event = new GameStartEvent(game);
        if (event.isCancelled()) {
            return false;
        }

        games.put(game.getUniqueId(), game);
        return true;
    }

    public void removeGame(Game game) {
        games.remove(game.getUniqueId());
    }

    public Game get(UUID uniqueId) {
        return games.get(uniqueId);
    }

    public Game get(Profile profile) {
        return games.get(profile.getGameProfile().getGameId());
    }

    public Game get(Player player) {
        return games.get(plugin.getProfiles().get(player.getUniqueId()).getGameProfile().getGameId());
    }

    public Collection<Game> getAll() {
        return games.values();
    }

    public int getSize() {
        return games.values()
                .stream()
                .map(Game::getEveryone)
                .map(Collection::size)
                .reduce(0, Integer::sum);
    }

    public int getSize(Loadout loadout, AbstractQueue<?> queue) {
        return games.values()
                .stream()
                .filter(game -> game.getLoadout().equals(loadout) && game.getQueue().equals(queue))
                .map(Game::getEveryone)
                .map(Collection::size)
                .reduce(0, Integer::sum);
    }

    public boolean needsMovement() {
        return games.values().stream().anyMatch(game -> game.getLoadout().needsMovement());
    }

    public void giveLoadouts(Player player, Profile profile, Loadout loadout) {
        if (loadout.getType() == LoadoutType.SUMO) {
            return;
        }

        ItemStack defaultBook = plugin.getLobby().get(HotbarContext.DEFAULT_BOOK).getItem();
        if (profile.hasPersonalLoadouts(loadout.getName())) {
            int i = -2;
            for (PersonalLoadout personalLoadout : profile.getPersonalLoadouts(loadout.getName())) {
                if (personalLoadout != null) {
                    player.getInventory().setItem(i += 2, new ItemBuilder(Material.ENCHANTED_BOOK).setName(personalLoadout.getDisplayName()).build());
                    player.getInventory().setItem(8, defaultBook);
                }
            }
        } else {
            player.getInventory().setItem(0, defaultBook);
        }

        player.updateInventory();
    }
}
