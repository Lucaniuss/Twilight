package me.lucanius.twilight.service.lobby.hotbar;

import lombok.Getter;
import me.lucanius.twilight.service.lobby.hotbar.context.HotbarContext;
import me.lucanius.twilight.service.lobby.hotbar.context.HotbarType;
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
public abstract class AbstractHotbar {

    private final List<HotbarItem> items;

    protected final List<HotbarItem> lobbyItems;
    protected final List<HotbarItem> partyItems;
    protected final List<HotbarItem> queueItems;
    protected final List<HotbarItem> spectatorItems;

    public AbstractHotbar() {
        this.items = new ArrayList<>(
                Arrays.asList(
                        new HotbarItem(new ItemBuilder(Material.IRON_SWORD).setName(CC.MAIN + "Unranked &7(Right-Click)").build(), 0, HotbarContext.UNRANKED, HotbarType.LOBBY),
                        new HotbarItem(new ItemBuilder(Material.DIAMOND_SWORD).setName(CC.MAIN + "Ranked &7(Right-Click)").build(), 1, HotbarContext.RANKED, HotbarType.LOBBY),
                        new HotbarItem(new ItemBuilder(Material.INK_SACK).setData(1).setName(CC.MAIN + "Leave Queue &7(Right-Click)").build(), 0, HotbarContext.LEAVE_QUEUE, HotbarType.QUEUE)
                )
        );

        this.lobbyItems = items.stream().filter(i -> i.getType() == HotbarType.LOBBY).collect(Collectors.toList());
        this.partyItems = items.stream().filter(i -> i.getType() == HotbarType.PARTY).collect(Collectors.toList());
        this.queueItems = items.stream().filter(i -> i.getType() == HotbarType.QUEUE).collect(Collectors.toList());
        this.spectatorItems = items.stream().filter(i -> i.getType() == HotbarType.SPECTATING).collect(Collectors.toList());
    }

    public HotbarItem get(ItemStack item) {
        return items.stream().filter(i -> i.getItem().isSimilar(item)).findFirst().orElse(null);
    }

    public HotbarItem get(HotbarContext context) {
        return items.stream().filter(i -> i.getContext() == context).findFirst().orElse(null);
    }
}
