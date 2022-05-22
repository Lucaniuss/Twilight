package me.lucanius.twilight.tools.command;

import lombok.Getter;
import me.lucanius.twilight.Twilight;
import me.lucanius.twilight.tools.Scheduler;
import me.lucanius.twilight.tools.Tools;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.help.GenericCommandHelpTopic;
import org.bukkit.help.HelpTopic;
import org.bukkit.help.HelpTopicComparator;
import org.bukkit.help.IndexHelpTopic;
import org.bukkit.plugin.SimplePluginManager;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Command Framework - CommandFramework <br>
 * The main command framework class used for controlling the framework.
 *
 * @author minnymin3
 */
public class CommandFramework implements CommandExecutor {

    private final Twilight plugin;
    private final Map<String, Map.Entry<Method, Object>> commandMap = new HashMap<>();

    @Getter private final List<Command> allCommands = new ArrayList<>();
    @Getter private final List<Command> disabledCommands = new ArrayList<>();

    private CommandMap map;

    public CommandFramework(Twilight plugin) {
        this.plugin = plugin;
        if (plugin.getServer().getPluginManager() instanceof SimplePluginManager) {
            SimplePluginManager manager = (SimplePluginManager) plugin.getServer().getPluginManager();
            try {
                Field field = SimplePluginManager.class.getDeclaredField("commandMap");
                field.setAccessible(true);
                map = (CommandMap) field.get(manager);
            } catch (IllegalArgumentException | NoSuchFieldException | IllegalAccessException | SecurityException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
        return handleCommand(sender, cmd, label, args);
    }

    public boolean handleCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
        for (int i = args.length; i >= 0; i--) {
            StringBuilder buffer = new StringBuilder();
            buffer.append(label.toLowerCase());
            for (int x = 0; x < i; x++) {
                buffer.append(".").append(args[x].toLowerCase());
            }
            String cmdLabel = buffer.toString();
            if (commandMap.containsKey(cmdLabel)) {
                Method method = commandMap.get(cmdLabel).getKey();
                Object methodObject = commandMap.get(cmdLabel).getValue();
                Command command = method.getAnnotation(Command.class);
                if (!command.permission().equals("") && !sender.hasPermission(command.permission())) {
                    sender.sendMessage(command.noPerm());
                    return true;
                }
                if (command.inGameOnly() && !(sender instanceof Player)) {
                    sender.sendMessage("§cThis command is only performable in-game!");
                    return true;
                }
                if (command.isAsync()) {
                    Scheduler.runAsync(() -> invoke(method, methodObject, new CommandArgs(sender, cmd, label, args, cmdLabel.split("\\.").length - 1)));
                } else {
                    invoke(method, methodObject, new CommandArgs(sender, cmd, label, args, cmdLabel.split("\\.").length - 1));
                }

                return true;
            }
        }

        defaultCommand(new CommandArgs(sender, cmd, label, args, 0));
        return true;
    }

    private void invoke(Method method, Object methodObject, CommandArgs args) {
        try {
            method.invoke(methodObject, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public void registerCommands(Object obj) {
        for (Method m : obj.getClass().getMethods()) {
            if (m.getAnnotation(Command.class) != null) {
                Command command = m.getAnnotation(Command.class);
                if (m.getParameterTypes().length > 1 || m.getParameterTypes()[0] != CommandArgs.class) {
                    Tools.log("Unable to register command " + m.getName() + ". Unexpected method arguments");
                    continue;
                }
                registerCommand(command, command.name(), m, obj);
                for (String alias : command.aliases()) {
                    registerCommand(command, alias, m, obj);
                }
            } else if (m.getAnnotation(Completer.class) != null) {
                Completer comp = m.getAnnotation(Completer.class);
                if (m.getParameterTypes().length != 1 || m.getParameterTypes()[0] != CommandArgs.class) {
                    Tools.log("Unable to register tab completer " + m.getName() + ". Unexpected method arguments");
                    continue;
                }
                if (m.getReturnType() != List.class) {
                    Tools.log("Unable to register tab completer " + m.getName() + ". Unexpected return type");
                    continue;
                }
                registerCompleter(comp.name(), m, obj);
                for (String alias : comp.aliases()) {
                    registerCompleter(alias, m, obj);
                }
            }
        }
    }

    public void registerHelp() {
        Set<HelpTopic> help = new TreeSet<>(HelpTopicComparator.helpTopicComparatorInstance());
        for (String s : commandMap.keySet()) {
            if (!s.contains(".")) {
                org.bukkit.command.Command cmd = map.getCommand(s);
                HelpTopic topic = new GenericCommandHelpTopic(cmd);
                help.add(topic);
            }
        }
        IndexHelpTopic topic = new IndexHelpTopic(plugin.getName(), "All commands for " + plugin.getName(), null, help, "Below is a list of all " + plugin.getName() + " commands:");
        Bukkit.getServer().getHelpMap().addTopic(topic);
    }

    public void registerCommand(Command command, String label, Method m, Object obj) {
        if (!this.allCommands.contains(command) && !command.name().contains(".")) {
            this.allCommands.add(command);
        }

        commandMap.put(label.toLowerCase(), new AbstractMap.SimpleEntry<>(m, obj));
        commandMap.put(this.plugin.getName() + ':' + label.toLowerCase(), new AbstractMap.SimpleEntry<>(m, obj));
        String cmdLabel = label.split("\\.")[0].toLowerCase();

        if (map.getCommand(cmdLabel) == null) {
            org.bukkit.command.Command cmd = new BukkitCommand(cmdLabel, this, plugin);
            map.register(plugin.getName(), cmd);
        }
        if (!command.description().equalsIgnoreCase("") && cmdLabel.equals(label)) {
            map.getCommand(cmdLabel).setDescription(command.description());
        }
        if (!command.usage().equalsIgnoreCase("") && cmdLabel.equals(label)) {
            map.getCommand(cmdLabel).setUsage(command.usage());
        }
        if (!command.permission().equalsIgnoreCase("") && cmdLabel.equals(label)) {
            map.getCommand(cmdLabel).setPermission(command.permission());
            map.getCommand(cmdLabel).setPermissionMessage(command.noPerm());
        }
    }

    public void registerCompleter(String label, Method m, Object obj) {
        String cmdLabel = label.split("\\.")[0].toLowerCase();
        if (map.getCommand(cmdLabel) == null) {
            org.bukkit.command.Command command = new BukkitCommand(cmdLabel, this, plugin);
            map.register(plugin.getName(), command);
        }
        if (map.getCommand(cmdLabel) instanceof BukkitCommand) {
            BukkitCommand command = (BukkitCommand) map.getCommand(cmdLabel);
            if (command.completer == null) {
                command.completer = new BukkitCompleter();
            }
            command.completer.addCompleter(label, m, obj);
        } else if (map.getCommand(cmdLabel) instanceof PluginCommand) {
            try {
                Object command = map.getCommand(cmdLabel);
                Field field = command.getClass().getDeclaredField("completer");
                field.setAccessible(true);
                if (field.get(command) == null) {
                    BukkitCompleter completer = new BukkitCompleter();
                    completer.addCompleter(label, m, obj);
                    field.set(command, completer);
                } else if (field.get(command) instanceof BukkitCompleter) {
                    BukkitCompleter completer = (BukkitCompleter) field.get(command);
                    completer.addCompleter(label, m, obj);
                } else {
                    Tools.log("Unable to register tab completer " + m.getName() + ". A tab completer is already registered for that command!");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void registerCommand(Command command) {
        if (map != null) {
            String name = command.name();
            org.bukkit.command.Command c = map.getCommand(name);
            if (c == null) {
                c = new BukkitCommand(name.split("\\.")[0].toLowerCase(), this, plugin);
            }

            map.register(plugin.getName(), c);

            for (String alias : command.aliases()) {
                org.bukkit.command.Command a = map.getCommand(alias);
                if (a == null) {
                    a = new BukkitCommand(alias.split("\\.")[0].toLowerCase(), this, plugin);
                }

                map.register(plugin.getName(), a);
            }
        }

        disabledCommands.removeIf(cmd -> cmd == command);
    }

    @SuppressWarnings("unchecked")
    public void unregisterCommand(Command command) {
        if (map != null) {
            org.bukkit.command.Command c = map.getCommand(command.name());
            if (c != null) {
                try {
                    Field field = map.getClass().getDeclaredField("knownCommands");
                    field.setAccessible(true);
                    Map<String, org.bukkit.command.Command> commands = (Map<String, org.bukkit.command.Command>) field.get(map);
                    commands.remove(c.getName().toLowerCase());
                    for (String alias : command.aliases()) {
                        org.bukkit.command.Command a = map.getCommand(alias);
                        if (a != null) {
                            commands.remove(a.getName().toLowerCase());
                        }
                    }

                    field.set(map, commands);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        }

        disabledCommands.add(command);
    }

    private void defaultCommand(CommandArgs args) {
        args.getSender().sendMessage(args.getLabel() + " is not registered! Contact Lucanius for help!");
    }
}