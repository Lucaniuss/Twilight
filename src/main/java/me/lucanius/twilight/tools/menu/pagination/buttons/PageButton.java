package me.lucanius.twilight.tools.menu.pagination.buttons;

import lombok.RequiredArgsConstructor;
import me.lucanius.twilight.tools.Tools;
import me.lucanius.twilight.tools.item.ItemBuilder;
import me.lucanius.twilight.tools.menu.Button;
import me.lucanius.twilight.tools.menu.PaginatedMenu;
import me.lucanius.twilight.tools.menu.pagination.PagesMenu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

/**
 * @author Lucanius
 * @since May 20, 2022
 */
@RequiredArgsConstructor
public class PageButton extends Button {

    private final int mod;
    private final PaginatedMenu menu;

    @Override
    public ItemStack getItem(Player player) {
        final boolean next = hasNext(player);
        final ItemBuilder builder = new ItemBuilder(Material.INK_SACK);

        builder.setData(next && this.mod > 0 ? 10 : next ? 13 : 8);
        builder.setName(next && this.mod > 0 ? "&aNext Page" : next ? "&dPrevious Page" : this.mod > 0 ? "&7Last Page" : "&7First Page");

        return builder.build();
    }

    @Override
    public void onClick(Player player, ClickType clickType) {
        if (clickType == ClickType.RIGHT) {
            new PagesMenu(this.menu).open(player);
            Tools.playSuccess(player);
        } else {
            if (this.hasNext(player)) {
                this.menu.modPage(player, this.mod);
                Tools.playSuccess(player);
            } else {
                Tools.playFail(player);
            }
        }
    }

    private boolean hasNext(Player player) {
        int i = this.menu.getPage() + this.mod;
        return i > 0 && this.menu.getPages(player) >= i;
    }
}
