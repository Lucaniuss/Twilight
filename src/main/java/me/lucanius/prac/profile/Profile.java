package me.lucanius.prac.profile;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import lombok.Getter;
import lombok.Setter;
import me.lucanius.prac.Twilight;
import me.lucanius.prac.tools.Scheduler;
import me.lucanius.prac.tools.Tools;
import org.bson.Document;

import java.util.UUID;

/**
 * @author Lucanius
 * @since May 20, 2022
 */
@Getter @Setter
public class Profile {

    private final static Twilight plugin = Twilight.getInstance();
    private final static UpdateOptions options = new UpdateOptions().upsert(true);

    private final UUID uniqueId;

    private boolean loaded;

    public Profile(UUID uniqueId) {
        this.uniqueId = uniqueId;
        this.loaded = false;
    }

    public void load() {
        Document document = plugin.getMongo().getProfiles().find(Filters.eq("uniqueId", uniqueId.toString())).first();
        if (document == null) {
            Scheduler.runAsync(this::save);
            loaded = true;
            return;
        }

        Scheduler.runAsync(() -> load(document));
    }

    public void load(Document document) {
        loaded = true;
    }

    public void save() {
        if (!plugin.isDisabling() && !Tools.isAsync()) {
            Scheduler.runAsync(this::save);
            return;
        }

        Document document = new Document();

        plugin.getMongo().getProfiles().replaceOne(Filters.eq("uniqueId", uniqueId.toString()), document, options);
    }
}
