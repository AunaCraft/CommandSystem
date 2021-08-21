package de.febanhd.fcommand;

import de.febanhd.fcommand.exceptions.NoPermissionCommandException;
import de.febanhd.fcommand.executor.CommandExecutor;
import de.febanhd.fcommand.executor.CommandExecutorCallable;
import de.febanhd.fcommand.handler.CommandHandler;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

@Getter
public class Command {

    private final String name;
    private final String description;
    private final List<String> aliases;
    private final List<Parameter> parameters;
    private final List<Command> subCommands;
    private final boolean executable;
    private final CommandHandler handler;
    private Command parentCommand;
    private final String permission;
    private final CommandExecutorCallable<List<String>> customUsageCallable;

    public Command(String name, String description, String permission, List<String> aliases, List<Parameter> parameters, List<Command> subCommands, boolean executable, CommandHandler handler, CommandExecutorCallable<List<String>> customUsageCallable) {
        this.name = name;
        this.aliases = aliases;
        this.description = description;
        this.permission = permission;
        this.parameters = parameters;
        this.subCommands = subCommands;
        this.handler = handler;
        this.executable = executable;
        this.customUsageCallable = customUsageCallable;
        this.subCommands.forEach(subFCommand -> subFCommand.setParent(this));
    }

    private String getParentUsage(CommandExecutor player, String str) throws NoPermissionCommandException {
        if(!this.hasPermission(player)) throw new NoPermissionCommandException();
        if(this.parentCommand != null) {
            return this.parentCommand.getParentUsage(player, this.name + " " + str);
        }else {
            return this.name + " " + str;
        }
    }

    public List<String> getUsages(CommandExecutor player) {
        List<String> usages;
        if(customUsageCallable != null) {
            usages = customUsageCallable.call(player);
            if(usages != null) return usages;
        }
        usages = new ArrayList<>();
        String parentUsage;
        try {
            parentUsage = this.getParentUsage(player, "");
        }catch (NoPermissionCommandException e) {
            player.sendMessage("§cYou don't have permissions to do that!");
            return usages;
        }
        if(executable) {
            StringJoiner joiner = new StringJoiner(" ");
            for (Parameter parameter : parameters) {
                if(parameter.hasPermission(player)) {
                    String name = parameter.isRequired() ? "§8<§e" + parameter.getName() + "§8>" : "§8[§e" + parameter.getName() + "§8]";
                    joiner.add(name);
                }
            }
            usages.add("§e/" + parentUsage + joiner);
        }

        for (Command subCommand : subCommands) {
            if(subCommand.hasPermission(player)) {
                for (String subCommandUsage : subCommand.getUsages(player)) {
                    usages.add(subCommandUsage);
                }
            }
        }

        return usages;
    }

    public void setParent(Command Command) {
        if(this.parentCommand == null) this.parentCommand = Command;
        else throw new IllegalStateException("Parent command is already defined");
    }

    public List<Parameter> getNeededParameters() {
        return this.parameters.stream().filter(p -> p.isRequired()).collect(Collectors.toList());
    }

    public boolean hasSubCommands() {
        return this.subCommands.size() > 0;
    }

    public boolean hasRequiredParameters() {
        return this.parameters.stream().filter(parameter -> parameter.isRequired()).collect(Collectors.toList()).size() > 0;
    }

    public boolean hasPermission(CommandExecutor player) {
        return this.permission.isEmpty() || player.hasPermission(this.permission);
    }

    public boolean isNameOrAlias(String in) {
        if (this.name.equalsIgnoreCase(in)) return true;
        for (String alias : aliases) {
            if (alias.equalsIgnoreCase(in)) return true;
        }
        return false;
    }

    public int getMaxLength() {
        return this.getMaxArgLength(0);
    }

    protected int getMaxArgLength(int i) {
        for (Parameter parameter : parameters)
            if(parameter.isOpenEnd()) return Integer.MAX_VALUE;
        if(hasSubCommands()) {
            int j = i;
            for (Command subCommand : subCommands) {
                int length = subCommand.getMaxArgLength(j);
                if(length > j) {
                    j = length;
                }
            }
            return j;
        }else {
            return i + this.parameters.size();
        }
    }

}
