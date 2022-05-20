package me.lucanius.prac.tools.menu.pagination.buttons;

import lombok.RequiredArgsConstructor;
import me.lucanius.prac.tools.CC;
import me.lucanius.prac.tools.Tools;
import me.lucanius.prac.tools.item.ItemBuilder;
import me.lucanius.prac.tools.menu.Button;
import me.lucanius.prac.tools.menu.PaginatedMenu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

/**
 * @author Lucanius
 * @since May 20, 2022
 */
@RequiredArgsConstructor
public class JumpButton extends Button {

    private final int page;
    private final PaginatedMenu menu;
    private final boolean current;

    @Override
    public ItemStack getItem(Player player) {
        final ItemBuilder builder = new ItemBuilder(Material.PAPER);

        builder.setName(CC.SECOND + "Page: " + CC.MAIN + this.page);
        if (current) {
            builder.shine();
            builder.addLore("&e&oCurrent Page");
        }

        return builder.build();
    }

    @Override
    public void onClick(Player player, ClickType clickType) {
        this.menu.modPage(player, this.page - this.menu.getPage());
        Tools.playSuccess(player);
    }
}
