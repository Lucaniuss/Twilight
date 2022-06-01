package me.lucanius.twilight.service.game.team;

import lombok.Getter;
import lombok.Setter;
import me.lucanius.twilight.Twilight;
import me.lucanius.twilight.service.arena.Arena;
import me.lucanius.twilight.service.game.team.member.TeamMember;
import me.lucanius.twilight.tools.Scheduler;
import me.lucanius.twilight.tools.Tools;
import me.lucanius.twilight.tools.functions.Voluntary;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
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
        this.members = members.stream().map(uuid -> new TeamMember(uuid, this)).collect(Collectors.toList());
        this.color = color;
        this.name = Tools.getEnumName(color.name());
        this.prefix = color + "[" + color.name().charAt(0) + "] ";

        this.spawn = null;
        this.points = 0;
        this.allowedToScore = true;
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

    public Collection<TeamMember> getAliveMembers() {
        return members.stream().filter(TeamMember::isAlive).collect(Collectors.toList());
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

    public void spawnCage() {
        if (spawn == null) {
            return;
        }

        Location loc = spawn.clone().add(0, 5, 0);
        for (int x = -2; x <= 2; x++) {
            for (int y = -2; y <= 2; y++) {
                for (int z = -2; z <= 2; z++) {
                    Voluntary.of(loc.getWorld().getBlockAt(loc.getBlockX() + x, loc.getBlockY() + y, loc.getBlockZ() + z))
                            .filter(block -> block.getType() == Material.AIR)
                            .ifPresent(block -> {
                                block.setType(Material.STAINED_GLASS);
                                block.setData(color == ChatColor.BLUE ? (byte) 11 : color == ChatColor.RED ? (byte) 14 : (byte) 0);
                            });
                }
            }
        }

        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    Voluntary.of(loc.getWorld().getBlockAt(loc.getBlockX() + x, loc.getBlockY() + y, loc.getBlockZ() + z))
                            .filter(block -> block.getType() == Material.STAINED_GLASS)
                            .ifPresent(block -> block.setType(Material.AIR));
                }
            }
        }

        toPlayers().forEach(player -> player.teleport(loc));
    }

    public void destroyCage() {
        if (spawn == null) {
            return;
        }

        Location loc = spawn.clone().add(0, 5, 0);
        for (int x = -2; x <= 2; x++) {
            for (int y = -2; y <= 2; y++) {
                for (int z = -2; z <= 2; z++) {
                    Voluntary.of(loc.getWorld().getBlockAt(loc.getBlockX() + x, loc.getBlockY() + y, loc.getBlockZ() + z))
                            .filter(block -> block.getType() == Material.STAINED_GLASS)
                            .ifPresent(block -> block.setType(Material.AIR));
                }
            }
        }
    }
}
