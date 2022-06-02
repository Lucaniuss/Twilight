package me.lucanius.twilight.service.profile.modules;

import me.lucanius.twilight.Twilight;
import me.lucanius.twilight.service.profile.modules.stats.Stat;
import me.lucanius.twilight.service.profile.modules.stats.StatContext;
import me.lucanius.twilight.tools.functions.Voluntary;
import org.bson.Document;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Lucanius
 * @since June 01, 2022.
 * Twilight - All Rights Reserved.
 */
public class StatsProfile {

    private final static Twilight plugin = Twilight.getInstance();
    private final static StatContext[] contexts = StatContext.values();

    private final Map<Document, Stat> stats = new HashMap<>();

    public void loadNulls() {
        plugin.getLoadouts().getAll().forEach(loadout -> {
            for (StatContext context : contexts) {
                stats.put(new Document(), new Stat(context, loadout.getName()));
            }
        });
    }

    public void load(Document document) {
        document.keySet().forEach(k -> {
            Document doc = (Document) document.get(k);
            for (StatContext context : contexts) {
                stats.put(doc, new Stat(context, k, doc.getInteger(context.getDatabaseKey(), context.getDefaultValue())));
            }
        });
    }

    public Document save() {
        Document document = new Document();

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
}
