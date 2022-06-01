package me.lucanius.twilight.layout.tab;

import me.lucanius.edge.adapter.TabAdapter;
import me.lucanius.edge.context.TabColumn;
import me.lucanius.edge.entry.TabData;
import me.lucanius.twilight.Twilight;
import me.lucanius.twilight.tools.CC;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Lucanius
 * @since May 29, 2022.
 * Twilight - All Rights Reserved.
 */
public class TabLayout implements TabAdapter {

    private final Twilight plugin = Twilight.getInstance();

    @Override
    public List<String> getHeader(Player player) {
        return Arrays.asList(" ", CC.BLUE + CC.BOLD + "Edge", " ");
    }

    @Override
    public List<String> getFooter(Player player) {
        return Arrays.asList(" ", CC.GRAY + CC.ITALIC + "lucanius.me", " ");
    }

    @Override
    public Set<TabData> getEntries(Player player) {
        Set<TabData> entries = new HashSet<>();

        entries.add(new TabData(TabColumn.MIDDLE, 1, CC.BLUE + CC.BOLD + "Edge"));
        entries.add(new TabData(TabColumn.MIDDLE, 2, CC.GRAY + CC.ITALIC + "lucanius.me"));

        entries.add(new TabData(TabColumn.LEFT, 4, "Left"));
        entries.add(new TabData(TabColumn.MIDDLE, 4, "Center"));
        entries.add(new TabData(TabColumn.RIGHT, 4, "Right"));
        entries.add(new TabData(TabColumn.FAR_RIGHT, 4, "Far Right"));

        entries.add(new TabData(TabColumn.MIDDLE, 19, "Your Skin", plugin.getEdge().getSkin(player.getUniqueId())));

        return entries;
    }
}
