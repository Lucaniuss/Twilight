package me.lucanius.twilight.tools.menu.handlers;

import me.lucanius.twilight.tools.Scheduler;
import me.lucanius.twilight.tools.menu.Button;
import me.lucanius.twilight.tools.menu.Menu;
import me.lucanius.twilight.tools.events.Events;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

/**
 * @author Lucanius
 * @since May 20, 2022
 */
public class MenuListener {

    public MenuListener() {
        Events.subscribe(InventoryClickEvent.class, event -> {
            final Player player = (Player) event.getWhoClicked();
            final UUID uniqueId = player.getUniqueId();
            final Menu openedMenu = MenuSaver.get(uniqueId);
            if (openedMenu == null) {
                return;
            }

            final int slot = event.getSlot();
            final ClickType clickType = event.getClick();
            if (slot != event.getRawSlot()) {
                if ((clickType == ClickType.SHIFT_LEFT || clickType == ClickType.SHIFT_RIGHT)) {
                    event.setCancelled(true);
                }

                return;
            }

            final Button button = openedMenu.getButtonMap().get(slot);
            if (button != null) {
                if (button.getItem(player).isSimilar(Menu.getGlass())) {
                    event.setCancelled(true);
                    return;
                }

                final boolean cancel = button.shouldCancel(player, clickType);
                if (!cancel && (clickType == ClickType.SHIFT_LEFT || clickType == ClickType.SHIFT_RIGHT)) {
                    event.setCancelled(true);
                    final ItemStack currentItem = event.getCurrentItem();
                    if (currentItem != null) {
                        player.getInventory().addItem(currentItem);
                    }
                } else {
                    event.setCancelled(cancel);
                }

                button.onClick(player, clickType);

                final Menu newMenu = MenuSaver.get(uniqueId);
                if (openedMenu.isAutoUpdate()) {
                    openedMenu.open(player);
                } else if (newMenu != null) {
                    newMenu.open(player);
                } else if (button.shouldUpdate(player, clickType)) {
                    openedMenu.update(player);
                }

                if (event.isCancelled()) {
                    Scheduler.runLater(player::updateInventory, 1L);
                }
            } else {
                final InventoryAction action = event.getAction();
                if ((clickType == ClickType.SHIFT_LEFT || clickType == ClickType.SHIFT_RIGHT || action == InventoryAction.MOVE_TO_OTHER_INVENTORY || action == InventoryAction.HOTBAR_MOVE_AND_READD || action == InventoryAction.HOTBAR_SWAP)) {
                    event.setCancelled(true);
                }
            }
        });

        Events.subscribe(InventoryCloseEvent.class, event -> {
            final Player player = (Player) event.getPlayer();
            final Menu openedMenu = MenuSaver.get(player.getUniqueId());
            if (openedMenu != null) {
                openedMenu.onClose(player);
            }
        });
    }
}
