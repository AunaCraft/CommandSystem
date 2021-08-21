package de.febanhd.fcommand.wrapper.bukkit;

import de.febanhd.fcommand.Command;
import de.febanhd.fcommand.FCommandManager;
import de.febanhd.fcommand.wrapper.CommandWrapper;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.InvocationTargetException;

public class BukkitCommandWrapper implements CommandWrapper {

    private final Plugin plugin;

    public BukkitCommandWrapper(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void register(Command command, FCommandManager commandManager) {
        try {
            BukkitFCommand bukkitFCommand = new BukkitFCommand(command, commandManager);
            Object craftServer = Bukkit.getServer();
            Object simpleCommandMap = Bukkit.getServer().getClass().getMethod("getCommandMap").invoke(craftServer);
            simpleCommandMap.getClass().getMethod("register", String.class, org.bukkit.command.Command.class).invoke(simpleCommandMap, plugin.getName().toLowerCase(), bukkitFCommand);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        new BukkitFCommand(command, commandManager);
    }
}
