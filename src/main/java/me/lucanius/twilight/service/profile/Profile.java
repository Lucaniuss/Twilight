package me.lucanius.twilight.service.profile;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import lombok.Getter;
import lombok.Setter;
import me.lucanius.twilight.Twilight;
import me.lucanius.twilight.service.loadout.personal.PersonalLoadout;
import me.lucanius.twilight.service.profile.modules.EditorProfile;
import me.lucanius.twilight.service.profile.modules.GameProfile;
import me.lucanius.twilight.tools.Scheduler;
import me.lucanius.twilight.tools.Tools;
import org.bson.Document;

import java.util.HashMap;
import java.util.Map;
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
    private final String idToString;
    private final Map<String, PersonalLoadout[]> loadouts;

    private ProfileState state;
    private GameProfile gameProfile;
    private EditorProfile editorProfile;

    private int pingRange;
    private boolean loaded;

    public Profile(UUID uniqueId) {
        this.uniqueId = uniqueId;
        this.idToString = uniqueId.toString();
        this.loadouts = new HashMap<>();

        this.state = ProfileState.LOBBY;
        this.gameProfile = new GameProfile();
        this.editorProfile = new EditorProfile(plugin.getLoadouts().getAll());

        this.pingRange = -1;

        this.loaded = false;
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
