package me.lucanius.twilight.service.arena;

import lombok.Data;
import me.lucanius.twilight.Twilight;
import me.lucanius.twilight.service.arena.generator.ArenaGenerator;
import me.lucanius.twilight.tools.config.ConfigFile;
import me.lucanius.twilight.tools.location.Cuboid;
import me.lucanius.twilight.tools.location.SerializableLocation;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Lucanius
 * @since May 24, 2022
 */
@Data
public class Arena {

    private final static Twilight plugin = Twilight.getInstance();

    private final String name;

    private List<Arena> copies;
    private List<String> loadouts;
    private SerializableLocation min, max, a, b, middle;
    private double buildHeight;

    public Arena(String name) {
        this.name = name;
    }

    public Arena(String name, List<Arena> copies, List<String> loadouts, SerializableLocation min, SerializableLocation max, SerializableLocation a, SerializableLocation b, double buildHeight) {
        this.name = name;

        this.copies = copies;
        this.loadouts = loadouts;
        this.min = min;
        this.max = max;
        this.a = a;
        this.b = b;
        this.buildHeight = buildHeight;
        this.middle = new SerializableLocation(a.getWorld(), (a.getX() + b.getX()) / 2, buildHeight, (a.getZ() + b.getZ()) / 2);
    }

    public Arena(String name, SerializableLocation min, SerializableLocation max, SerializableLocation a, SerializableLocation b) {
        this.name = name;

        this.copies = new ArrayList<>();
        this.loadouts = new ArrayList<>();
        this.min = min;
        this.max = max;
        this.a = a;
        this.b = b;
        this.buildHeight = (a.getY() + b.getY()) / 2;
        this.middle = new SerializableLocation(a.getWorld(), (a.getX() + b.getX()) / 2, buildHeight, (a.getZ() + b.getZ()) / 2);
    }

    public Arena(ConfigFile conf, String name) {
        final String key = "ARENAS." + name + ".";

        this.name = name;

        if (copies == null) {
            copies = new ArrayList<>();
        }

        ConfigurationSection section = conf.getConfigurationSection(key + "COPIES");
        if (section != null) {
            for (String copy : section.getKeys(false)) {
                copies.add(deserialize(conf.getString(key + "COPIES." + copy)));
            }
        }

        if (loadouts == null) {
            loadouts = new ArrayList<>();
        }
        loadouts.addAll(conf.getStringList(key + "LOADOUTS"));

        this.min = new SerializableLocation(conf.getString(key + "MIN"));
        this.max = new SerializableLocation(conf.getString(key + "MAX"));
        this.a = new SerializableLocation(conf.getString(key + "A"));
        this.b = new SerializableLocation(conf.getString(key + "B"));
        this.buildHeight = conf.getDouble(key + "BUILDHEIGHT");
    }

    public void save(ConfigFile conf) {
        final String key = "ARENAS." + name + ".";

        conf.set(key + "NAME", name);

        if (copies != null && !copies.isEmpty()) {
            for (Arena copy : copies) {
                conf.set(key + "COPIES." + copy.getName(), serialize());
            }
        }

        if (loadouts != null && !loadouts.isEmpty()) {
            conf.set(key + "LOADOUTS", loadouts);
        }

        conf.set(key + "MIN", min.serialize());
        conf.set(key + "MAX", max.serialize());
        conf.set(key + "A", a.serialize());
        conf.set(key + "B", b.serialize());
        conf.set(key + "BUILDHEIGHT", buildHeight);
    }

    public boolean isInside(Location loc) {
        double minX = Math.min(min.getX(), max.getX());
        double minZ = Math.min(min.getZ(), max.getZ());
        double maxX = Math.max(min.getX(), max.getX());
        double maxZ = Math.max(min.getZ(), max.getZ());

        return loc.getX() >= minX && loc.getX() <= maxX && loc.getZ() >= minZ && loc.getZ() <= maxZ;
    }

    public void generate(int amount) {
        ArenaGenerator generator = new ArenaGenerator(this);
        for (int i = 0; i < amount; i++) {
            generator.generate();
        }
    }

    public Cuboid getCuboid() {
        return new Cuboid(min, max);
    }

    public Arena getRandomCopy() {
        if (copies.isEmpty()) {
            return null;
        }

        return copies.remove(plugin.getRandom().nextInt(copies.size()));
    }

    public String serialize() {
        return name + ";" + min.serialize() + ";" + max.serialize() + ";" + a.serialize() + ";" + b.serialize();
    }

    public static Arena deserialize(String serialized) {
        String[] parts = serialized.split(";");
        return new Arena(
                parts[0],
                new SerializableLocation(parts[1]),
                new SerializableLocation(parts[2]),
                new SerializableLocation(parts[3]),
                new SerializableLocation(parts[4])
        );
    }
}
