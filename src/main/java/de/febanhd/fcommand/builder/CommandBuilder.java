package de.febanhd.fcommand.builder;

import com.google.common.collect.Lists;
import de.febanhd.fcommand.Command;
import de.febanhd.fcommand.Parameter;
import de.febanhd.fcommand.executor.CommandExecutorCallable;
import de.febanhd.fcommand.handler.CommandHandler;

import java.util.Arrays;
import java.util.List;

public class CommandBuilder {

    public static CommandBuilder beginCommand(String name) {
        return new CommandBuilder(name);
    }

    private final String name;
    private String description;
    private String permission = "";
    private final List<Parameter> parameters = Lists.newArrayList();
    private final List<Command> subCommands = Lists.newArrayList();
    private final List<String> aliases = Lists.newArrayList();
    private CommandExecutorCallable<List<String>> customUsages;
    private CommandHandler handler;

    private CommandBuilder(String name) {
        this.name = name;
    }

    public CommandBuilder description(String description) {
        this.description = description;
        return this;
    }

    public CommandBuilder parameter(Parameter parameter) {
        parameters.add(parameter);
        return this;
    }

    public CommandBuilder subCommand(Command command) {
        subCommands.add(command);
        return this;
    }

    public CommandBuilder handler(CommandHandler handler) {
        this.handler = handler;
        return this;
    }

    public CommandBuilder permission(String permission) {
        this.permission = permission;
        return this;
    }
    public CommandBuilder aliases(String... aliases) {
        this.aliases.addAll(Arrays.asList(aliases));
        return this;
    }

    public CommandBuilder customUsages(CommandExecutorCallable<List<String>> customUsages) {
        this.customUsages = customUsages;
        return this;
    }

    public Command build() {
        boolean executable = this.handler != null;
        if(description == null) description = "No description";

        for (Parameter parameter : parameters) {
            int parameterIndex = this.parameters.indexOf(parameter);
            boolean end = parameterIndex + 1 == this.parameters.size();
            if(!parameter.isRequired() && !end)
                throw new IllegalStateException("Optional parameters are only allowed at the end of the command!");
            if(parameter.isOpenEnd() && !end) {
                throw new IllegalStateException("Open-End parameters are only allowed at the end the command!");
            }
        }
        return new Command(this.name, this.description, this.permission, this.aliases, this.parameters, this.subCommands, executable, this.handler, customUsages);
    }
}
