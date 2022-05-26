package me.lucanius.twilight.service.game.team;

import lombok.Getter;
import lombok.Setter;
import me.lucanius.twilight.Twilight;
import me.lucanius.twilight.service.arena.Arena;
import me.lucanius.twilight.service.game.team.member.TeamMember;
import me.lucanius.twilight.tools.Scheduler;
import me.lucanius.twilight.tools.Tools;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Lucanius
 * @since May 25, 2022
 */
@Getter @Setter
public class GameTeam {

    private final static Twilight plugin = Twilight.getInstance();

    private final List<TeamMember> members;
    private final ChatColor color;
    private final String name;
    private final String prefix;

    private Location spawn;
    private int points;
    private boolean allowedToScore;

    public GameTeam(List<UUID> members, ChatColor color) {
        this.members = members.stream().map(member -> new TeamMember(member, this)).collect(Collectors.toList());
        this.color = color;
        this.name = Tools.getEnumName(color.name());
        this.prefix = color + "[" + color.name().charAt(0) + "] ";
    }

    public TeamMember getSpecific(UUID uniqueId) {
        return members.stream().filter(player -> player.getUniqueId().equals(uniqueId)).findFirst().orElse(null);
    }

    public Player getFirstPlayer() {
        return members.stream().map(TeamMember::getPlayer).filter(Objects::nonNull).findFirst().orElse(null);
    }

    public Player getRandomPlayer() {
        return members.get(plugin.getRandom().nextInt(members.size())).getPlayer();
    }

    public Collection<Player> toPlayers() {
        return members.stream().map(TeamMember::getPlayer).filter(Objects::nonNull).collect(Collectors.toList());
    }

    public void killSpecific(UUID uniqueId) {
        getSpecific(uniqueId).setAlive(false);
    }

    public String getFormattedName() {
        return color + name;
    }

    public void detectSpawn(Arena arena) {
        spawn = color == ChatColor.BLUE ? arena.getA().getBukkitLocation() : color == ChatColor.RED ? arena.getB().getBukkitLocation() : null;
    }

    public int getAliveSize() {
        return (int) members.stream().filter(TeamMember::isAlive).count();
    }

    public void score() {
        points++;
        allowedToScore = false;

        Scheduler.runLaterAsync(() -> allowedToScore = true, 100L);
    }
}
