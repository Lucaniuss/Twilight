package me.lucanius.twilight.service.profile.modules;

import lombok.Getter;
import lombok.Setter;
import me.lucanius.twilight.service.loadout.Loadout;
import me.lucanius.twilight.service.loadout.personal.PersonalLoadout;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Lucanius
 * @since May 27, 2022
 */
@Getter @Setter
public class EditorProfile {

    private final Map<String, PersonalLoadout[]> loadouts;

    private PersonalLoadout editingLoadout;
    private Loadout selectedLoadout;

    private boolean editing;
    private boolean renaming;

    public EditorProfile(Collection<Loadout> all) {
        loadouts = new HashMap<>();
        all.forEach(loadout -> loadouts.put(loadout.getName(), new PersonalLoadout[4]));

        editingLoadout = null;
        selectedLoadout = null;

        editing = false;
        renaming = false;
    }

    public PersonalLoadout[] getAll(String loadout) {
        return loadouts.get(loadout);
    }

    public PersonalLoadout get(String loadout, int number) {
        return loadouts.get(loadout)[number];
    }

    public void replace(String loadout, int number, PersonalLoadout personalLoadout) {
        PersonalLoadout[] personalLoadouts = loadouts.get(loadout);
        personalLoadouts[number] = personalLoadout;

        loadouts.put(loadout, personalLoadouts);
    }

    public void destroy(String loadout, PersonalLoadout personalLoadout) {
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

    public boolean has(String loadout) {
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

    public int size(String loadout) {
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
