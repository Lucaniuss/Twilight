package me.lucanius.twilight.service.profile.modules;

import lombok.Getter;
import lombok.Setter;
import me.lucanius.twilight.Twilight;
import me.lucanius.twilight.service.loadout.Loadout;
import me.lucanius.twilight.service.profile.modules.stats.Stat;
import me.lucanius.twilight.service.profile.modules.stats.StatContext;
import me.lucanius.twilight.tools.Scheduler;
import me.lucanius.twilight.tools.functions.Voluntary;
import org.bson.Document;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Lucanius
 * @since June 01, 2022.
 * Twilight - All Rights Reserved.
 */
@Getter @Setter
public class StatsProfile {

    private final static Twilight plugin = Twilight.getInstance();
    private final static StatContext[] contexts = StatContext.values();

    private final Map<Document, Stat> stats;

    private int gamesPlayed;
    private int globalWins;
    private int globalLosses;
    private int globalElo;

    public StatsProfile(Collection<Loadout> all) {
        stats = new HashMap<>();

        gamesPlayed = 0;
        globalWins = 0;
        globalLosses = 0;
        globalElo = 1000;

        all.forEach(loadout -> {
            for (StatContext context : contexts) {
                stats.put(new Document(), new Stat(context, loadout.getName()));
            }
        });
    }

    public void load(Document document) {
        gamesPlayed = document.getInteger("gamesPlayed", 0);
        globalWins = document.getInteger("globalWins", 0);
        globalLosses = document.getInteger("globalLosses", 0);
        globalElo = document.getInteger("globalElo", 1000);

        document.keySet().forEach(k -> {
            Document doc = (Document) document.get(k);
            if (doc == null) {
                return;
            }

            for (StatContext context : contexts) {
                stats.put(doc, new Stat(context, k, doc.getInteger(context.getDatabaseKey(), context.getDefaultValue())));
            }
        });
    }

    public Document save() {
        Document document = new Document();

        document.put("gamesPlayed", gamesPlayed);
        document.put("globalWins", globalWins);
        document.put("globalLosses", globalLosses);
        document.put("globalElo", recalculate());

        stats.forEach((doc, stat) ->
                document.put(stat.getLoadoutName(), doc.append(stat.getContext().getDatabaseKey(), stat.getValue()))
        );

        return document;
    }

    public Stat get(StatContext context, String name) {
        return stats.values().stream()
                .filter(stat -> stat.getContext().equals(context))
                .filter(stat -> stat.getLoadoutName().equalsIgnoreCase(name))
                .findFirst().orElse(null);
    }

    public Stat getOrCreate(StatContext context, String name) {
        Stat stat = Voluntary.ofNull(get(context, name)).orElse(new Stat(context, name));
        if (!stats.containsValue(stat)) {
            stats.put(new Document(), stat);
        }

        return stat;
    }

    public Map<Document, Stat> getAll() {
        return stats;
    }

    public int getWins(String name) {
        return getOrCreate(StatContext.WINS, name).getValue();
    }

    public void setWins(String name, int wins) {
        getOrCreate(StatContext.WINS, name).setValue(wins);
    }

    public int getLosses(String name) {
        return getOrCreate(StatContext.LOSSES, name).getValue();
    }

    public void setLosses(String name, int losses) {
        getOrCreate(StatContext.LOSSES, name).setValue(losses);
    }

    public int getElo(String name) {
        return getOrCreate(StatContext.ELO, name).getValue();
    }

    public void setElo(String name, int elo) {
        getOrCreate(StatContext.ELO, name).setValue(elo);
        Scheduler.runAsync(this::recalculate);
    }

    public int recalculate() {
        int elo = 0;
        int count = 0;

        for (Loadout loadout : plugin.getLoadouts().getAll()) {
            Stat stat = get(StatContext.ELO, loadout.getName());
            if (stat != null) {
                elo += stat.getValue();
                count++;
            }
        }

        if (count == 0) {
            count = 1;
        }

        return globalElo = (int) Math.round((double) elo / count);
    }
}
