package me.lucanius.twilight;

import lombok.Getter;
import me.lucanius.twilight.event.EventProvider;
import me.lucanius.twilight.event.helper.StandardEventProvider;
import me.lucanius.twilight.layout.BoardLayout;
import me.lucanius.twilight.service.arena.ArenaService;
import me.lucanius.twilight.service.damage.DamageService;
import me.lucanius.twilight.service.game.GameService;
import me.lucanius.twilight.service.loadout.LoadoutService;
import me.lucanius.twilight.service.lobby.LobbyService;
import me.lucanius.twilight.service.profile.Profile;
import me.lucanius.twilight.service.profile.ProfileService;
import me.lucanius.twilight.service.profile.standard.StandardProfileService;
import me.lucanius.twilight.service.queue.QueueService;
import me.lucanius.twilight.storage.MongoServer;
import me.lucanius.twilight.storage.builder.MongoBuilder;
import me.lucanius.twilight.tools.CC;
import me.lucanius.twilight.tools.Tools;
import me.lucanius.twilight.tools.board.Board;
import me.lucanius.twilight.tools.command.CommandFramework;
import me.lucanius.twilight.tools.config.ConfigFile;
import me.lucanius.twilight.tools.registration.ClassRegistration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collection;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Lucanius
 * @since May 20, 2022
 */
@Getter
public final class Twilight extends JavaPlugin {

    @Getter private static Twilight instance;

    private final Random random = ThreadLocalRandom.current();
    private final ClassRegistration registration = new ClassRegistration();

    private ConfigFile config;

    private MongoServer mongo;
    private CommandFramework framework;
    private LobbyService lobby;
    private ProfileService profiles;
    private LoadoutService loadouts;
    private QueueService queues;
    private ArenaService arenas;
    private GameService games;
    private DamageService damages;

    private EventProvider events;
    private Board board;

    private boolean disabling;

    @Override
    public void onEnable() {
        instance = this;
        disabling = false;

        long start = System.currentTimeMillis();

        config = new ConfigFile(this, "config.yml");

        mongo = new MongoBuilder()
                .host(config.getString("MONGO.HOST"))
                .port(config.getInt("MONGO.PORT"))
                .database(config.getString("MONGO.DATABASE"))
                .auth(config.getBoolean("MONGO.AUTH.ENABLED"))
                .user(config.getString("MONGO.AUTH.USER"))
                .pass(config.getString("MONGO.AUTH.PASS"))
                .authDb(config.getString("MONGO.AUTH.AUTH-DB"))
                .build();
        framework = new CommandFramework(this);
        lobby = new LobbyService(this);
        profiles = new StandardProfileService(this);
        loadouts = new LoadoutService(this);
        queues = new QueueService(this);
        arenas = new ArenaService(this);
        games = new GameService(this);
        damages = new DamageService();

        events = new StandardEventProvider();
        board = new Board(this, new BoardLayout());

        registration.init("me.lucanius.twilight.event.bukkit.listeners")
                .init("me.lucanius.twilight.commands.impl")
                .init("me.lucanius.twilight.event.listeners");

        Tools.log(CC.BAR);
        Tools.log(CC.MAIN + "Twilight&r &av" + getDescription().getVersion() + " &7~ &blucA#0999");
        Tools.log(CC.SECOND + "Load time: &a" + (System.currentTimeMillis() - start) + CC.SECOND + " ms.");
        Tools.log(CC.BAR);
    }

    @Override
    public void onDisable() {
        disabling = true;

        profiles.getAll().forEach(Profile::save);
        loadouts.save();
        arenas.save();

        mongo.dispose();
    }

    public Collection<? extends Player> getOnline() {
        return getServer().getOnlinePlayers();
    }
}
