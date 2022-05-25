package me.lucanius.twilight.service.queue.menu.abstr;

import lombok.RequiredArgsConstructor;
import me.lucanius.twilight.service.loadout.Loadout;
import me.lucanius.twilight.service.queue.abstr.AbstractQueue;
import me.lucanius.twilight.tools.CC;
import me.lucanius.twilight.tools.item.ItemBuilder;
import me.lucanius.twilight.tools.menu.Button;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author Lucanius
 * @since May 23, 2022
 */
@RequiredArgsConstructor
public abstract class AbstractQueueButton extends Button {

    private final AbstractQueue<?> queue;
    protected final Loadout loadout;

    @Override
    public ItemStack getItem(Player player) {
        ItemBuilder builder = new ItemBuilder(loadout.getIcon().clone());

        int playing = plugin.getGames().getSize(loadout, queue);

        builder.setName(CC.MAIN + CC.BOLD + loadout.getName());
        builder.addLore(CC.MEDIUM_BAR);
        builder.addLore(CC.ICON + CC.WHITE + "In Game: " + CC.SECOND + playing);
        builder.addLore(CC.ICON + CC.WHITE + "In Queue: " + CC.SECOND + plugin.getQueues().getSize(loadout, queue));
        builder.addLore(" ");
        builder.addLore(CC.SECOND + CC.ITALIC + "Click to queue");
        builder.addLore(CC.MEDIUM_BAR);

        builder.setAmount(playing == 0 ? 1 : playing);

        return builder.build();
    }
}
