package me.lucanius.twilight.listeners;

import me.lucanius.twilight.Twilight;
import me.lucanius.twilight.service.lobby.hotbar.HotbarContext;
import me.lucanius.twilight.service.lobby.hotbar.HotbarItem;
import me.lucanius.twilight.service.profile.Profile;
import me.lucanius.twilight.tools.events.Events;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

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
            switch (profile.getState()) {
                case LOBBY:
                    if (nullItem) {
                        return;
                    }

                    switch (item.getContext()) {
                        case UNRANKED:
                            plugin.getQueues().getSoloQueue().getMenu().open(player);
                            break;
                        case RANKED:
                            break;
                    }
                    break;
                case PLAYING:
                    break;
                case QUEUE:
                    if (nullItem) {
                        return;
                    }

                    if (item.getContext() == HotbarContext.LEAVE_QUEUE) {
                        plugin.getQueues().getData(uniqueId).dequeue();
                    }
                    break;
            }
        });
    }
}
