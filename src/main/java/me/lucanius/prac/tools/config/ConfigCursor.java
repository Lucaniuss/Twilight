package me.lucanius.prac.tools.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * I don't know who the author is.
 * @edited Lucanius
 */
@Getter @AllArgsConstructor
public class ConfigCursor {

    private final ConfigFile config;
    private final String path;

    public String getString(String path) {
        return this.config.getString(((this.path == null) ? "" : (this.path + ".")) + "." + path);
    }

    public String getUncoloredString(String path) {
        return this.config.getString(((this.path == null) ? "" : (this.path + ".")) + "." + path, true);
    }

    public List<String> getStringList(String path) {
        return this.config.getStringList(((this.path == null) ? "" : (this.path + ".")) + "." + path);
    }

    public boolean getBoolean(String path) {
        return this.config.getBoolean(((this.path == null) ? "" : (this.path + ".")) + "." + path);
    }

    public int getInt(String path) {
        return this.config.getInt(((this.path == null) ? "" : (this.path + ".")) + "." + path);
    }

    public double getDouble(String path) {
        return this.config.getDouble(((this.path == null) ? "" : (this.path + ".")) + "." + path);
    }

    public double getDouble(String path, double defaultValue) {
        return this.config.getDouble(((this.path == null) ? "" : (this.path + ".")) + "." + path, defaultValue);
    }

    public long getLong(String path) {
        return this.config.getLong(((this.path == null) ? "" : (this.path + ".")) + "." + path);
    }

    public UUID getUUID(String path) {
        return UUID.fromString(this.config.getString(this.path + "." + path));
    }

    public World getBukkitWorld(String path) {
        return Bukkit.getWorld(this.config.getString(this.path + "." + path));
    }

    public void set(String path, Object value) {
        this.config.set(this.path + ((path == null) ? "" : ("." + path)), value);
    }

    public void set(Object value) {
        set(null, value);
    }

    public boolean contains(String path) {
        return this.config.contains(this.path + ((path == null) ? "" : ("." + path)));
    }

    public boolean contains() {
        return contains(null);
    }

    public Set<String> getKeys(String path) {
        return this.config.getConfigurationSection(this.path + ((path == null) ? "" : ("." + path))).getKeys(false);
    }

    public Set<String> getKeys() {
        return getKeys(null);
    }

    public void save() {
        this.config.save();
    }

    public ItemStack getItem() {
        return this.config.getItem(this.path);
    }
}
