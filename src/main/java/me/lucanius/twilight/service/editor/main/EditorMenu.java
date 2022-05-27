package me.lucanius.twilight.service.editor.main;

import lombok.Getter;
import me.lucanius.twilight.Twilight;
import me.lucanius.twilight.service.editor.main.listener.EditorListener;
import me.lucanius.twilight.service.loadout.Loadout;
import me.lucanius.twilight.service.loadout.personal.PersonalLoadout;
import me.lucanius.twilight.service.profile.modules.EditorProfile;
import me.lucanius.twilight.tools.CC;
import me.lucanius.twilight.tools.item.ItemBuilder;
import me.lucanius.twilight.tools.menu.Menu;
import me.lucanius.twilight.tools.menu.handlers.MenuSaver;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

/**
 * @author Lucanius
 * @since May 27, 2022
 */
@Getter
public class EditorMenu implements InventoryHolder {

    private final static Twilight plugin = Twilight.getInstance();
    private final static ItemStack save = new ItemBuilder(Material.INK_SACK).setData(10).setName(CC.GREEN + "Save").build();
    private final static ItemStack load = new ItemBuilder(Material.INK_SACK).setData(14).setName(CC.GOLD + "Reset").build();
    private final static ItemStack cancel = new ItemBuilder(Material.INK_SACK).setData(1).setName(CC.RED + "Cancel").build();
    private final static ItemStack placeholder = Menu.getGlass();
    private final static ItemStack air = new ItemStack(Material.AIR);
    private final static int size = 6 * 9;

    private static EditorListener listener;

    private final Player player;
    private final EditorProfile profile;
    private final PersonalLoadout personalLoadout;
    private final Loadout loadout;

    private Inventory inventory;

    public EditorMenu(Player player, EditorProfile profile, PersonalLoadout personalLoadout, Loadout loadout) {
        if (listener == null) {
            listener = new EditorListener();
        }

        this.player = player;
        this.profile = profile;
        this.personalLoadout = personalLoadout;
        this.loadout = loadout;
    }

    public void open() {
        inventory = plugin.getServer().createInventory(this, size, CC.GRAY + CC.ITALIC + "Editing " + personalLoadout.getDisplayName());

        inventory.setItem(40, save);
        inventory.setItem(41, load);
        inventory.setItem(42, cancel);

        ItemStack[] armor = loadout.getArmor();
        inventory.setItem(10, armor[3] != null ? armor[3] : air);
        inventory.setItem(19, armor[2] != null ? armor[2] : air);
        inventory.setItem(28, armor[1] != null ? armor[1] : air);
        inventory.setItem(37, armor[0] != null ? armor[0] : air);

        for (int i = 0; i < size; i++) {
            if (inventory.getItem(i) == null) {
                inventory.setItem(i, placeholder);
            }
        }

        for (int i = 12; i < 17; i++) {
            ItemStack stack = loadout.getRefill()[i - 12];
            inventory.setItem(i, stack != null ? stack : air);
        }
        for (int i = 21; i < 26; i++) {
            ItemStack stack = loadout.getRefill()[i - 16];
            inventory.setItem(i, stack != null ? stack : air);
        }

        MenuSaver.remove(player.getUniqueId());

        player.openInventory(inventory);

        profile.setEditing(true);

        player.getInventory().setContents(personalLoadout.getContents());
        player.updateInventory();
    }
}
