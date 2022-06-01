package me.lucanius.twilight.event;

import lombok.Getter;
import lombok.Setter;
import me.lucanius.twilight.Twilight;

/**
 * @author Clouke
 * @since 25.05.2022 14:33
 * © Twilight - All Rights Reserved
 */
@Getter @Setter
public abstract class AbstractEvent implements TwilightEvent {

    protected final static Twilight plugin = Twilight.getInstance();

    private boolean cancelled = false;

    public AbstractEvent() {
        plugin.getEvents().publish(this);
    }
}
