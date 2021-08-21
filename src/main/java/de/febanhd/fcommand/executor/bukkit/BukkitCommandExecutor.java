package de.febanhd.fcommand.executor.bukkit;

import de.febanhd.fcommand.executor.CommandExecutor;
import org.bukkit.command.CommandSender;

public class BukkitCommandExecutor implements CommandExecutor {

    private final CommandSender sender;

    public BukkitCommandExecutor(CommandSender sender) {
        this.sender = sender;
    }

    @Override
    public void sendMessage(String message) {
        sender.sendMessage(message);
    }

    @Override
    public boolean hasPermission(String permission) {
        return sender.hasPermission(permission);
    }

    @Override
    public CommandSender toBukkitCommandSender() {
        return sender;
    }

    @Override
    public net.md_5.bungee.api.CommandSender toBungeeCommandSender() {
        return null;
    }
}
