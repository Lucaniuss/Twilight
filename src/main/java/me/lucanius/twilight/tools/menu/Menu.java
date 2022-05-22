package me.lucanius.twilight.tools.menu;

import lombok.Getter;
import lombok.Setter;
import me.lucanius.twilight.Twilight;
import me.lucanius.twilight.tools.CC;
import me.lucanius.twilight.tools.item.ItemBuilder;
import me.lucanius.twilight.tools.menu.handlers.MenuListener;
import me.lucanius.twilight.tools.menu.handlers.MenuSaver;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;

/**
 * I don't know who the author is.
 * @edited Lucanius
 */
@Getter @Setter
public abstract class Menu {

    protected final static Twilight plugin = Twilight.getInstance();
    @Getter protected final static ItemStack glass = new ItemBuilder(Material.STAINED_GLASS_PANE).setData(15).setName("&7").build();

    private static MenuListener listener;

    private Map<Integer, Button> buttons;

    private boolean updateAfterClick;
    private boolean isAutoUpdate;

    public Menu() {
        if (listener == null) {
            listener = new MenuListener();
        }

        this.buttons = new HashMap<>();
        this.updateAfterClick = true;
        this.isAutoUpdate = false;
    }

    public abstract String getTitle(Player player);

    public abstract Map<Integer, Button> getButtons(Player player);

    public void onOpen(Player player) {
        MenuSaver.add(player.getUniqueId(), this);
    }

    public void onClose(Player player) {
        MenuSaver.remove(player.getUniqueId());
    }

    public int getSize() {
        return this.buttonSize();
    }

    private ItemStack createItemStack(Player player, Button button) {
        ItemStack item = button.getItem(player);
        if (item == null) {
            return null;
        }

        if (item.getType() != Material.SKULL) {
            ItemMeta meta = item.getItemMeta();
            if (meta != null && meta.hasDisplayName()) {
                meta.setDisplayName(meta.getDisplayName() + "§b§c§d§e");
            }
            item.setItemMeta(meta);
        }

        return item;
    }

    public void open(Player player) {
        this.buttons = this.getButtons(player);
        Menu previousMenu = MenuSaver.get(player.getUniqueId());

        int size = this.getSize();
        String title = CC.translate("&7&o" + this.getTitle(player));
        Inventory inventory = null;
        if (title.length() > 32) {
            title = title.substring(0, 32);
        }

        InventoryView open = player.getOpenInventory();
        if (open != null) {
            if (previousMenu != null) {
                Inventory top = open.getTopInventory();
                if (top.getSize() == size && top.getTitle().equals(title)) {
                    inventory = top;
                } else {
                    previousMenu.onClose(player);
                }
            }
        }

        if (inventory == null) {
            inventory = plugin.getServer().createInventory(player, size, title);
        }

        inventory.setContents(new ItemStack[inventory.getSize()]);
        for (Map.Entry<Integer, Button> buttonEntry : this.buttons.entrySet()) {
            inventory.setItem(buttonEntry.getKey(), this.createItemStack(player, buttonEntry.getValue()));
        }

        player.openInventory(inventory);

        this.onOpen(player);
    }

    public int buttonSize() {
        int highest = 0;
        for (int buttonValue : this.buttons.keySet()) {
            if (buttonValue > highest) {
                highest = buttonValue;
            }
        }

        return (int) (Math.ceil((highest + 1) / 9d) * 9d);
    }

    public void update(Player player) {
        Inventory inventory = player.getOpenInventory().getTopInventory();
        if (inventory == null) {
            return;
        }

        inventory.setContents(new ItemStack[inventory.getSize()]);
        MenuSaver.add(player.getUniqueId(), this);

        for (Map.Entry<Integer, Button> buttonEntry : this.buttons.entrySet()) {
            inventory.setItem(buttonEntry.getKey(), this.createItemStack(player, buttonEntry.getValue()));
        }

        player.updateInventory();
    }

    public Map<Integer, Button> getButtonMap() {
        return this.buttons;
    }

    public void fillEmptyWithGlass(Map<Integer, Button> buttons) {
        for (int slot = 0; slot < this.getSize(); slot++) {
            buttons.putIfAbsent(slot, this.glass());
        }
    }

    public Button glass() {
        return new Button() {
            @Override
            public ItemStack getItem(Player player) {
                return glass.clone();
            }
        };
    }

    public Button placeholder(ItemStack item) {
        return new Button() {
            @Override
            public ItemStack getItem(Player player) {
                return item;
            }
        };
    }

    public int getBackSlot() {
        return this.getSize() - 9;
    }
}
