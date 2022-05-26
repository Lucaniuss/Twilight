package me.lucanius.twilight.service.cooldown;

import me.lucanius.twilight.Twilight;
import me.lucanius.twilight.tools.functions.pair.Triad;
import me.lucanius.twilight.tools.functions.pair.Triads;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Lucanius
 * @since May 26, 2022
 */
public class CooldownService {

    private final Twilight plugin;
    private final List<Triad<UUID, String, Cooldown>> cooldowns;

    public CooldownService(Twilight plugin) {
        this.plugin = plugin;
        this.cooldowns = new ArrayList<>();
    }

    public Cooldown get(UUID uuid, String name) {
        return cooldowns.stream().filter(t -> t.getA().equals(uuid) && t.getB().equals(name)).findFirst().map(Triad::getC).orElse(null);
    }

    public boolean contains(UUID uuid, String name) {
        return cooldowns.stream().anyMatch(t -> t.getA().equals(uuid) && t.getB().equals(name));
    }

    public void add(UUID uniqueId, String name, Cooldown cooldown) {
        this.cooldowns.add(Triads.of(uniqueId, name, cooldown));
    }

    public void clear(UUID uuid, String name) {
        Optional.ofNullable(get(uuid, name)).ifPresent(Cooldown::cancel);
    }
}
