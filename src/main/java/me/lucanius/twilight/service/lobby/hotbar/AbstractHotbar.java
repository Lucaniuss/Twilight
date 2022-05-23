package me.lucanius.twilight.service.lobby.hotbar;

import lombok.Getter;
import me.lucanius.twilight.tools.CC;
import me.lucanius.twilight.tools.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Lucanius
 * @since May 23, 2022
 */
@Getter
public class AbstractHotbar {

    protected final List<HotbarItem> items;

    public AbstractHotbar() {
        String click = " &7(Right-Click)";
        this.items = new ArrayList<>(
                Arrays.asList(
                        new HotbarItem(new ItemBuilder(Material.IRON_SWORD).setName(CC.MAIN + "Unranked" + click).build(), 0, HotbarContext.UNRANKED),
                        new HotbarItem(new ItemBuilder(Material.DIAMOND_SWORD).setName(CC.MAIN + "Ranked" + click).build(), 1, HotbarContext.RANKED)
                )
        );
    }

    public HotbarItem get(ItemStack item) {
        return items.stream().filter(i -> i.getItem().isSimilar(item)).findFirst().orElse(null);
    }

    public HotbarItem get(HotbarContext context) {
        return items.stream().filter(i -> i.getContext() == context).findFirst().orElse(null);
    }
}
