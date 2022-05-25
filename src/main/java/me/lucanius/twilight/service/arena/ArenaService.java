package me.lucanius.twilight.service.arena;

import me.lucanius.twilight.Twilight;
import me.lucanius.twilight.tools.config.ConfigFile;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * @author Lucanius
 * @since May 24, 2022
 */
public class ArenaService {

    private final Twilight plugin;
    private final ConfigFile config;
    private final Set<Arena> arenas;

    public ArenaService(Twilight plugin) {
        this.plugin = plugin;
        this.config = new ConfigFile(plugin, "arenas.yml");
        this.arenas = new HashSet<>();

        load();
    }

    public void load() {
        arenas.clear();
        Optional.ofNullable(config.getConfigurationSection("ARENAS")).ifPresent(section ->
                section.getKeys(false).forEach(key -> arenas.add(new Arena(config, key))));
    }

    public void save() {
        config.set("ARENAS", null);
        arenas.forEach(arena ->
                arena.save(config)
        );
        config.save();
    }

    public Arena get(String name) {
        return arenas.stream().filter(arena -> arena.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public Collection<Arena> getAll() {
        return arenas;
    }

    public Arena build(String name) {
        Arena arena = new Arena(name);
        arenas.add(arena);
        return arena;
    }

    public void destroy(Arena arena) {
        arenas.remove(arena);
    }

    public void register(Arena arena) {
        arenas.add(arena);
    }

    public void reload() {
        save();
        load();
    }

    public Arena getRandom() {
        return arenas.stream().findAny().orElse(null);
    }
}
