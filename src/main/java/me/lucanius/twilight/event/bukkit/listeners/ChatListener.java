package me.lucanius.twilight.event.bukkit.listeners;

import me.lucanius.twilight.Twilight;
import me.lucanius.twilight.event.bukkit.Events;
import me.lucanius.twilight.service.editor.overview.EditorOverviewMenu;
import me.lucanius.twilight.service.profile.Profile;
import me.lucanius.twilight.service.profile.modules.EditorProfile;
import me.lucanius.twilight.tools.CC;
import me.lucanius.twilight.tools.Scheduler;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.UUID;

/**
 * @author Lucanius
 * @since May 27, 2022
 */
public class ChatListener {

    private final Twilight plugin = Twilight.getInstance();

    public ChatListener() {
        Events.subscribe(AsyncPlayerChatEvent.class, event -> {
            Player player = event.getPlayer();
            UUID uniqueId = player.getUniqueId();
            Profile profile = plugin.getProfiles().get(uniqueId);
            EditorProfile editorProfile = profile.getEditorProfile();
            if (!editorProfile.isRenaming()) {
                return;
            }
            event.setCancelled(true);

            String message = event.getMessage();
            if (message.length() > 16) {
                player.sendMessage(CC.RED + "A loadout name cannot be longer than 16 characters..." + "\n" + CC.RED + "Try again...");
                return;
            }

            editorProfile.getEditingLoadout().setDisplayName(message);
            editorProfile.setRenaming(false);
            player.sendMessage(CC.SECOND + "Successfully set your loadout name to: " + CC.MAIN + message);

            Scheduler.runLater(() -> new EditorOverviewMenu(editorProfile.getSelectedLoadout(), editorProfile).open(player), 2L);
        });
    }
}
