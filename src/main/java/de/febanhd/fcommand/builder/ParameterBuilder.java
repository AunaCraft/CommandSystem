package de.febanhd.fcommand.builder;

import de.febanhd.fcommand.Parameter;
import de.febanhd.fcommand.autocompleetion.AutoCompletionHandler;

public class ParameterBuilder {

    public static ParameterBuilder beginParameter(String name) {
        return new ParameterBuilder(name);
    }

    private final String name;
    private String description;
    private boolean required = false;
    private boolean openEnd = false;
    private String permission = "";
    private AutoCompletionHandler<?> autoCompletionHandler;

    private ParameterBuilder(String name) {
        this.name = name;
    }

    public ParameterBuilder description(String description) {
        this.description = description;
        return this;
    }

    public ParameterBuilder required() {
        this.required = true;
        return this;
    }

    public ParameterBuilder optional() {
        this.required = false;
        return this;
    }

    public ParameterBuilder autoCompletionHandler(AutoCompletionHandler<?> autoCompletionHandler) {
        this.autoCompletionHandler = autoCompletionHandler;
        return this;
    }

    public ParameterBuilder permission(String permission) {
        this.permission = permission;
        return this;
    }

    public ParameterBuilder openEnd() {
        this.openEnd = true;
        return this;
    }

    public Parameter build() {
        if(autoCompletionHandler == null && !openEnd)
            throw new IllegalStateException("AutoCompletionHandler cannot be null");
        return new Parameter(this.name, this.description, this.permission, this.required, openEnd, this.autoCompletionHandler);
    }

}
