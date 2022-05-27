package me.lucanius.twilight.service.loadout;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import me.lucanius.twilight.service.loadout.personal.PersonalLoadout;
import me.lucanius.twilight.service.loadout.type.LoadoutType;
import me.lucanius.twilight.service.profile.Profile;
import me.lucanius.twilight.service.profile.modules.GameProfile;
import me.lucanius.twilight.tools.CC;
import me.lucanius.twilight.tools.Serializer;
import me.lucanius.twilight.tools.Tools;
import me.lucanius.twilight.tools.config.ConfigFile;
import org.bukkit.ChatColor;
import org.bukkit.Color;
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

        if (conf.contains(key + "EFFECTS") && !conf.getString(key + "EFFECTS").equals("")) {
            this.effects = Serializer.deserializeEffects(conf.getString(key + "EFFECTS"));
        } else {
            this.effects = new ArrayList<>();
        }

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

        if (effects != null && !effects.isEmpty()) {
            conf.set(key + "EFFECTS", Serializer.serializeEffects(effects));
        } else {
            conf.set(key + "EFFECTS", "");
        }

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

    public boolean isBuild() {
        return type == LoadoutType.BRIDGES;
    }

    public boolean needsMovement() {
        return (type == LoadoutType.BRIDGES || type == LoadoutType.SUMO);
    }

    public void apply(Player player) {
        apply(player, contents, armor);
    }

    public void apply(Player player, ItemStack[] current) {
        apply(player, current, armor);
    }

    public void apply(Player player, ItemStack[] current, ItemStack[] armor) {
        PlayerInventory inv = player.getInventory();

        inv.setContents(current);
        inv.setArmorContents(armor);

        if (effects != null && !effects.isEmpty()) {
            effects.forEach(player::addPotionEffect);
        }

        player.updateInventory();
    }

    public void apply(Player player, Profile profile) {
        if (type != LoadoutType.BRIDGES) {
            apply(player);
            return;
        }

        GameProfile gameProfile = profile.getGameProfile();
        ItemStack[] current = gameProfile.getPersonalContents();

        ItemStack[] playerContents = current != null ? current : contents;
        ItemStack[] playerArmor = armor;

        Color color = gameProfile.getTeam().getColor() == ChatColor.BLUE ? Color.BLUE : gameProfile.getTeam().getColor() == ChatColor.RED ? Color.RED : Color.WHITE;
        int data = color == Color.BLUE ? 11 : color == Color.RED ? 14 : 0;

        int i = 0;
        ItemStack[] finalContents = Tools.getColoredItems(playerContents, data, i);
        ItemStack[] finalArmor = Tools.getColoredArmor(playerArmor, color, i);

        gameProfile.setPersonalContents(finalContents);

        apply(player, finalArmor, finalArmor);
    }

    public void apply(Player player, Profile profile, PersonalLoadout loadout) {
        ItemStack[] playerContents = loadout.getContents() != null ? loadout.getContents() : contents;
        ItemStack[] playerArmor = armor;

        GameProfile gameProfile = profile.getGameProfile();
        Color color = gameProfile.getTeam().getColor() == ChatColor.BLUE ? Color.BLUE : gameProfile.getTeam().getColor() == ChatColor.RED ? Color.RED : Color.WHITE;
        int data = color == Color.BLUE ? 11 : color == Color.RED ? 14 : 0;

        int i = 0;
        ItemStack[] finalContents = type == LoadoutType.BRIDGES ? Tools.getColoredItems(playerContents, data, i) : playerContents;
        ItemStack[] finalArmor = type == LoadoutType.BRIDGES ? Tools.getColoredArmor(playerArmor, color, i) : playerArmor;

        gameProfile.setPersonalContents(finalContents);

        apply(player, finalArmor, finalArmor);

        player.sendMessage(CC.SECOND + "Successfully applied your " + CC.MAIN + loadout.getDisplayName() + CC.SECOND + " loadout!");
    }
}
