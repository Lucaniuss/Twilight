package me.lucanius.twilight.tools.menu;

import me.lucanius.twilight.Twilight;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

/**
 * I don't know who the author is.
 * @edited Lucanius
 */
public abstract class Button {

    protected final static Twilight plugin = Twilight.getInstance();

    public abstract ItemStack getItem(Player player);

    public void onClick(Player player, ClickType clickType) {}

    public boolean shouldCancel(Player player, ClickType clickType) {
        return true;
    }

    public boolean shouldUpdate(Player player, ClickType clickType) {
        return false;
    }
}
