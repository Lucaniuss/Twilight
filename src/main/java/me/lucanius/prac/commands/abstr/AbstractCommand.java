package me.lucanius.prac.commands.abstr;

import me.lucanius.prac.Twilight;
import me.lucanius.prac.tools.CC;
import me.lucanius.prac.tools.command.CommandArgs;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Lucanius
 * @since May 20, 2022
 */
public abstract class AbstractCommand {

    protected final static Twilight plugin = Twilight.getInstance();

    public AbstractCommand() {
        plugin.getFramework().registerCommands(this);
    }

    public abstract void onCommand(CommandArgs cmd);

    public String getUsage(String label, String args) {
        return CC.RED + "Usage: /" + label.replace(".", " ") + " " + args;
    }

    public List<String> getTabComplete(String packageName) {
        final List<String> list = new ArrayList<>();

        for (final Class<?> clazz : plugin.getRegistration().getClassesInPackage(packageName)) {
            list.add(clazz.getSimpleName().replace("Command", "").toLowerCase());
        }

        return list;
    }
}
