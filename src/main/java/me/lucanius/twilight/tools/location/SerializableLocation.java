package me.lucanius.twilight.tools.location;

import lombok.Getter;
import lombok.Setter;
import me.lucanius.twilight.Twilight;
import org.bukkit.Location;
import org.bukkit.World;

/**
 * @author Lucanius
 * @since May 20, 2022
 */
@Getter @Setter
public class SerializableLocation {

    private final static Twilight plugin = Twilight.getInstance();

    private String world;

    private double x;
    private double y;
    private double z;

    private float yaw;
    private float pitch;

    private World bukkitWorld;
    private Location bukkitLocation;

    public SerializableLocation(String world, double x, double y, double z, float yaw, float pitch) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public SerializableLocation(double x, double y, double z) {
        this("world", x, y, z, 0.0f, 0.0f);
    }

    public SerializableLocation(double x, double y, double z, float yaw, float pitch) {
        this("world", x, y, z, yaw, pitch);
    }

    public SerializableLocation(String world, double x, double y, double z) {
        this(world, x, y, z, 0.0f, 0.0f);
    }

    public SerializableLocation(Location location) {
        this(location.getWorld().getName(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    }

    public SerializableLocation(String serialized) {
        String[] split = serialized.split(", ");
        this.world = split[0];
        this.x = Double.parseDouble(split[1]);
        this.y = Double.parseDouble(split[2]);
        this.z = Double.parseDouble(split[3]);
        this.yaw = Float.parseFloat(split[4]);
        this.pitch = Float.parseFloat(split[5]);
    }

    public String serialize() {
        return world + ", " + x + ", " + y + ", " + z + ", " + yaw + ", " + pitch;
    }

    public World getBukkitWorld() {
        return bukkitWorld = bukkitWorld != null ? bukkitWorld : world != null ? plugin.getServer().getWorld(world) : plugin.getServer().getWorlds().get(0);
    }

    public Location getBukkitLocation() {
        return bukkitLocation = bukkitLocation != null ? bukkitLocation : new Location(getBukkitWorld(), x, y, z, yaw, pitch);
    }
}
