package me.lucanius.prac.tools.board;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author ThatKawaiiSam from his Assemble project
 * @edited Lucanius
 */
@Getter
public class BoardManager {

    private final Board board;

    private final List<BoardEntry> entries = new ArrayList<>();
    private final List<String> identifiers = new ArrayList<>();

    private final UUID uniqueId;

    public BoardManager(Player player, Board board) {
        this.uniqueId = player.getUniqueId();
        this.board = board;
        this.setup(player);
    }

    public Scoreboard getScoreboard() {
        final Player player = Bukkit.getPlayer(getUniqueId());
        final ScoreboardManager manager = Bukkit.getScoreboardManager();

        if (player.getScoreboard() != manager.getMainScoreboard()) {
            return player.getScoreboard();
        } else {
            return manager.getNewScoreboard();
        }
    }

    public Objective getObjective() {
        final Scoreboard scoreboard = getScoreboard();
        final Objective objective;
        if (scoreboard.getObjective("Twilight") == null) {
            objective = scoreboard.registerNewObjective("Twilight", "dummy");
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);
            objective.setDisplayName(this.board.getAdapter().getTitle(Bukkit.getPlayer(uniqueId)));
        } else {
            objective = scoreboard.getObjective("Twilight");
        }

        return objective;
    }

    private void setup(Player player) {
        final Scoreboard scoreboard = getScoreboard();
        player.setScoreboard(scoreboard);

        this.getObjective();
    }

    public BoardEntry getEntryAtPosition(int pos) {
        return pos >= this.entries.size() ? null : this.entries.get(pos);
    }

    public String getUniqueIdentifier(int position) {
        String identifier = this.getRandomChatColor(position) + ChatColor.WHITE;
        while (this.identifiers.contains(identifier)) {
            identifier = identifier + this.getRandomChatColor(position) + ChatColor.WHITE;
        }

        if (identifier.length() > 16) {
            return this.getUniqueIdentifier(position);
        }

        this.identifiers.add(identifier);
        return identifier;
    }

    private String getRandomChatColor(int position) {
        return this.board.getChatColorCache()[position].toString();
    }
}
