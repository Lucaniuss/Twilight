package me.lucanius.prac.tools.menu.pagination.buttons;

import me.lucanius.prac.tools.Tools;
import me.lucanius.prac.tools.item.ItemBuilder;
import me.lucanius.prac.tools.menu.Button;
import me.lucanius.prac.tools.menu.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * @author Lucanius
 * @since May 20, 2022
 */
public class BackButton extends Button {

    private Menu menu;
    private Inventory inventory;

    public BackButton(Menu menu) {
        this.menu = menu;
    }

    public BackButton(Inventory inventory) {
        this.inventory = inventory;
    }

    @Override
    public ItemStack getItem(Player player) {
        return new ItemBuilder(Material.BED)
                .setName("&cBack")
                .setLore("&e&oClick to go back")
                .build();
    }

    @Override
    public void onClick(Player player, ClickType clickType) {
        if (this.menu != null && this.inventory == null) {
            this.menu.open(player);
        } else if (this.menu == null && this.inventory != null) {
            player.openInventory(this.inventory);
        }

        Tools.playSuccess(player);
    }
}
