package me.lucanius.twilight.service.game;

import io.netty.util.internal.ConcurrentSet;
import lombok.Getter;
import lombok.Setter;
import me.lucanius.twilight.Twilight;
import me.lucanius.twilight.service.arena.Arena;
import me.lucanius.twilight.service.game.context.GameContext;
import me.lucanius.twilight.service.game.context.GameState;
import me.lucanius.twilight.service.game.context.callback.DestroyCallback;
import me.lucanius.twilight.service.game.task.GameTask;
import me.lucanius.twilight.service.game.team.GameTeam;
import me.lucanius.twilight.service.game.team.member.TeamMember;
import me.lucanius.twilight.service.loadout.Loadout;
import me.lucanius.twilight.service.loadout.type.LoadoutType;
import me.lucanius.twilight.service.queue.abstr.AbstractQueue;
import me.lucanius.twilight.tools.CC;
import me.lucanius.twilight.tools.Scheduler;
import me.lucanius.twilight.tools.Tools;
import me.lucanius.twilight.tools.date.DateTools;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Lucanius
 * @since May 25, 2022
 */
@Getter @Setter
public class Game {

    private final static Twilight plugin = Twilight.getInstance();

    private final Set<Location> placedBlocks;
    private final Map<Location, BlockState> brokenBlocks;
    private final Set<Entity> droppedItems;
    private final Set<UUID> spectators;
    private final UUID uniqueId;
    private final GameContext context;
    private final Loadout loadout;
    private final Arena arena;
    private final AbstractQueue<?> queue;
    private final List<GameTeam> teams;

    private GameState state;
    private long timeStamp;
    private Arena arenaCopy;
    private GameTask task;
    private int countdown;

    public Game(GameContext context, Loadout loadout, Arena arena, AbstractQueue<?> queue, List<GameTeam> teams) {
        this.placedBlocks = new HashSet<>();
        this.brokenBlocks = new HashMap<>();
        this.droppedItems = new HashSet<>();
        this.spectators = new ConcurrentSet<>(); // to prevent ConcurrentModificationException
        this.uniqueId = UUID.randomUUID();
        this.context = context;
        this.loadout = loadout;
        this.arena = arena;
        this.queue = queue;
        this.teams = teams;

        this.state = GameState.STARTING;
        this.timeStamp = System.currentTimeMillis();
        this.arenaCopy = null;
        this.task = null;
        this.countdown = 6;
    }

