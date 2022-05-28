package me.lucanius.twilight.service.party;

import lombok.Getter;
import me.lucanius.twilight.Twilight;
import me.lucanius.twilight.service.game.team.GameTeam;
import me.lucanius.twilight.tools.CC;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Lucanius
 * @since May 25, 2022
 */
@Getter
public class Party {

    private final static Twilight plugin = Twilight.getInstance();

    private final UUID leader;
    private final List<UUID> members;

    public Party(UUID leader) {
        this.leader = leader;
        (this.members = new ArrayList<>()).add(leader);
    }

    public void add(UUID member) {
        members.add(member);
    }

    public void remove(UUID member) {
        members.remove(member);
    }

    public Collection<Player> toPlayers() {
        return members.stream().map(plugin.getServer()::getPlayer).collect(Collectors.toList());
    }

    public void broadcast(String message) {
        toPlayers().forEach(player -> player.sendMessage(CC.translate(message)));
    }

    public Player getLeaderPlayer() {
        return plugin.getServer().getPlayer(leader);
    }

    public List<GameTeam> split() {
        List<UUID> first = new ArrayList<>();
        List<UUID> second = new ArrayList<>();

        for (UUID member : members) {
            if (first.size() == second.size()) {
                if (plugin.getRandom().nextBoolean()) {
                    first.add(member);
                } else {
                    second.add(member);
                }
            } else {
                if (first.size() < second.size()) {
                    first.add(member);
                } else {
                    second.add(member);
                }
            }
        }

        GameTeam blue = new GameTeam(first, ChatColor.BLUE);
        GameTeam red = new GameTeam(second, ChatColor.RED);

        return Arrays.asList(blue, red);
    }
}
