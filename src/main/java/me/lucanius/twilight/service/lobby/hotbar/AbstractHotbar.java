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
    protected final List<HotbarItem> gameItems;
    protected final List<HotbarItem> queueItems;
    protected final List<HotbarItem> spectatorItems;

    public AbstractHotbar() {
        final String click = " " + CC.GRAY + "(Right-Click)";
        this.items = new ArrayList<>(Arrays.asList(
                new HotbarItem(new ItemBuilder(Material.IRON_SWORD).setName(CC.MAIN + "Unranked" + click).build(), 0, HotbarContext.UNRANKED, HotbarType.LOBBY),
                new HotbarItem(new ItemBuilder(Material.DIAMOND_SWORD).setName(CC.MAIN + "Ranked" + click).build(), 1, HotbarContext.RANKED, HotbarType.LOBBY),
                new HotbarItem(new ItemBuilder(Material.BOOK).setName(CC.MAIN + "Loadout Editor" + click).build(), 2, HotbarContext.LOADOUT_EDITOR, HotbarType.LOBBY),
                new HotbarItem(new ItemBuilder(Material.NAME_TAG).setName(CC.MAIN + "Create Party" + click).build(), 4, HotbarContext.CREATE_PARTY, HotbarType.LOBBY),
                new HotbarItem(new ItemBuilder(Material.EMERALD).setName(CC.MAIN + "Leaderboards" + click).build(), 7, HotbarContext.LEADERBOARDS, HotbarType.LOBBY),
                new HotbarItem(new ItemBuilder(Material.NETHER_STAR).setName(CC.MAIN + "Your Settings" + click).build(), 8, HotbarContext.PERSONAL_SETTINGS, HotbarType.LOBBY),

                new HotbarItem(new ItemBuilder(Material.GOLD_SWORD).setName(CC.MAIN + "Duos" + click).build(), 0, HotbarContext.DUOS, HotbarType.PARTY),
                new HotbarItem(new ItemBuilder(Material.GOLD_AXE).setName(CC.MAIN + "Party Games" + click).build(), 1, HotbarContext.PARTY_GAMES, HotbarType.PARTY),
                new HotbarItem(new ItemBuilder(Material.CHEST).setName(CC.MAIN + "Other Parties" + click).build(), 4, HotbarContext.OTHER_PARTIES, HotbarType.PARTY),
                new HotbarItem(new ItemBuilder(Material.PAPER).setName(CC.MAIN + "Party Info" + click).build(), 7, HotbarContext.PARTY_INFO, HotbarType.PARTY),
                new HotbarItem(new ItemBuilder(Material.INK_SACK).setData(1).setName(CC.MAIN + "Leave Party" + click).build(), 8, HotbarContext.LEAVE_PARTY, HotbarType.PARTY),

                new HotbarItem(new ItemBuilder(Material.ENCHANTED_BOOK).setName(CC.MAIN + "Default Loadout" + click).build(), 0, HotbarContext.DEFAULT_BOOK, HotbarType.GAME),

                new HotbarItem(new ItemBuilder(Material.INK_SACK).setData(1).setName(CC.MAIN + "Leave Queue" + click).build(), 0, HotbarContext.LEAVE_QUEUE, HotbarType.QUEUE),

                new HotbarItem(new ItemBuilder(Material.COMPASS).setName(CC.MAIN + "View Players" + click).build(), 0, HotbarContext.VIEW_PLAYERS, HotbarType.SPECTATING),
                new HotbarItem(new ItemBuilder(Material.INK_SACK).setData(1).setName(CC.MAIN + "Stop Spectating" + click).build(), 8, HotbarContext.STOP_SPECTATING, HotbarType.SPECTATING)
        ));

        this.lobbyItems = items.stream().filter(i -> i.getType().equals(HotbarType.LOBBY)).collect(Collectors.toList());
        this.partyItems = items.stream().filter(i -> i.getType().equals(HotbarType.PARTY)).collect(Collectors.toList());
        this.gameItems = items.stream().filter(i -> i.getType().equals(HotbarType.GAME)).collect(Collectors.toList());
        this.queueItems = items.stream().filter(i -> i.getType().equals(HotbarType.QUEUE)).collect(Collectors.toList());
        this.spectatorItems = items.stream().filter(i -> i.getType().equals(HotbarType.SPECTATING)).collect(Collectors.toList());
    }

    public HotbarItem get(ItemStack item) {
        return items.stream().filter(i -> i.getItem().isSimilar(item)).findFirst().orElse(null);
    }

    public HotbarItem get(HotbarContext context) {
        return items.stream().filter(i -> i.getContext() == context).findFirst().orElse(null);
    }
}
