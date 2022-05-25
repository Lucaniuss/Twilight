package me.lucanius.twilight.service.loadout;

import me.lucanius.twilight.Twilight;
import me.lucanius.twilight.tools.config.ConfigFile;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * @author Lucanius
 * @since May 20, 2022
 */
public class LoadoutService {

    private final Twilight plugin;
    private final ConfigFile config;
    private final Set<Loadout> loadouts;

    public LoadoutService(Twilight plugin) {
        this.plugin = plugin;
        this.config = new ConfigFile(plugin, "loadouts.yml");
        this.loadouts = new HashSet<>();

        load();
    }

    public void load() {
        loadouts.clear();
        Optional.ofNullable(config.getConfigurationSection("LOADOUTS")).ifPresent(section ->
                section.getKeys(false).forEach(key -> loadouts.add(new Loadout(config, key))));
    }

    public void save() {
        config.set("LOADOUTS", null);
        loadouts.forEach(loadout ->
                loadout.save(config)
        );
        config.save();
    }

    public Loadout get(String name) {
        return loadouts.stream().filter(loadout -> loadout.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public Collection<Loadout> getAll() {
        return loadouts;
    }

    public Loadout build(String name) {
        Loadout loadout = new Loadout(name);
        loadouts.add(loadout);
        return loadout;
    }

    public void destroy(Loadout loadout) {
        loadouts.remove(loadout);
    }

    public void register(Loadout loadout) {
        loadouts.add(loadout);
    }

    public void reload() {
        save();
        load();
    }
}
