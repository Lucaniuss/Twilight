package me.lucanius.twilight.service.queue.abstr;

import lombok.Getter;
import lombok.Setter;
import me.lucanius.twilight.Twilight;
import me.lucanius.twilight.service.loadout.Loadout;
import me.lucanius.twilight.service.queue.callback.QueueCallback;
import me.lucanius.twilight.tools.CC;
import me.lucanius.twilight.tools.date.DateTools;

/**
 * @author Lucanius
 * @since May 22, 2022
 */
@Getter @Setter
public abstract class AbstractQueueData<E> {

    protected final static Twilight plugin = Twilight.getInstance();
    protected final static String[] messages = new String[]{
            " ",
            CC.MAIN + CC.BOLD + "Searching for a game...",
            CC.ICON + CC.WHITE + "Loadout: " + CC.SECOND + "<loadout>",
            " "
    };

    protected final E element;

    protected final Loadout loadout;
    protected final AbstractQueue<?> queue;

    private final long timeStamp;
    private final int playerPing, pingRange;

    private int minPing, maxPing;

    public AbstractQueueData(E element, Loadout loadout, AbstractQueue<?> queue, int playerPing, int pingRange) {
        this.element = element;

        this.loadout = loadout;
        this.queue = queue;

        this.timeStamp = System.currentTimeMillis();
        this.playerPing = playerPing;
        this.pingRange = pingRange;

        this.minPing = 0;
        this.maxPing = playerPing + pingRange;
    }

    public abstract void sendMessage();

    public abstract void dequeue();

    public abstract void dequeue(QueueCallback callback);

    public boolean hasRange() {
        return pingRange != -1;
    }

    public void increaseRange() {
        maxPing = Math.min(maxPing + 20, 1500);
        minPing = Math.max(minPing - 20, 0);
    }

    public String getTime() {
        return DateTools.formatIntToMMSS((int) ((System.currentTimeMillis() - timeStamp) / 1000L));
    }
}
