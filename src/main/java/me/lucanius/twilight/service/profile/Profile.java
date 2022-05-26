package me.lucanius.twilight.service.profile;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import lombok.Getter;
import lombok.Setter;
import me.lucanius.twilight.Twilight;
import me.lucanius.twilight.service.loadout.personal.PersonalLoadout;
import me.lucanius.twilight.service.party.Party;
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

    private int pingRange;
    private boolean loaded;

    private Party partyInvite; // caching party invite
    private UUID partyLeader; // caching party leader

    public Profile(UUID uniqueId) {
        this.uniqueId = uniqueId;
        this.idToString = uniqueId.toString();
        this.loadouts = new HashMap<>();

        this.state = ProfileState.LOBBY;
        this.gameProfile = new GameProfile();

        this.pingRange = -1;

        this.loaded = false;

        plugin.getLoadouts().getAll().forEach(loadout -> loadouts.put(loadout.getName(), new PersonalLoadout[4]));
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

    public PersonalLoadout[] getPersonalLoadouts(String loadout) {
        return loadouts.get(loadout);
    }

    public PersonalLoadout getPersonalLoadout(String loadout, int number) {
        return loadouts.get(loadout)[number];
    }

    public void replacePersonalLoadout(String loadout, int number, PersonalLoadout personalLoadout) {
        PersonalLoadout[] personalLoadouts = loadouts.get(loadout);
        personalLoadouts[number] = personalLoadout;

        loadouts.put(loadout, personalLoadouts);
    }

    public void deletePersonalLoadout(String loadout, PersonalLoadout personalLoadout) {
        if (personalLoadout == null) {
            return;
        }
        PersonalLoadout[] kits = loadouts.get(loadout);
        for (int i = 0; i < 4; i++) {
            if (kits[i] != null && kits[i] == personalLoadout) {
                kits[i] = null;
                break;
            }
        }

        loadouts.put(loadout, kits);
    }

    public boolean hasPersonalLoadouts(String loadout) {
        boolean hasLoadout = false;
        PersonalLoadout[] kits = loadouts.get(loadout);
        for (int i = 0; i < 4; i++) {
            if (kits[i] != null) {
                hasLoadout = true;
                break;
            }
        }

        return hasLoadout;
    }

    public int getPersonalLoadoutSize(String loadout) {
        int amount = 0;
        PersonalLoadout[] kits = loadouts.get(loadout);
        for (int i = 0; i < 4; i++) {
            if (kits[i] != null) {
                amount += 1;
            }
        }

        return amount;
    }
}
