package de.febanhd.fcommand.exceptions;

import de.febanhd.fcommand.Command;
import lombok.Getter;

public class InvalidCommandUsageException extends Exception {

    @Getter
    private final Command command;

    public InvalidCommandUsageException(Command command) {
        this.command = command;
    }
}
