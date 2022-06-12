package me.lucanius.twilight.layout.tab;

import me.lucanius.edge.adapter.TabAdapter;
import me.lucanius.edge.column.TabColumn;
import me.lucanius.edge.entry.TabData;
import me.lucanius.twilight.Twilight;
import me.lucanius.twilight.service.loadout.Loadout;
import me.lucanius.twilight.service.profile.Profile;
import me.lucanius.twilight.tools.CC;
import me.lucanius.twilight.tools.atomic.TwilightNumber;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Lucanius
 * @since May 29, 2022.
 * Twilight - All Rights Reserved.
 */
public class TabLayout implements TabAdapter {

    private final Twilight plugin = Twilight.getInstance();

    @Override
    public List<String> getHeader(Player player) {
        return Arrays.asList(" ", CC.MAIN + CC.BOLD + "Twilight", " ");
    }

    @Override
    public List<String> getFooter(Player player) {
        return Arrays.asList(" ", CC.SIGNATURE, " ");
    }

    @Override
    public Set<TabData> getEntries(Player player) {
        Set<TabData> entries = new HashSet<>();
        Profile profile = plugin.getProfiles().get(player.getUniqueId());

        entries.add(new TabData(TabColumn.LEFT, 2, CC.WHITE + "Online: " + CC.SECOND + plugin.getOnline().size()));
        entries.add(new TabData(TabColumn.MIDDLE, 2, CC.WHITE + "In Game: " + CC.SECOND + plugin.getGames().getSize()));
        entries.add(new TabData(TabColumn.RIGHT, 2, CC.WHITE + "In Queue: " + CC.SECOND + plugin.getQueues().getSize()));

        entries.add(new TabData(TabColumn.LEFT, 19, CC.SIGNATURE));
        entries.add(new TabData(TabColumn.MIDDLE, 19, CC.GRAY + CC.ITALIC + "github.com/Lucaniuss"));
        entries.add(new TabData(TabColumn.RIGHT, 19, CC.GRAY + CC.ITALIC + "lucA#0999"));

        TwilightNumber number = new TwilightNumber();

        switch (profile.getState()) {
            case LOBBY:
                Collection<Loadout> loadouts = plugin.getLoadouts().getAll().stream().filter(Loadout::isRanked).collect(Collectors.toList());
                loadouts.forEach(loadout -> {

                });
                break;
            case QUEUE:
                break;
            case PLAYING:
                break;
            case SPECTATING:
                break;
        }

        return entries;
    }
}
