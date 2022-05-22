package me.lucanius.twilight.service.loadout;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import me.lucanius.twilight.service.loadout.type.LoadoutType;
import me.lucanius.twilight.tools.Serializer;
import me.lucanius.twilight.tools.config.ConfigFile;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Lucanius
 * @since May 20, 2022
 */
@Getter @Setter
@AllArgsConstructor @RequiredArgsConstructor
public class Loadout {

    private final String name;

    private int slot = 0;
    private ItemStack icon = new ItemStack(Material.WOOD_SWORD);

    private ItemStack[] contents = new ItemStack[36];
    private ItemStack[] armor = new ItemStack[4];
    private ItemStack[] edit = new ItemStack[36];
    private ItemStack[] refill = new ItemStack[10];

    private Collection<PotionEffect> effects = new ArrayList<>();

    private LoadoutType type = LoadoutType.NONE;

    private boolean ranked = false;
    private boolean noDamage = false;
    private boolean noDrop = false;
    private boolean noFall = false;
    private boolean noHunger = false;
    private boolean noRegen = false;
    private boolean healthShow = false;

    private String kbProfile = "";

    public Loadout(ConfigFile conf, String name) {
        final String key = "LOADOUTS." + name + ".";

        this.name = name;

        this.slot = conf.getInt(key + "SLOT");
        this.icon = conf.getItemStack(key + "ICON");

        this.contents = Serializer.deserializeItems(conf.getString(key + "CONTENTS"));
        this.armor = Serializer.deserializeItems(conf.getString(key + "ARMOR"));
        this.edit = Serializer.deserializeItems(conf.getString(key + "EDIT"));
        this.refill = Serializer.deserializeItems(conf.getString(key + "REFILL"));

        this.effects = Serializer.deserializeEffects(conf.getString(key + "EFFECTS"));

        this.type = LoadoutType.getOrDefault(conf.getString(key + "TYPE"));

        this.ranked = conf.getBoolean(key + "RANKED");
        this.noDamage = conf.getBoolean(key + "NODAMAGE");
        this.noDrop = conf.getBoolean(key + "NODROP");
        this.noFall = conf.getBoolean(key + "NOFALL");
        this.noHunger = conf.getBoolean(key + "NOHUNGER");
        this.noRegen = conf.getBoolean(key + "NOREGEN");
        this.healthShow = conf.getBoolean(key + "HEALTHSHOW");

        this.kbProfile = conf.getString(key + "KBPROFILE");
    }

    public void save(ConfigFile conf) {
        final String key = "LOADOUTS." + name + ".";

        conf.set(key + "NAME", name);

        conf.set(key + "SLOT", slot);
        conf.set(key + "ICON", icon);

        conf.set(key + "CONTENTS", Serializer.serializeItems(contents));
        conf.set(key + "ARMOR", Serializer.serializeItems(armor));
        conf.set(key + "EDIT", Serializer.serializeItems(edit));
        conf.set(key + "REFILL", Serializer.serializeItems(refill));

        conf.set(key + "EFFECTS", Serializer.serializeEffects(effects));

        conf.set(key + "TYPE", type.name());

        conf.set(key + "RANKED", ranked);
        conf.set(key + "NODAMAGE", noDamage);
        conf.set(key + "NODROP", noDrop);
        conf.set(key + "NOFALL", noFall);
        conf.set(key + "NOHUNGER", noHunger);
        conf.set(key + "NOREGEN", noRegen);
        conf.set(key + "HEALTHSHOW", healthShow);

        conf.set(key + "KBPROFILE", kbProfile);
    }

    public void apply(Player player) {
        final PlayerInventory inv = player.getInventory();

        inv.setContents(contents);
        inv.setArmorContents(armor);

        if (effects != null && !effects.isEmpty()) {
            effects.forEach(player::addPotionEffect);
        }

        player.updateInventory();
    }
}