    public void addSpectator(Player player, boolean fromGame) {
        UUID uniqueId = player.getUniqueId();
        spectators.add(uniqueId);

        Tools.clearPlayer(player);
        if (!fromGame) {
            if (!player.hasPermission("twilight.staff")) {
                sendMessage(CC.SECOND + player.getName() + " is now spectating the game.");
            }

            player.teleport(arena.getMiddle().getBukkitLocation());
            player.setAllowFlight(true);
            player.setFlying(true);

            plugin.getProfiles().get(uniqueId).getGameProfile().setGameId(this.uniqueId);

            plugin.getLobby().getSpectatorItems().forEach(item -> player.getInventory().setItem(item.getSlot(), item.getItem()));
            plugin.getOnline().forEach(online -> {
                online.hidePlayer(player);
                player.hidePlayer(online);
            });

            getAlive().forEach(player::showPlayer);
            return;
        }

        EntityPlayer nmsPlayer = Tools.getEntityPlayer(player);
        Location location = player.getLocation();
        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();

        PacketPlayOutSpawnEntityWeather lightning = new PacketPlayOutSpawnEntityWeather(new EntityLightning(nmsPlayer.world, x, y, z));
        PacketPlayOutNamedSoundEffect lightningSound = new PacketPlayOutNamedSoundEffect("ambient.weather.thunder", x, y, z, 10.0f, 1.0f);

        getEveryone().stream().filter(other -> player != other).forEach(other -> {
            PlayerConnection otherConnection = Tools.getEntityPlayer(other).playerConnection;
            otherConnection.sendPacket(lightning);
            otherConnection.sendPacket(lightningSound);
        });

        PlayerConnection connection = nmsPlayer.playerConnection;
        connection.sendPacket(lightning);
        connection.sendPacket(lightningSound);

        Scheduler.run(() -> {
            player.setWalkSpeed(0.0f);
            player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, -5));
        });

        Scheduler.runLater(() -> {
            getAlive().forEach(member -> member.hidePlayer(player));
            player.getActivePotionEffects().stream().map(PotionEffect::getType).forEach(player::removePotionEffect);
            player.setFlySpeed(0.4f);
            player.setWalkSpeed(0.2f);
            player.setAllowFlight(true);
        }, 20L);
    }

    public void removeSpectator(UUID uniqueId) {
        spectators.remove(uniqueId);

        Player player = plugin.getServer().getPlayer(uniqueId);
        if (player == null) {
            return;
        }

        if (state != GameState.TERMINATED && !player.hasPermission("twilight.staff")) {
            sendMessage(CC.SECOND + player.getName() + " is no longer spectating the game.");
        }

        plugin.getLobby().toLobby(player, true);
    }

    public GameTask setTask(GameTask task) {
        return this.task = task;
    }

    public Collection<Player> getEveryone() {
        Collection<Player> players = teams.stream().flatMap(team -> team.getMembers().stream()).map(TeamMember::getPlayer).collect(Collectors.toList());
        spectators.stream().map(plugin.getServer()::getPlayer).filter(Objects::nonNull).forEach(players::add);
        return players;
    }

    public Collection<Player> getAlive() {
        return teams.stream().flatMap(team -> team.getMembers().stream()).filter(TeamMember::isAlive).map(TeamMember::getPlayer).collect(Collectors.toList());
    }

    public List<TeamMember> getMembers() {
        return teams.stream().flatMap(team -> team.getMembers().stream()).collect(Collectors.toList());
    }

    public Collection<TeamMember> getAliveMembers() {
        return teams.stream().flatMap(team -> team.getMembers().stream()).filter(TeamMember::isAlive).collect(Collectors.toList());
    }

    public GameTeam getTeam(UUID uniqueId) {
        return teams.stream().filter(team -> team.getSpecific(uniqueId) != null).findFirst().orElse(null);
    }

    public GameTeam getOpposingTeam(UUID uniqueId) {
        return teams.stream().filter(team -> team.getSpecific(uniqueId) == null).findFirst().orElse(null);
    }

    public GameTeam getOpposingTeam(GameTeam team) {
        return teams.stream().filter(t -> t.getColor() != team.getColor()).findFirst().orElse(null);
    }

    public void sendMessage(String message) {
        getEveryone().forEach(player -> player.sendMessage(CC.translate(CC.GAME_PREFIX + message)));
    }

    public void sendSound(Sound sound) {
        getEveryone().forEach(player -> player.playSound(player.getLocation(), sound, 1, 1));
    }

    public void sendSoundToTeam(GameTeam team, Sound sound) {
        team.toPlayers().forEach(player -> player.playSound(player.getLocation(), sound, 1, 1));
    }

    public void sendMessageWithSound(String message, Sound sound) {
        getEveryone().forEach(player -> {
            player.sendMessage(CC.translate(CC.GAME_PREFIX + message));
            player.playSound(player.getLocation(), sound, 1, 1);
        });
    }

    @SuppressWarnings("deprecation")
    public void clearArena() {
        if (!droppedItems.isEmpty()) {
            droppedItems.forEach(Entity::remove);
            droppedItems.clear();
        }

        if (!placedBlocks.isEmpty()) {
            placedBlocks.forEach(location -> location.getBlock().setType(Material.AIR));
            placedBlocks.clear();
        }

        if (!brokenBlocks.isEmpty()) {
            brokenBlocks.forEach((location, state) -> {
                Block block = location.getBlock();
                block.setType(state.getType());
                block.setData(state.getRawData());
            });
            brokenBlocks.clear();
        }

        if (loadout.getType() == LoadoutType.BRIDGES) {
            teams.forEach(GameTeam::destroyCage);
        }

        if (arenaCopy != null) {
            arenaCopy.resetCopy(arena);
        }
    }

    public DestroyCallback isBreakable(Block block) {
        if (!arenaCopy.isInside(block.getLocation())) return DestroyCallback.NOT_INSIDE;
        if (placedBlocks.contains(block.getLocation())) return DestroyCallback.PLACED;
        if (loadout.getType() != LoadoutType.BRIDGES) return DestroyCallback.INVALID_TYPE;
        if (block.getType() != Material.STAINED_CLAY) return DestroyCallback.INVALID_BLOCK;
        if (block.getLocation().getY() > arenaCopy.getBuildHeight()) return DestroyCallback.ABOVE_LIMIT;

        byte data = block.getData();
        return (data == 0 || data == 11 || data == 14) ? DestroyCallback.VALID : DestroyCallback.INVALID;
    }

    public int decrement() {
        return --countdown;
    }

    public String getTime() {
        return DateTools.formatIntToMMSS((int) ((System.currentTimeMillis() - timeStamp) / 1000L));
    }

    public boolean isRanked() {
        return queue.equals(plugin.getQueues().getRanked());
    }
}
