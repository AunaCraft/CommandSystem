package de.febanhd.fcommand.exceptions;

import de.febanhd.fcommand.Command;
import de.febanhd.fcommand.Parameter;
import lombok.Getter;

@Getter
public class InvalidCommandParameterException extends Exception {

    private final Command command;
    private final Parameter parameter;

    public InvalidCommandParameterException(Command command, Parameter parameter) {
        this.command = command;
        this.parameter = parameter;
    }
}
