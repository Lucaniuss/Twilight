package me.lucanius.twilight.event.bukkit.listeners;

import me.lucanius.twilight.Twilight;
import me.lucanius.twilight.event.bukkit.Events;
import me.lucanius.twilight.service.cooldown.Cooldown;
import me.lucanius.twilight.service.game.Game;
import me.lucanius.twilight.service.game.context.GameState;
import me.lucanius.twilight.service.loadout.Loadout;
import me.lucanius.twilight.service.loadout.personal.PersonalLoadout;
import me.lucanius.twilight.service.lobby.hotbar.HotbarItem;
import me.lucanius.twilight.service.lobby.hotbar.context.HotbarContext;
import me.lucanius.twilight.service.party.Party;
import me.lucanius.twilight.service.profile.Profile;
import me.lucanius.twilight.tools.CC;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Lucanius
 * @since May 25, 2022
 */
public class InteractListener {

    private final Twilight plugin = Twilight.getInstance();

    public InteractListener() {
        Events.subscribe(PlayerInteractEvent.class, event -> {
            Action action = event.getAction();
            if ((action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK)) {
                return;
            }

            Player player = event.getPlayer();
            ItemStack stack = player.getItemInHand();
            if (stack == null) {
                return;
            }

            UUID uniqueId = player.getUniqueId();
            Profile profile = plugin.getProfiles().get(uniqueId);
            if (profile == null) {
                return;
            }

            HotbarItem item = plugin.getLobby().get(stack);
            boolean nullItem = item == null;

            event.setCancelled(!nullItem);
            Optional<Game> optionalGame = Optional.ofNullable(plugin.getGames().get(profile));
            Optional<Party> party = Optional.ofNullable(plugin.getParties().getParty(uniqueId));
            switch (profile.getState()) {
                case LOBBY:
                    if (nullItem) {
                        return;
                    }

                    switch (item.getContext()) {
                        case UNRANKED:
                            plugin.getQueues().getUnranked().getMenu().open(player);
                            break;
                        case RANKED:
                            plugin.getQueues().getRanked().getMenu().open(player);
                            break;
                        case LOADOUT_EDITOR:
                            break;
                        case CREATE_PARTY:
                            if (party.isPresent()) {
                                player.sendMessage(CC.RED + "You are already in a party!");
                                return;
                            }

                            plugin.getParties().createParty(player);
                            break;
                        case LEADERBOARDS:
                            break;
                        case PERSONAL_SETTINGS:
                            break;
                        case DUOS:
                            if (!party.isPresent() || !party.get().getLeader().equals(uniqueId)) {
                                player.sendMessage(CC.RED + "You are not the leader of your party!");
                                return;
                            }

                            plugin.getQueues().getDuos().getMenu().open(player);
                            break;
                        case PARTY_GAMES:
                            break;
                        case OTHER_PARTIES:
                            break;
                        case PARTY_INFO:
                            break;
                        case LEAVE_PARTY:
                            if (plugin.getParties().leaveParty(player) == null) {
                                player.sendMessage(CC.RED + "You are not in a party!");
                            }
                            break;
                    }
                    break;
                case PLAYING:
                    if (!optionalGame.isPresent()) {
                        return;
                    }

                    Game game = optionalGame.get();
                    switch (stack.getType()) {
                        case MUSHROOM_SOUP:
                            double health = player.getHealth();
                            if (!player.isDead() && health < 19.0d) {
                                player.setHealth(Math.min(health + 7.0d, 20.0d));
                                stack.setType(Material.BOWL);
                                player.updateInventory();
                                return;
                            }
                            break;
                        case POTION:
                            if (stack.getDurability() == 16421 && game.getState() == GameState.STARTING) {
                                event.setCancelled(true);
                                player.updateInventory();
                                player.sendMessage(CC.RED + "You can't do this while the game isn't ongoing!");
                                return;
                            }
                            break;
                        case ENDER_PEARL:
                            if (game.getState() == GameState.STARTING) {
                                event.setCancelled(true);
                                player.updateInventory();
                                player.sendMessage(CC.RED + "You can't do this while the game isn't ongoing!");
                                return;
                            }

                            Cooldown cooldown = plugin.getCooldowns().get(uniqueId, "ENDERPEARL");
                            if (cooldown == null) {
                                cooldown = new Cooldown(15 * 1000L, () -> player.sendMessage(CC.GREEN + "You can now use pearls again!"));
                                plugin.getCooldowns().add(uniqueId, "ENDERPEARL", cooldown);
                            }

                            if (cooldown.active()) {
                                event.setCancelled(true);
                                player.updateInventory();
                                player.sendMessage(CC.RED + "You can't use pearls for another " + cooldown.remaining() + " seconds!");
                                return;
                            }

                            cooldown.reset();
                            break;
                        case BOW:
                            Optional.ofNullable(plugin.getCooldowns().get(uniqueId, "BRIDGES")).ifPresent(cd -> {
                                if (cd.active()) {
                                    event.setCancelled(true);
                                    player.updateInventory();
                                    player.sendMessage(CC.RED + "You shoot arrows for another " + cd.remaining() + " seconds!");
                                }
                            });
                            break;
                    }

                    if (!nullItem && item.getContext() == HotbarContext.DEFAULT_BOOK) {
                        game.getLoadout().apply(player, profile);
                        player.sendMessage(CC.SECOND + "Successfully equipped the default loadout!");
                        return;
                    }

                    if (!stack.getType().equals(Material.ENCHANTED_BOOK) || !stack.hasItemMeta() || !stack.getItemMeta().hasDisplayName()) {
                        return;
                    }

                    Loadout loadout = game.getLoadout();
                    PersonalLoadout personal = Arrays.stream(profile.getPersonalLoadouts(loadout.getName()))
                            .filter(Objects::nonNull).filter(l -> CC.translate(l.getDisplayName()).equalsIgnoreCase(stack.getItemMeta().getDisplayName()))
                            .findFirst().orElse(null);
                    if (personal != null) {
                        loadout.apply(player, profile, personal);
                    } else {
                        loadout.apply(player, profile);
                    }
                    break;
                case QUEUE:
                    if (nullItem) {
                        return;
                    }

                    if (item.getContext() == HotbarContext.LEAVE_QUEUE) {
                        plugin.getQueues().getData(uniqueId).dequeue();
                    }
                    break;
                case SPECTATING:
                    if (nullItem) {
                        return;
                    }

                    switch (item.getContext()) {
                        case VIEW_PLAYERS:
                            break;
                        case STOP_SPECTATING:
                            optionalGame.ifPresent(value -> value.removeSpectator(uniqueId));
                            break;
                    }
                    break;
            }
        });
    }
}
