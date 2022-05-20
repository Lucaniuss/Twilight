package me.lucanius.prac.tools.board;

import lombok.SneakyThrows;
import me.lucanius.prac.Twilight;
import me.lucanius.prac.tools.CC;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.Collections;
import java.util.List;

/**
 * @author ThatKawaiiSam from his Assemble project
 * @edited Lucanius
 */
public class BoardThread extends Thread {

    private final Twilight plugin;
    private final Board board;
    private final long sleepTime;

    public BoardThread(Twilight plugin, Board board, long ticks) {
        this.plugin = plugin;
        this.board = board;
        this.sleepTime = (ticks * 50);

        this.start();
    }

    @Override @SneakyThrows
    public void run() {
        while (true) {
            tick();
            sleep(sleepTime);
        }
    }

    private void tick() {
        plugin.getOnline().forEach(player -> {
            final BoardManager board = this.board.getBoards().get(player.getUniqueId());
            if (board == null) {
                return;
            }

            final Scoreboard scoreboard = board.getScoreboard();
            final Objective objective = board.getObjective();
            if (scoreboard == null || objective == null) {
                return;
            }

            final String title = CC.translate(this.board.getAdapter().getTitle(player));
            if (!objective.getDisplayName().equals(title)) {
                objective.setDisplayName(title);
            }

            List<String> newLines = this.board.getAdapter().getLines(player);
            if (newLines == null || newLines.isEmpty()) {
                board.getEntries().forEach(BoardEntry::remove);
                board.getEntries().clear();
            } else {
                if (newLines.size() > 15) {
                    newLines = newLines.subList(0, 15);
                }

                if (!this.board.getBoardStyle().isDescending()) {
                    Collections.reverse(newLines);
                }

                if (board.getEntries().size() > newLines.size()) {
                    for (int i = newLines.size(); i < board.getEntries().size(); i++) {
                        BoardEntry entry = board.getEntryAtPosition(i);
                        if (entry != null) {
                            entry.remove();
                        }
                    }
                }

                int cache = this.board.getBoardStyle().getStartNumber();
                for (int i = 0; i < newLines.size(); i++) {
                    BoardEntry entry = board.getEntryAtPosition(i);
                    String line = CC.translate(newLines.get(i));

                    if (entry == null) {
                        entry = new BoardEntry(board, line, i);
                    }

                    entry.setText(line);
                    entry.setup();
                    entry.send(this.board.getBoardStyle().isDescending() ? cache-- : cache++);
                }
            }

            if (player.getScoreboard() != scoreboard) {
                player.setScoreboard(scoreboard);
            }
        });
    }
}
