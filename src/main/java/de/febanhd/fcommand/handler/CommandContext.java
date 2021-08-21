package de.febanhd.fcommand.handler;

import de.febanhd.fcommand.Command;
import de.febanhd.fcommand.Parameter;

import java.util.List;

public class CommandContext {

    private final List<Parameter> parameters;
    private final String[] args;

    public CommandContext(Command command, String[] args) {
        this.parameters = command.getParameters();
        this.args = args;
    }

    public <T> T getParameterValue(String parameterName, Class<T> type) {
        hasParameter(parameterName);
        Parameter parameter = parameters.stream().filter(p -> p.getName().equals(parameterName)).findAny().orElse(null);
        assert parameter != null;
        int index = parameters.indexOf(parameter);
        if(parameter.isOpenEnd()) {
            if(type != String.class) {
                throw new IllegalStateException("For open-end parameters the type must be a string");
            }
            StringBuilder builder = new StringBuilder();
            for(int i = index; i < args.length; i++) {
                if(i == args.length - 1)
                    builder.append(args[i]);
                else
                    builder.append(args[i] + " ");
            }
            return type.cast(builder.toString());
        }
        return type.cast(parameter.getAutoCompletionHandler().convertString(args[index]));
    }

    public boolean hasParameter(String parameterName) {
        Parameter parameter = parameters.stream().filter(p -> p.getName().equals(parameterName)).findAny().orElse(null);
        if(parameter == null) throw new IllegalStateException("Cant find parameter with name: '" + parameterName + "'");
        int index = parameters.indexOf(parameter);
        return args.length > index;
    }
}
