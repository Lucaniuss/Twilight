package me.lucanius.prac;

import lombok.Getter;
import me.lucanius.prac.profile.ProfileService;
import me.lucanius.prac.storage.MongoServer;
import me.lucanius.prac.storage.builder.MongoBuilder;
import me.lucanius.prac.tools.CC;
import me.lucanius.prac.tools.Tools;
import me.lucanius.prac.tools.command.CommandFramework;
import me.lucanius.prac.tools.config.ConfigFile;
import me.lucanius.prac.tools.registration.ClassRegistration;
import me.lucanius.prac.tools.events.EventsListener;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collection;

/**
 * @author Lucanius
 * @since May 20, 2022
 */
@Getter
public final class Twilight extends JavaPlugin {

    @Getter private static Twilight instance;

    private final EventsListener eventsListener = new EventsListener();
    private final ClassRegistration registration = new ClassRegistration();

    private ConfigFile config;

    private MongoServer mongo;
    private CommandFramework framework;
    private ProfileService profiles;

    private boolean disabling = false;

    @Override
    public void onEnable() {
        instance = this;

        long start = System.currentTimeMillis();

        this.config = new ConfigFile(this, "config.yml");

        this.mongo = new MongoBuilder().host(config.getString("MONGO.HOST"))
                .port(config.getInt("MONGO.PORT"))
                .database(config.getString("MONGO.DATABASE"))
                .auth(config.getBoolean("MONGO.AUTH.ENABLED"))
                .user(config.getString("MONGO.AUTH.USER"))
                .pass(config.getString("MONGO.AUTH.PASS"))
                .authDb(config.getString("MONGO.AUTH.AUTH-DB"))
                .build();
        this.framework = new CommandFramework(this);
        this.profiles = new ProfileService(this);

        this.registration.init("me.lucanius.prac.listeners").init("me.lucanius.prac.commands");

        Tools.log(CC.BAR);
        Tools.log(CC.MAIN + "Twilight&r &av" + getDescription().getVersion() + " &7~ &blucA#0999");
        Tools.log(CC.SECOND + "Load time: &a" + (System.currentTimeMillis() - start) + CC.SECOND + " ms.");
        Tools.log(CC.BAR);
    }

    @Override
    public void onDisable() {
        disabling = true;
    }

    public Collection<? extends Player> getOnline() {
        return getServer().getOnlinePlayers();
    }
}
