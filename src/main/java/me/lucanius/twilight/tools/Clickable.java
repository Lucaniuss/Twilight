package me.lucanius.twilight.tools;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Clickable {

    private final List<TextComponent> components;

    public Clickable() {
        this.components = new ArrayList<>();
    }

    public Clickable(String msg) {
        this.components = new ArrayList<>();
        this.components.add(new TextComponent(msg));
    }

    public Clickable(String msg, String hoverMsg, String clickString) {
        this.components = new ArrayList<>();
        this.add(msg, hoverMsg, clickString);
    }

    public TextComponent add(String msg, String hoverMsg, String clickString) {
        TextComponent message = new TextComponent(msg);
        if (hoverMsg != null) {
            message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hoverMsg).create()));
        }

        if (clickString != null && !clickString.equals("")) {
            message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, clickString));
        }

        this.components.add(message);

        return message;
    }

    public void add(String message) {
        this.components.add(new TextComponent(message));
    }

    public void send(Player player) {
        player.spigot().sendMessage(this.components.toArray(new TextComponent[0]));
    }
}


