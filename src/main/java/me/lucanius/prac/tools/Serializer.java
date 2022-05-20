package me.lucanius.prac.tools;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Lucanius
 * @since May 20, 2022
 */
@UtilityClass
public final class Serializer {

    @SneakyThrows
    public String serializeItems(final ItemStack[] items) {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

        dataOutput.writeInt(items.length);

        for (final ItemStack item : items) {
            dataOutput.writeObject(item);
        }

        dataOutput.close();

        return Base64Coder.encodeLines(outputStream.toByteArray());
    }

    @SneakyThrows
    public ItemStack[] deserializeItems(final String input) {
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(input));
        final BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);

        final ItemStack[] items = new ItemStack[dataInput.readInt()];

        for (int i = 0; i < items.length; i++) {
            items[i] = (ItemStack) dataInput.readObject();
        }

        dataInput.close();

        return items;
    }

    public String serializeEffects(final Collection<PotionEffect> effects) {
        final StringBuilder builder = new StringBuilder();

        effects.forEach(effect -> builder.append(serializeEffect(effect)).append(";"));

        return builder.toString();
    }

    public Collection<PotionEffect> deserializeEffects(final String input) {
        if (!input.contains(";")) {
            return null;
        }

        final Collection<PotionEffect> effects = new ArrayList<>();
        final String[] split = input.split(";");
        for (final String piece : split) {
            effects.add(deserializeEffect(piece));
        }

        return effects;
    }

    public String serializeEffect(final PotionEffect effect) {
        if (effect == null) {
            return "null";
        }

        return "n@" + effect.getType().getName() + ":d@" + effect.getDuration() + ":a@" + effect.getAmplifier();
    }

    public PotionEffect deserializeEffect(final String input) {
        if (input.equals("null")) {
            return null;
        }

        String name = "";
        String duration = "";
        String amplifier = "";

        final String[] split = input.split(":");
        for (final String info : split) {
            final String[] attr = info.split("@");
            final String attrSplit = attr[0];
            if (attrSplit.equalsIgnoreCase("n")) {
                name = attr[1];
            }
            if (attrSplit.equalsIgnoreCase("d")) {
                duration = attr[1];
            }
            if (attrSplit.equalsIgnoreCase("a")) {
                amplifier = attr[1];
            }
        }

        return new PotionEffect(PotionEffectType.getByName(name), Integer.parseInt(duration), Integer.parseInt(amplifier));
    }
}
