package de.febanhd.fcommand.wrapper.bukkit;

import de.febanhd.fcommand.Command;
import de.febanhd.fcommand.FCommandManager;
import de.febanhd.fcommand.executor.bukkit.BukkitCommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.StringJoiner;

public class BukkitFCommand extends org.bukkit.command.Command {

    private final Command command;
    private final FCommandManager manager;

    public BukkitFCommand(Command command, FCommandManager manager) {
        super(command.getName());
        this.setAliases(command.getAliases());
        this.command = command;
        this.manager = manager;
    }

    @Override
    public boolean execute(CommandSender commandSender, String label, String[] strings) {
        StringJoiner joiner = new StringJoiner(" ");
        joiner.add(label);
        for (String string : strings) {
            joiner.add(string);
        }
        BukkitCommandExecutor commandExecutor = new BukkitCommandExecutor(commandSender);
        manager.execute(commandExecutor, joiner.toString());
        return false;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        return manager.getAutoCompletions(new BukkitCommandExecutor(sender), command, args);
    }
}
