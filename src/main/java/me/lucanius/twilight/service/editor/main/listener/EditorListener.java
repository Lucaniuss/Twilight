package me.lucanius.twilight.service.editor.main.listener;

import me.lucanius.twilight.Twilight;
import me.lucanius.twilight.event.bukkit.Events;
import me.lucanius.twilight.service.editor.main.EditorMenu;
import me.lucanius.twilight.service.editor.overview.EditorOverviewMenu;
import me.lucanius.twilight.service.loadout.Loadout;
import me.lucanius.twilight.service.loadout.personal.PersonalLoadout;
import me.lucanius.twilight.service.profile.modules.EditorProfile;
import me.lucanius.twilight.tools.Scheduler;
import me.lucanius.twilight.tools.Tools;
import me.lucanius.twilight.tools.menu.Menu;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * @author Lucanius
 * @since May 27, 2022
 */
public class EditorListener {

    private final Twilight plugin = Twilight.getInstance();
    private final ItemStack glass = Menu.getGlass();

    public EditorListener() {
        Events.subscribe(InventoryClickEvent.class, event -> {
            Inventory inventory = event.getView().getTopInventory();
            if (inventory == null) {
                return;
            }
            if (inventory.getHolder() == null) {
                return;
            }
            if (!(inventory.getHolder() instanceof EditorMenu)) {
                return;
            }

            ItemStack currentItem = event.getCurrentItem();
            if (currentItem == null) {
                return;
            }

            if (event.getClick().isShiftClick()) {
                event.setCancelled(true);
                return;
            }

            Player player = (Player) event.getWhoClicked();
            EditorProfile editorProfile = plugin.getProfiles().get(player.getUniqueId()).getEditorProfile();
            PersonalLoadout personalLoadout = editorProfile.getEditingLoadout();
            if (personalLoadout == null) {
                event.setCancelled(true);
                return;
            }

            Loadout selectedLoadout = editorProfile.getSelectedLoadout();
            if (selectedLoadout == null) {
                event.setCancelled(true);
                return;
            }

            if (event.getClickedInventory() != inventory) {
                return;
            }

            int slot = event.getRawSlot();
            for (int i = 12; i < 17; i++) {
                if (slot == i && !currentItem.isSimilar(glass)) {
                    event.setCancelled(true);

                    player.setItemOnCursor(currentItem);
                    player.updateInventory();
                    return;
                }
            }
            for (int i = 21; i < 26; i++) {
                if (slot == i && !currentItem.isSimilar(glass)) {
                    event.setCancelled(true);

                    player.setItemOnCursor(currentItem);
                    player.updateInventory();
                    return;
                }
            }

            switch (slot) {
                case 40:
                    player.setItemOnCursor(null);
                    event.setCancelled(true);

                    personalLoadout.setContents(player.getInventory().getContents());
                    new EditorOverviewMenu(selectedLoadout, editorProfile).open(player);

                    Tools.playSuccess(player);
                    break;
                case 41:
                    player.setItemOnCursor(null);
                    event.setCancelled(true);

                    player.getInventory().setContents(selectedLoadout.getContents());
                    player.updateInventory();

                    Tools.playSuccess(player);
                    break;
                case 42:
                    player.setItemOnCursor(null);
                    event.setCancelled(true);

                    new EditorOverviewMenu(selectedLoadout, editorProfile).open(player);

                    Tools.playSuccess(player);
                    break;
                default:
                    event.setCancelled(true);
                    break;
            }
        });

        Events.subscribe(InventoryCloseEvent.class, event -> {
            Inventory inventory = event.getView().getTopInventory();
            if (inventory == null) {
                return;
            }
            if (inventory.getHolder() == null) {
                return;
            }
            if (!(inventory.getHolder() instanceof EditorMenu)) {
                return;
            }

            Scheduler.runLater(() -> plugin.getLobby().toLobby((Player) event.getPlayer(), false), 1L);
        });
    }
}
