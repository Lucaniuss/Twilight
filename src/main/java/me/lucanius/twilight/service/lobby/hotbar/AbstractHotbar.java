package me.lucanius.twilight.service.lobby.hotbar;

import lombok.Getter;
import me.lucanius.twilight.tools.CC;
import me.lucanius.twilight.tools.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Lucanius
 * @since May 23, 2022
 */
@Getter
public class AbstractHotbar {

    private final List<HotbarItem> items;

    protected final List<HotbarItem> lobbyItems;
    protected final List<HotbarItem> partyItems;
    protected final List<HotbarItem> queueItems;

    public AbstractHotbar() {
        String click = " &7(Right-Click)";
        this.items = new ArrayList<>(
                Arrays.asList(
                        new HotbarItem(new ItemBuilder(Material.IRON_SWORD).setName(CC.MAIN + "Unranked" + click).build(), 0, HotbarContext.UNRANKED, HotbarType.LOBBY),
                        new HotbarItem(new ItemBuilder(Material.DIAMOND_SWORD).setName(CC.MAIN + "Ranked" + click).build(), 1, HotbarContext.RANKED, HotbarType.LOBBY),
                        new HotbarItem(new ItemBuilder(Material.BARRIER).setName(CC.MAIN + "Spectate" + click).build(), 2, HotbarContext.SPECTATE, HotbarType.LOBBY),
                )
        );

        this.lobbyItems = items.stream().filter(i -> i.getType() == HotbarType.LOBBY).collect(Collectors.toList());
        this.partyItems = items.stream().filter(i -> i.getType() == HotbarType.PARTY).collect(Collectors.toList());
        this.queueItems = items.stream().filter(i -> i.getType() == HotbarType.QUEUE).collect(Collectors.toList());
    }

    public HotbarItem get(ItemStack item) {
        return items.stream().filter(i -> i.getItem().isSimilar(item)).findFirst().orElse(null);
    }

    public HotbarItem get(HotbarContext context) {
        return items.stream().filter(i -> i.getContext() == context).findFirst().orElse(null);
    }

    public HotbarItem get(HotbarType type) {
        return items.stream().filter(i -> i.getType() == type).findFirst().orElse(null);
    }
}
