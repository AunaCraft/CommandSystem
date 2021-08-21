package de.febanhd.fcommand.executor.bungee;

import de.febanhd.fcommand.executor.CommandExecutor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;

public class BungeeCommandExecutor implements CommandExecutor {

    private final CommandSender sender;

    public BungeeCommandExecutor(CommandSender sender) {
        this.sender = sender;
    }

    @Override
    public void sendMessage(String message) {
        sender.sendMessage(new TextComponent(message));
    }

    @Override
    public boolean hasPermission(String permission) {
        return sender.hasPermission(permission);
    }

    @Override
    public org.bukkit.command.CommandSender toBukkitCommandSender() {
        return null;
    }

    @Override
    public CommandSender toBungeeCommandSender() {
        return sender;
    }
}
