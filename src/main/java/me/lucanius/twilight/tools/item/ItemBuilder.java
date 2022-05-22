package me.lucanius.twilight.tools.item;

import me.lucanius.twilight.tools.CC;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Lucanius
 * @since May 20, 2022
 */
public class ItemBuilder {

    private final ItemStack itemStack;

    public ItemBuilder(ItemStack item) {
        this.itemStack = item;
    }

    public ItemBuilder(Material material) {
        this.itemStack = new ItemStack(material);
    }

    public final ItemBuilder setName(String name) {
        final ItemMeta itemMeta = this.itemStack.getItemMeta();

        itemMeta.setDisplayName(CC.translate(name));
        this.itemStack.setItemMeta(itemMeta);

        return this;
    }

    public final ItemBuilder setLore(List<String> lore) {
        final ItemMeta itemMeta = this.itemStack.getItemMeta();

        itemMeta.setLore(CC.translate(lore));
        this.itemStack.setItemMeta(itemMeta);

        return this;
    }

    public final ItemBuilder setLore(String lore) {
        final ItemMeta itemMeta = this.itemStack.getItemMeta();

        itemMeta.setLore(Collections.singletonList(CC.translate(lore)));
        this.itemStack.setItemMeta(itemMeta);

        return this;
    }

    public final ItemBuilder addLore(String string) {
        final ItemMeta itemMeta = this.itemStack.getItemMeta();
        final List<String> lore = itemMeta.hasLore() ? itemMeta.getLore() : new ArrayList<>();

        lore.add(CC.translate(string));
        itemMeta.setLore(lore);
        this.itemStack.setItemMeta(itemMeta);

        return this;
    }

    public final ItemBuilder addLore(List<String> lore) {
        final ItemMeta itemMeta = this.itemStack.getItemMeta();
        final List<String> newLore = itemMeta.hasLore() ? itemMeta.getLore() : new ArrayList<>();

        newLore.addAll(CC.translate(lore));
        itemMeta.setLore(newLore);
        this.itemStack.setItemMeta(itemMeta);

        return this;
    }

    public final ItemBuilder setData(int data) {
        this.itemStack.setDurability((short) data);

        return this;
    }

    public final ItemBuilder setAmount(int amount) {
        this.itemStack.setAmount(amount);

        return this;
    }

    public final ItemBuilder addEnchant(Enchantment enchantment, int level) {
        this.itemStack.addUnsafeEnchantment(enchantment, level);

        return this;
    }

    public final ItemBuilder addEnchant(Enchantment enchantment) {
        return this.addEnchant(enchantment, 1);
    }

    public final ItemBuilder hideAllFlags() {
        final ItemMeta itemMeta = this.itemStack.getItemMeta();

        itemMeta.addItemFlags(ItemFlag.values());
        this.itemStack.setItemMeta(itemMeta);

        return this;
    }

    public final ItemBuilder setUnbreakable(boolean b) {
        final ItemMeta itemMeta = this.itemStack.getItemMeta();

        itemMeta.spigot().setUnbreakable(b);
        this.itemStack.setItemMeta(itemMeta);

        return this;
    }

    public final ItemBuilder setType(Material material) {
        this.itemStack.setType(material);

        return this;
    }

    public final ItemBuilder removeLore() {
        final ItemMeta itemMeta = this.itemStack.getItemMeta();

        itemMeta.setLore(new ArrayList<>());
        this.itemStack.setItemMeta(itemMeta);

        return this;
    }

    public final ItemBuilder removeEnchants() {
        this.itemStack.getEnchantments().keySet().forEach(this.itemStack::removeEnchantment);

        return this;
    }

    public final ItemBuilder setOwner(String owner) {
        if (this.itemStack.getType() != Material.SKULL_ITEM) {
            throw new IllegalArgumentException("setOwner is only applicable for Material.SKULL_ITEM!");
        }

        final SkullMeta skullMeta = (SkullMeta) this.itemStack.getItemMeta();

        skullMeta.setOwner(owner);
        this.itemStack.setItemMeta(skullMeta);

        return this;
    }

    public final ItemBuilder setColor(Color color) {
        if (!this.itemStack.getType().name().startsWith("LEATHER_")) {
            throw new IllegalArgumentException("setColor is only applicable for leather armor!");
        }

        final LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) this.itemStack.getItemMeta();

        leatherArmorMeta.setColor(color);
        this.itemStack.setItemMeta(leatherArmorMeta);

        return this;
    }

    public final ItemBuilder shine() {
        final ItemMeta itemMeta = this.itemStack.getItemMeta();
        itemMeta.addEnchant(Enchantment.DURABILITY, 1, true);
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        this.itemStack.setItemMeta(itemMeta);
        return this;
    }

    public final ItemStack build() {
        return this.itemStack;
    }
}
