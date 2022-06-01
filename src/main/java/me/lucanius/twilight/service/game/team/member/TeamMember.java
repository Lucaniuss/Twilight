package me.lucanius.twilight.service.game.team.member;

import lombok.Getter;
import lombok.Setter;
import me.lucanius.twilight.Twilight;
import me.lucanius.twilight.service.game.team.GameTeam;
import me.lucanius.twilight.service.profile.Profile;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * @author Lucanius
 * @since May 25, 2022
 */
@Getter @Setter
public class TeamMember {

    private final static Twilight plugin = Twilight.getInstance();

    private final UUID uniqueId;
    private final GameTeam team;

    private boolean alive;
    private Player player;
    private Profile profile;

    public TeamMember(UUID uniqueId, GameTeam team) {
        this.uniqueId = uniqueId;
        this.team = team;

        this.alive = true;
    }

    public Player getPlayer() {
        return player = player != null ? player : plugin.getServer().getPlayer(uniqueId);
    }

    public Profile getProfile() {
        return profile = profile != null ? profile : plugin.getProfiles().get(uniqueId);
    }
}
