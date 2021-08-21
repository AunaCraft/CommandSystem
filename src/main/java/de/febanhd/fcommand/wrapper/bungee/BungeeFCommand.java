package de.febanhd.fcommand.wrapper.bungee;

import de.febanhd.fcommand.Command;
import de.febanhd.fcommand.FCommandManager;
import de.febanhd.fcommand.executor.bungee.BungeeCommandExecutor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.StringJoiner;

public class BungeeFCommand extends net.md_5.bungee.api.plugin.Command implements TabExecutor {

    private final Command command;
    private final FCommandManager manager;

    public BungeeFCommand(Command command, FCommandManager commandManager) {
        super(command.getName(), "", command.getAliases().toArray(new String[0]));
        this.manager = commandManager;
        this.command = command;
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        StringJoiner joiner = new StringJoiner(" ");
        joiner.add(this.getName());
        for (String string : strings) {
            joiner.add(string);
        }
        manager.execute(new BungeeCommandExecutor(commandSender), joiner.toString());
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        return manager.getAutoCompletions(new BungeeCommandExecutor(sender), this.command, args);
    }
}
