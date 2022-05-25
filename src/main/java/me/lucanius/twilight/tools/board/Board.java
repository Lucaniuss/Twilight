package me.lucanius.twilight.tools.board;

import lombok.Getter;
import lombok.Setter;
import me.lucanius.twilight.Twilight;
import me.lucanius.twilight.event.bukkit.Events;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ThatKawaiiSam from his Assemble project
 * @edited Lucanius
 */
@Getter @Setter
public class Board {

    private final Twilight plugin;
    private final Map<UUID, BoardManager> boards;

    private final ChatColor[] chatColorCache;

    private BoardAdapter adapter;

    private BoardThread thread;
    private BoardStyle boardStyle;

    public Board(Twilight plugin, BoardAdapter adapter) {
        this.plugin = plugin;
        this.adapter = adapter;

        this.chatColorCache = ChatColor.values();

        this.boards = new ConcurrentHashMap<>();

        this.thread = null;
        this.boardStyle = BoardStyle.MODERN;

        Events.subscribe(PlayerJoinEvent.class, event -> {
            final Player player = event.getPlayer();
            boards.put(player.getUniqueId(), new BoardManager(player, this));
        });

        Events.subscribe(PlayerQuitEvent.class, event -> boards.remove(event.getPlayer().getUniqueId()));

        this.setup();
    }

    public void setup() {
        if (this.thread != null) {
            this.thread.stop();
            this.thread = null;
        }

        plugin.getOnline().forEach(player ->
                this.boards.putIfAbsent(player.getUniqueId(), new BoardManager(player, this))
        );

        this.thread = new BoardThread(plugin, this, 2L);
    }

    public void cleanup() {
        if (this.thread != null) {
            this.thread.stop();
            this.thread = null;
        }

        this.boards.keySet().forEach(uniqueId -> {
            final Player player = this.plugin.getServer().getPlayer(uniqueId);
            if (player == null || !player.isOnline()) {
                return;
            }

            this.boards.remove(uniqueId);
            player.setScoreboard(this.plugin.getServer().getScoreboardManager().getNewScoreboard());
        });
    }
}
