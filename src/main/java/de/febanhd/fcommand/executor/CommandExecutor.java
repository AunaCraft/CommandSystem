package de.febanhd.fcommand.executor;

public interface CommandExecutor {

    void sendMessage(String message);

    boolean hasPermission(String permission);

    org.bukkit.command.CommandSender toBukkitCommandSender();

    net.md_5.bungee.api.CommandSender toBungeeCommandSender();
}
