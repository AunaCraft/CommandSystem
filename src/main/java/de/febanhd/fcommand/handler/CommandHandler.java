package de.febanhd.fcommand.handler;

import de.febanhd.fcommand.executor.CommandExecutor;

public interface CommandHandler {

    void handle(CommandExecutor executor, CommandContext ctx);
}
