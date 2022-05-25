package me.lucanius.twilight.service.profile;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import lombok.Getter;
import lombok.Setter;
import me.lucanius.twilight.Twilight;
import me.lucanius.twilight.service.loadout.personal.PersonalLoadout;
import me.lucanius.twilight.service.profile.modules.GameProfile;
import me.lucanius.twilight.tools.Scheduler;
import me.lucanius.twilight.tools.Tools;
import org.bson.Document;

import java.util.*;

/**
 * @author Lucanius
 * @since May 20, 2022
 */
@Getter @Setter
public class Profile {

    private final static Twilight plugin = Twilight.getInstance();
    private final static UpdateOptions options = new UpdateOptions().upsert(true);

    private final UUID uniqueId;
    private final String idToString;
    private final List<PersonalLoadout[]> loadouts;

    private ProfileState state;
    private GameProfile gameProfile;

    private int pingRange;

    private boolean loaded;

    public Profile(UUID uniqueId) {
        this.uniqueId = uniqueId;
        this.idToString = uniqueId.toString();
        this.loadouts = new ArrayList<>();

        this.state = ProfileState.LOBBY;
        this.gameProfile = new GameProfile();

        this.pingRange = -1;

        this.loaded = false;

        plugin.getLoadouts().getAll().forEach(loadout -> loadouts.add(new PersonalLoadout[3]));
    }

    public void load() {
        Document document = plugin.getMongo().getProfiles().find(Filters.eq("uniqueId", idToString)).first();
        if (document == null) {
            Scheduler.runAsync(this::save);
            loaded = true;
            return;
        }

        Scheduler.runAsync(() -> load(document));
    }

    public void load(Document document) {
        if (document == null) {
            Scheduler.runAsync(this::save);
            loaded = true;
            return;
        }

        loaded = true;
    }

    public void save() {
        if (!plugin.isDisabling() && !Tools.isAsync()) {
            Scheduler.runAsync(this::save);
            return;
        }

        Document document = new Document("uniqueId", idToString);

        plugin.getMongo().getProfiles().replaceOne(Filters.eq("uniqueId", idToString), document, options);
    }
}
