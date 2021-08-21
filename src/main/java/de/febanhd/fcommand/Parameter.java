package de.febanhd.fcommand;

import de.febanhd.fcommand.autocompleetion.AutoCompletionHandler;
import de.febanhd.fcommand.executor.CommandExecutor;
import lombok.Getter;

@Getter
public class Parameter {

    private final String name;
    private final String description;
    private final String permission;
    private final boolean required;
    private final boolean openEnd;
    private final AutoCompletionHandler autoCompletionHandler;

    public Parameter(String name, String description, String permission, boolean required, boolean openEnd, AutoCompletionHandler autoCompletionHandler) {
        this.name = name;
        this.description = description;
        this.permission = permission;
        this.required = required;
        this.openEnd = openEnd;
        this.autoCompletionHandler = autoCompletionHandler;
    }

    public boolean hasPermission(CommandExecutor player) {
        return this.permission.isEmpty() || player.hasPermission(this.permission);
    }
}
