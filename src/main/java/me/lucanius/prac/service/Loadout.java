package me.lucanius.prac.service;

import javafx.print.PageLayout;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
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
@Getter @Setter @AllArgsConstructor
public class Loadout {

    private final String name;

    private int slot;
    private ItemStack icon;

    private ItemStack[] contents;
    private ItemStack[] armor;
    private ItemStack[] edit;
    private ItemStack[] refill;

    private Collection<PotionEffect> effects;

    private LoadoutType type;

    private boolean ranked;
    private boolean noDamage;
    private boolean noDrop;
    private boolean noFall;
    private boolean noHunger;
    private boolean noRegen;
    private boolean healthShow;

    private String kbProfile;

    public Loadout(String name) {
        this.name = name;

        this.slot = 0;
        this.icon = new ItemStack(Material.WOOD_SWORD);

        this.contents = new ItemStack[36];
        this.armor = new ItemStack[4];
        this.edit = new ItemStack[36];
        this.refill = new ItemStack[10];

        this.effects = new ArrayList<>();

        this.type = LoadoutType.NONE;

        this.ranked = false;
        this.noDamage = false;
        this.noDrop = false;
        this.noFall = false;
        this.noHunger = false;
        this.noRegen = false;
        this.healthShow = false;

        this.kbProfile = "";
    }

    public void apply(Player player) {
        final PlayerInventory inv  = player.getInventory();

        inv.setContents(contents);
        inv.setArmorContents(armor);

        if (!effects.isEmpty()) {
            effects.forEach(player::addPotionEffect);
        }

        player.updateInventory();
    }
}
