package me.lucanius.twilight.tools.config;

import me.lucanius.twilight.tools.CC;
import me.lucanius.twilight.tools.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * I don't know who the author is.
 * @edited Lucanius
 */
public class ConfigFile extends YamlConfiguration {

    private final Plugin plugin;
    private final String name;

    private File file;

    public ConfigFile(Plugin plugin, String name) {
        this.file = new File(plugin.getDataFolder(), name);
        this.plugin = plugin;
        this.name = name;

        if (!this.file.exists()) {
            plugin.saveResource(name, false);
        }

        try {
            this.load(this.file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void load() {
        this.file = new File(plugin.getDataFolder(), name);

        if (!this.file.exists()) {
            plugin.saveResource(name, false);
        }
        try {
            this.load(this.file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            this.save(this.file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getInt(String path) {
        return super.getInt(path, 0);
    }

    @Override
    public double getDouble(String path) {
        return super.getDouble(path, 0.0);
    }

    @Override
    public boolean getBoolean(String path) {
        return super.getBoolean(path, false);
    }

    public String getString(String path, boolean ignored) {
        return super.getString(path, null);
    }

    @Override
    public String getString(String path) {
        return CC.translate(super.getString(path, "&cMissing configuration option at path '&e" + path + "&c'."));
    }

    @Override
    public List<String> getStringList(String path) {
        return super.getStringList(path).stream().map(CC::translate).collect(Collectors.toList());
    }

    public List<String> getStringList(String path, boolean ignored) {
        if (!super.contains(path)) {
            return null;
        }

        return CC.translate(super.getStringList(path));
    }

    public List<String> getStringList(String path, List<String> def) {
        if (!super.contains(path)) {
            return def;
        }

        return CC.translate(super.getStringList(path));
    }

    public ItemStack getItem(String path) {
        return new ItemBuilder(Material.valueOf(getString(path + ".MATERIAL"))).setData(getInt(path + ".DATA")).setName(getString(path + ".NAME")).setLore(getStringList(path + ".LORE")).hideAllFlags().build();
    }

    public ItemStack getUnbreakableItem(String path) {
        return new ItemBuilder(Material.valueOf(getString(path + ".MATERIAL"))).setData(getInt(path + ".DATA")).setName(getString(path + ".NAME")).setLore(getStringList(path + ".LORE")).setUnbreakable(true).hideAllFlags().build();
    }
}
