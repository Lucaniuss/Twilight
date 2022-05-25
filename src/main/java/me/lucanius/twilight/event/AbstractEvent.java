package me.lucanius.twilight.event;

import me.lucanius.twilight.Twilight;

/**
 * @author Clouke
 * @since 25.05.2022 14:33
 * © Twilight - All Rights Reserved
 */
public abstract class AbstractEvent {

    protected final Twilight plugin = Twilight.getInstance();
    private boolean cancelled;

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public boolean isCancelled() {
        return cancelled;
    }

}
