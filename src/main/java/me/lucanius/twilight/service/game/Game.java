package me.lucanius.twilight.service.game;

import lombok.Getter;
import lombok.Setter;
import me.lucanius.twilight.Twilight;
import me.lucanius.twilight.service.arena.Arena;
import me.lucanius.twilight.service.game.context.GameContext;
import me.lucanius.twilight.service.game.context.GameState;
import me.lucanius.twilight.service.game.team.GameTeam;
import me.lucanius.twilight.service.game.team.member.TeamMember;
import me.lucanius.twilight.service.loadout.Loadout;
import me.lucanius.twilight.service.queue.abstr.AbstractQueue;
import me.lucanius.twilight.tools.CC;
import me.lucanius.twilight.tools.date.DateTools;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Lucanius
 * @since May 25, 2022
 */
@Getter @Setter
public class Game {

    private final static Twilight plugin = Twilight.getInstance();

    private final Set<Location> blocks;
    private final UUID uniqueId;
    private final GameContext context;
    private final Loadout loadout;
    private final Arena arena;
    private final AbstractQueue<?> queue;
    private final List<GameTeam> teams;

    private GameState state;
    private long timeStamp;

    public Game(GameContext context, Loadout loadout, Arena arena, AbstractQueue<?> queue, List<GameTeam> teams) {
        this.blocks = new HashSet<>();
        this.uniqueId = UUID.randomUUID();
        this.context = context;
        this.loadout = loadout;
        this.arena = arena;
        this.queue = queue;
        this.teams = teams;

        this.state = GameState.STARTING;
        this.timeStamp = System.currentTimeMillis();
    }

    public Collection<Player> getEveryone() {
        return teams.stream().flatMap(team -> team.getMembers().stream()).map(TeamMember::getPlayer).collect(Collectors.toList());
    }

    public Collection<Player> getAlive() {
        return teams.stream().flatMap(team -> team.getMembers().stream()).filter(TeamMember::isAlive).map(TeamMember::getPlayer).collect(Collectors.toList());
    }

    public Collection<TeamMember> getMembers() {
        return teams.stream().flatMap(team -> team.getMembers().stream()).collect(Collectors.toList());
    }

    public GameTeam getTeam(UUID uniqueId) {
        return teams.stream().filter(team -> team.getSpecific(uniqueId) != null).findFirst().orElse(null);
    }

    public GameTeam getOpposingTeam(UUID uniqueId) {
        return teams.stream().filter(team -> team.getSpecific(uniqueId) == null).findFirst().orElse(null);
    }

    public void sendMessage(String message) {
        getEveryone().forEach(player -> player.sendMessage(CC.translate(message)));
    }

    public void sendSound(Sound sound) {
        getEveryone().forEach(player -> player.playSound(player.getLocation(), sound, 1, 1));
    }

    public void clearBlocks() {
        blocks.forEach(location -> location.getBlock().setType(Material.AIR));
        blocks.clear();
    }

    public String getTime() {
        return DateTools.formatIntToMMSS((int) ((System.currentTimeMillis() - timeStamp) / 1000L));
    }
}
