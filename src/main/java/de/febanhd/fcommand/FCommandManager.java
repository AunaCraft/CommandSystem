package de.febanhd.fcommand;

import com.google.common.collect.Lists;
import de.febanhd.fcommand.builder.CommandBuilder;
import de.febanhd.fcommand.exceptions.InvalidCommandParameterException;
import de.febanhd.fcommand.exceptions.InvalidCommandUsageException;
import de.febanhd.fcommand.executor.CommandExecutor;
import de.febanhd.fcommand.exceptions.*;
import de.febanhd.fcommand.handler.CommandContext;
import de.febanhd.fcommand.wrapper.CommandWrapper;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FCommandManager {

    public static FCommandManager create(CommandWrapper wrapper) {
        return new FCommandManager(wrapper);
    }

    private final List<Command> commands = new ArrayList<>();

    private CommandWrapper wrapper;

    private FCommandManager(CommandWrapper wrapper) {

    }

    public void registerCommand(Command command) {
        wrapper.register(command, this);
    }
    public void registerCommand(FCommand fCommand, String commandName) {
        Command command = fCommand.create(CommandBuilder.beginCommand(commandName)).build();
        this.registerCommand(command);
    }

    public Command getCommandByName(String commandName) {
        return this.commands.stream().filter(cmd -> cmd.isNameOrAlias(commandName)).findAny().orElse(null);
    }

    public void execute(CommandExecutor player, String line) {
        String commandName = line.split(" ")[0];
        Command command = this.getCommandByName(commandName);
        if(command != null) {
            try {
                String[] args = tokenize(line);
                if(args.length == 0 && (command.hasSubCommands() || command.hasRequiredParameters())) {
                    throw new InvalidCommandUsageException(command);
                }
                /*
                    REQUEST IF HAS HANDLER AND THE EXECUTING THE HANDLER
                 */
                if(args.length == 0 && command.isExecutable()){
                    CommandContext commandContext = new CommandContext(command, new String[0]);
                    command.getHandler().handle(player, commandContext);
                    return;
                }
                /*
                    WILL BE EXECUTED WHEN THE CODE ABOVE CAN NOT BE EXECUTED
                 */
                if(args.length == 0){
                    throw new InvalidCommandUsageException(command);
                }
                Pair<Command, Integer> subCommandPair = getSubCommand(player, command, args, 0);
                Command subCommand = subCommandPair.getA();
                int subCommandIndex = subCommandPair.getB();
                List<String> subCommandArgs = new ArrayList<>();
                if(subCommandIndex + 1 <= args.length) {
                    subCommandArgs.addAll(Arrays.asList(Arrays.copyOfRange(args, subCommandIndex + 1, args.length)));
                }
                String[] subCommandArgArray = new String[subCommandArgs.size()];
                subCommandArgs.toArray(subCommandArgArray);

                this.parseParameters(player, subCommand, subCommandArgArray);

                CommandContext commandContext = new CommandContext(subCommand, subCommandArgArray);

                if(!subCommand.isExecutable()) {
                    throw new InvalidCommandUsageException(subCommand);
                }

                subCommand.getHandler().handle(player, commandContext);
            }catch (InvalidCommandUsageException e) {
                sendUsage(e.getCommand(), player);
            }catch (InvalidCommandParameterException e) {
                player.sendMessage("§cThe argument &7" + e.getParameter().getName() + " %nis invalid!");
            }catch (NoPermissionCommandException e) {
                player.sendMessage(FCommandConfig.instance.getNoPermissionMessage());
            }
        }
    }

    private void sendUsage(Command command, CommandExecutor player) {
        List<String> usages = command.getUsages(player);
        if(usages.isEmpty()) return;
        player.sendMessage("api.commands.usage.header");
        int i = 1;
        for (String usage : usages) {
            player.sendMessage(usage);
            i++;
        }
        if(i >= 5) player.sendMessage("api.commands.usage.header");
    }

    private String[] tokenize(String line) {
        String[] args = Arrays.copyOfRange(line.split(" "), 1, line.split(" ").length);
        return args;
    }

    private Pair<Command, Integer> getSubCommand(CommandExecutor player, Command command, String[] args, int index) throws NoPermissionCommandException {
        if(!command.hasPermission(player)) {
            throw new NoPermissionCommandException();
        }
        if(index >= args.length) return new Pair<>(command, index);
        String name = args[index];
        Command subCommand = command.getSubCommands().stream().filter(cmd -> cmd.isNameOrAlias(name)).findAny().orElse(null);
        if(subCommand == null) {
            if(index == 0) new Pair<>(command, 0);
            return new Pair<>(command, index - 1);
        }else {
            int newIndex = index += 1;
            if(args.length > newIndex) {
                return getSubCommand(player, subCommand, args, newIndex);
            }else {
                return new Pair<>(subCommand, index);
            }
        }
    }

    private void parseParameters(CommandExecutor player, Command command, String[] args) throws InvalidCommandParameterException, NoPermissionCommandException, InvalidCommandUsageException {
        if(args.length > command.getMaxLength())
            throw new InvalidCommandUsageException(command);

        if(args.length == command.getNeededParameters().size() || args.length == command.getParameters().size()) {
            for (int i = 0; i < args.length; i++) {
                Parameter parameter = command.getParameters().get(i);
                if(!parameter.getPermission().isEmpty() && !player.hasPermission(parameter.getPermission())) {
                    throw new NoPermissionCommandException();
                }
                if (parameter.getAutoCompletionHandler() != null && !parameter.getAutoCompletionHandler().isValid(args[i], player)) {
                    throw new InvalidCommandParameterException(command, parameter);
                }
            }
        }else if(args.length < command.getNeededParameters().size()) {
            throw new InvalidCommandUsageException(command);
        }
    }

    public List<String> getAutoCompletions(CommandExecutor player, Command cmd, String[] args) {
        List<String> matches = new ArrayList<>();
        try {
            Pair<Command, Integer> subCommandPair = getSubCommand(player, cmd, args, 0);
            Command subCommand = subCommandPair.getA();
            int subCommandIndex = subCommandPair.getB();
            if (subCommandIndex + 1 > args.length) return matches;
            String[] subCommandArgs = Arrays.copyOfRange(args, subCommandIndex + 1, args.length);
            if (subCommandArgs.length > 0) {
                if (subCommandArgs.length == 1) {
                    for (int i = 0; i < subCommand.getSubCommands().size(); i++) {
                        Command subCmd = subCommand.getSubCommands().get(i);
                        if(subCmd.hasPermission(player)) {
                            if(subCmd.getName().toLowerCase().startsWith(subCommandArgs[0].toLowerCase()))
                                matches.add(subCmd.getName());
                        }
                    }
                }
                if (subCommand.getParameters().size() > subCommandArgs.length - 1) {
                    Parameter parameter = subCommand.getParameters().get(subCommandArgs.length - 1);
                    if(parameter.hasPermission(player)) {
                        if (parameter.getAutoCompletionHandler() != null) {
                            List<String> allMatches = Lists.newArrayList();
                            allMatches.addAll(parameter.getAutoCompletionHandler().getCompletions(player));
                            String arg = subCommandArgs[subCommandArgs.length - 1];
                            for (String allMatch : allMatches) {
                                if(allMatch.toLowerCase().startsWith(arg.toLowerCase()))
                                    matches.add(allMatch);
                            }
                        }
                    }
                }
                return matches;
            }
        }catch (CommandException e) {

        }
        return matches;
    }


    @Getter
    @Setter
    private class Pair<A, B> {
        public A a;
        public B b;

        public Pair(A a, B b) {
            this.a = a;
            this.b = b;
        }
    }

}
